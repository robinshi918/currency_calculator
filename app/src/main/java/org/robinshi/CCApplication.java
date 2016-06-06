package org.robinshi;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.ExecutorService;

/**
 * Created by shiyun on 16/6/3.
 */
public class CCApplication extends Application {

    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

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
