package com.jkdroid.johnny.matchbox.main.contract;

import com.jkdroid.johnny.matchbox.base.MyBaseView;
import com.jkdroid.johnny.matchbox.base.MyCallBack;
import com.jkdroid.johnny.matchbox.bean.UserInfo;
import rx.Subscription;

/**
 * Created by johnny on 24/04/2017.
 */

public class LoginContract {


    public interface IMainModel {

        Subscription login(String username, String password, MyCallBack<UserInfo> myCallBack);
    }

    public interface ILoginView extends MyBaseView {

        void showLoading();

        void moveToHomeView();
    }
}
