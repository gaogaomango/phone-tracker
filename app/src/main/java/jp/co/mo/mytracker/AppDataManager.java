package jp.co.mo.mytracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

public class AppDataManager {
    public static final String TAG = AppDataManager.class.getSimpleName();

    private static AppDataManager mInstance = null;

    private HashMap<String, String> mMapData = null;
    private static final String SAVE_DATA_FILE_NAME = "SaveData";

    public AppDataManager() {
        mMapData = new HashMap<String, String>();
    }

    public static AppDataManager getInstance() {
        if (mInstance == null) {
            synchronized (AppDataManager.class) {
                mInstance = new AppDataManager();
            }
        }
        return mInstance;
    }

    public boolean saveStringData(Context mContext, String key, String data) {
        if (mContext == null) {
            Log.d(TAG, "saveData. mContext is null");
            return false;
        }
        SharedPreferences pref = mContext.getSharedPreferences(SAVE_DATA_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(key, data);
        return edit.commit();

    }

    public boolean saveBooleanData(Context mContext, String key, Boolean data) {
        if (mContext == null) {
            Log.d(TAG, "saveBooleanData. mContext is null");
            return false;
        }

        SharedPreferences pref = mContext.getSharedPreferences(SAVE_DATA_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(key, data);
        return edit.commit();

    }

    public String loadStringData(Context mContext, String name) {
        if (mContext == null) {
            Log.d(TAG, "loadData. mContext is null");
            return "";
        }

        Log.d(TAG, "name is : " + name);

        SharedPreferences pref = mContext.getSharedPreferences(SAVE_DATA_FILE_NAME, Context.MODE_PRIVATE);
        return pref.getString(name, "");

    }

    public Boolean loadBooleanData(Context mContext, String name) {
        if (mContext == null) {
            Log.d(TAG, "loadBooleanData. mContext is null");
            return false;
        }

        SharedPreferences pref = mContext.getSharedPreferences(SAVE_DATA_FILE_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(name, false);

    }

    public void clearData(Context mContext, String key) {
        SharedPreferences pref = mContext.getSharedPreferences(SAVE_DATA_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.remove(key);
        edit.commit();

    }


}
