package com.geek.exercise.responses;

import java.util.Locale;

public class ErrorResponse extends Response {

	public static final String GATEWAY_ERROR_MESSAGE = "Server monkeys were unable to process your request!";
	
	private String mMessage;
	
	private Object mExtra;
	
	public ErrorResponse( Builder builder ) {
		super();
		
		mMessage = builder.message;
		
		mExtra = builder.extra;
	}
	
	@Override
	public ResponseStatus getStatus() {
		return ResponseStatus.ERROR;
	}
	
	public Object getExtra() {
		return mExtra;
	}
	
	public String getMessage() {
		return mMessage;
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}
	
	public static final class Builder {
		
		private String message;
		
		private Object extra;
		
		public Builder() {
			super();
		}
		
		public Builder setExtra( Object extra ) {
			this.extra = extra;
			
			return this;
		}
		
		public Builder setMessage( String message ) {
			this.message = toMessage( message );
			
			return this;
		}
		
		public Builder setMessage( Throwable throwable ) {
			this.message = throwable != null ? toMessage( throwable.toString() ) : null;
			
			return this;
		}
		
		public ErrorResponse build() {
			return new ErrorResponse( this );
		}
		
		private static String toMessage( String msg ) {
	        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

	        String caller = "<unknown>";
	        
	        for ( int i = 2; i < trace.length; i++ ) {
	            Class<?> clazz = trace[i].getClass();
	            
	            if ( !clazz.equals( ErrorResponse.class ) ) {
	                String name = trace[i].getClassName();
	                
	                name = name.substring( name.lastIndexOf( '.' ) + 1 );
	                
	                name = name.substring( name.lastIndexOf( '$' ) + 1 );

	                caller = name + "." + trace[i].getMethodName();
	                
	                break;
	            }
	        }
	        
	        return String.format( Locale.US, "[%d] %s: %s", Thread.currentThread().getId(), caller, msg );
	    }
		
	}

}
