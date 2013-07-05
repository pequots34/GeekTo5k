package com.geek.exercise.transfer;

public class Account {

	private String mChannelId;
	
	public Account( Builder builder ) {
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
		
		public Account build() {
			return new Account( this );
		}
	}

}
