package com.geek.exercise.services;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.geek.exercise.dao.RegistrationDataAccess;
import com.geek.exercise.requests.RegistrationRequest;
import com.geek.exercise.responses.ErrorResponse;
import com.geek.exercise.responses.Response;

public class RegistrationServiceImpl implements RegistrationService {

	private RegistrationDataAccess mRegistrationDataAccess;
	
	@Autowired
	public RegistrationServiceImpl( RegistrationDataAccess registrationDao ) {
		super();
		
		mRegistrationDataAccess = registrationDao;
	}

	@Override
	public Response register( RegistrationRequest request ) {
		if ( StringUtils.isEmpty( request.getChannelId() ) ) {
			return ErrorResponse.newBuilder()
					.setMessage( "channel id is required to register client!" )
					.build();
		}
		
		if ( mRegistrationDataAccess.getAccountById( request.getChannelId() ) != null ) {
			return ErrorResponse.newBuilder()
					.setMessage( new StringBuilder()
							.append( "channel id already exists: " )
							.append( request.getChannelId() )
							.toString() )
					.build();
		}
		
		return null;
	}

}
