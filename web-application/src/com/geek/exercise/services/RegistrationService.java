package com.geek.exercise.services;

import com.geek.exercise.requests.RegistrationRequest;
import com.geek.exercise.responses.Response;

public interface RegistrationService {
	
	public Response register( RegistrationRequest request );

}
