package org.robinshi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.example.shiyun.myapplication.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RatesManager {

    private static final String TAG = RatesManager.class.getSimpleName();

    private BaiDuApi baiduApi;

    private static RatesManager instance;

    public static synchronized RatesManager getInstance() {
        if (instance == null) {
            instance = new RatesManager();
        }
        return instance;
    }

    private RatesManager() {

    }

    /**
     * look up the rates table and do the converting.
     * @param fromValue The value
     * @param fromCurrency from currency type
     * @param toCurrency to currency type
     * @return result after converting
     */
    public String convert(Float fromValue, String fromCurrency, String toCurrency) {
        if (fromValue <  0.0 || TextUtils.isEmpty(fromCurrency) || TextUtils.isEmpty(toCurrency)) {
            Log.d(TAG, "convert() invalid parameter");
            return "";
        }
        return "";
    }

    public List<String> getCurrencyList() {
        List<String> list = new ArrayList<>();
        String[] freqList = {"CNY",
                "JPY",
                "GBP",
                "CHF",
                "CAD",
                "HKD",
                "IEP",
                "LUF",
                "PTE",
                "IDR",
                "NZD",
                "SUR",
                "KRW"
        };

        return list;
    }

    public List<String> getFrequentCurrencyList() {
        List<String> list = new ArrayList<>();

        return list;
    }

    public String getCurrencyName(String currencyCode) {
        String name = null;

        return name;
    }
}
