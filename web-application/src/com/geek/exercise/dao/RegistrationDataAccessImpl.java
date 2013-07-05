package com.geek.exercise.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import com.geek.exercise.throwables.DataAccessLayerException;
import com.geek.exercise.transfer.Account;

public class RegistrationDataAccessImpl extends NamedParameterJdbcDaoSupport implements RegistrationDataAccess {

	private static final String ACCOUNT_BY_ID = "SELECT * FROM ACCOUNTS WHERE CHANNEL_ID = ?";
	
	private static final String ADD_ACCOUNT = "INSERT INTO ACCOUNTS ( CHANNEL_ID ) VALUES ( ? )";
	
	public RegistrationDataAccessImpl() {
		super();
	}

	@Override
	public Account getAccountById( String id ) {
		try{
			return getJdbcTemplate().queryForObject( ACCOUNT_BY_ID, new Object[] {
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
					PreparedStatement statement = connection.prepareStatement( ADD_ACCOUNT );
					
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
			Timestamp created = results.getTimestamp( "CREATED" );
			
			return Account.newBuilder()
					.setId( results.getLong( "ID" ) )
					.setCreated( created != null ? new DateTime( created.getTime() ) : null )
					.setChannelId( results.getString( "CHANNEL_ID" ) )
					.build();
		}
		
	}

}
