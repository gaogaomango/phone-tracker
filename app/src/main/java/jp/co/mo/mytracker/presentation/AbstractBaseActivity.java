package jp.co.mo.mytracker.presentation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import jp.co.mo.mytracker.PermissionCallBackAction;

public abstract class AbstractBaseActivity extends AppCompatActivity {

    private static final String[] PERMISSION_LIST = {Manifest.permission.READ_CONTACTS};
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private PermissionCallBackAction mPermissionCallBackAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void checkPermission(PermissionCallBackAction permissionCallBackAction) {
        checkPermission(PERMISSION_LIST, permissionCallBackAction);
    }

    protected void checkPermission(String[] permissions, PermissionCallBackAction permissionCallBackAction) {
        mPermissionCallBackAction = permissionCallBackAction;
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermissionGranted(permissions)) {
                requestPermissions(permissions,
                        REQUEST_CODE_ASK_PERMISSIONS);
                mPermissionCallBackAction = permissionCallBackAction;
                return;
            }
        }
        permissionCallBackAction.success();
    }

    private boolean checkPermissionGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean isSuccess = true;
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for(int i : grantResults) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        isSuccess = false;
                    }
                }
                if (isSuccess) {
                    mPermissionCallBackAction.success();
                } else {
                    Toast.makeText(this, "please check permissions.", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
}
