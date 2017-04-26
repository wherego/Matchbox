package com.jkdroid.johnny.matchbox.base;

import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;

import java.util.Map;

/**
 * Created by Johnny on 2015/7/8.
 */
public abstract class BaseWebViewFragment extends BaseFragment{

    private static final String TAG = "BaseWebViewFragment";
    private CookieManager cookieManager;
    public Map<String, Object> viewMap;


    @Override
    public View initView(LayoutInflater inflater) {
//        View view = inflater.inflate(R.layout.base_frag_webview, null);
//        ViewUtils.inject(this, view);
        return view;
    }

    public abstract void initData();

//    private void onSyscCookie(Context context, CookieStore cookieStore, String url) {
//        CookieSyncManager.createInstance(context);
//        cookieManager = CookieManager.getInstance();
//        cookieManager.setAcceptCookie(true);
//        cookieManager.removeAllCookie(); //移除上一次的session
//        //设置cookie
//        List<Cookie> cookies = cookieStore.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                String cookieString =cookie.getName() + "=" + cookie.getValue() + ";domain=" + cookie.getDomain()+";path=/";
//                Config.SESSIONID = cookie.getValue();
//                Log.d(TAG, "Cookie内容:"+cookieString);
//                cookieManager.setCookie(url, cookieString);
//            }
//        }
//        CookieSyncManager.getInstance().sync(); //同步
//    }

//    public void loadWebView(WebView webView, String url, Object object) {
//        WebSettings webSetting = webView.getSettings();
//        webSetting.setBuiltInZoomControls(false);//不支持缩放
//        webSetting.setJavaScriptEnabled(true);//支持JavaScript
//        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSetting.setBlockNetworkImage(false);
//        webSetting.setAppCacheEnabled(false);//不支持缓存
////        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);//设置默认缓存模式
//        webSetting.setDefaultTextEncodingName("UTF-8");//设置解码时的默认编码
//        String userAgen = webSetting.getUserAgentString();//
//        webSetting.setUserAgentString(userAgen + " SietaiMobileX/0.0");
//        //
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WebView.setWebContentsDebuggingEnabled(true);
//        }
//        Log.d(TAG, "COOKIESTORE:" + Config.COOKIESTORE);
//        if(null!= Config.COOKIESTORE){
//            onSyscCookie(context,Config.COOKIESTORE,url);//同步Cookie值
//        }
//        if (null!=webView) {
//            webView.setWebChromeClient(new WebChromeClient() {
//                @Override
//                public void onProgressChanged(WebView view, int newProgress) {
//                    super.onProgressChanged(view, newProgress);
//                    view.requestFocus();
//                }
//            });
//            webView.requestFocus(View.FOCUS_DOWN);
//            webView.loadUrl(url);
//            webView.reload();
//            webView.addJavascriptInterface(object,"control");
//        }
//    }
}
