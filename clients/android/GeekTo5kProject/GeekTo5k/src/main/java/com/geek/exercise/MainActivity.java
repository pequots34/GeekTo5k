package com.geek.exercise;

import android.os.Bundle;
import android.app.Activity;
import com.geek.exercise.fragments.AccountFragment;

public class MainActivity extends Activity implements AccountFragment.IAccountListener {

    public MainActivity() {
        super();
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_main );
    }

    @Override
    public void onAccountChanged( String account, boolean stored ) {
        if ( !stored ) {
            return;
        }

    }
}
