package com.geek.exercise;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;
import com.geek.exercise.fragments.AccountFragment;
import com.geek.exercise.managers.StateManager;
import com.geek.exercise.transfer.Account;
import com.geek.exercise.utilities.GooglePlayServiceUtils;
import com.geek.exercise.utilities.IntentUtils;

public class MainActivity extends Activity implements AccountFragment.IAccountListener {

    public MainActivity() {
        super();
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_main );

        if ( !StateManager.ApplicationManager.INSTANCE.isInitialized() ) {
            StateManager.ApplicationManager.INSTANCE.initialize( this );
        }

        if ( !GooglePlayServiceUtils.isGoogleServicesAvailable( this ) ) {
            Toast.makeText( this, getString( R.string.common_google_play_services_unknown_issue ), Toast.LENGTH_LONG ).show();

            finish();

            return;
        }

        if ( getIntent().getBooleanExtra( Constants.FORCE_KILL_EXTRA, false ) ) {
            finish();

            return;
        }

        final SharedPreferences preferences = getSharedPreferences( Constants.ACCOUNT_PREFERENCE_NAME, Context.MODE_PRIVATE );

        final String username = preferences.getString( Constants.ACCOUNT_PREFERENCE_USERNAME, null );

        if ( username != null && !Constants.EMPTY_STRING.equalsIgnoreCase( username ) ) {
            StateManager.ApplicationManager.INSTANCE.setAccount( Account.newBuilder()
                    .setUsername(username)
                    .setCreated(preferences.getLong(Constants.ACCOUNT_PREFERENCE_CREATED, -1))
                .build() );

            startActivity( IntentUtils.getActivityRecognitionIntent( this ) );

            finish();
        }
    }

    @Override
    public void onAccountChanged( String account, boolean stored ) {
        if ( !stored ) {
            Toast.makeText( this, getString( R.string.account_store_exception ), Toast.LENGTH_LONG ).show();

            finish();

            return;
        }

        startActivity( IntentUtils.getActivityRecognitionIntent( this ) );

        finish();
    }
}
