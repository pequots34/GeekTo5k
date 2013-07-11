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
import com.geek.exercise.fragments.ActivityStatusFragment;
import com.geek.exercise.managers.StateManager;
import com.geek.exercise.transfer.ActivityStatus;
import com.geek.exercise.utilities.ActivityStatusUtils;
import com.geek.exercise.utilities.GooglePlayServiceUtils;
import com.geek.exercise.utilities.IntentUtils;

/**
 * Created by Pequots34 on 7/9/13.
 */
public class ActivityStatusActivity extends Activity implements ActivityStatusFragment.IActivityStatusListener {

    private IntentFilter mIntentFilter;

    private LocalBroadcastManager mLocalBroadcastManager;

    private ActivityStatusFragment mActivityStatus;

    private ActivityStatus mCurrentActivity;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive( Context context, Intent intent ) {
            ActivityStatus recognition = intent.getParcelableExtra( Constants.RECOGNITION_SERVICE_INTENT_EXTRA );

            Toast.makeText( ActivityStatusActivity.this, ActivityStatusUtils.getActivityFromType( recognition.getType() ), Toast.LENGTH_LONG ).show();

            if ( recognition != null && recognition.compareTo( mCurrentActivity ) != 0 && ActivityStatusUtils.isOnTheMove( recognition.getType() ) ) {
                mCurrentActivity = recognition;

                mActivityStatus.setCurrentActivity( mCurrentActivity );
            }
        }
    };

    public ActivityStatusActivity() {
        super();
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_status);

        if ( !StateManager.ApplicationManager.INSTANCE.isInitialized() ) {
            startActivity( IntentUtils.getMainIntent( this ) );

            finish();

            return;
        }

        if ( !GooglePlayServiceUtils.isGoogleServicesAvailable( this ) ) {
            Intent intent = IntentUtils.getMainIntent( this );

            intent.putExtra( Constants.FORCE_KILL_EXTRA, true );

            startActivity( intent );

            finish();

            return;
        }

        mLocalBroadcastManager = LocalBroadcastManager.getInstance( this );

        mIntentFilter = new IntentFilter( Constants.ACTION_RECOGNITION_SERVICE );

        mActivityStatus = (ActivityStatusFragment) getFragmentManager().findFragmentById( R.id.recognition );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate( R.menu.activity_status, menu );

        return true;
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent intent ) {
        switch ( requestCode ) {
            case ActivityStatusFragment.CONNECTION_FAILURE_RESOLUTION_REQUEST:
                switch( resultCode ) {
                    case RESULT_OK:

                        break;
                    default:
                        Logger.debug( "connection failure result code not ok" );

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
                Toast.makeText( this, getString( R.string.activity_starting_updates ), Toast.LENGTH_SHORT).show();

                mActivityStatus.requestUpdates();

                return true;
            case R.id.action_stop_recognition_updates:
                //TODO: STOP UPDATES

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
