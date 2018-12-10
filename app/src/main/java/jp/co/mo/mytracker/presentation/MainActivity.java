package jp.co.mo.mytracker.presentation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Map;

import jp.co.mo.mytracker.MyLocationListener;
import jp.co.mo.mytracker.MyLocationService;
import jp.co.mo.mytracker.PermissionCallBackAction;
import jp.co.mo.mytracker.R;
import jp.co.mo.mytracker.presentation.login.LoginActivity;
import jp.co.mo.mytracker.repository.DatabaseManager;

public class MainActivity extends AbstractBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<String, String> map = DatabaseManager.loadMyTrackers(this);
        if(map.isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        checkPermission(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                new PermissionCallBackAction() {
                    @Override
                    public void success() {
                        startService();
                    }

                    @Override
                    public void failed() {

                    }
                });
    }


    @SuppressLint("MissingPermission")
    private void startService() {
        if(!MyLocationListener.isRunning) {
            MyLocationListener locationService = new MyLocationListener();
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0 , locationService);
        }
        if(!MyLocationService.isRunning) {
            Intent intent = new Intent(this, MyLocationService.class);
            startService(intent);
        }
    }

}
