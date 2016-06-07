package org.robinshi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import org.robinshi.R;

import java.io.IOException;

/**
 * Created by shiyun on 16/6/3.
 */
public class H5JavascriptActivity extends AppCompatActivity {
    private static final String TAG = H5JavascriptActivity.class.getSimpleName();

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
            Intent intent = new Intent(H5JavascriptActivity.this, CalculatorActivity.class);
            startActivity(intent);
        }
    };

    @JavascriptInterface
    public void startFunction() {
        Toast.makeText(this, "js调用了java函数", Toast.LENGTH_SHORT).show();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(H5JavascriptActivity.this, "js调用了java函数", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @JavascriptInterface
    public void startFunction(String text) {
        Toast.makeText(H5JavascriptActivity.this, "js调用了java函数(带参数)" + text, Toast.LENGTH_SHORT).show();
    }
}