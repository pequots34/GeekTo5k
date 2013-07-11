package com.geek.exercise.fragments;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.geek.exercise.R;
import com.geek.exercise.managers.StateManager;
import com.geek.exercise.network.requests.MessageRequest;
import com.geek.exercise.services.ActivityIntentService;
import com.geek.exercise.transfer.Account;
import com.geek.exercise.transfer.ActivityStatus;
import com.geek.exercise.utilities.ActivityStatusUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.ActivityRecognitionClient;
import org.json.JSONObject;

/**
 * Created by Pequots34 on 7/8/13.
 */
public class ActivityStatusFragment extends ListFragment implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    public static final int DETECTION_INTERVAL_SECONDS = 10;

    public static final int MILLISECONDS_PER_SECOND = 1000;

    public static final int DETECTION_INTERVAL_MILLISECONDS = MILLISECONDS_PER_SECOND * DETECTION_INTERVAL_SECONDS;

    public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private static final String MESSAGE_REQUEST_TAG = "message";

    private static final String TAG = ActivityStatusFragment.class.getSimpleName();

    private IActivityStatusListener mActivityStatusListener;

    private ActivityRecognitionClient mActivityRecognitionClient;

    private PendingIntent mActivityRecognitionPendingIntent;

    private View mBanner;

    private RequestQueue mRequestQueue;

    private ActivityStatusAdapter mActivityStatusAdapter;

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

        mActivityStatusAdapter = new ActivityStatusAdapter( getActivity() );
    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach( activity );

        try {
            mActivityStatusListener = (IActivityStatusListener) activity;
        } catch( ClassCastException e ) {
            throw new ClassCastException( e.toString() );
        }
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );

        setListAdapter( mActivityStatusAdapter );
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
    public void onDestroy() {
        mRequestQueue.cancelAll( MESSAGE_REQUEST_TAG );

        super.onDestroy();
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
            mBanner.setBackgroundResource( ActivityStatusUtils.getLayerDrawableByType( activity.getType() ) );

            Account account = StateManager.ApplicationManager.INSTANCE.getAccount();

            MessageRequest message = MessageRequest.newBuilder()
                    .setStatus( activity )
                    .setUsername( account != null ? account.getUsername() : null )
                    .build();

            JSONObject data = message.toJSONObject();

            Toast.makeText( getActivity(), message.toPayload().toString(), Toast.LENGTH_LONG ).show();

            JsonObjectRequest request = new JsonObjectRequest( Request.Method.POST, message.toURL(), data, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse( JSONObject data ) {
                    Toast.makeText( getActivity(), data.toString(), Toast.LENGTH_SHORT ).show();
                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse( VolleyError error ) {
                    Toast.makeText( getActivity(), error.getMessage(), Toast.LENGTH_LONG ).show();
                }
            } );

            request.setTag( MESSAGE_REQUEST_TAG );

            mRequestQueue.add( request );
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

    public static interface IActivityStatusListener {

    }

    public static class ActivityStatusAdapter extends ArrayAdapter<String> {

        public ActivityStatusAdapter( Context context ) {
            super( context, -1 );
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent ) {
            ViewHolder holder = null;

            if ( convertView == null ) {
                convertView = LayoutInflater.from( getContext() ).inflate( R.layout.status_item, null );

                if ( convertView != null ) {
                    holder = new ViewHolder();

                    holder.status = (TextView) convertView.findViewById( R.id.status );

                    holder.time = (TextView) convertView.findViewById( R.id.time );
                }
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if ( holder != null ) {
                int color = position % 2 == 0 ? getContext().getResources().getColor( R.color.grey ) :
                        getContext().getResources().getColor( R.color.dark_grey );

                convertView.setBackgroundColor( color );
            }

            return convertView;
        }
    }

    public static class ViewHolder {

        public TextView status;

        public TextView time;

    }
}
