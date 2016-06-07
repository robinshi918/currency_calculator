package org.robinshi.engine.baidu.domain;

/**
 * convert result of BaiduApi
 */
public class ConvertResult extends CacheItem{

    public int errNum;
    public String errMsg;
    ConvertRetData retData;

    public int getErrNum() {
        return errNum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public ConvertRetData getRetData() {
        return retData;
    }

    @Override
    public String toString() {
        return "ConvertResult{" +
                "errNum=" + errNum +
                ", errMsg='" + errMsg + '\'' +
                ", retData=" + retData +
                '}';
    }

    @Override
    public boolean hasExpire() {
        return true;
    }
}
