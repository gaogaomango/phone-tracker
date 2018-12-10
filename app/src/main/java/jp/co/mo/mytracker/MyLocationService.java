package jp.co.mo.mytracker;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import jp.co.mo.mytracker.common.Parameter;
import jp.co.mo.mytracker.repository.AppDataManager;
import jp.co.mo.mytracker.repository.DatabaseManager;

public class MyLocationService extends IntentService {

    public static boolean isRunning = false;

    public MyLocationService() {
        super("MyLocationService");
        isRunning = true;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        final String phoneNumber = AppDataManager.getInstance().loadStringData(this, Parameter.KEY_PHONE_NUMBER);
        if(!TextUtils.isEmpty(phoneNumber)) {
            FirebaseDatabase.getInstance().getReference()
                    .child(DatabaseManager.DATA_KEY_USERS)
                    .child(phoneNumber)
                    .child(DatabaseManager.DATA_KEY_UPDATES)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            DatabaseManager.updateLatitude(phoneNumber,
                                    MyLocationListener.location,
                                    new CallBackAction() {
                                        @Override
                                        public void success() {

                                        }

                                        @Override
                                        public void failed() {

                                        }
                                    });

                            DatabaseManager.updateLongitude(phoneNumber,
                                    MyLocationListener.location,
                                    new CallBackAction() {
                                        @Override
                                        public void success() {

                                        }

                                        @Override
                                        public void failed() {

                                        }
                                    });

                            DatabaseManager.updateLocationDate(phoneNumber,
                                    new CallBackAction() {
                                        @Override
                                        public void success() {

                                        }

                                        @Override
                                        public void failed() {

                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }


    }
}
