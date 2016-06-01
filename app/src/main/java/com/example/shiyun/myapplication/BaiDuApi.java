package com.example.shiyun.myapplication;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.shiyun.myapplication.com.example.shiyun.myapplication.baidu.Convert;
import com.example.shiyun.myapplication.com.example.shiyun.myapplication.baidu.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;


/**
 * Created by shiyun on 16/5/27.
 */
public class BaiDuApi {

    private static final String TAG = BaiDuApi.class.getSimpleName();

    private static final String API_KEY = "910031a7306e19669ccea798b2b20a55";

    private static final String CURRENCY_TYPE_URL = "http://apis.baidu.com/apistore/currencyservice/type";
    private static final String CONVERT_API_URL = "http://apis.baidu.com/apistore/currencyservice/currency";
    private static BaiDuApi instance;

    public static BaiDuApi getInstance() {
        if (instance == null) {
            instance = new BaiDuApi();
        }
        return instance;
    }


    private BaiDuApi() {
    }

    public interface Listener<T> {
        void onResponse(T result);
        void onError(Exception e);
    }


    public void getCurrencyList(final Listener<List<String>> listener) {

        if (listener == null) {
            throw new IllegalArgumentException("listener is null");
        }

        httpRequest(CURRENCY_TYPE_URL, "", new Listener<String>() {
            @Override
            public void onResponse(String text) {
                Log.d(TAG, "getCurrencyList: " + text);

                Gson gson = new Gson();
                Type type = gson.fromJson(text, new TypeToken<Type>() {}.getType());

                if (type.getRetData() != null && type.getRetData().size() > 0) {
                    listener.onResponse(type.getRetData());
                } else {
                    listener.onError(new Exception("getCurrencyTypeList() 服务端返回数据解析失败" + text));
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                listener.onError(e);
            }
        });


    }

    public void convert(float amount, String fromCurrency, String toCurrency, final Listener<Convert> listener) {

        if (listener == null) {
            throw new IllegalArgumentException("convert() listener == null");
        }

        if (amount < 0 || TextUtils.isEmpty(fromCurrency) || TextUtils.isEmpty(toCurrency)) {
            listener.onResponse(null);
            return;
        }
        String httpArgs = String.format("fromCurrency=%s&toCurrency=%s&amount=%f", fromCurrency, toCurrency, amount);
        httpRequest(CONVERT_API_URL, httpArgs, new Listener<String>() {
            @Override
            public void onResponse(String data) {
                Log.d(TAG, "convert() data = " + data);

                if (!TextUtils.isEmpty(data)) {
                    Gson gson = new Gson();
                    Convert result = gson.fromJson(data, new TypeToken<Convert>() {
                    }.getType());

                    Log.i(TAG, result.toString());

                    if (result != null) {
                        listener.onResponse(result);
                    }
                } else {
                    listener.onError(new Exception("server returned empty data!"));
                }
            }

            @Override
            public void onError(Exception e) {
                listener.onError(e);
            }
        });
    }

    private static final int INVALID_CODE = 0xDEADBEEF;

    //TODO
    public String getTimestamp() {
        if (updateTime != null) {
            return DateFormat.format("", updateTime).toString(); // FIXME TODO
        }
        return "";
    }

    private Date updateTime;

    // TODO
    public Date getUpdateTime() {
        return updateTime;
    }

    public void httpRequest(final String apiAddress, final String httpArg, final Listener<String> listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader = null;
                String result = null;
                StringBuffer sbf = new StringBuffer();
                String httpUrl = apiAddress + "?" + httpArg;

                try {
                    URL url = new URL(httpUrl);
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("apikey", API_KEY);  // 填入apikey到HTTP header
                    connection.connect();
                    InputStream is = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String strRead = null;
                    while ((strRead = reader.readLine()) != null) {
                        sbf.append(strRead);
                        sbf.append("\r\n");
                    }
                    reader.close();
                    result = sbf.toString();

                    if (listener != null) {
                        listener.onResponse(result);
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                }
            }
        }).start();

//        ArrayAdapter<String> arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        Spinner sp;


    }
}
