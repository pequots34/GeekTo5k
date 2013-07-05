package com.geek.exercise.transfer;

import org.joda.time.DateTime;

public class Account {

	private String mChannelId;
	
	private long mId;
	
	private DateTime mCreated;
	
	public Account( Builder builder ) {
		super();
		
		mChannelId = builder.channelId;
		
		mId = builder.id;
		
		mCreated = builder.created;
	}
	
	public DateTime getCreated() {
		return mCreated;
	}
	
	public long getId() {
		return mId;
	}

	public String getChannelId() {
		return mChannelId;
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}
	
	public static final class Builder {
		
		private String channelId;
		
		private long id;
		
		private DateTime created;
		
		public Builder setCreated( DateTime time ) {
			created = time;
			
			return this;
		}
		
		public Builder setId( long id ) {
			this.id = id;
			
			return this;
		}
		
		public Builder setChannelId( String id ) {
			channelId = id;
			
			return this;
		}
		
		public Account build() {
			return new Account( this );
		}
	}

}
