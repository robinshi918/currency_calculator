package org.robinshi.baidu.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shiyun on 16/5/28.
 */
public class TypeResult extends CacheItem implements Serializable {

    /**
*
         {
             "errNum": 0,
             "errMsg": "success",
             "retData": [
                 "AED",
                 "AFN",
                 "ALL",
                 "AMD",
                 "ANG",
                 "AOA"

             ]
         }
 */
    private int errNum;
    private String errMsg;
    private List<String> retData;

    public int getErrNum() {
        return errNum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public List<String> getRetData() {
        return retData;
    }

    @Override
    public boolean hasExpire() {
        return false;
    }
}
