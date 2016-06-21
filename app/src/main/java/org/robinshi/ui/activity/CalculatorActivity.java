package org.robinshi.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.robinshi.engine.Setting;
import org.robinshi.util.DLog;
import org.robinshi.engine.Currency;
import org.robinshi.engine.CurrencyMapper;
import org.robinshi.ui.widget.CurrencyListAdapter;
import org.robinshi.R;
import org.robinshi.engine.baidu.BaiDuApi;
import org.robinshi.engine.baidu.domain.ConvertResult;
import org.robinshi.engine.baidu.ResultListener;

import java.util.Collection;
import java.util.List;

public class CalculatorActivity extends AppCompatActivity {

    private static final String TAG = CalculatorActivity.class.getSimpleName();

    private Button swapButton;
    private EditText upperEdit;
    private EditText lowerEdit;
    private Spinner upperSpinner;
    private Spinner lowerSpinner;
    private TextView hintTextView;

    private ArrayAdapter<Currency> upperAdapter;
    private ArrayAdapter<Currency> lowerAdapter;

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

    /**
     * when user selects an item in spinner, save the user selection
     */
    private AdapterView.OnItemSelectedListener mUpperSpinnerSelectionListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Currency currency = upperAdapter.getItem(position);
            Setting.getInstance().setString(Setting.UPPER_CURRENCY_CODE, currency.alphabeticCode);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener mlowerSpinnerSelectionListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Currency currency = lowerAdapter.getItem(position);
            Setting.getInstance().setString(Setting.LOWER_CURRENCY_CODE, currency.alphabeticCode);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void convert() {

        String from, to;
        float amount;
        if (isUpperEditFocused) {
            from = ((Currency) upperSpinner.getSelectedItem()).alphabeticCode;
            to = ((Currency) lowerSpinner.getSelectedItem()).alphabeticCode;
            amount = Float.parseFloat(upperEdit.getText().toString());
        } else {
            from = ((Currency)lowerSpinner.getSelectedItem()).alphabeticCode;
            to = ((Currency) upperSpinner.getSelectedItem()).alphabeticCode;
            amount = Float.parseFloat(lowerEdit.getText().toString());
        }

        DLog.d(TAG, String.format("converting:  %s->%s (%f) ", from, to, amount));

        BaiDuApi.getInstance().convert(amount, from, to, new ResultListener<ConvertResult>() {
            @Override
            public void onResponse(final ConvertResult result) {

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

            @Override
            public void onError(Exception e) {
                Toast.makeText(CalculatorActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initCurrencySpinner() {
        upperAdapter = new CurrencyListAdapter(this, this, R.layout.spinner_dropdown_item);
        Collection<Currency> list = CurrencyMapper.getInstance().getCurrencyList();
        upperAdapter.addAll(list);
        upperAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        upperSpinner.setAdapter(upperAdapter);
        upperSpinner.setOnItemSelectedListener(mUpperSpinnerSelectionListener);

        lowerAdapter = new CurrencyListAdapter(this, this, R.layout.spinner_dropdown_item);
        lowerAdapter.addAll(CurrencyMapper.getInstance().getCurrencyList());
        lowerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        lowerSpinner.setAdapter(lowerAdapter);
        lowerSpinner.setOnItemSelectedListener(mlowerSpinnerSelectionListener);

        initUserSelection();
    }


    private void initUserSelection() {
        String upperSelection = Setting.getInstance().getString(Setting.UPPER_CURRENCY_CODE);
        if (!TextUtils.isEmpty(upperSelection)) {
            Currency currency = CurrencyMapper.getInstance().getCurrency(upperSelection);
            int pos = CurrencyMapper.getInstance().find(currency);
            if (pos != -1) {
                upperSpinner.setSelection(pos);
            }
        }

        String lowerSelection = Setting.getInstance().getString(Setting.LOWER_CURRENCY_CODE);
        if (!TextUtils.isEmpty(lowerSelection)) {
            Currency currency = CurrencyMapper.getInstance().getCurrency(upperSelection);
            int pos = CurrencyMapper.getInstance().find(currency);
            if (pos != -1) {
                lowerSpinner.setSelection(pos);
            }
        }

    }


    private void getList() {
        BaiDuApi.getInstance().getCurrencyList(new ResultListener<List<String>>() {
            @Override
            public void onResponse(final List<String> result) {

                currencyList = result;

//                setDefaultCurrency();
            }

            @Override
            public void onError(final Exception e) {
                Toast.makeText(CalculatorActivity.this, "failed to get currency list!" + e.toString(), Toast.LENGTH_SHORT).show();
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
        initCurrencySpinner();
    }

    private float exchangeRate = 0.0f;
    private String upperCurrency = "CNY";
    private String lowerCurrency = "USD";

    private void initData() {
        //getList();
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
                upperCurrency = upperAdapter.getItem(position).alphabeticCode;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        lowerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lowerCurrency = lowerAdapter.getItem(position).alphabeticCode;
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
