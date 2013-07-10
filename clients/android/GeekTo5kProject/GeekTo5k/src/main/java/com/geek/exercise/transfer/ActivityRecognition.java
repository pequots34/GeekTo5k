package com.geek.exercise.transfer;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

/**
 * Created by Pequots34 on 7/9/13.
 */
public class ActivityRecognition implements Parcelable {

    private ActivityRecognitionResult mRecognition;

    private long mTime;

    private long mElapsedRealtime;

    private DetectedActivity mProbability;

    public ActivityRecognition( Builder builder ) {
        super();

        mRecognition = builder.recognition;

        if ( mRecognition != null ) {
            mTime = mRecognition.getTime();

            mElapsedRealtime = mRecognition.getElapsedRealtimeMillis();

            mProbability = mRecognition.getMostProbableActivity();
        }
    }

    private ActivityRecognition( Parcel in ) {
        super();

        mRecognition = in.readParcelable( ActivityRecognitionResult.class.getClassLoader() );

        mTime = in.readLong();

        mElapsedRealtime = in.readLong();

        mProbability = in.readParcelable( DetectedActivity.class.getClassLoader() );
    }

    public DetectedActivity getProbability() {
        return mProbability;
    }

    public long getTime() {
        return mTime;
    }

    public long getElapsedRealtime() {
        return mElapsedRealtime;
    }

    public ActivityRecognitionResult getRecognition() {
        return mRecognition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel( Parcel destination, int flags ) {
        destination.writeParcelable( mRecognition, flags );

        destination.writeLong( mTime );

        destination.writeLong( mElapsedRealtime );

        destination.writeParcelable( mProbability, flags );
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final Parcelable.Creator<ActivityRecognition> CREATOR = new Parcelable.Creator<ActivityRecognition>() {

        public ActivityRecognition createFromParcel(Parcel in) {
            return new ActivityRecognition( in );
        }

        public ActivityRecognition[] newArray( int size ) {
            return new ActivityRecognition[ size ];
        }
    };

    public static final class Builder {

        private ActivityRecognitionResult recognition;

        public Builder() {
            super();
        }

        public Builder setRecognitionResult( ActivityRecognitionResult recognition ) {
            this.recognition = recognition;

            return this;
        }

        public ActivityRecognition build() {
            return new ActivityRecognition( this );
        }
    }
}
