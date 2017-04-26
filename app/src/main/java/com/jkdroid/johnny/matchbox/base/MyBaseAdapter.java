package com.jkdroid.johnny.matchbox.base;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by John on 2015/4/8.
 */
public abstract class MyBaseAdapter<T,Q> extends BaseAdapter {

    public Context context;
    public List<T> list;
    public Q q;

    public MyBaseAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
    }

    public MyBaseAdapter(Context context, List<T> list, Q q) {
        this.context = context;
        this.list = list;
        this.q=q;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}

