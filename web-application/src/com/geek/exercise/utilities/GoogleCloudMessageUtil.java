package com.geek.exercise.utilities;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.geek.exercise.transfer.GoogleMessage;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.gson.Gson;

public class GoogleCloudMessageUtil {

	public static final int DEFAULT_SUB_CHANNEL_ID = 0;
	
	private GoogleCloudMessageUtil() {
		super();
		
		throw new InstantiationError();
	}
	
	public static String getMessagingEndpoint() {
		return PropertyPlaceholderUtil.getPropertyByKey( PropertyPlaceholderUtil.GOOGLE_CLOUD_MESSAGING_ENDPOINT );
	}
	
	public static String toAuthorizationHeader( GoogleTokenResponse token ) {
		if ( token != null ) {
			return token.getTokenType() + " " + token.getAccessToken();
		}
		
		return null;
	}
	
	public static HttpEntity toEntity( GoogleMessage message ) {
		if ( message != null ) {
			try {
				return new StringEntity( new Gson().toJson( message ).toString() );
			} catch ( UnsupportedEncodingException e ) {}
		}
		
		return null;
	}

}
