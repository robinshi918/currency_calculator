package org.robinshi.baidu;

import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.robinshi.CCApplication;
import org.robinshi.CacheManager;
import org.robinshi.baidu.domain.ConvertResult;
import org.robinshi.baidu.domain.TypeResult;

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


    public void getCurrencyList(final ResultListener<List<String>> resultListener) {

        if (resultListener == null) {
            throw new IllegalArgumentException("resultListener is null");
        }

        httpRequest(CURRENCY_TYPE_URL, "", new ResultListener<String>() {
            @Override
            public void onResponse(String text) {
                Log.d(TAG, "getCurrencyList: " + text);

                Gson gson = new Gson();
                TypeResult type = gson.fromJson(text, new TypeToken<TypeResult>() {
                }.getType());

                if (type.getRetData() != null && type.getRetData().size() > 0) {
                    CacheManager.getInstance().putString(CacheManager.KEY_CURRENCY_LIST, text);
                    resultListener.onResponse(type.getRetData());
                } else {
                    resultListener.onError(new Exception("getCurrencyTypeList() 服务端返回数据解析失败" + text));
                }

            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                resultListener.onError(e);
            }
        });


    }

    public void convert(float amount, String fromCurrency, String toCurrency, final ResultListener<ConvertResult> resultListener) {

        if (resultListener == null) {
            throw new IllegalArgumentException("convert() resultListener == null");
        }

        if (amount < 0 || TextUtils.isEmpty(fromCurrency) || TextUtils.isEmpty(toCurrency)) {
            resultListener.onResponse(null);
            return;
        }
        String httpArgs = String.format("fromCurrency=%s&toCurrency=%s&amount=%f", fromCurrency, toCurrency, amount);
        httpRequest(CONVERT_API_URL, httpArgs, new ResultListener<String>() {
            @Override
            public void onResponse(String data) {
                Log.d(TAG, "convert() data = " + data);

                if (!TextUtils.isEmpty(data)) {
                    Gson gson = new Gson();
                    ConvertResult result = gson.fromJson(data, new TypeToken<ConvertResult>() {
                    }.getType());

                    Log.i(TAG, result.toString());

                    CacheManager.getInstance().putString(
                            result.getRetData().getFromCurrency() + result.getRetData().getToCurrency(),
                            data);

                    resultListener.onResponse(result);
                } else {
                    resultListener.onError(new Exception("server returned empty data!"));
                }
            }

            @Override
            public void onError(Exception e) {
                resultListener.onError(e);
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

    public void httpRequest(final String apiAddress, final String httpArg, final ResultListener<String> resultListener) {

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

                    final String tmpResult = result;
                    new Handler(CCApplication.getAppInstance().getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (resultListener != null) {
                                resultListener.onResponse(tmpResult);
                            }
                        }
                    });
                } catch (final Exception e) {
                    new Handler(CCApplication.getAppInstance().getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (resultListener != null) {
                                resultListener.onError(e);
                            }
                        }
                    });

                }
            }
        }).start();
    }
}
