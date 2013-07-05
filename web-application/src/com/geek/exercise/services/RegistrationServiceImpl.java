package com.geek.exercise.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.geek.exercise.dao.RegistrationDataAccess;
import com.geek.exercise.requests.RegistrationRequest;
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
		// TODO Auto-generated method stub
		return null;
	}

}
