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
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.geek.exercise.R;
import com.geek.exercise.managers.StateManager;
import com.geek.exercise.services.ActivityIntentService;
import com.geek.exercise.transfer.ActivityStatus;
import com.geek.exercise.utilities.ActivityStatusUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.ActivityRecognitionClient;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pequots34 on 7/8/13.
 */
public class ActivityStatusFragment extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    public static final int DETECTION_INTERVAL_SECONDS = 10;

    public static final int MILLISECONDS_PER_SECOND = 1000;

    public static final int DETECTION_INTERVAL_MILLISECONDS = MILLISECONDS_PER_SECOND * DETECTION_INTERVAL_SECONDS;

    public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private static final String TAG = ActivityStatusFragment.class.getSimpleName();

    private IActivityRecognitionListener mRecognitionListener;

    private ActivityRecognitionClient mActivityRecognitionClient;

    private PendingIntent mActivityRecognitionPendingIntent;

    private View mBanner;

    private RequestQueue mRequestQueue;

    public ActivityStatusFragment() {
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

        mRequestQueue = StateManager.ApplicationManager.INSTANCE.getRequestQueue();
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
        View view = inflater.inflate( R.layout.fragment_activity_status, null, false );

        if ( view != null ) {
            mBanner = view.findViewById( R.id.banner );
        }

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

    public void setCurrentActivity( ActivityStatus activity ) {
        if ( activity != null ) {
            mBanner.setBackgroundResource(ActivityStatusUtils.getLayerDrawableByType(activity.getType()));
        }
    }

    public void setType( String type ) {
        JSONObject data = new JSONObject();

        try {
            data.put( "message", type );
        } catch ( JSONException e ) {
            e.printStackTrace();
        }

         /*DetectedActivity probability = result.getMostProbableActivity();

            final int confidence = probability.getConfidence();

            final int type = probability.getType();

            Log.d( TAG, "CONFIDENCE: " + Integer.toString( confidence ) );

            Log.d( TAG, "ACTIVITY TYPE: " + getNameFromType( type ) );

            Intent broadcast = new Intent();

            broadcast.setAction( MainActivity.ACTION_REFRESH_ACTIVITY );

            broadcast.putExtra( "type", getNameFromType( type ) );

            LocalBroadcastManager.getInstance( this ).sendBroadcast( broadcast );

            if ( isOnTheMove( type ) && ( confidence >= 50 ) ) {
                // Toast.makeText( getApplicationContext(), getNameFromType( type ), Toast.LENGTH_SHORT ).show();
            }*/

        JsonObjectRequest request = new JsonObjectRequest( Request.Method.POST, "http://geek-to-5k.elasticbeanstalk.com/message/send", data, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse( JSONObject jsonObject ) {
                Toast.makeText( getActivity(), jsonObject.toString(), Toast.LENGTH_SHORT ).show();
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse( VolleyError volleyError ) {
                Toast.makeText( getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );

        //mRequestQueue.add( request );
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
