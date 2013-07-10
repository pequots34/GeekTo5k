package com.geek.exercise;

import android.util.Log;
import com.geek.exercise.BuildConfig;

import java.util.Locale;

/**
 * Created by Pequots34 on 6/1/13.
 */
public class Logger {

    public static String TAG = "PsychicRobot";

    public static final boolean DEBUG = BuildConfig.DEBUG && Log.isLoggable( TAG, Log.VERBOSE );

    private Logger() {
        super();

        throw new InstantiationError();
    }

    public static void verbose( String format, Object... args ) {
        if ( DEBUG ) {
            Log.v( TAG, builder( format, args ) );
        }
    }

    public static void debug( String format, Object... args ) {
        if ( DEBUG ) {
            Log.d( TAG, builder( format, args ) );
        }
    }

    public static void error( String format, Object... args ) {
        if ( DEBUG ) {
            Log.e( TAG, builder( format, args ) );
        }
    }

    public static void error( Throwable throwable, String format, Object... args ) {
        if ( DEBUG ) {
            Log.e( TAG, builder( format, args ), throwable );
        }
    }

    public static void wtf( String format, Object... args ) {
        if ( DEBUG ) {
            Log.wtf( TAG, builder( format, args ) );
        }
    }

    public static void wtf( Throwable throwable, String format, Object... args ) {
        if ( DEBUG ) {
            Log.wtf( TAG, builder( format, args ), throwable );
        }
    }

    private static String builder( String format, Object... args ) {
        String message = ( args == null ) ? format : String.format( Locale.US, format, args );

        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

        String caller = "<unknown>";

        for ( int i = 2; i < trace.length; i++ ) {
            Class<?> implementation = trace[i].getClass();

            if ( !implementation.equals( Logger.class ) ) {
                String calling = trace[i].getClassName();

                calling = calling.substring( calling.lastIndexOf('.') + 1 );

                calling = calling.substring( calling.lastIndexOf('$') + 1 );

                caller = calling + "." + trace[i].getMethodName();

                break;
            }
        }

        return String.format( Locale.US, "[%d] %s: %s", Thread.currentThread().getId(), caller, message );
    }

}
