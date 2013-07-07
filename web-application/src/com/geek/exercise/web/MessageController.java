package com.geek.exercise.web;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.geek.exercise.responses.Response;
import com.geek.exercise.services.MessageService;

@Controller()
@RequestMapping( "/message" )
public class MessageController {

	private MessageService mMessageService;
	
	@Autowired
	public MessageController( MessageService messageService ) {
		super();
		
		mMessageService = messageService;
	}
	
	@RequestMapping( value ="/send", method = RequestMethod.GET )
	@ResponseBody
	public Response register( @RequestBody String body, HttpServletRequest request ) {
		return null;
	}

}
