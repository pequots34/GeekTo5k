package com.geek.exercise;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.geek.exercise.fragments.ActivityRecognitionFragment;
import com.geek.exercise.transfer.ActivityRecognition;
import com.geek.exercise.utilities.GooglePlayServiceUtil;

/**
 * Created by Pequots34 on 7/9/13.
 */
public class ActivityRecognitionActivity extends Activity implements ActivityRecognitionFragment.IActivityRecognitionListener {

    private IntentFilter mIntentFilter;

    private LocalBroadcastManager mLocalBroadcastManager;

    private ActivityRecognitionFragment mActivityRecognition;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive( Context context, Intent intent ) {
            ActivityRecognition recognition = intent.getParcelableExtra( Constants.RECOGNITION_SERVICE_INTENT_EXTRA );
        }
    };

    public ActivityRecognitionActivity() {
        super();
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_recognition );

        mLocalBroadcastManager = LocalBroadcastManager.getInstance( this );

        mIntentFilter = new IntentFilter( Constants.ACTION_RECOGNITION_SERVICE );

        mActivityRecognition = (ActivityRecognitionFragment) getFragmentManager().findFragmentById( R.id.recognition );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate( R.menu.activity_recognition, menu );

        return true;
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent intent ) {
        switch ( requestCode ) {
            case ActivityRecognitionFragment.CONNECTION_FAILURE_RESOLUTION_REQUEST:
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
                if ( GooglePlayServiceUtil.isGoogleServicesAvailable(this) ) {
                    Toast.makeText(this, "Starting activity request!", Toast.LENGTH_SHORT).show();

                    mActivityRecognition.requestUpdates();
                }

                return true;
            case R.id.action_stop_recognition_updates:
                if ( GooglePlayServiceUtil.isGoogleServicesAvailable(this) ) {

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

}
