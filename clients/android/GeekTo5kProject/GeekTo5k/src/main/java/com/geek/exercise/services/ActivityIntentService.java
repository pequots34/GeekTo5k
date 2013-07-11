package com.geek.exercise.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.geek.exercise.transfer.ActivityStatus;
import com.geek.exercise.Constants;
import com.google.android.gms.location.ActivityRecognitionResult;

/**
 * Created by Pequots34 on 7/8/13.
 */
public class ActivityIntentService extends IntentService {

    private static final String TAG = ActivityIntentService.class.getSimpleName();

    public ActivityIntentService() {
        super( TAG );
    }

    @Override
    protected void onHandleIntent( Intent intent ) {
        if ( ActivityRecognitionResult.hasResult( intent ) ) {
            Intent broadcast = new Intent();

            broadcast.setAction( Constants.ACTION_RECOGNITION_SERVICE );

            broadcast.putExtra( Constants.RECOGNITION_SERVICE_INTENT_EXTRA, ActivityStatus.newBuilder()
                    .setRecognitionResult( ActivityRecognitionResult.extractResult( intent ) )
                    .build() );

            LocalBroadcastManager.getInstance( this ).sendBroadcast( broadcast );
        }
    }

}
