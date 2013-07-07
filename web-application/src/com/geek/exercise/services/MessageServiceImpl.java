package com.geek.exercise.services;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.geek.exercise.responses.ErrorResponse;
import com.geek.exercise.responses.RegisteredResponse;
import com.geek.exercise.responses.Response;
import com.geek.exercise.transfer.Account;

public class MessageServiceImpl implements MessageService {

	private RegistrationService mRegistrationService;
	
	@Autowired
	public MessageServiceImpl( RegistrationService registrationService ) {
		super();
		
		mRegistrationService = registrationService;
	}
	
	@Override
	public Response send( String message ) {
		if ( StringUtils.isEmpty( message ) ) {
			return ErrorResponse.newBuilder()
					.setMessage( "message is required!" )
					.build();
		}
		
		RegisteredResponse response = mRegistrationService.getRegistered();
		
		if ( response.isEmpty() ) {
			return ErrorResponse.newBuilder()
					.setMessage( "no registered accounts!" )
					.build();
		}
		
		List<Account> accounts = response.getAccounts();
		
		return null;
	}

}
