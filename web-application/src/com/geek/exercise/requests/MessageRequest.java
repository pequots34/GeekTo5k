package com.geek.exercise.requests;

public class MessageRequest {

	private String mPayload;
	
	public MessageRequest( Builder builder ) {
		super();
		
		mPayload = builder.payload;
	}
	
	public String getPayload() {
		return mPayload;
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}
	
	public static final class Builder {
		
		private String payload;
		
		public Builder setPayload( String payload ) {
			this.payload = payload;
			
			return this;
		}
		
		public MessageRequest build() {
			return new MessageRequest( this );
		}
	}

}
