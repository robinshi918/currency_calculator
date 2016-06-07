package org.robinshi;

import android.app.Application;
import android.content.Context;

import org.robinshi.engine.CurrencyNameMapper;

/**
 * Created by shiyun on 16/6/3.
 */
public class CCApplication extends Application {

    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        init();
    }

    private void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CurrencyNameMapper.getInstance();
            }
        }).start();
    }

    public static Context getAppInstance() {
        return instance;
    }

}
