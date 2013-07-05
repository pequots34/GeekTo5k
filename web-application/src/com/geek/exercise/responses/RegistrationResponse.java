package com.geek.exercise.responses;

import com.geek.exercise.transfer.Account;

public class RegistrationResponse extends Response {

	private Account mAccount;
	
	public RegistrationResponse( Builder builder ) {
		super();
		
		mAccount = builder.account;
	}
	
	public Account getAccount() {
		return mAccount;
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}
	
	public static final class Builder {
		
		private Account account;
		
		public Builder setAccount( Account account ) {
			this.account = account;
			
			return this;
		}
		
		public RegistrationResponse build() {
			return new RegistrationResponse( this );
		}
	}

}
