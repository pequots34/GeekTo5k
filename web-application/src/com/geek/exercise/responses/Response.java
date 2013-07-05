package com.geek.exercise.responses;

import org.joda.time.DateTime;

public class Response {

	public enum ResponseStatus {
		SUCCESS,
		ERROR
	}
	
	public Response() {
		super();
	}
	
	
	public DateTime getTime() {
		return DateTime.now();
	}
	
	public ResponseStatus getStatus() {
		return ResponseStatus.SUCCESS;
	}

}
