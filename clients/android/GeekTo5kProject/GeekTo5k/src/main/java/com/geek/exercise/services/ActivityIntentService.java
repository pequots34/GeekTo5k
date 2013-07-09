package com.geek.exercise.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.geek.exercise.MainActivity;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

/**
 * Created by Pequots34 on 7/8/13.
 */
public class ActivityIntentService extends IntentService {

    private static final String TAG = ActivityIntentService.class.getSimpleName();

    public ActivityIntentService() {
        super( ActivityIntentService.class.getSimpleName() );
    }

    @Override
    protected void onHandleIntent( Intent intent ) {
        if ( ActivityRecognitionResult.hasResult( intent ) ) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult( intent );

            DetectedActivity probability = result.getMostProbableActivity();

            final int confidence = probability.getConfidence();

            final int type = probability.getType();

            Log.d( TAG, "CONFIDENCE: " + Integer.toString( confidence ) );

            Log.d( TAG, "ACTIVITY TYPE: " + getNameFromType( type ) );

            Intent broadcast = new Intent();

            broadcast.setAction( MainActivity.ACTION_REFRESH_ACTIVITY );

            broadcast.putExtra( "activity", intent );

            LocalBroadcastManager.getInstance( this ).sendBroadcast( broadcast );

            if ( isOnTheMove( type ) && ( confidence >= 50 ) ) {
                // Toast.makeText( getApplicationContext(), getNameFromType( type ), Toast.LENGTH_SHORT ).show();
            }

        }
    }

    private boolean isOnTheMove( int type ) {
        switch ( type ) {
            case DetectedActivity.STILL :
            case DetectedActivity.TILTING :
            case DetectedActivity.UNKNOWN :
                return false;
            default:
                return true;
        }
    }

    private String getNameFromType( int type ) {
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
