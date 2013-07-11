package com.geek.exercise.services;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.geek.exercise.requests.MessageRequest;
import com.geek.exercise.responses.ErrorResponse;
import com.geek.exercise.responses.RegisteredResponse;
import com.geek.exercise.responses.Response;

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
	public Response send( MessageRequest request ) {
		if ( request == null || StringUtils.isEmpty( request.getPayload() ) ) {
			return ErrorResponse.newBuilder()
					.setMessage( "payload is required!" )
					.build();
		}
		
		RegisteredResponse registered = mRegistrationService.getRegistered();
		
		if ( registered.isEmpty() ) {
			return ErrorResponse.newBuilder()
					.setMessage( "no registered accounts!" )
					.build();
		}
		
		return mGoogleMessageService.sendToGoogleCloudMessage( registered.getAccounts(), request.getPayload() );
	}

}
