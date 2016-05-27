package com.example.shiyun.myapplication;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shiyun on 16/5/27.
 */
public class BaiDuApi {

    private static final String TAG = BaiDuApi.class.getSimpleName();

    private BaiDuApi() {}

    private static final String API_KEY = "910031a7306e19669ccea798b2b20a55";
    private static final String CURRENCY_TYPE_URL = "http://apis.baidu.com/apistore/currencyservice/type";
    private static final String CURRENCY_CONVERT_URL = "http://apis.baidu.com/apistore/currencyservice/currency";

    private static BaiDuApi instance;


    public static BaiDuApi getInstance() {
        if (instance == null) {
            instance = new BaiDuApi();
        }
        return instance;
    }

    public List<String> getCurrencyList() {
        List<String> list = new ArrayList<>();
        String httpResult = request(CURRENCY_TYPE_URL, "");
        if (TextUtils.isEmpty(httpResult)) {
            try {
                JSONObject jsonObj = new JSONObject(httpResult);
                int errNum = jsonObj.getInt("errNum");
                String errMsg = jsonObj.getString("errMsg");
                JSONArray data = jsonObj.getJSONArray("retData");
                Log.d(TAG, "errNum = " + errNum + ", errMsg = " + errMsg + "\nretData = " + data);
                if (data != null && data.length() > 0 ) {
                    for (int i = 0; i < data.length() ; i++) {
                        String value = (String)data.get(i);
                        if (!TextUtils.isEmpty(value)) {
                            list.add(value);
                        }
                    }
                } else {
                    throw  new JSONException("getCurrencyList() server returns empty currency list.");
                }
            } catch (JSONException e) {
                Log.e(TAG, "getCurrencyList failed - " + e.toString());
            }
        }
        return list;
    }

    public float convert(float amount, String fromCurrency, String toCurrency) {
        if (amount < 0 || TextUtils.isEmpty(fromCurrency) || TextUtils.isEmpty(toCurrency)) {
            return 0.0f;
        }
        String httpArgs = String.format("fromCurrency=%s&toCurrency=%s&amount=%f", fromCurrency, toCurrency, amount);
        String data = request(CURRENCY_CONVERT_URL, httpArgs);


        return 0.0f;
    }


    /**
     *
     * @param httpUrl
     * @param httpArg
     * @return
     */
    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey", API_KEY);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
