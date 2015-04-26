package com.bishe.ui.base;




import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
/**
 * @author robin
 * @date 2015-4-21
 * Copyright 2015 The robin . All rights reserved
 */
public abstract class BaseHomeFragment extends BaseFragment{
	

	
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
	

	

	protected abstract int getLayoutId();
	protected abstract void findViews(View view);
	protected abstract void setupViews(Bundle bundle);
	protected abstract void initListeners();  
	protected abstract void initData();  
    
}
