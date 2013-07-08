package com.geek.exercise.transfer;

public class GoogleMessage {

	private String channelId;
	
	private int subchannelId;
	
	private String payload;
	
	public GoogleMessage( String payload ) {
		super();
		
		this.payload = payload;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId( String channelId ) {
		this.channelId = channelId;
	}

	public int getSubchannelId() {
		return subchannelId;
	}

	public void setSubchannelId( int subchannelId ) {
		this.subchannelId = subchannelId;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload( String payload ) {
		this.payload = payload;
	}

}
