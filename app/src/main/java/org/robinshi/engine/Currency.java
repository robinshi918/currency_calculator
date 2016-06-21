package org.robinshi.engine;

import android.graphics.Bitmap;

/**
 * Created by shiyun on 6/21/16.
 */
public class Currency implements Comparable<Currency>{
    public String alphabeticCode;     // ISO alphabeticCode
    public String numericCode;        // ISO number code
    public String enName;             // English Name
    public String cnName;             // Chinese Name
    public Bitmap bitmap;             // national flag icon

    @Override
    public int compareTo(Currency another) {
        return this.alphabeticCode.compareTo(another.alphabeticCode);
    }

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
