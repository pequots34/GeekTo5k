package com.geek.exercise.services;

import com.geek.exercise.requests.MessageRequest;
import com.geek.exercise.responses.Response;

public interface MessageService {
	
	public Response send( MessageRequest request );

}
