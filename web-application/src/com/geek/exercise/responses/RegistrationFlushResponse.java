package com.geek.exercise.responses;

public class RegistrationFlushResponse extends Response {

	private long mCount;
	
	public RegistrationFlushResponse( Builder builder ) {
		super();
		
		mCount = builder.count;
	}
	
	public long getCount() {
		return mCount;
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}
	
	public static final class Builder {
		
		private long count;
		
		public Builder setCount( long count ) {
			this.count = count;
			
			return this;
		}
		
		public RegistrationFlushResponse build() {
			return new RegistrationFlushResponse( this );
		}
		
	}

}
