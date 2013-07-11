package com.geek.exercise.transfer;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

/**
 * Created by Pequots34 on 7/9/13.
 */
public class ActivityStatus implements Parcelable, Comparable<ActivityStatus> {

    private ActivityRecognitionResult mRecognition;

    private long mTime;

    private long mElapsedRealtime;

    private DetectedActivity mProbability;

    public ActivityStatus(Builder builder) {
        super();

        mRecognition = builder.recognition;

        if ( mRecognition != null ) {
            mTime = mRecognition.getTime();

            mElapsedRealtime = mRecognition.getElapsedRealtimeMillis();

            mProbability = mRecognition.getMostProbableActivity();
        }
    }

    private ActivityStatus(Parcel in) {
        super();

        mRecognition = in.readParcelable( ActivityRecognitionResult.class.getClassLoader() );

        mTime = in.readLong();

        mElapsedRealtime = in.readLong();

        mProbability = in.readParcelable( DetectedActivity.class.getClassLoader() );
    }

    public int getType() {
        if ( mProbability != null ) {
            return mProbability.getType();
        }

        return -34;
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
    public int compareTo( ActivityStatus another ) {
        if ( another == null ) {
            return -1;
        }

        return getType() - another.getType();
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

    public static final Parcelable.Creator<ActivityStatus> CREATOR = new Parcelable.Creator<ActivityStatus>() {

        public ActivityStatus createFromParcel(Parcel in) {
            return new ActivityStatus( in );
        }

        public ActivityStatus[] newArray( int size ) {
            return new ActivityStatus[ size ];
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

        public ActivityStatus build() {
            return new ActivityStatus( this );
        }
    }
}
