package com.jkdroid.johnny.matchbox.bean;

/**
 * Created by johnny on 24/04/2017.
 */

public class BaseResponse<T> {

    private int responseCode;
    private String responseMsg;
    private T data;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
