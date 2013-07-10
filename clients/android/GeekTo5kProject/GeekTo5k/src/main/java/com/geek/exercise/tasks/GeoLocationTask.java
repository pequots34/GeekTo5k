package com.geek.exercise.tasks;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import com.geek.exercise.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Pequots34 on 6/22/13.
 */
public class GeoLocationTask extends AsyncTask<Location, Void, Address> {

    private Context mContext;

    public GeoLocationTask(Context context) {
        super();

        mContext = context;
    }

    @Override
    protected Address doInBackground( Location... params ) {
        Geocoder geocoder = new Geocoder( mContext, Locale.getDefault() );

        Location location = params[0];

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation( location.getLatitude(), location.getLongitude(), 1 );
        } catch ( IOException e ) {
            Logger.error( e.toString() );
        } catch ( IllegalArgumentException e ) {
            Logger.error( e.toString() );
        }

        if ( addresses != null && addresses.size() > 0 ) {
            return addresses.get(0);
        }

        return null;
    }

}
