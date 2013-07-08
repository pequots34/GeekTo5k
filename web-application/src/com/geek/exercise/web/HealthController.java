package com.geek.exercise.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.geek.exercise.responses.Response;

@Controller()
@RequestMapping( "/health" )
public class HealthController {

	public HealthController() {
		super();
	}
	
	@RequestMapping( value ="/check", method = RequestMethod.GET )
	@ResponseBody
	public Response health() {
		return new Response();
	}

}
