package com.geek.exercise.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.geek.exercise.responses.ErrorResponse;
import com.geek.exercise.responses.Response;
import com.geek.exercise.services.MessageService;

@Controller()
@RequestMapping( "/message" )
public class MessageController {

	private ObjectMapper mObjectMapper = new ObjectMapper();
	
	private MessageService mMessageService;
	
	@Autowired
	public MessageController( MessageService messageService ) {
		super();
		
		mMessageService = messageService;
	}
	
	@RequestMapping( value ="/send", method = RequestMethod.POST )
	@ResponseBody
	public Response send( @RequestBody String body, HttpServletRequest request ) {
		if ( !StringUtils.isEmpty( body ) ) {
			try {
				JsonNode data = mObjectMapper.readTree( body );
				
				JsonNode message = data.get( "message" );
				
				if ( message != null ) {
					return mMessageService.send( message.getTextValue() );
				}
			} catch ( JsonProcessingException e ) {
				return ErrorResponse.newBuilder()
						.setMessage( e )
						.build();
			} catch ( IOException e ) {
				return ErrorResponse.newBuilder()
						.setMessage( e )
						.build();
			}
		}
		
		return ErrorResponse.newBuilder()
				.setMessage( ErrorResponse.GATEWAY_ERROR_MESSAGE )
				.build();
	}

}
