package com.geek.exercise.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.geek.exercise.R;
import com.geek.exercise.managers.StateManager;

/**
 * Created by Pequots34 on 7/9/13.
 */
public class AccountFragment extends Fragment {

    private IAccountListener mAccountListener;

    public AccountFragment() {
        super();
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
            mAccountListener = (IAccountListener) activity;
        } catch( ClassCastException e ) {
            throw new ClassCastException( e.toString() );
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View view = inflater.inflate( R.layout.fragment_account, null, false );

        return view;
    }

    public static interface IAccountListener {

    }

}
