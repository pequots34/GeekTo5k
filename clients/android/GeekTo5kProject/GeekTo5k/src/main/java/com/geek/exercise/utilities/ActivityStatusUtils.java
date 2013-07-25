package com.geek.exercise.utilities;

import com.google.android.gms.location.ActivityRecognitionResult;
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

    public static ActivityRecognitionResult getActivityRecognitionMockResultByType( int type ) {
        switch( type ) {
            case DetectedActivity.IN_VEHICLE:
                return new ActivityRecognitionResult( new DetectedActivity( DetectedActivity.IN_VEHICLE, 100 ),
                        System.currentTimeMillis(), System.currentTimeMillis() );
            case DetectedActivity.ON_BICYCLE:
                return new ActivityRecognitionResult( new DetectedActivity( DetectedActivity.ON_BICYCLE, 100 ),
                        System.currentTimeMillis(), System.currentTimeMillis() );
            case DetectedActivity.ON_FOOT:
                return new ActivityRecognitionResult( new DetectedActivity( DetectedActivity.ON_FOOT, 100 ),
                        System.currentTimeMillis(), System.currentTimeMillis() );
            case DetectedActivity.STILL:
                return new ActivityRecognitionResult( new DetectedActivity( DetectedActivity.STILL, 100 ),
                        System.currentTimeMillis(), System.currentTimeMillis() );
            case DetectedActivity.TILTING:
                return new ActivityRecognitionResult( new DetectedActivity( DetectedActivity.TILTING, 100 ),
                        System.currentTimeMillis(), System.currentTimeMillis() );
            default:
                return new ActivityRecognitionResult( new DetectedActivity( DetectedActivity.UNKNOWN, 100 ),
                        System.currentTimeMillis(), System.currentTimeMillis() );
        }
    }
}
