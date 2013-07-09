package com.geek.exercise;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.geek.exercise.fragments.ActivityRecognition;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends Activity implements ActivityRecognition.IActivityRecognitionListener {

    public static final String ACTION_REFRESH_ACTIVITY = "com.priceline.labs.recognition.ACTION_REFRESH_ACTIVITY";

    private IntentFilter mIntentFilter;

    private LocalBroadcastManager mLocalBroadcastManager;

    private ActivityRecognition mActivityRecognition;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive( Context context, Intent intent ) {
            Toast.makeText(context, intent.getStringExtra("type"), Toast.LENGTH_LONG).show();
        }
    };

    public MainActivity() {
        super();
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_main );

        mLocalBroadcastManager = LocalBroadcastManager.getInstance( this );

        mIntentFilter = new IntentFilter( ACTION_REFRESH_ACTIVITY );

        mActivityRecognition = (ActivityRecognition) getFragmentManager().findFragmentById( R.id.recognition );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate( R.menu.main, menu );

        return true;
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent intent ) {
        switch ( requestCode ) {
            case ActivityRecognition.CONNECTION_FAILURE_RESOLUTION_REQUEST:
                switch( resultCode ) {
                    case RESULT_OK:
                        break;
                    default:
                        break;
                }
                break;
            default:

        }
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        switch ( item.getItemId() ) {
            case R.id.action_start_recognition_updates:
                if ( isGoogleServicesAvailable() ) {
                    mActivityRecognition.requestUpdates();
                }

                return true;
            case R.id.action_stop_recognition_updates:
                if ( isGoogleServicesAvailable() ) {

                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        mLocalBroadcastManager.unregisterReceiver( mBroadcastReceiver );

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLocalBroadcastManager.registerReceiver( mBroadcastReceiver, mIntentFilter );
    }

    public boolean isGoogleServicesAvailable() {
        return ConnectionResult.SUCCESS == GooglePlayServicesUtil.isGooglePlayServicesAvailable( this );
    }
    
}
