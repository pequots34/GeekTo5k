package com.geek.exercise.utilities;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class HTTPTransport {

	private static HttpClient mHttpClient;
	
	{
        HttpParams parameters = new BasicHttpParams();
        
        HttpProtocolParams.setVersion( parameters, HttpVersion.HTTP_1_1 );
        
        HttpProtocolParams.setContentCharset( parameters, "utf-8" );
        
        SchemeRegistry registry = new SchemeRegistry();
        
        registry.register( new Scheme( "http", PlainSocketFactory.getSocketFactory(), 80 ) );
        
        final SSLSocketFactory factory = SSLSocketFactory.getSocketFactory();
        
        factory.setHostnameVerifier( SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER );
        
        registry.register( new Scheme( "https", factory, 443 ) );
 
        ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager( parameters, registry );
        
        mHttpClient = new DefaultHttpClient( manager, parameters );
	}
	
	private HTTPTransport() {
		super();
		
		throw new InstantiationError();
	}
	
	public static HttpResponse execute( HttpPost method ) throws ClientProtocolException, IOException {
		return mHttpClient.execute( method );
	}
	
	public static HttpResponse execute( HttpGet method ) throws ClientProtocolException, IOException {
		return mHttpClient.execute( method );
	}

}
