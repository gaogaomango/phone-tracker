package jp.co.mo.mytracker.presentation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.co.mo.mytracker.MyLocationListener;
import jp.co.mo.mytracker.MyLocationService;
import jp.co.mo.mytracker.PermissionCallBackAction;
import jp.co.mo.mytracker.R;
import jp.co.mo.mytracker.common.Parameter;
import jp.co.mo.mytracker.presentation.login.LoginActivity;
import jp.co.mo.mytracker.presentation.map.MapsActivity;
import jp.co.mo.mytracker.presentation.tracker.MyTrackerActivity;
import jp.co.mo.mytracker.presentation.tracker.UserAdapter;
import jp.co.mo.mytracker.presentation.tracker.UserInfo;
import jp.co.mo.mytracker.repository.AppDataManager;
import jp.co.mo.mytracker.repository.DatabaseManager;

public class MainActivity extends AbstractBaseActivity {

    @BindView(R.id.contactList)
    ListView mContactListView;

    private List<UserInfo> mUserList;
    private UserAdapter mUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Map<String, String> map = DatabaseManager.loadMyTrackers(this);
        String phoneNumber = AppDataManager.getInstance().loadStringData(this, Parameter.KEY_PHONE_NUMBER);
        if (map == null || map.isEmpty() || TextUtils.isEmpty(phoneNumber)) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        mUserList = new ArrayList<>();
        mUserAdapter = new UserAdapter(mUserList, this);
        mContactListView.setAdapter(mUserAdapter);
        refresh();
        mContactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phoneNumber = mUserList.get(position).getPhoneNumber();
                if (!TextUtils.isEmpty(phoneNumber)) {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra(Parameter.KEY_PHONE_NUMBER, phoneNumber);
                    startActivity(intent);
                }
            }
        });

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

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        mUserList.clear();
        if(DatabaseManager.myTrackers == null || DatabaseManager.myTrackers.isEmpty()) {
            return;
        }

        for (Map.Entry e : DatabaseManager.myTrackers.entrySet()) {
            mUserList.add(new UserInfo(e.getValue().toString(), e.getKey().toString()));
        }
        mUserAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.logoutBtn)
    public void onClickLogoutBtn() {
        AppDataManager.getInstance().clearData(getApplicationContext(), Parameter.KEY_PHONE_NUMBER);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        stopService();
    }

    @SuppressLint("MissingPermission")
    private void startService() {
        if (!MyLocationListener.isRunning) {
            MyLocationListener locationService = new MyLocationListener();
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationService);
        }
        if (!MyLocationService.isRunning) {
            Intent intent = new Intent(this, MyLocationService.class);
            startService(intent);
        }
    }

    private void stopService() {
        MyLocationListener.isRunning = false;
        MyLocationService.isRunning = false;
        Intent intent = new Intent(this, MyLocationService.class);
        stopService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                return true;
            case R.id.addtracker:
                Intent intent = new Intent(this, MyTrackerActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
