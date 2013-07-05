package com.geek.exercise.throwables;

public class DataAccessLayerException extends Throwable {

	private static final long serialVersionUID = -877445079126701907L;

	private Object mExtra;
	
	public DataAccessLayerException() {
		super();
	}

	public DataAccessLayerException( String message, Throwable cause ) {
		super( message, cause );
	}

	public DataAccessLayerException( String message ) {
		super( message );
	}

	public DataAccessLayerException( Throwable cause ) {
		super( cause );
	}

	public Object getExtra() {
		return mExtra;
	}

	public void setExtra( Object extra ) {
		mExtra = extra;
	}

}
