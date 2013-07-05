package com.geek.exercise.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import com.geek.exercise.throwables.DataAccessLayerException;
import com.geek.exercise.transfer.Account;

public class RegistrationDataAccessImpl extends NamedParameterJdbcDaoSupport implements RegistrationDataAccess {

	public RegistrationDataAccessImpl() {
		super();
	}

	@Override
	public Account getAccountById( String id ) {
		try{
			return getJdbcTemplate().queryForObject( "", new Object[] {
					id
			}, new AccountMapper() );
		} catch( DataAccessException e ) {
			return null;
		}
	}
	
	@Override
	public long register( final String id ) throws DataAccessLayerException {
		try {
			return getJdbcTemplate().update( new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement( Connection connection ) throws SQLException {
					PreparedStatement statement = connection.prepareStatement( "" );
					
					statement.setString( 1, id );
					
					return statement;
				}
			} );
		} catch( DataAccessException e ) {
			throw new DataAccessLayerException( e.toString() );
		}
	}
	
	public static final class AccountMapper implements RowMapper<Account> {

		@Override
		public Account mapRow( ResultSet results, int position ) throws SQLException {
			Account account = new Account();
			
			return account;
		}
		
	}

}
