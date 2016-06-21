package org.robinshi.engine;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.robinshi.CCApplication;
import org.robinshi.util.DLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Map currency ISO code to its Chinese Name and English Name
 */
public class CurrencyMapper {
    private static final String TAG = CurrencyMapper.class.getSimpleName();

    private static CurrencyMapper mInstance;

    private CurrencyMapper() {
        load();
    }


    public static synchronized CurrencyMapper getInstance() {
        if (mInstance == null) {
            mInstance = new CurrencyMapper();
        }
        return mInstance;
    }

    Map<String, Currency> mMap = new ConcurrentHashMap<>(100);
    List<Currency> mList = new LinkedList<>();

    /**
     * get full list of currencies
     * @return
     */
    public List<Currency> getCurrencyList() {
        return mList;
    }

    public Currency getCurrency(String alphabeticCode) {
        return mMap.get(alphabeticCode);
    }

    /**
     * load map data from disk.
     */
    private void load() {

        String jsonData = readFile("currency_table.txt");

        if (!TextUtils.isEmpty(jsonData)) {
            Gson gson = new Gson();
            mList = gson.fromJson(jsonData, new TypeToken<LinkedList<Currency>>() {
            }.getType());

            mMap.clear();
            if (mList != null) {
                for (Currency currency: mList) {
                    if (!currency.alphabeticCode.startsWith("X")) {  //currency code starting with 'X' --> IGNORED
                        mMap.put(currency.alphabeticCode, currency);
                    }
                    DLog.d(TAG, currency.toString());
                }
            }

            Collections.sort(mList);
        }
    }

    /**
     * locate the position in the currency list for the given currency.
     * @param currency
     * @return return index if found, else return -1
     */
    public int find(Currency currency) {

        if (currency == null) return -1;

        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).alphabeticCode.equals(currency.alphabeticCode)) {
                return i;
            }
        }

        return -1;
    }

    private String readFile(String fileName) {

        String content = "";
        try {
            InputStream in = CCApplication.getAppInstance().getAssets().open(fileName);

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line  = bufReader.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            content = sb.toString();

        } catch (IOException e) {
            DLog.e(TAG, e.toString());
            e.printStackTrace();
            return null;
        }

        return content;
    }

}
