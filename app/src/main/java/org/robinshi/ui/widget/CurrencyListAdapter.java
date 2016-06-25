package org.robinshi.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.robinshi.engine.Currency;
import org.robinshi.engine.CurrencyMapper;
import org.robinshi.engine.ImageManager;
import org.robinshi.R;
import org.robinshi.ui.activity.CalculatorActivity;

import java.util.List;

/**
 * List Adapter for currency list spinner.
 */
public class CurrencyListAdapter extends ArrayAdapter<Currency> {

    LayoutInflater mInflater;
    List<Currency> mData;
    private CalculatorActivity calculatorActivity;

    public CurrencyListAdapter(CalculatorActivity calculatorActivity, Context context, int resId) {
        super(context, resId);
        this.calculatorActivity = calculatorActivity;
        mInflater = LayoutInflater.from(context);
        mData = CurrencyMapper.getInstance().getCurrencyList();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spinner_dropdown_item, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text_view);
            viewHolder.imgView = (ImageView) convertView.findViewById(R.id.flag_icon);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Currency item = mData.get(position);
        viewHolder.textView.setText(item.alphabeticCode + "" + item.cnName);
        viewHolder.imgView.setImageBitmap(
                ImageManager.getInstance().getBitmap(item.alphabeticCode));

        return convertView;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }


    class ViewHolder {
        TextView textView;
        ImageView imgView;
    }
}
