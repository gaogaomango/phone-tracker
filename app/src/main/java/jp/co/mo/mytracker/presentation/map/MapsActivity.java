package jp.co.mo.mytracker.presentation.map;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import jp.co.mo.mytracker.R;
import jp.co.mo.mytracker.common.Parameter;
import jp.co.mo.mytracker.repository.DatabaseManager;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng mTargetLocation;
    private String mLastDateOnLine = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if(getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            loadLocation(bundle.getString(Parameter.KEY_PHONE_NUMBER));
        }
    }

    void loadLocation(String phoneNumber) {
        if(TextUtils.isEmpty(phoneNumber)) {
            return;
        }

        FirebaseDatabase.getInstance().getReference()
                .child(DatabaseManager.DATA_KEY_USERS)
                .child(phoneNumber)
                .child(DatabaseManager.DATA_KEY_LOCATION)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                        if(values == null || values.isEmpty()) {
                            return;
                        }

                        double lat = Double.parseDouble(values.get(DatabaseManager.DATA_KEY_LATITUDE).toString());
                        double lon = Double.parseDouble(values.get(DatabaseManager.DATA_KEY_LONGITUDE).toString());
                        mTargetLocation = new LatLng(lat, lon);
                        mLastDateOnLine = values.get(DatabaseManager.DATA_KEY_DATE).toString();
                        loadMap();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(mTargetLocation).title("Last updated: " + mLastDateOnLine));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mTargetLocation, 15));
    }
}
