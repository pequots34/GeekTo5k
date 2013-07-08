package com.geek.exercise.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;

import com.geek.exercise.responses.ErrorResponse;
import com.geek.exercise.responses.Response;
import com.geek.exercise.transfer.Account;
import com.geek.exercise.utilities.HTTPTransport;
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
		
		try {
			ExecutorService executor = Executors.newFixedThreadPool( 10 );
			
			List<Callable<MessageTaskResponse>> callables = new ArrayList<Callable<MessageTaskResponse>>( accounts.size() );
			
			for ( Account account: accounts ) {
				callables.add( new MessageTask( account, message ) );
			}
			
			List<Future<MessageTaskResponse>> executed = executor.invokeAll( callables );
			
			return new Response();
		} catch ( InterruptedException e ) {
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
	
	class MessageTask implements Callable<MessageTaskResponse> {

		private Account mAccount;
		
		private String mMessage;
		
		public MessageTask( Account account, String message ) {
			super();
			
			mAccount = account;
			
			mMessage = message;
		}
		
		@Override
		public MessageTaskResponse call() throws Exception {
			MessageTaskResponse.Builder builder = MessageTaskResponse.newBuilder()
					.setAccount( mAccount )
					.setMessage( mMessage );
			
			final String key = PropertyPlaceholderUtil.getPropertyByKey( PropertyPlaceholderUtil.GOOGLE_CLIENT_SECRET );
			
			GoogleTokenResponse token = null;
			
			if ( mGoogleAuthorization == null ) {
				try {
					token = mGoogleAuthorizationService.getAuthorization();
					
					newCache( key, token );
				} catch( IOException e ) {
					return builder.setExtra( e.toString() )
							.build();
				}
			}
			
			try {
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
			} catch ( ExecutionException e ) {
				return builder.setExtra( e.toString() )
						.build();
			}
			
			if ( token != null ) {
				HttpPost method = new HttpPost( "https://www.googleapis.com/gcm_for_chrome/v1/messages" );
				
				method.addHeader( "Content-Type", "application/json" );
				
				method.addHeader( "Authorization", token.getTokenType() + " " + token.getAccessToken() );
				
				method.setEntity( new StringEntity( "{'channelId': '01282915067796969032/mdidlpphalgcdbfaoegncdpoolcokkpf','subchannelId': '0', 'payload': 'HTTP Transport JAY S'}" ) );
				
				HttpResponse response = HTTPTransport.execute( method );
			}
			
			return builder.build();
		}
		
	}
	
	public static final class MessageTaskResponse {
		
		private Account mAccount;
		
		public MessageTaskResponse( Builder builder ) {
			super();
			
			mAccount = builder.account;
		}
		
		public Account getAccount() {
			return mAccount;
		}
		
		public static Builder newBuilder() {
			return new Builder();
		}
		
		public static final class Builder {
			
			private Account account;
			
			private boolean executed;
			
			private Object extra;
			
			private String message;
			
			public Builder setMessage( String message ) {
				this.message = message;
				
				return this;
			}
			
			public Builder setExtra( Object extra ) {
				this.extra = extra;
				
				return this;
			}
			
			public Builder setExecuted( boolean executed ) {
				this.executed = executed;
				
				return this;
			}
			
			public Builder setAccount( Account account ) {
				this.account = account;
				
				return this;
			}
			
			public MessageTaskResponse build() {
				return new MessageTaskResponse( this );
			}
		}
		
	}
	
}
