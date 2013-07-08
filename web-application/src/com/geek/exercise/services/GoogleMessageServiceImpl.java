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
import org.springframework.beans.factory.annotation.Autowired;

import com.geek.exercise.responses.ErrorResponse;
import com.geek.exercise.responses.Response;
import com.geek.exercise.transfer.Account;
import com.geek.exercise.transfer.GoogleMessage;
import com.geek.exercise.utilities.GlobalConstants;
import com.geek.exercise.utilities.GoogleCloudMessageUtil;
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
			ExecutorService executor = Executors.newFixedThreadPool( 5 );
			
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
			final String key = PropertyPlaceholderUtil.getPropertyByKey( PropertyPlaceholderUtil.GOOGLE_CLIENT_SECRET );
			
			GoogleTokenResponse token = null;
			
			if ( mGoogleAuthorization == null ) {
				try {
					token = mGoogleAuthorizationService.getAuthorization();
					
					newCache( key, token );
				} catch( IOException e ) {
					return MessageTaskResponse.newBuilder()
							.setExtra( e.toString() )
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
				return MessageTaskResponse.newBuilder()
						.setExtra( e.toString() )
						.build();
			}
			
			if ( token != null ) {
				HttpPost method = new HttpPost( GoogleCloudMessageUtil.getMessagingEndpoint() );
				
				method.addHeader( "Content-Type", GlobalConstants.APPLICATION_JSON_CONTENT_TYPE );
				
				method.addHeader( "Authorization", GoogleCloudMessageUtil.toAuthorizationHeader( token ) );
				
				GoogleMessage payload = new GoogleMessage( mMessage );
				
				payload.setChannelId( mAccount.getChannelId() );
				
				payload.setSubchannelId( GoogleCloudMessageUtil.DEFAULT_SUB_CHANNEL_ID );
				
				method.setEntity( GoogleCloudMessageUtil.toEntity( payload ) );
				
				HttpResponse response = HTTPTransport.execute( method );
				
				int statusCode = response.getStatusLine().getStatusCode();
				
				return MessageTaskResponse.newBuilder()
						.setExtra( statusCode )
						.setAccount( mAccount )
						.setMessage( mMessage )
						.setExecuted( statusCode == 204 )
					.build();
			}
			
			return MessageTaskResponse.newBuilder()
					.setExtra( "Google Auth token couldn't be found!" )
					.build();
		}
		
	}
	
	public static final class MessageTaskResponse {
		
		private Account mAccount;
		
		private boolean mExecuted;
		
		private Object mExtra;
		
		private String mMessage;
		
		public MessageTaskResponse( Builder builder ) {
			super();
			
			mAccount = builder.account;
			
			mExecuted = builder.executed;
			
			mExtra = builder.extra;
			
			mMessage = builder.message;
		}
		
		public boolean executed() {
			return mExecuted;
		}
		
		public Object getExtra() {
			return mExtra;
		}
		
		public String getMessage() {
			return mMessage;
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
