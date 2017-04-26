package com.jkdroid.johnny.matchbox.base;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * Created by mwqi on 2014/6/7.
 */
public abstract class BaseHolder<Data> {
	private View mRootView;
	private int mPosition;
	private List<Data> mData;
	protected Context context;

	public BaseHolder(Context context) {
		this.context = context;
		mRootView = initView();
		mRootView.setTag(this);
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public View getRootView() {
		return mRootView;
	}

	public void setData(List<Data> data) {
		mData = data;
		refreshView(mData);
	}

	public List<Data> getData() {
		return mData;
	}

	public void setPosition(int position) {
		mPosition = position;
	}

	public int getPosition() {
		return mPosition;
	}

//	public void recycleImageView(ImageView view) {
//		Object tag = view.getTag();
//		if (tag != null && tag instanceof String) {
//			String key = (String) tag;
//			ImageLoader.cancel(key);
//			// view.setImageDrawable(null);
//		}
//	}

	/** 子类必须覆盖用于实现UI初始化 */
	protected abstract View initView();

	/** 子类必须覆盖用于实现UI刷新 */
	public abstract void refreshView(List<Data> list);

	/** 用于回收 */
	public void recycle() {

	}
}
