package com.geek.exercise.network.requests;

import com.geek.exercise.Logger;
import com.geek.exercise.network.INetworkRequest;
import com.geek.exercise.transfer.ActivityStatus;
import com.geek.exercise.utilities.ActivityStatusUtils;
import com.geek.exercise.utilities.NetworkUtils;
import com.google.android.gms.location.DetectedActivity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pequots34 on 7/11/13.
 */
public class MessageRequest implements INetworkRequest {

    private ActivityStatus mActivityStatus;

    private String mUsername;

    public MessageRequest( Builder builder ) {
        super();

        mActivityStatus = builder.activity;

        mUsername = builder.username;
    }

    public JSONObject toJSONObject() {
        JSONObject data = new JSONObject();

        JSONObject payload = toPayload();

        if ( payload != null ) {
            try {
                data.put( "payload", payload.toString() );
            } catch ( JSONException e ) {
                try {
                    data.put( "payload", ActivityStatusUtils.getActivityFromType( DetectedActivity.UNKNOWN ) );
                } catch ( JSONException ex ) {
                    Logger.error( ex.toString() );

                    return null;
                }
            }
        }

        return data;
    }

    public JSONObject toPayload() {
        JSONObject data = new JSONObject();

        if ( mActivityStatus != null ) {
            try {
                data.put( "username", mUsername );

                data.put( "activity", ActivityStatusUtils.getActivityFromType( mActivityStatus.getType() ) );

                data.put( "elapsed", mActivityStatus.getElapsedRealtime() );

                data.put( "time", mActivityStatus.getTime() );

                data.put( "type", mActivityStatus.getType() );
            } catch ( JSONException e ) {
                Logger.error( e.toString() );
            }
        }

        return data;
    }

    @Override
    public String toURL() {
        return NetworkUtils.newBaseBuilder()
                .appendPath( "message" )
                .appendPath( "send" )
                .toString();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        private ActivityStatus activity;

        private String username;

        public Builder() {
            super();
        }

        public Builder setUsername( String username ) {
            this.username = username;

            return this;
        }

        public Builder setStatus( ActivityStatus activity ) {
            this.activity = activity;

            return this;
        }

        public MessageRequest build() {
            return new MessageRequest( this );
        }
    }
}
