package com.geek.exercise.services;

import java.io.IOException;

import com.geek.exercise.utilities.PropertyPlaceholderUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;

public class GoogleAuthorizationServiceImpl implements GoogleAuthorizationService {

	public GoogleAuthorizationServiceImpl() {
		super();
	}

	@Override
	public GoogleTokenResponse getAuthorization() throws IOException {
		return new GoogleRefreshTokenRequest( new NetHttpTransport(), new JacksonFactory(), PropertyPlaceholderUtil.getPropertyByKey( PropertyPlaceholderUtil.GOOGLE_REFRESH_TOKEN ), 
				PropertyPlaceholderUtil.getPropertyByKey( PropertyPlaceholderUtil.GOOGLE_CLIENT_ID ), PropertyPlaceholderUtil.getPropertyByKey( PropertyPlaceholderUtil.GOOGLE_CLIENT_SECRET ) )
			 .setGrantType( "refresh_token" )
			 .execute();
	}

}
