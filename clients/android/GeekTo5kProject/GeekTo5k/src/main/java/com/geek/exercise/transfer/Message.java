package com.geek.exercise.transfer;

import com.geek.exercise.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pequots34 on 7/11/13.
 */
public class Message {

    private String mUsername;

    private String mActivity;

    private long mElapsedRealtime;

    private long mTime;

    private int mType;

    public Message( Builder builder ) {
        super();

        mUsername = builder.username;

        mActivity = builder.activity;

        mElapsedRealtime = builder.elapsedRealtime;

        mTime = builder.time;

        mType = builder.type;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getActivity() {
        return mActivity;
    }

    public long getElapsedRealtime() {
        return mElapsedRealtime;
    }

    public long getTime() {
        return mTime;
    }

    public int getType() {
        return mType;
    }

    public JSONObject toJSONObject() {
        JSONObject data = new JSONObject();

        try {
            data.put( "username", mUsername );

            data.put( "activity", mActivity );

            data.put( "elapsed", mElapsedRealtime );

            data.put( "time", mTime );

            data.put( "type", mType );
        } catch ( JSONException e ) {
            Logger.error(e.toString());
        }
        return data;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        private String username;

        private String activity;

        private long elapsedRealtime;

        private long time;

        private int type;

        public Builder() {
            super();
        }

        public Builder setUsername( String username ) {
            this.username = username;

            return this;
        }

        public Builder setActivity( String activity ) {
            this.activity = activity;

            return this;
        }

        public Builder setElapsedRealtime( long time ) {
            this.elapsedRealtime = time;

            return this;
        }

        public Builder setTime( long time ) {
            this.time = time;

            return this;
        }

        public Builder setType( int type ) {
            this.type = type;

            return this;
        }

        public Message build() {
            return new Message( this );
        }
    }
}
