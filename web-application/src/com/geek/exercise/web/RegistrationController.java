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

import com.geek.exercise.requests.RegistrationRequest;
import com.geek.exercise.responses.ErrorResponse;
import com.geek.exercise.responses.Response;
import com.geek.exercise.services.RegistrationService;

@Controller()
public class RegistrationController {

	private ObjectMapper mObjectMapper = new ObjectMapper();
	
	private RegistrationService mRegistrationService;
	
	@Autowired
	public RegistrationController( RegistrationService registrationService ) {
		super();
		
		mRegistrationService = registrationService;
	}
	
	@RequestMapping( value ="/register", method = RequestMethod.POST )
	@ResponseBody
	public Response register( @RequestBody String body, HttpServletRequest request ) {
		if ( !StringUtils.isEmpty( body ) ) {
			try {
				JsonNode data = mObjectMapper.readTree( body );
				
				JsonNode channelId = data.get( "channelId" );
				
				if ( channelId != null ) {
					Response response =  mRegistrationService.register( RegistrationRequest.newBuilder()
							.setChannelId( channelId.getTextValue() )
							.build() );
					
					if ( response != null ) {
						return response;
					}
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
