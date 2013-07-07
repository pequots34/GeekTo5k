package com.geek.exercise.services;

import org.springframework.beans.factory.annotation.Autowired;

public class MessageServiceImpl implements MessageService {

	private MessageService mMessageService;
	
	@Autowired
	public MessageServiceImpl( MessageService messageService ) {
		super();
		
		mMessageService = messageService;
	}

}
