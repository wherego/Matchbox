package com.jkdroid.johnny.matchbox.main.presenter;

import android.util.Log;
import com.jkdroid.johnny.matchbox.R;
import com.jkdroid.johnny.matchbox.base.BasePresenter;
import com.jkdroid.johnny.matchbox.base.MyCallBack;
import com.jkdroid.johnny.matchbox.bean.UserInfo;
import com.jkdroid.johnny.matchbox.main.contract.LoginContract;
import com.jkdroid.johnny.matchbox.main.model.MainModel;
import com.jkdroid.johnny.matchbox.utils.StringUtils;
import com.jkdroid.johnny.matchbox.utils.UIUtils;

/**
 * Created by johnny on 2017/3/12.
 */

public class MainPresenter extends BasePresenter {

    private static final String TAG = MainPresenter.class.getSimpleName();
    private LoginContract.ILoginView iLoginView;
    private LoginContract.IMainModel mainModel;

    public MainPresenter(LoginContract.ILoginView iLoginView) {
        this.iLoginView = iLoginView;
        mainModel = new MainModel();
    }

    public void login(String username,String password){
        if(StringUtils.isEmpty(username)){
            iLoginView.showToast(UIUtils.getString(R.string.login_username_empty));
        }else if(StringUtils.isEmpty(password)){
            iLoginView.showToast(UIUtils.getString(R.string.login_password_empty));
        }else {
            iLoginView.showLoading();
            addSubscription(mainModel.login(username, password, new MyCallBack<UserInfo>() {
                @Override
                public void onSuccess(UserInfo userInfo) {
                    iLoginView.moveToHomeView();
                }

                @Override
                public void onFailure(int errorType, String errorMsg) {
                    Log.e(TAG, errorMsg);
                }
            }));
        }
    }

}
