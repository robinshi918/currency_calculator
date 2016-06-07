package org.robinshi.engine.baidu.domain;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shiyun on 16/5/28.
 */
public class ConvertRetData implements Serializable{

//    date: "2015-08-12",  //日期
//    time: "07:10:46",    //时间
//    fromCurrency: "CNY", //待转化币种的简称，这里为人民币
//    amount: 2,    //转化的金额
//    toCurrency: "USD",  //转化后的币种的简称，这里为美元
//    currency: 0.1628,   //当前汇率
//    convertedamount: 0.3256  //转化后的金额

    public String date;
    public String time;
    public String fromCurrency;
    public String toCurrency;
    public float currency;
    public float convertedamount;
    public float amount;

    public Date getDateObject() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(date + " " + time);
        } catch (ParseException e) {
            return null;
        }
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public float getCurrency() {
        return currency;
    }

    public float getConvertedamount() {
        return convertedamount;
    }

    public float getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "ConvertRetData{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", fromCurrency='" + fromCurrency + '\'' +
                ", toCurrency='" + toCurrency + '\'' +
                ", currency=" + currency +
                ", convertedamount=" + convertedamount +
                ", amount=" + amount +
                '}';
    }
}
