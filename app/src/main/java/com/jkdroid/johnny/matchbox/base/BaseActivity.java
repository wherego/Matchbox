package com.jkdroid.johnny.matchbox.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import com.jkdroid.johnny.matchbox.utils.CommonUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Johnny on 2015/8/14.
 */
public abstract class BaseActivity extends AppCompatActivity {

    /** 记录处于前台的Activity */
    private static BaseActivity mForegroundActivity = null;
    /** 记录所有活动的Activity */
    private static final List<BaseActivity> mActivities = new LinkedList<BaseActivity>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initView(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//禁止输入法自动弹出
    }

    @Override
    protected void onResume() {
        mForegroundActivity = this;
        super.onResume();
    }

    @Override
    protected void onPause() {
        mForegroundActivity = null;
        super.onPause();
    }

    public abstract void initView(Bundle savedInstanceState);

    /**
     * 输入法失焦后，自动退出
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (CommonUtils.isShouldHideInput(view, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }


    /** 关闭所有Activity */
    public static void finishAll() {
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new ArrayList<BaseActivity>(mActivities);
        }
        for (BaseActivity activity : copy) {
            activity.finish();
        }
    }

    /** 关闭所有Activity，除了参数传递的Activity */
    public static void finishAll(BaseActivity except) {
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new ArrayList<BaseActivity>(mActivities);
        }
        for (BaseActivity activity : copy) {
            if (activity != except) activity.finish();
        }
    }

    /** 是否有启动的Activity */
    public static boolean hasActivity() {
        return mActivities.size() > 0;
    }

    /** 获取当前处于前台的activity */
    public static BaseActivity getForegroundActivity() {
        return mForegroundActivity;
    }

    /** 获取当前处于栈顶的activity，无论其是否处于前台 */
    public static BaseActivity getCurrentActivity() {
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new ArrayList<BaseActivity>(mActivities);
        }
        if (copy.size() > 0) {
            return copy.get(copy.size() - 1);
        }
        return null;
    }

    /** 退出应用 */
    public void exitApp() {
        finishAll();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
