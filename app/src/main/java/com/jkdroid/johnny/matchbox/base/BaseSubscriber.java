package com.jkdroid.johnny.matchbox.base;

import rx.Subscriber;

/**
 * Created by johnny on 24/04/2017.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {

    public abstract void onCompleted();

    public abstract void onNext(T t);

    public abstract void onFailure(Throwable e);

    public void onError(Throwable e) {
        //TODO:1.无网络判断，读缓存；2.请求失败，读缓存；
        onFailure(e);
    }
}
