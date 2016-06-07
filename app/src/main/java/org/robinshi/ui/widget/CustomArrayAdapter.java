package org.robinshi.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.robinshi.engine.CurrencyNameMapper;
import org.robinshi.engine.ImageManager;
import org.robinshi.R;
import org.robinshi.ui.activity.CalculatorActivity;

import java.util.List;

/**
 * Created by shiyun on 16/6/7.
 */
public class CustomArrayAdapter extends ArrayAdapter<CurrencyNameMapper.Currency> {

    LayoutInflater mInflater;
    List<CurrencyNameMapper.Currency> mData;
    private CalculatorActivity calculatorActivity;

    public CustomArrayAdapter(CalculatorActivity calculatorActivity, Context context, int resId) {
        super(context, resId);
        this.calculatorActivity = calculatorActivity;
        mInflater = LayoutInflater.from(context);
        mData = CurrencyNameMapper.getInstance().getAll();
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

        CurrencyNameMapper.Currency item = mData.get(position);
        viewHolder.textView.setText(item.alphabeticCode + "  " + item.cnName);
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
