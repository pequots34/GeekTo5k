package com.geek.exercise.transfer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pequots34 on 7/10/13.
 */
public class Account implements Parcelable {

    private String mUsername;

    public Account( Builder builder ) {
        super();

        mUsername = builder.username;
    }

    private Account( Parcel in ) {
        super();

        mUsername = in.readString();
    }

    public String getUsername() {
        return mUsername;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel( Parcel destination, int flags ) {
        destination.writeString( mUsername );
    }

    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {

        public Account createFromParcel(Parcel in) {
            return new Account( in );
        }

        public Account[] newArray( int size ) {
            return new Account[ size ];
        }
    };

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        private String username;

        public Builder() {
            super();
        }

        public Builder setUsername( String username ) {
            this.username = username;

            return this;
        }

        public Account build() {
            return new Account( this );
        }
    }
}
