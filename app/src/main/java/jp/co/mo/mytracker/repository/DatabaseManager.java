package jp.co.mo.mytracker.repository;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import jp.co.mo.mytracker.CallBackAction;
import jp.co.mo.mytracker.common.DateUtil;
import jp.co.mo.mytracker.common.Parameter;

public class DatabaseManager {
    private static final String TAG = DatabaseManager.class.getSimpleName();

    public static final String DATA_KEY_USERS = "users";
    public static final String DATA_KEY_UPDATES = "updates";
    public static final String DATA_KEY_FINDERS = "finders";
    public static final String DATA_KEY_LOCATION = "location";
    public static final String DATA_KEY_LATITUDE = "lat";
    public static final String DATA_KEY_LONGITUDE = "lon";
    public static final String DATA_KEY_DATE = "date";

    public  static Map<String,String> myTrackers = new HashMap<>();

    public static void updateLocationInFirebase(String userPhone, final CallBackAction callBack) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(DATA_KEY_USERS)
                .child(userPhone)
                .child(DATA_KEY_UPDATES)
                .setValue(DateUtil.format(DateUtil.DATE_FORMAT_YYYM_MD_DH_H_M_SS, DateUtil.now()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callBack.success();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callBack.failed();
                    }
                });
    }

    public static void updateLatitude(String userPhone, Location location, final CallBackAction callBack) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(DATA_KEY_USERS)
                .child(userPhone)
                .child(DATA_KEY_LOCATION)
                .child(DATA_KEY_LATITUDE)
                .setValue(location.getLatitude())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callBack.success();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callBack.failed();
                    }
                });
    }

    public static void updateLongitude(String userPhone, Location location, final CallBackAction callBack) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(DATA_KEY_USERS)
                .child(userPhone)
                .child(DATA_KEY_LOCATION)
                .child(DATA_KEY_LONGITUDE)
                .setValue(location.getLongitude())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callBack.success();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callBack.failed();
                    }
                });
    }

    public static void updateLocationDate(String userPhone, final CallBackAction callBack) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(DATA_KEY_USERS)
                .child(userPhone)
                .child(DATA_KEY_LOCATION)
                .child(DATA_KEY_DATE)
                .setValue(DateUtil.format(DateUtil.DATE_FORMAT_YYYM_MD_DH_H_M_SS, DateUtil.now()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callBack.success();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callBack.failed();
                    }
                });
    }


    public static void putFindersInfo(Context context, String number) {
        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child(DATA_KEY_USERS)
                .child(number)
                .child(DATA_KEY_FINDERS)
                .child(AppDataManager.getInstance().loadStringData(context, Parameter.KEY_PHONE_NUMBER))
                .setValue(true);
    }

    public static void deleteFindersInfo(Context context, String number) {
        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child(DATA_KEY_USERS)
                .child(number)
                .child(DATA_KEY_FINDERS)
                .child(AppDataManager.getInstance().loadStringData(context, Parameter.KEY_PHONE_NUMBER))
                .removeValue();
    }

    public static String formatPhoneNumber(String oldNumber) {
//        try {
//            String numberOnly = oldNumber.replaceAll("[^0-9]]", "");
//            if(oldNumber.charAt(0) == '+') {
//                numberOnly = "+" + numberOnly;
//            }
//
//            if(numberOnly.length() >= 10) {
//                numberOnly = numberOnly.substring(numberOnly.length()-10, numberOnly.length());
//            }
//            return numberOnly;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return " ";
//        }
        return oldNumber;
    }

    public static boolean saveMyTrackers(Context context) {
        Gson gson = new Gson();
        String jsonMyTracker = gson.toJson(myTrackers);

        Log.e(TAG, "gson mytracker: " + jsonMyTracker);
        return AppDataManager.getInstance().saveStringData(context, Parameter.KEY_TRACKER, jsonMyTracker);
    }

    public static Map<String,String> loadMyTrackers(Context context) {
        myTrackers.clear();
        Gson gson = new Gson();
        Map<String, String> map = gson.fromJson(AppDataManager.getInstance().loadStringData(context, Parameter.KEY_TRACKER), Map.class);
        myTrackers = map;
        return map;
    }
}
