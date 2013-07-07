package com.geek.exercise.dao;

import java.util.List;

import com.geek.exercise.throwables.DataAccessLayerException;
import com.geek.exercise.transfer.Account;

public interface RegistrationDataAccess {

	public Account getAccountById( String id );
	
	public long register( String id ) throws DataAccessLayerException;

	public List<Account> getRegistered();
	
}
