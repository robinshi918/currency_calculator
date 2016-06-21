package org.robinshi.engine;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.robinshi.util.DLog;
import org.robinshi.engine.baidu.BaiDuApi;
import org.robinshi.engine.baidu.domain.ConvertResult;
import org.robinshi.engine.baidu.ResultListener;
import org.robinshi.engine.baidu.domain.TypeResult;

import java.util.ArrayList;
import java.util.List;

public class RatesManager {

    private static final String TAG = RatesManager.class.getSimpleName();

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
    public void convert(Float fromValue, String fromCurrency, String toCurrency, ResultListener<ConvertResult> listener) {

        if (fromValue <  0.0 || TextUtils.isEmpty(fromCurrency) || TextUtils.isEmpty(toCurrency)) {
            DLog.d(TAG, "convert() invalid parameter");
            return;
        }

        String cache = Cache.getInstance().getString(fromCurrency + toCurrency);
        if (TextUtils.isEmpty(cache)) {
            if (listener != null) {
                Gson gson = new Gson();
                ConvertResult convert = gson.fromJson(cache, new TypeToken<ConvertResult>(){}.getType());
                if (!convert.hasExpire()) {
                    if (listener != null) {
                        listener.onResponse(convert);
                    }
                }
            }
        } else {
            BaiDuApi.getInstance().convert(fromValue, fromCurrency, toCurrency, listener);
        }


    }

    public void getCurrencyList(ResultListener<List<String>> listener) {
        String cache = Cache.getInstance().getString(Cache.KEY_CURRENCY_LIST);

        if (TextUtils.isEmpty(cache)) {
            BaiDuApi.getInstance().getCurrencyList(listener);
        } else {
            Gson gson = new Gson();
            TypeResult type = gson.fromJson(cache, new TypeToken<TypeResult>() {}.getType());
            if (listener != null) {
                listener.onResponse(type.getRetData());
            }
        }
    }


    public List<String> getFrequentCurrencyList() {
        List<String> list = new ArrayList<>();
        String[] freqList = {
                "CNY",
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

    public String getCurrencyName(String currencyCode) {
        String name = null;

        return name;
    }
}
