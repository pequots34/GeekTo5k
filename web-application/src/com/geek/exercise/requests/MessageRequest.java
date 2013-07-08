package com.geek.exercise.requests;

public class MessageRequest {

	private String mMessage;
	
	public MessageRequest( Builder builder ) {
		super();
		
		mMessage = builder.message;
	}
	
	public String getMessage() {
		return mMessage;
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}
	
	public static final class Builder {
		
		private String message;
		
		public Builder setMessage( String message ) {
			this.message = message;
			
			return this;
		}
		
		public MessageRequest build() {
			return new MessageRequest( this );
		}
	}

}
