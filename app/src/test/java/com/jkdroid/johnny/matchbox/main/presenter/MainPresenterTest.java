package com.jkdroid.johnny.matchbox.main.presenter;

import com.jkdroid.johnny.matchbox.base.MyCallBack;
import com.jkdroid.johnny.matchbox.main.model.IMainModel;
import com.jkdroid.johnny.matchbox.main.view.ILoginView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

/**
 * Created by johnny on 2017/3/12.
 */
@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock
    private ILoginView iLoginView;
    @Mock
    private IMainModel mainModel;
    @Captor
    private ArgumentCaptor<MyCallBack<String>> cb;

    MainPresenter mainPresenter = new MainPresenter(iLoginView);

    @Test
    public void login() throws Exception {

        mainPresenter.login("123456","张三张三");

//        verify(mainModel).login(anyString(),anyString(),cb.capture());
//
//        cb.getValue().onSuccess("登录成功");


        verify(iLoginView).showLoading();
        verify(iLoginView).moveToHomeView();
    }

}