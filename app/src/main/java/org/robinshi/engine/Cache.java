package org.robinshi.engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.robinshi.CCApplication;
import org.robinshi.util.DLog;

/**
 * Created by shiyun on 16/6/2.
 */
public class Cache {
    private static final String TAG = Cache.class.getSimpleName();

    public static String KEY_CURRENCY_LIST = "currency_list";

    private static Cache mInstance;
    private SharedPreferences mFile;

    private Cache() {
        mFile = CCApplication.getAppInstance().getSharedPreferences("cache_file", Context.MODE_PRIVATE);
    }

    public static synchronized Cache getInstance() {
        if (mInstance == null) {
            mInstance = new Cache();
        }
        return mInstance;
    }

    public int getInt(@NonNull String key) {
        return mFile.getInt(key, 0);
    }

    public float getFloat(@NonNull String key) {
        return mFile.getFloat(key, 0.0f);
    }

    public JSONObject getJSONObject(@NonNull String key) {
        String text = mFile.getString(key, null);
        if (!TextUtils.isEmpty(text)) {
            try {
                return new JSONObject(text);
            } catch (JSONException e) {
                DLog.e(TAG, "getJSONObject failed: parse error!");
                return null;
            }
        } else {
            return null;
        }
    }

    public String getString(@NonNull String key) {
        return mFile.getString(key, null);
    }

    public void putString(@NonNull String key, String content) {
        mFile.edit().putString(key, content).apply();
    }

    public void putFloat(@NonNull String key, float value) {
        mFile.edit().putFloat(key, value).apply();
    }

    public void putInt(@NonNull String key, int value) {
        mFile.edit().putInt(key, value).apply();
    }

    public void putJSONObject(@NonNull String key, JSONObject value) {
        if (value != null) {
            putString(key, value.toString());
        } else {
            putString(key, null);
        }
    }
}
