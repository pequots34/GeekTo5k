package com.geek.exercise.requests;

public class RegistrationRequest {

	private String mChannelId;
	
	public RegistrationRequest( Builder builder ) {
		super();
		
		mChannelId = builder.channelId;
	}
	
	public String getChannelId() {
		return mChannelId;
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}
	
	public static final class Builder {
		
		private String channelId;
		
		public Builder setChannelId( String id ) {
			channelId = id;
			
			return this;
		}
		
		public RegistrationRequest build() {
			return new RegistrationRequest( this );
		}
		
	}

}
