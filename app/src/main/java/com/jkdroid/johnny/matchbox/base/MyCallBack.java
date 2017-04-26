package com.jkdroid.johnny.matchbox.base;

/**
 * Created by John on 2015/12/29.
 */
public abstract class MyCallBack<T> {

    public MyCallBack() {
    }

    public abstract void onSuccess(T t);

    public abstract void onFailure(int errorType,String errorMsg);

    public void onLoading(int progress) {
    }
}
