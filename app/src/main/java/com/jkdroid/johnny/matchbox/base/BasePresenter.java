package com.jkdroid.johnny.matchbox.base;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johnny on 24/04/2017.
 */

public class BasePresenter {

    protected CompositeSubscription mCompositeSubscription;

    //RxJava注册
    public void addSubscription(Subscription subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        if (null!=subscriber)
            mCompositeSubscription.add(subscriber);
    }

    //RxJava取消注册防止内存泄漏
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions())
            mCompositeSubscription.unsubscribe();
    }
}
