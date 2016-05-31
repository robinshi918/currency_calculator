package com.example.shiyun.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.shiyun.myapplication.com.example.shiyun.myapplication.baidu.Convert;

import java.util.List;

public class CalculatorActivity extends AppCompatActivity {

    private static final String TAG = CalculatorActivity.class.getSimpleName();

    Button swapButton;
    ImageButton upperButton;
    EditText upperEdit;
    ImageButton lowerButton;
    EditText lowerEdit;

    View.OnClickListener upperChangeCurrencyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "upperChangeCurrencyListener.onClick");
        }
    };

    View.OnClickListener lowerChangeCurrencyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "lowerChangeCurrencyListener.onClick");
        }
    };

    static int i = 0;
    View.OnClickListener swapButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getList();
        }
    };

    private void convert() {
        BaiDuApi.getInstance().convert(100.0f, "USD", "CNY", new BaiDuApi.Listener<Convert>() {
            @Override
            public void onResponse(final Convert result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result != null) {
                            String value = String.format("%.4f", result.getRetData().getConvertedamount());
                            upperEdit.setText(value);
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void getList() {
        BaiDuApi.getInstance().getCurrencyList(new BaiDuApi.Listener<List<String>>() {
            @Override
            public void onResponse(List<String> result) {

                for (String currency: result) {
                    Log.d(TAG, currency);
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        findViews();
    }

    private void findViews() {
        //init widgets
        swapButton = (Button) findViewById(R.id.button_exchange);
        upperButton = (ImageButton) findViewById(R.id.upper_change_currency_button);
        upperEdit = (EditText) findViewById(R.id.upper_currency_value);
        lowerButton = (ImageButton) findViewById(R.id.lower_change_currency_button);
        lowerEdit = (EditText) findViewById(R.id.lower_currency_value);

        // init listeners
        lowerButton.setOnClickListener(lowerChangeCurrencyListener);
        upperButton.setOnClickListener(upperChangeCurrencyListener);
        swapButton.setOnClickListener(swapButtonListener);

        lowerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lowerEdit.setText("");
            }
        });

        upperEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upperEdit.setText("");
            }
        });

        upperEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (upperEdit.hasFocus()) {
                    lowerEdit.setText(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lowerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lowerEdit.setText("");
            }
        });

        lowerEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (lowerEdit.hasFocus()) {
                    upperEdit.setText(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

}
