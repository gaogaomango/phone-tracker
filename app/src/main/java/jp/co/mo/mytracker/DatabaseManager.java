package jp.co.mo.mytracker;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import jp.co.mo.mytracker.common.DateUtil;

public class DatabaseManager {
    private static final String DATA_KEY_USERS = "users";
    private static final String DATA_KEY_UPDATES = "updates";

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
}
