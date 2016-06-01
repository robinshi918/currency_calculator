package com.example.shiyun.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shiyun.myapplication.com.example.shiyun.myapplication.baidu.Convert;

import java.util.List;

public class CalculatorActivity extends AppCompatActivity {

    private static final String TAG = CalculatorActivity.class.getSimpleName();

    private Button swapButton;
    private EditText upperEdit;
    private EditText lowerEdit;
    private Spinner upperSpinner;
    private Spinner lowerSpinner;
    private TextView hintTextView;

    private ArrayAdapter<String> upperCurrencyListAdapter;
    private ArrayAdapter<String> lowerCurrencyListAdapter;

    private List<String> currencyList;

    View.OnClickListener swapButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            convert();
        }
    };
    private TextWatcher lowerEditChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (lowerEdit.hasFocus()) {
//                upperEdit.setText(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private TextWatcher upperEditChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (upperEdit.hasFocus()) {
//                lowerEdit.setText(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };



    private void convert() {

        String from, to;
        float amount;
        if (isUpperEditFocused) {
            from = (String) upperSpinner.getSelectedItem();
            to = (String) lowerSpinner.getSelectedItem();
            amount = Float.parseFloat(upperEdit.getText().toString());
        } else {
            from = (String) lowerSpinner.getSelectedItem();
            to = (String) upperSpinner.getSelectedItem();
            amount = Float.parseFloat(lowerEdit.getText().toString());
        }

        Log.d(TAG, String.format("converting:  %s->%s (%f) ", from, to, amount));

        BaiDuApi.getInstance().convert(amount, from, to, new BaiDuApi.Listener<Convert>() {
            @Override
            public void onResponse(final Convert result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result != null) {
                            String value = String.format("%.4f", result.getRetData().getConvertedamount());

                            if (isUpperEditFocused) {
                                lowerEdit.setText(value);
                            } else {
                                upperEdit.setText(value);
                            }

                            hintTextView.setText(result.getRetData().getDate() + " " + result.getRetData().getTime());

                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CalculatorActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void getList() {
        BaiDuApi.getInstance().getCurrencyList(new BaiDuApi.Listener<List<String>>() {
            @Override
            public void onResponse(final List<String> result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currencyList = result;

                        if (upperCurrencyListAdapter == null) {
                            upperCurrencyListAdapter = new ArrayAdapter<>(CalculatorActivity.this, android.R.layout.simple_spinner_item, result);
                            upperCurrencyListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        } else {
                            upperCurrencyListAdapter.clear();
                            upperCurrencyListAdapter.addAll(result);
                        }
                        upperSpinner.setAdapter(upperCurrencyListAdapter);

                        if (lowerCurrencyListAdapter == null) {
                            lowerCurrencyListAdapter = new ArrayAdapter<>(CalculatorActivity.this, android.R.layout.simple_spinner_item, result);
                            lowerCurrencyListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        } else {
                            lowerCurrencyListAdapter.clear();
                            lowerCurrencyListAdapter.addAll(result);
                        }
                        lowerSpinner.setAdapter(lowerCurrencyListAdapter);

                        setDefaultCurrency();
                    }
                });

            }

            @Override
            public void onError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CalculatorActivity.this, "failed to get currency list!" + e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        findViews();
        initListeners();
        initData();
    }

    private float exchangeRate = 0.0f;
    private String upperCurrency = "CNY";
    private String lowerCurrency = "USD";

    private void initData() {
        getList();
    }

    private void initListeners() {
        // init listeners
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

        upperEdit.addTextChangedListener(upperEditChangeListener);

        lowerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lowerEdit.setText("");
            }
        });

        lowerEdit.addTextChangedListener(lowerEditChangeListener);

        upperSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                upperCurrency = upperCurrencyListAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        lowerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lowerCurrency = lowerCurrencyListAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        upperEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    isUpperEditFocused = true;
                }
            }
        });

        lowerEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    isUpperEditFocused  = false;
                }
            }
        });
    }

    private boolean isUpperEditFocused = true;

    private void findViews() {
        //init widgets
        swapButton = (Button) findViewById(R.id.button_exchange);
        upperEdit = (EditText) findViewById(R.id.upper_currency_value);
        lowerEdit = (EditText) findViewById(R.id.lower_currency_value);
        upperSpinner = (Spinner) findViewById(R.id.upper_spinner);
        lowerSpinner = (Spinner) findViewById(R.id.lower_spinner);
        hintTextView = (TextView) findViewById(R.id.tipText);
    }

    /**
     * set default currencies
     */
    private void setDefaultCurrency() {

        upperCurrency = "CNY";
        lowerCurrency = "USD";

        int upperPos = currencyList.indexOf(upperCurrency);
        if (upperPos != -1) {
            upperSpinner.setSelection(upperPos);
        }

        int lowerPos = currencyList.indexOf(lowerCurrency);
        if (lowerPos != -1) {
            lowerSpinner.setSelection(lowerPos);
        }


    }
}
