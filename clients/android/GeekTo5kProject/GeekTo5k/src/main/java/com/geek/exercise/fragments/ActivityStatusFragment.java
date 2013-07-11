package com.geek.exercise.fragments;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
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
import com.geek.exercise.Constants;
import com.geek.exercise.R;
import com.geek.exercise.managers.StateManager;
import com.geek.exercise.network.requests.MessageRequest;
import com.geek.exercise.services.ActivityIntentService;
import com.geek.exercise.transfer.Account;
import com.geek.exercise.transfer.ActivityStatus;
import com.geek.exercise.transfer.IStatus;
import com.geek.exercise.transfer.Message;
import com.geek.exercise.utilities.ActivityStatusUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.ActivityRecognitionClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by Pequots34 on 7/8/13.
 */
public class ActivityStatusFragment extends ListFragment implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private static final int DETECTION_INTERVAL_SECONDS = 20;

    private static final int MILLISECONDS_PER_SECOND = 1000;

    private static final int DETECTION_INTERVAL_MILLISECONDS = MILLISECONDS_PER_SECOND * DETECTION_INTERVAL_SECONDS;

    private static final String MESSAGE_REQUEST_TAG = "message";

    private static final String TAG = ActivityStatusFragment.class.getSimpleName();

    private IActivityStatusListener mActivityStatusListener;

    private ActivityRecognitionClient mActivityRecognitionClient;

    private PendingIntent mActivityRecognitionPendingIntent;

    private View mBanner;

    private TextView mMe;

    private TextView mType;

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

        try {
            JSONArray collection = getSavedState();

            if ( collection.length() > 0 ) {
                for ( int i = 0; i < collection.length(); i ++ ) {
                    JSONObject data = collection.getJSONObject( i );

                    mActivityStatusAdapter.add(Message.newBuilder()
                            .setActivity(data.optString("activity", null))
                            .setUsername(data.optString("username", null))
                            .setElapsedRealtime(data.optLong("elapsed"))
                            .setTime(data.optLong("time"))
                            .setType(data.optInt("type"))
                            .build());
                }

                mActivityStatusAdapter.notifyDataSetChanged();
            }
        } catch ( JSONException e ) {
            Toast.makeText( getActivity(), e.toString(), Toast.LENGTH_SHORT ).show();
        }

        requestUpdates();
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View view = inflater.inflate( R.layout.fragment_activity_status, null, false );

        if ( view != null ) {
            mBanner = view.findViewById( R.id.banner );

            mType = (TextView) view.findViewById( R.id.type );

            mMe = (TextView) view.findViewById( R.id.me );
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
            IStatus status = activity.getStatusByType();

            mBanner.setBackgroundResource( status.getBannerResource() );

            mType.setText( status.getTextResource() );

            mMe.setTextColor( getResources().getColor( status.getColorStateResource() ) );

            mType.setTextColor( getResources().getColor( status.getColorStateResource() ) );

            mActivityStatusAdapter.setColorState( status.getColorStateResource() );

            Account account = StateManager.ApplicationManager.INSTANCE.getAccount();

            MessageRequest message = MessageRequest.newBuilder()
                    .setStatus( activity )
                    .setUsername( account != null ? account.getUsername() : null )
                    .build();

            JSONObject payload = message.toPayload();

            Toast.makeText( getActivity(), payload.toString(), Toast.LENGTH_LONG ).show();

            final JsonObjectRequest request = new JsonObjectRequest( Request.Method.POST, message.toURL(),
                    message.toJSONObject(), new Response.Listener<JSONObject>() {

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

            if ( ActivityStatusUtils.isOnTheMove( activity.getType() ) ) {
                mActivityStatusAdapter.add( message.toMessage() );

                mActivityStatusAdapter.notifyDataSetChanged();

                saveStateToClient( payload );
            }

            //mRequestQueue.add( request );
        }
    }

    public void setRequestPendingIntent( PendingIntent intent ) {
        mActivityRecognitionPendingIntent = intent;
    }

    public PendingIntent getRequestPendingIntent() {
        return mActivityRecognitionPendingIntent;
    }

    public void requestUpdates() {
        Toast.makeText( getActivity(), getString( R.string.activity_starting_updates ), Toast.LENGTH_SHORT).show();

        getActivityRecognitionClient().connect();
    }

    private JSONArray getSavedState() throws JSONException {
        return new JSONArray( getSharedPreferences().getString(Constants.PAYLOAD_EXTRA, new JSONArray().toString()) );
    }

    private void saveStateToClient( JSONObject payload ) {
        SharedPreferences preferences = getSharedPreferences();

        try {
            JSONArray collection = getSavedState();

            collection.put( payload );

            preferences.edit()
                    .putString( Constants.PAYLOAD_EXTRA, collection.toString() )
                    .commit();
        } catch ( JSONException e ) {
            Toast.makeText( getActivity(), e.toString(), Toast.LENGTH_SHORT ).show();
        }
    }

    private SharedPreferences getSharedPreferences() {
        return getActivity().getSharedPreferences( Constants.HISTORY_SHARED_PREFERENCES, Context.MODE_PRIVATE );
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

    public static class ActivityStatusAdapter extends ArrayAdapter<Message> {

        private static int MODULUS = 2;

        private int mColorState;

        public ActivityStatusAdapter( Context context ) {
            super( context, -1 );

            mColorState = R.color.searching;
        }

        public void setColorState( int color ) {
            mColorState = color;
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

            Message message = getItem( position );

            if ( holder != null && message != null ) {
                int color = position % MODULUS == 0 ? getContext().getResources().getColor( R.color.grey ) :
                        getContext().getResources().getColor( R.color.dark_grey );

                convertView.setBackgroundColor( color );

                holder.status.setTextColor( getContext().getResources().getColor( mColorState ) );

                holder.time.setTextColor(  getContext().getResources().getColor( mColorState ) );

                holder.status.setText( ActivityStatusUtils.getActivityFromType( message.getType() ).toUpperCase( Locale.US ) );
            }

            return convertView;
        }
    }

    public static class ViewHolder {

        public TextView status;

        public TextView time;

    }
}
