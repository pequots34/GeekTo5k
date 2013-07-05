package com.geek.exercise.dao;

import com.geek.exercise.transfer.Account;

public interface RegistrationDataAccess {

	public Account getAccountById( String id );
	
}
