package com.geek.exercise.utilities;

import com.google.android.gms.location.DetectedActivity;

/**
 * Created by Pequots34 on 7/9/13.
 */
public class ActivityRecognitionUtils {

    private ActivityRecognitionUtils() {
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
                return "in_vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "on_bicycle";
            case DetectedActivity.ON_FOOT:
                return "on_foot";
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.UNKNOWN:
                return "unknown";
            case DetectedActivity.TILTING:
                return "tilting";
        }

        return "unknown";
    }
}
