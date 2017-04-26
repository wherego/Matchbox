package com.jkdroid.johnny.matchbox.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	private static final String TAG = BaseFragment.class.getSimpleName();
	public Context context;
    public View view;

	public BaseFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		view = initView(inflater);
		return view;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

	@Override
	public void onStart() {
		super.onStart();
		if(!(getActivity() instanceof BackHandledInterface)){
			throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
		}else{
			//告诉FragmentActivity，当前Fragment在栈顶
			((BackHandledInterface) getActivity()).setTopFragment(this);
		}
	}

	public abstract View initView(LayoutInflater inflater);
	public abstract void initData();

	public boolean onBackPressed() {
		return false;
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if(getView()!=null){
			getView().setVisibility(menuVisible? View.VISIBLE: View.INVISIBLE);
		}
	}

	/**
	 *Fragment跳转接口
	 */
	public interface FragmentSwitchInterface{
		void switchFragment(Fragment fromfragment, Fragment tofragment, Boolean isSave);
	}

	/**
	 * Fragment返回键接口
	 */
	public interface BackHandledInterface {
		void setTopFragment(BaseFragment topFragment);
	}
	
	public void switchFragment(Fragment fromfragment, Fragment tofragment, Boolean isSave) {
		if (tofragment != null) {
			if (getActivity() instanceof FragmentSwitchInterface) {
				((FragmentSwitchInterface) getActivity()).switchFragment(fromfragment, tofragment,isSave);
			}else {
				throw new ClassCastException("Hosting Activity must implement FragmentSwitchInterface");
			}
		}
	}
}
