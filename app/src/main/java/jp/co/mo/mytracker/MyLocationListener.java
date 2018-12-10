package jp.co.mo.mytracker;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocationListener implements LocationListener{

    public static Location location;
    public static boolean isRunning = false;

    public MyLocationListener() {
        isRunning = true;
        location = new Location("initial location");
    }


    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
