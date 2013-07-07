package com.geek.exercise.services;

import java.io.IOException;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;

public interface GoogleAuthorizationService {

	public GoogleTokenResponse getAuthorization() throws IOException;
	
}
