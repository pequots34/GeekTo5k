package com.geek.exercise.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.PropertyPlaceholderHelper;

public class PropertyPlaceholderUtil extends PropertyPlaceholderConfigurer {

	public static final String GOOGLE_REFRESH_TOKEN = "google.authorization.refresh.token";
	
	public static final String GOOGLE_CLIENT_ID = "google.authorization.clientid";
	
	public static final String GOOGLE_CLIENT_SECRET = "google.authorization.clientsecret";
	
	private static final String PROPERTY_PREFIX = "${";
	
	private static final String PROPERTY_SUFFIX = "}";
	
	private final PropertyPlaceholderHelper mPlaceholder = new PropertyPlaceholderHelper( PROPERTY_PREFIX, PROPERTY_SUFFIX );

	private static Map<String, String> mProperties;
	
	private PropertyPlaceholderUtil() {
		super();
	}

	@Override
	protected void processProperties( ConfigurableListableBeanFactory beanFactoryToProcess, Properties props ) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		
		mProperties = new HashMap<String, String>();
		
		for( Object entry : props.keySet() ) {
			String key = entry.toString();
			
			String value = mPlaceholder.replacePlaceholders( props.getProperty( key.toString() ), props);
			
			if ( key != null && !key.isEmpty() && value != null && !value.isEmpty() ) {
				mProperties.put(key, value);
			}
		}
	}
	
	public static String getPropertyByKey( String key ) {
		return mProperties.get( key );
	}
	
}
