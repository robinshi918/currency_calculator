package org.robinshi.ui.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.robinshi.R;
import org.robinshi.engine.Currency;
import org.robinshi.engine.CurrencyMapper;
import org.robinshi.engine.Setting;
import org.robinshi.engine.baidu.BaiDuApi;
import org.robinshi.engine.baidu.ResultListener;
import org.robinshi.engine.baidu.domain.ConvertResult;
import org.robinshi.ui.widget.CurrencyListAdapter;
import org.robinshi.util.DLog;

import java.util.Collection;
import java.util.Locale;

public class CalculatorActivity extends AppCompatActivity {

    private static final String TAG = CalculatorActivity.class.getSimpleName();

    // widgets
    private Button swapButton;
    private EditText upperEdit;
    private EditText lowerEdit;
    private Spinner upperSpinner;
    private Spinner lowerSpinner;
    private TextView hintTextView;

    // spinner adapter
    private ArrayAdapter<Currency> upperAdapter;
    private ArrayAdapter<Currency> lowerAdapter;

    // indicate which edit widget has the focus
    private boolean isUpperEditFocused = true;

    private View loadingView;
    private AnimationDrawable loadingAnimation;

    //listeners
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
     * when user selects an item in spinner, save user selection
     */
    private AdapterView.OnItemSelectedListener mUpperSpinnerSelectionListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Currency currency = upperAdapter.getItem(position);
            Setting.getInstance().setString(Setting.UPPER_CURRENCY_CODE, currency.alphabeticCode);
            DLog.d(TAG, "user selected[upper] " + currency.alphabeticCode);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    private AdapterView.OnItemSelectedListener mLowerSpinnerSelectionListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Currency currency = lowerAdapter.getItem(position);
            Setting.getInstance().setString(Setting.LOWER_CURRENCY_CODE, currency.alphabeticCode);
            DLog.d(TAG, "user selected[lower] " + currency.alphabeticCode);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        findViews();
        initListeners();
        initData();
        initCurrencySpinner();
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
        lowerSpinner.setOnItemSelectedListener(mLowerSpinnerSelectionListener);

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
            Currency currency = CurrencyMapper.getInstance().getCurrency(lowerSelection);
            int pos = CurrencyMapper.getInstance().find(currency);
            if (pos != -1) {
                lowerSpinner.setSelection(pos);
            }
        }
    }



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

    private void findViews() {
        swapButton = (Button) findViewById(R.id.button_exchange);
        upperEdit = (EditText) findViewById(R.id.upper_currency_value);
        lowerEdit = (EditText) findViewById(R.id.lower_currency_value);
        upperSpinner = (Spinner) findViewById(R.id.upper_spinner);
        lowerSpinner = (Spinner) findViewById(R.id.lower_spinner);
        hintTextView = (TextView) findViewById(R.id.tipText);
        loadingView = (View) findViewById(R.id.loading_view);
    }

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

        DLog.d(TAG, String.format(Locale.ENGLISH, "converting:  %s->%s (%f) ", from, to, amount));

        showLoading();
        BaiDuApi.getInstance().convert(amount, from, to, new ResultListener<ConvertResult>() {
            @Override
            public void onResponse(final ConvertResult result) {
                stopLoading();
                if (result != null) {
                    String value = String.format("%.4f", result.getRetData().getConvertedamount());

                    if (isUpperEditFocused) {
                        lowerEdit.setText(value);
                    } else {
                        upperEdit.setText(value);
                    }
                    hintTextView.setText(result.getRetData().getLocaleDateString());
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(CalculatorActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                stopLoading();
            }
        });
    }

    private void showLoading() {
        if (loadingView != null) {
            loadingAnimation = (AnimationDrawable)loadingView.getBackground();
            loadingAnimation.start();
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    private void stopLoading() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
            loadingAnimation.stop();
        }
    }
}
