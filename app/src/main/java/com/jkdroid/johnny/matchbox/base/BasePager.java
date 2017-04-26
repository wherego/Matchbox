package com.jkdroid.johnny.matchbox.base;

import android.content.Context;
import android.view.View;

public abstract class BasePager {
    public Context context ;
	private View view;
	public boolean is_loading = false;
	public BasePager(Context ct) {
		this.context = ct;
		view = initView();
	}
	public abstract View initView() ;
	public abstract void initData() ;

	public View getRootView(){
		return view;
	}

//	public void loadData(HttpRequest.HttpMethod method,String url,RequestParams params,RequestCallBack<String> callBack){
//		HttpUtils httpUtils = new HttpUtils();
//		if(params == null){
//			params = new RequestParams();
//			params.addHeader("Cookie", "JSESSIONID="+ Config.SESSIONID);
//		}
//		if(CommonUtil.isNetworkAvailable(context) != 0) {
//			httpUtils.configCurrentHttpCacheExpiry(1000 * 10);
//			httpUtils.send(method, url, params, callBack);
//		}else{
//			Toast.makeText(context, "请开启网络", Toast.LENGTH_SHORT).show();
//		}
//	}
}
