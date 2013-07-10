package com.geek.exercise.managers;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.geek.exercise.transfer.Account;

/**
 * Created by Pequots34 on 7/9/13.
 */
public class StateManager {

    private StateManager() {
        super();

        throw new InstantiationError();
    }

    public enum ApplicationManager {
        INSTANCE;

        private RequestQueue mRequestQueue;

        private Account mAccount;

        public void initialize( Context context ) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        public void setAccount( Account account ) {
            mAccount = account;
        }

        public Account getAccount() {
            return mAccount;
        }

        public RequestQueue getRequestQueue() {
            return mRequestQueue;
        }

        public boolean isInitialized() {
            return mRequestQueue != null;
        }
    }
}
