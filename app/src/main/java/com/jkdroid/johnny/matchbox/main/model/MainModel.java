package com.jkdroid.johnny.matchbox.main.model;

import android.util.Log;
import com.google.gson.Gson;
import com.jkdroid.johnny.matchbox.base.BaseSubscriber;
import com.jkdroid.johnny.matchbox.base.MyCallBack;
import com.jkdroid.johnny.matchbox.bean.UserInfo;
import com.jkdroid.johnny.matchbox.http.HttpClient;
import com.jkdroid.johnny.matchbox.main.contract.LoginContract;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Subscription;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by johnny on 2017/3/12.
 */

public class MainModel implements LoginContract.IMainModel {

    private static final String TAG = MainModel.class.getSimpleName();

    @Override
    public Subscription login(String username, String password, final MyCallBack<UserInfo> myCallBack) {
        String url = "login";
        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("username", username);
        loginMap.put("password", password);
        Subscription subscription=HttpClient.getInstance().login(url, loginMap, new BaseSubscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String resultJson = responseBody.string();
                    Log.d(TAG, "ResponseBody:" + resultJson);
                    JSONObject result = new JSONObject(resultJson);
                    if (result.has("responseCode") && result.getInt("responseCode") == 200) {
                        Gson gson = new Gson();
                        UserInfo userInfo = gson.fromJson(result.getString("userInfo"), UserInfo.class);
                        myCallBack.onSuccess(userInfo);
                    } else {
                        myCallBack.onFailure(-1, "登录失败:" + resultJson);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    myCallBack.onFailure(-1, "登录失败:" + e.getLocalizedMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                    myCallBack.onFailure(-1, "登录失败:" + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
        return subscription;
    }
}
