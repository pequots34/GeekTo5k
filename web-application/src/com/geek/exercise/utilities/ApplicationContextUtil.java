package com.geek.exercise.utilities;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextUtil implements ApplicationContextAware {

	private static ApplicationContext mContext;

	public ApplicationContextUtil() {
		super();
	}

	@Override
	public void setApplicationContext( ApplicationContext context ) throws BeansException {
		mContext = context;
	}
	
	public static Object getBeanByName( String name ) {
		if ( mContext != null && mContext.containsBean( name ) ) {
			return mContext.getBean( name );
		}
		
		return null;
	}
}
