package com.geek.exercise.services;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.geek.exercise.responses.ErrorResponse;
import com.geek.exercise.responses.RegisteredResponse;
import com.geek.exercise.responses.Response;
import com.geek.exercise.transfer.Account;

public class MessageServiceImpl implements MessageService {

	private RegistrationService mRegistrationService;
	
	private GoogleMessageService mGoogleMessageService;
	
	@Autowired
	public MessageServiceImpl( RegistrationService registrationService, GoogleMessageService googleMessageService ) {
		super();
		
		mRegistrationService = registrationService;
		
		mGoogleMessageService = googleMessageService;
	}
	
	@Override
	public Response send( String message ) {
		if ( StringUtils.isEmpty( message ) ) {
			return ErrorResponse.newBuilder()
					.setMessage( "message is required!" )
					.build();
		}
		
		RegisteredResponse registered = mRegistrationService.getRegistered();
		
		if ( registered.isEmpty() ) {
			return ErrorResponse.newBuilder()
					.setMessage( "no registered accounts!" )
					.build();
		}
		
		List<Account> accounts = registered.getAccounts();
		
		try {
			HttpClient client = new DefaultHttpClient();
			
			HttpPost method = new HttpPost( "https://www.googleapis.com/gcm_for_chrome/v1/messages" );
			
			method.addHeader( "Content-Type", "application/json" );
			
			method.addHeader( "Authorization", "Bearer ya29.AHES6ZT4UxXdc_1IInDsnlTCTavs99Ke1iVDZG_hl00K23yF" );
			
			method.setEntity( new StringEntity( "{'channelId': '01282915067796969032/mdidlpphalgcdbfaoegncdpoolcokkpf','subchannelId': '0', 'payload': 'sent from the server'}" ) );
			
			HttpResponse response = client.execute( method );
			
			return ErrorResponse.newBuilder()
					.setMessage( "HTTP RESPONSE CODE: " + response.getStatusLine().getStatusCode() )
					.build();
		} catch (ClientProtocolException e) {
			return ErrorResponse.newBuilder()
					.setMessage( e )
					.build();
		} catch (IOException e) {
			return ErrorResponse.newBuilder()
					.setMessage( e )
					.build();
		}
	}

}
