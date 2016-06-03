package org.robinshi.baidu;

/**
 * convert result of BaiduApi
 */
public class Convert {

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
        return "Convert{" +
                "errNum=" + errNum +
                ", errMsg='" + errMsg + '\'' +
                ", retData=" + retData +
                '}';
    }
}
