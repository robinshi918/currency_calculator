package org.robinshi.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.robinshi.CCApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * maintain image cache
 */
public class ImageManager {

    public static final String TAG = ImageManager.class.getSimpleName();

    private ImageManager() {
    }

    private static ImageManager mInstance;

    private Map<String, Bitmap> mCache = new HashMap<>();

    public static synchronized ImageManager getInstance() {
        if (mInstance == null) {
            mInstance = new ImageManager();
        }
        return mInstance;
    }

    public Bitmap getBitmap(String code) {
        Bitmap bitmap = null;

        if (TextUtils.isEmpty(code)) {
            return null;
        }

        bitmap = mCache.get(code);

        if (bitmap == null) {
            String fileName = "flags/" + code + ".png";
            try {
                InputStream in = CCApplication.getAppInstance().getAssets().open(fileName);
                bitmap = BitmapFactory.decodeStream(in);
                mCache.put(code, bitmap);
                in.close();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }

        return bitmap;
    }


}
