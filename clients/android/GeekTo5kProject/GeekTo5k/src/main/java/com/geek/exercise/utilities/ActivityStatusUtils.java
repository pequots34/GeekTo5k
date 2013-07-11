package com.geek.exercise.utilities;

import com.google.android.gms.location.DetectedActivity;

/**
 * Created by Pequots34 on 7/9/13.
 */
public class ActivityStatusUtils {

    private ActivityStatusUtils() {
        super();

        throw new InstantiationError();
    }

    public static boolean isOnTheMove( int type ) {
        switch ( type ) {
            case DetectedActivity.STILL :
            case DetectedActivity.TILTING :
            case DetectedActivity.UNKNOWN :
                return false;
            default:
                return true;
        }
    }

    public static String getActivityFromType( int type ) {
        switch( type ) {
            case DetectedActivity.IN_VEHICLE:
                return "driving";
            case DetectedActivity.ON_BICYCLE:
                return "cycling";
            case DetectedActivity.ON_FOOT:
                return "on foot";
            case DetectedActivity.STILL:
                return "standing";
            case DetectedActivity.TILTING:
                return "tilting";
            default:
                return "unknown";
        }
    }
}
