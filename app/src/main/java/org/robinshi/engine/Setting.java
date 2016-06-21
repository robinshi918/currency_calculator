package org.robinshi.engine;

import android.content.Context;
import android.content.SharedPreferences;

import org.robinshi.CCApplication;

/**
 * Created by shiyun on 6/21/16.
 */
public class Setting {

    private static final String TAG = Cache.class.getSimpleName();
    public static String UPPER_CURRENCY_CODE;
    public static String LOWER_CURRENCY_CODE;


    private static Setting mInstance;
    public static synchronized Setting getInstance() {
        if (mInstance == null) {
            mInstance = new Setting();
        }
        return mInstance;
    }

    private SharedPreferences mFile;
    private Setting() {
        mFile = CCApplication.getAppInstance().getSharedPreferences("setting_file", Context.MODE_PRIVATE);
    }

    public void setString(String key, String value) {
        mFile.edit().putString(key, value).commit();
    }

    public String getString(String key) {
        return mFile.getString(key, "");
    }




}
