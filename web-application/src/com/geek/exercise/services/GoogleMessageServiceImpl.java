package com.geek.exercise.services;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.geek.exercise.responses.ErrorResponse;
import com.geek.exercise.responses.Response;
import com.geek.exercise.transfer.Account;
import com.geek.exercise.utilities.PropertyPlaceholderUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class GoogleMessageServiceImpl implements GoogleMessageService {

	private static Cache<String, GoogleTokenResponse> mGoogleAuthorization;
	
	private GoogleAuthorizationService mGoogleAuthorizationService;
	
	@Autowired
	public GoogleMessageServiceImpl( GoogleAuthorizationService googleAuthorizationService ) {
		super();
		
		mGoogleAuthorizationService = googleAuthorizationService;
	}

	@Override
	public Response sendToGoogleCloudMessage( List<Account> accounts, String message ) {
		if ( accounts == null || accounts.isEmpty() ) {
			return ErrorResponse.newBuilder()
					.setMessage( "accounts are required to send message!" )
					.build();
		}
		
		if ( StringUtils.isEmpty( message ) ) {
			return ErrorResponse.newBuilder()
					.setMessage( "message is required!" )
					.build();
		}
		
		final String key = PropertyPlaceholderUtil.getPropertyByKey( PropertyPlaceholderUtil.GOOGLE_CLIENT_SECRET );
		
		try {
			GoogleTokenResponse token = null;
			
			if ( mGoogleAuthorization == null ) {
				try {
					token = mGoogleAuthorizationService.getAuthorization();
					
					newCache( key, token );
				} catch( IOException e ) {
					return ErrorResponse.newBuilder()
							.setMessage( e )
							.build();
				}
			}
			
			token = mGoogleAuthorization.get( key, new Callable<GoogleTokenResponse>() {

				@Override
				public GoogleTokenResponse call() throws Exception {
					GoogleTokenResponse token = null;
					
					try {
						token = mGoogleAuthorizationService.getAuthorization();
						
						newCache( key, token );
					} catch( IOException e ) { }
					
					return token;
				}
				
			} );
			
			if ( token == null ) {
				return ErrorResponse.newBuilder()
						.setMessage( "Google authorization token couldn't be found!" )
						.build();
			}
			
			// cache token
			// connection pool ( static )
			//exector service
			//build response
			
			HttpClient client = new DefaultHttpClient();
			
			HttpPost method = new HttpPost( "https://www.googleapis.com/gcm_for_chrome/v1/messages" );
			
			method.addHeader( "Content-Type", "application/json" );
			
			method.addHeader( "Authorization", token.getTokenType() + " " + token.getAccessToken() );
			
			method.setEntity( new StringEntity( "{'channelId': '01282915067796969032/mdidlpphalgcdbfaoegncdpoolcokkpf','subchannelId': '0', 'payload': 'dinky.. yippie real token'}" ) );
			
			HttpResponse response = client.execute( method );
			
			return ErrorResponse.newBuilder()
					.setMessage( " --- HTTP RESPONSE CODE -- : " + response.getStatusLine().getStatusCode() )
					.build();
		} catch ( Exception e ) {
			return ErrorResponse.newBuilder()
					.setMessage( e )
					.build();
		}
	}
	
	private static void newCache( String key, GoogleTokenResponse token ) {
		mGoogleAuthorization = CacheBuilder.newBuilder()
				.expireAfterWrite( token.getExpiresInSeconds(), TimeUnit.SECONDS )
				.build();
		
		mGoogleAuthorization.put( key, token );
	}
	
}
