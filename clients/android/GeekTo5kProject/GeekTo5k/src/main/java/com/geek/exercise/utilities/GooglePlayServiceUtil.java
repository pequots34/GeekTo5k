package com.geek.exercise.utilities;

import android.content.Context;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by Pequots34 on 7/9/13.
 */
public class GooglePlayServiceUtil {

    private GooglePlayServiceUtil() {
        super();

        throw new InstantiationError();
    }

    public static boolean isGoogleServicesAvailable( Context context ) {
        return ConnectionResult.SUCCESS == GooglePlayServicesUtil.isGooglePlayServicesAvailable( context );
    }

}
