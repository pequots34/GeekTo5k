package com.geek.exercise.services;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.geek.exercise.dao.RegistrationDataAccess;
import com.geek.exercise.requests.RegistrationRequest;
import com.geek.exercise.responses.ErrorResponse;
import com.geek.exercise.responses.RegisteredResponse;
import com.geek.exercise.responses.RegistrationFlushResponse;
import com.geek.exercise.responses.RegistrationResponse;
import com.geek.exercise.responses.Response;
import com.geek.exercise.throwables.DataAccessLayerException;
import com.geek.exercise.transfer.Account;
import com.geek.exercise.utilities.PropertyPlaceholderUtil;

public class RegistrationServiceImpl implements RegistrationService {

	private RegistrationDataAccess mRegistrationDataAccess;
	
	@Autowired
	public RegistrationServiceImpl( RegistrationDataAccess registrationDao ) {
		super();
		
		mRegistrationDataAccess = registrationDao;
	}

	@Override
	public Response register( RegistrationRequest request ) {
		final String channelId = request.getChannelId();
		
		if ( StringUtils.isEmpty( channelId ) ) {
			return ErrorResponse.newBuilder()
					.setMessage( "channel id is required to register client!" )
					.build();
		}
		
		Account account = mRegistrationDataAccess.getAccountById( channelId );
		
		if ( account != null ) {
			return ErrorResponse.newBuilder()
					.setExtra( channelId )
					.setMessage( new StringBuilder()
							.append( "channel id already exists: " )
							.append( request.getChannelId() )
							.toString() )
					.build();
		}
		
		long registered = -1;
		
		try {
			registered = mRegistrationDataAccess.register( channelId );
		} catch ( DataAccessLayerException e ) {
			return ErrorResponse.newBuilder()
					.setMessage( e.toString() )
					.build();
		}
		
		if ( registered > 0 ) {
			return RegistrationResponse.newBuilder()
					.setAccount( mRegistrationDataAccess.getAccountById( channelId ) )
					.build();
		}
		
		return null;
	}

	@Override
	public RegisteredResponse getRegistered() {
		return RegisteredResponse.newBuilder()
				.setAccounts( mRegistrationDataAccess.getRegistered() )
				.build();
	}

	@Override
	public Response flush() {
		long count = mRegistrationDataAccess.flush();
		
		if ( count > 0 ) {
			return RegistrationFlushResponse.newBuilder()
					.setCount( count )
					.build();
		} else {
			return ErrorResponse.newBuilder()
					.setExtra( PropertyPlaceholderUtil.useCacheStorage() )
					.setMessage( "no items to evict from storage!" )
					.build();
		}
	}

}
