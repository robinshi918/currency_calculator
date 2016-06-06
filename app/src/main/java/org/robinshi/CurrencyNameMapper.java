package org.robinshi;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
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

    public Collection<Currency> getAll() {
        return mMap.values();
    }

    public Currency getCurrency(String alphabeticCode) {
        return mMap.get(alphabeticCode);
    }

    /**
     * load map data from disk.
     */
    private void load() {

        String jsonData = readFile2("currency_table.txt");

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

    /**
     * read file into a String.
     * @param fileName file name
     * @return
     */
    private String readFile(String fileName) {

        String str = "";
        try {
            AssetManager am = CCApplication.getAppInstance().getAssets();
            AssetFileDescriptor fd = am.openFd(fileName);
            FileInputStream fin = new FileInputStream(fd.getFileDescriptor());

            // size  为字串的长度 ，这里一次性读完
            int size = fin.available();
            byte[] buffer = new byte[size];
            fin.read(buffer);
            fin.close();
            str = new String(buffer, "UTF-8");


        } catch (IOException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            return null;
        }

        return str;
    }

    private String readFile2(String fileName) {
        String str = "";
        try {
            InputStream in = CCApplication.getAppInstance().getAssets().open(fileName);

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line  = bufReader.readLine()) != null) {
                sb.append(line);
            }

            str = sb.toString();

        } catch (IOException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            return null;
        }

        return str;
    }


    public class Currency {
        public String alphabeticCode;     // ISO alphabeticCode
        public String numericCode;        // ISO number code
        public String enName;             // English Name
        public String cnName;             // Chinese Name
        public String symbol;             // currency symbol character
        public String iconPath;           // icon path

        @Override
        public String toString() {
            return "Currency{" +
                    "alphabeticCode='" + alphabeticCode + '\'' +
                    ", numericCode='" + numericCode + '\'' +
                    ", enName='" + enName + '\'' +
                    ", cnName='" + cnName + '\'' +
                    ", symbol='" + symbol + '\'' +
                    ", iconPath='" + iconPath + '\'' +
                    '}';
        }
    }

}
