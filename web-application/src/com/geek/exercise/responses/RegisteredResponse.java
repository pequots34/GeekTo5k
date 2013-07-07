package com.geek.exercise.responses;

import java.util.List;

import com.geek.exercise.transfer.Account;

public class RegisteredResponse extends Response {

	private List<Account> mAccounts;
	
	public RegisteredResponse( Builder builder ) {
		super();
		
		mAccounts = builder.accounts;
	}
	
	public List<Account> getAccounts() {
		return mAccounts;
	}
	
	public int sizeOfCollection() {
		if ( mAccounts != null ) {
			return mAccounts.size();
		}
		
		return 0;
	}
	
	public boolean isEmpty() {
		return ! ( sizeOfCollection() > 0 );
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}
	
	public static class Builder {
		
		private List<Account> accounts;
		
		public Builder setAccounts( List<Account> accounts ) {
			this.accounts = accounts;
			
			return this;
		}
		
		public RegisteredResponse build() {
			return new RegisteredResponse( this );
		}
		
	}

}
