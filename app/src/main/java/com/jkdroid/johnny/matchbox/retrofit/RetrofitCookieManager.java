package com.jkdroid.johnny.matchbox.retrofit;

import android.content.Context;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.List;

/**
 * CookieManager
 * Created by johnny on 24/04/2017.
 */

public class RetrofitCookieManager implements CookieJar {

    private static Context context;
//    private static PersistentCookieStore cookieStore;

    public RetrofitCookieManager(Context context) {
        this.context = context;
//        if(null==cookieStore){
//            cookieStore = new PersistentCookieStore();
//        }
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
//                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
//        List<Cookie> cookies = cookieStore.get(url);
//        return cookies;
        return null;
    }
}
