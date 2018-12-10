package jp.co.mo.mytracker;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import jp.co.mo.mytracker.repository.DatabaseManager;

public class RebootOSReciever extends BroadcastReceiver {


    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null && intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            DatabaseManager.loadMyTrackers(context);

            if (!MyLocationListener.isRunning) {
                MyLocationListener locationService = new MyLocationListener();
                LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationService);
            }
            if (!MyLocationService.isRunning) {
                Intent i = new Intent(context, MyLocationService.class);
                context.startService(i);
            }

        }
    }
}
