package com.example.shiyun.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shiyun on 16/6/2.
 */
public class Cache {
    private static final String TAG = Cache.class.getSimpleName();

    private Context mContext;

    private static Cache mInstance;
    private SharedPreferences mFile;

    private Cache(@NonNull Context context) {
        mContext = context;
        mFile = mContext.getSharedPreferences("cache_file", Context.MODE_PRIVATE);
    }

    public static synchronized Cache getInstance(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("parameter is null");
        }

        if (mInstance == null) {
            mInstance = new Cache(context);
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
                Log.e(TAG, "getJSONObject failed: parse error!");
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
        mFile.edit().putString(key, content);
        mFile.edit().apply();
    }

    public void putFloat(@NonNull String key, float value) {
        mFile.edit().putFloat(key, value);
        mFile.edit().apply();
    }

    public void putInt(@NonNull String key, int value) {
        mFile.edit().putInt(key, value);
        mFile.edit().apply();
    }

    public void putJSONObject(@NonNull String key, JSONObject value) {
        if (value != null) {
            putString(key, value.toString());
        } else {
            putString(key, null);
        }
        mFile.edit().apply();
    }
}
