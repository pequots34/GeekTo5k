package com.geek.exercise.responses;

import java.util.List;

import com.geek.exercise.services.GoogleMessageServiceImpl.MessageTaskResponse;

public class GoogleCloudMessageResponse extends Response {

	private List<MessageTaskResponse> mTasks;
	
	public GoogleCloudMessageResponse( Builder builder ) {
		super();
		
		mTasks = builder.tasks;
	}
	
	public List<MessageTaskResponse> getTasks() {
		return mTasks;
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}
	
	public static final class Builder {
		
		private List<MessageTaskResponse> tasks;
		
		public Builder setTasks( List<MessageTaskResponse> tasks ) {
			this.tasks = tasks;
			
			return this;
		}
		
		public GoogleCloudMessageResponse build() {
			return new GoogleCloudMessageResponse( this );
		}
	}
}
