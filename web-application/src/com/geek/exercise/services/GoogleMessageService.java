package com.geek.exercise.services;

import java.util.List;

import com.geek.exercise.responses.Response;
import com.geek.exercise.transfer.Account;

public interface GoogleMessageService {
	
	public Response sendToGoogleCloudMessage( List<Account> accounts, String message );

}
