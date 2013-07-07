package com.geek.exercise.services;

import org.springframework.beans.factory.annotation.Autowired;

public class GoogleMessageServiceImpl implements GoogleMessageService {

	private GoogleAuthorizationService mGoogleAuthorizationService;
	
	@Autowired
	public GoogleMessageServiceImpl( GoogleAuthorizationService googleAuthorizationService ) {
		super();
		
		mGoogleAuthorizationService = googleAuthorizationService;
	}

}
