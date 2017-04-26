package com.jkdroid.johnny.matchbox.bean;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by johnny on 2017/3/12.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserInfoTest {

    @Mock
    UserInfo userInfo;

    @Test
    public void getUserId() throws Exception {
        when(userInfo.getUserId());
        Assert.assertEquals(123456,userInfo.getUserId());
    }

    @Test
    public void setUserId() throws Exception {
        userInfo.setUserId(123456);
        //TODO:这里为什么要用Matchers.eq(123456)
        verify(userInfo).setUserId(Matchers.eq(123456));
//        verify(userInfo).setUserId(123456);
    }

    @Test
    public void getPicture() throws Exception {

    }

    @Test
    public void setPicture() throws Exception {

    }

}