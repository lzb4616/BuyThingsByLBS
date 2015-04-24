package com.bishe.ui.base;



import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
/**
 * @author robin
 * @date 2015-4-21
 * Copyright 2015 The robin . All rights reserved
 */
public abstract class BaseHomeFragment extends BaseFragment{
	
	protected ActionBar actionBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(getLayoutId(),container,false);
		findViews(contentView);
		setupViews(savedInstanceState);
		initListeners();
		initData();
		return contentView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(getMenuRes(), menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		onMenuItemSelected(item);
		return super.onOptionsItemSelected(item);
	}
	
	protected void setActionBar(String title,Integer logoResId,Boolean isHomeAsUpEnabled)
	{
		actionBar = ((Activity)mContext).getActionBar();
		if (null != title) {
			actionBar.setTitle(title);
		}
		if (null != logoResId) {
			actionBar.setLogo(logoResId);
		}
		actionBar.setDisplayHomeAsUpEnabled(isHomeAsUpEnabled==true?true:false);
	}
	
	protected abstract int getMenuRes();
	protected abstract void onMenuItemSelected(MenuItem item);
	protected abstract int getLayoutId();
	protected abstract void findViews(View view);
	protected abstract void setupViews(Bundle bundle);
	protected abstract void initListeners();  
	protected abstract void initData();  
    
}
