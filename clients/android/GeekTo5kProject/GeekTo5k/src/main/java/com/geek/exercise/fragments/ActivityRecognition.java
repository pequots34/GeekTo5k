package com.geek.exercise.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.geek.exercise.R;
import com.geek.exercise.managers.StateManager;
import com.geek.exercise.services.ActivityIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.ActivityRecognitionClient;

/**
 * Created by Pequots34 on 7/8/13.
 */
public class ActivityRecognition extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    public static final int DETECTION_INTERVAL_SECONDS = 20;

    public static final int MILLISECONDS_PER_SECOND = 1000;

    public static final int DETECTION_INTERVAL_MILLISECONDS = MILLISECONDS_PER_SECOND * DETECTION_INTERVAL_SECONDS;

    public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private static final String TAG = ActivityRecognition.class.getSimpleName();

    private IActivityRecognitionListener mRecognitionListener;

    private ActivityRecognitionClient mActivityRecognitionClient;

    private PendingIntent mActivityRecognitionPendingIntent;

    public ActivityRecognition() {
        super();

        mActivityRecognitionPendingIntent = null;

        mActivityRecognitionClient = null;
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        setRetainInstance( true );

        if ( !StateManager.ApplicationManager.INSTANCE.isInitialized() ) {
            StateManager.ApplicationManager.INSTANCE.initialize( getActivity() );
        }
    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach( activity );

        try {
            mRecognitionListener = (IActivityRecognitionListener) activity;
        } catch( ClassCastException e ) {
            throw new ClassCastException( e.toString() );
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View view = inflater.inflate( R.layout.fragment_activity_recognition, null, false );

        return view;
    }

    @Override
    public void onConnected( Bundle bundle ) {
        getActivityRecognitionClient().requestActivityUpdates( DETECTION_INTERVAL_MILLISECONDS, createRequestPendingIntent() );

        getActivityRecognitionClient().disconnect();
    }

    @Override
    public void onDisconnected() {
        mActivityRecognitionClient = null;
    }

    @Override
    public void onConnectionFailed( ConnectionResult connectionResult ) {
        if ( connectionResult.hasResolution() ) {
            try {
                connectionResult.startResolutionForResult( getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST );
            } catch( IntentSender.SendIntentException e ) {
                Log.e(TAG, e.toString());
            }
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);

            if ( dialog != null ) {
                dialog.show();
            }
        }

    }

    public void setRequestPendingIntent( PendingIntent intent ) {
        mActivityRecognitionPendingIntent = intent;
    }

    public PendingIntent getRequestPendingIntent() {
        return mActivityRecognitionPendingIntent;
    }

    public void requestUpdates() {
        getActivityRecognitionClient().connect();
    }

    private PendingIntent createRequestPendingIntent() {
        if ( getRequestPendingIntent() != null ) {
            return mActivityRecognitionPendingIntent;
        } else {
            Intent intent = new Intent( getActivity(), ActivityIntentService.class );

            PendingIntent pendingIntent = PendingIntent.getService( getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );

            setRequestPendingIntent( pendingIntent );

            return pendingIntent;
        }

    }

    private ActivityRecognitionClient getActivityRecognitionClient() {
        if ( mActivityRecognitionClient == null ) {
            mActivityRecognitionClient = new ActivityRecognitionClient( getActivity(), this, this );
        }

        return mActivityRecognitionClient;
    }

    public static interface IActivityRecognitionListener {

    }
}
