package com.geek.exercise.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import com.geek.exercise.throwables.DataAccessLayerException;
import com.geek.exercise.transfer.Account;
import com.geek.exercise.utilities.PropertyPlaceholderUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class RegistrationDataAccessImpl extends NamedParameterJdbcDaoSupport implements RegistrationDataAccess {

	private static final String ACCOUNT_BY_ID = "SELECT * FROM ACCOUNTS WHERE CHANNEL_ID = ?";
	
	private static final String ADD_ACCOUNT = "INSERT INTO ACCOUNTS ( CHANNEL_ID ) VALUES ( ? )";
	
	private static final String REGISTERED_ACCOUNTS = "SELECT * FROM ACCOUNTS ORDER BY CREATED DESC";
	
	private static Cache<String, Account> mAccountsCache;
	
	public RegistrationDataAccessImpl() {
		super();
	}

	@Override
	public Account getAccountById( String id ) {
		try{
			if ( PropertyPlaceholderUtil.useCacheStorage() ) {
				return getLocalCache().getIfPresent( id );
			} else {
				return getJdbcTemplate().queryForObject( ACCOUNT_BY_ID, new Object[] {
						id
				}, new AccountMapper() );
			}
		} catch( DataAccessException e ) { }
		
		return null;
	}
	
	@Override
	public long register( final String id ) throws DataAccessLayerException {
		try {
			if ( PropertyPlaceholderUtil.useCacheStorage() ) {
				long created = System.currentTimeMillis();
				
				getLocalCache().put( id, Account.newBuilder() 
						.setChannelId( id )
						.setCreated( new DateTime( created ) )
						.setId( getLocalCache().size() + 1 )
						.build() );
				
				return created;
			} else {
				return getJdbcTemplate().update( new PreparedStatementCreator() {
					
					@Override
					public PreparedStatement createPreparedStatement( Connection connection ) throws SQLException {
						PreparedStatement statement = connection.prepareStatement( ADD_ACCOUNT );
						
						statement.setString( 1, id );
						
						return statement;
					}
				} );
			}
		} catch( DataAccessException e ) {
			throw new DataAccessLayerException( e.toString() );
		}
	}
	
	@Override
	public List<Account> getRegistered() {
		try{
			if ( PropertyPlaceholderUtil.useCacheStorage() ) {
				return new ArrayList<Account>( getLocalCache().asMap().values() );
			} else {
				return getJdbcTemplate().query( REGISTERED_ACCOUNTS, new AccountMapper() );
			}
		} catch( DataAccessException e ) { }
		
		return null;
	}
	
	@Override
	public long flush() {
		try {
			if ( PropertyPlaceholderUtil.useCacheStorage() ) {
				long size = getLocalCache().size();
				
				getLocalCache().invalidateAll();
				
				return size;
			}
		} catch( DataAccessException e ) { }
		
		return 0;
	}
	
	private static Cache<String, Account> getLocalCache() {
		if ( mAccountsCache == null ) {
			mAccountsCache = CacheBuilder.newBuilder().build();
		}
		
		return mAccountsCache;
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
