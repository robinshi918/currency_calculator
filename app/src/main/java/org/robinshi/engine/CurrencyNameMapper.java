package org.robinshi.engine;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.robinshi.CCApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Map currency ISO code to its Chinese Name and English Name
 */
public class CurrencyNameMapper {
    private static final String TAG = CurrencyNameMapper.class.getSimpleName();

    private static CurrencyNameMapper mInstance;

    private CurrencyNameMapper() {
        load();
    }


    public static synchronized CurrencyNameMapper getInstance() {
        if (mInstance == null) {
            mInstance = new CurrencyNameMapper();
        }
        return mInstance;
    }

    Map<String, Currency> mMap = new ConcurrentHashMap<>(100);

    public LinkedList<Currency> getAll() {
        return new LinkedList<>(mMap.values());
    }

    public Currency getCurrency(String alphabeticCode) {
        return mMap.get(alphabeticCode);
    }

    /**
     * load map data from disk.
     */
    private void load() {

        String jsonData = readFile("currency_table.txt");

        LinkedList<Currency> list = null;
        if (!TextUtils.isEmpty(jsonData)) {
            Gson gson = new Gson();
            list = gson.fromJson(jsonData, new TypeToken<LinkedList<Currency>>() {
            }.getType());

            mMap.clear();
            if (list != null) {
                for (Currency currency: list) {
                    mMap.put(currency.alphabeticCode, currency);
                    Log.d(TAG, currency.toString());
                }
            }
        }
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
            Log.e(TAG, e.toString());
            e.printStackTrace();
            return null;
        }

        return content;
    }


    public class Currency {
        public String alphabeticCode;     // ISO alphabeticCode
        public String numericCode;        // ISO number code
        public String enName;             // English Name
        public String cnName;             // Chinese Name
        public Bitmap bitmap;             // national flag icon

        @Override
        public String toString() {
            return "Currency{" +
                    "alphabeticCode='" + alphabeticCode + '\'' +
                    ", numericCode='" + numericCode + '\'' +
                    ", enName='" + enName + '\'' +
                    ", cnName='" + cnName + '\'' +
                    '}';
        }
    }

}
