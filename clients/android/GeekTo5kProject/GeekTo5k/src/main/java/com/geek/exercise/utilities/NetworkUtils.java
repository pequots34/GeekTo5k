package com.geek.exercise.utilities;

import android.net.Uri;

import java.util.Locale;

/**
 * Created by Pequots34 on 7/11/13.
 */
public class NetworkUtils {

    private static final String PRODUCTION = "http://geek-to-5k.elasticbeanstalk.com";

    private NetworkUtils() {
        super();

        throw new InstantiationError();
    }

    public static Uri.Builder newBaseBuilder() {
        Locale locale = Locale.getDefault();

        return Uri.parse( PRODUCTION ).buildUpon()
                .appendQueryParameter( "country", locale.getCountry() )
                .appendQueryParameter( "language", locale.getLanguage() )
                .appendQueryParameter( "device", "ANDROID" );
    }

}
