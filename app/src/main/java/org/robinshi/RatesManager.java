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


    public static class MainActivity extends AppCompatActivity {
        private static final String TAG = MainActivity.class.getSimpleName();

        WebView webView;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Button button1 = (Button) findViewById(R.id.button);
            Button button2 = (Button) findViewById(R.id.jumpButton);
            webView = (WebView) findViewById(R.id.web_view);

            if (button1 != null) {
                button1.setOnClickListener(firstButtonListener);
            }

            if (button2 != null) {
                button2.setOnClickListener(toCalculatorButtonListener);
            }

            if (webView != null) {
                webView.getSettings().setJavaScriptEnabled(true);
                String path = "file:///android_asset/html/hello.html";
                webView.addJavascriptInterface(this, "wst");
                webView.loadUrl(path);

                try {
                    String[] list = getBaseContext().getAssets().list("html");
                    for (String entry: list) {
                        Log.d(TAG, "asset entry: " + entry);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        View.OnClickListener firstButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "button clicked");

                webView.loadUrl("javascript:javacalljs()");
            }
        };

        View.OnClickListener toCalculatorButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalculatorActivity.class);
                startActivity(intent);
            }
        };

        @JavascriptInterface
        public void startFunction() {
            Toast.makeText(this, "js调用了java函数", Toast.LENGTH_SHORT).show();
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "js调用了java函数", Toast.LENGTH_SHORT).show();

                }
            });
        }

        @JavascriptInterface
        public void startFunction(String text) {
            Toast.makeText(MainActivity.this, "js调用了java函数(带参数)" + text, Toast.LENGTH_SHORT).show();
        }
    }
}
