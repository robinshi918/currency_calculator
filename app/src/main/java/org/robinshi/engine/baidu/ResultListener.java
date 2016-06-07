package org.robinshi.engine.baidu;

/**
 * Created by shiyun on 16/6/4.
 */
public interface ResultListener<T> {

    void onResponse(T result);

    void onError(Exception e);
}
