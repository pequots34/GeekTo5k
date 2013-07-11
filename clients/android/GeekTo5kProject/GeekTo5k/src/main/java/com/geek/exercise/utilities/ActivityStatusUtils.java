package com.geek.exercise.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.geek.exercise.R;
import com.google.android.gms.location.DetectedActivity;

/**
 * Created by Pequots34 on 7/9/13.
 */
public class ActivityStatusUtils {

    private ActivityStatusUtils() {
        super();

        throw new InstantiationError();
    }

    public static int getLayerDrawableByType( int type ) {
        switch( type ) {
            case DetectedActivity.IN_VEHICLE:
                return R.drawable.bg_vehicle;
            case DetectedActivity.ON_BICYCLE:
                return R.drawable.bg_cycling;
            case DetectedActivity.ON_FOOT:
                return R.drawable.bg_walking;
            default:
                return R.drawable.bg_cycling;
        }
    }

    public static Drawable getLayerDrawableByType( int type, Context context ) {
        final  Resources resources = context != null ? context.getResources() : null;

        if ( resources != null ) {
            return resources.getDrawable( getLayerDrawableByType( type ) );
        }

        return null;
    }

    public static Drawable getActionBarDrawableByType( int type, Context context ) {
        final  Resources resources = context != null ? context.getResources() : null;

        if ( resources != null ) {
            switch( type ) {
                case DetectedActivity.IN_VEHICLE:
                    return resources.getDrawable( R.drawable.ic_car_ab );
                case DetectedActivity.ON_BICYCLE:
                    return resources.getDrawable( R.drawable.ic_bike_ab );
                case DetectedActivity.ON_FOOT:
                    return resources.getDrawable( R.drawable.ic_walk_ab );
                default:
                    return null;
            }
        }

        return null;
    }

    public static boolean isOnTheMove( int type ) {
        switch ( type ) {
            case DetectedActivity.STILL :
            case DetectedActivity.TILTING :
            case DetectedActivity.UNKNOWN :
                return false;
            default:
                return true;
        }
    }

    public static String getActivityFromType( int type ) {
        switch( type ) {
            case DetectedActivity.IN_VEHICLE:
                return "in_vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "on_bicycle";
            case DetectedActivity.ON_FOOT:
                return "on_foot";
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.UNKNOWN:
                return "unknown";
            case DetectedActivity.TILTING:
                return "tilting";
            default:
                return "unknown";
        }
    }
}
