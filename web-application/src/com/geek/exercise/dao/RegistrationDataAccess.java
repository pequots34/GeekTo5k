package com.geek.exercise.dao;

import com.geek.exercise.throwables.DataAccessLayerException;
import com.geek.exercise.transfer.Account;

public interface RegistrationDataAccess {

	public Account getAccountById( String id );
	
	public long register( String id ) throws DataAccessLayerException;
	
}
