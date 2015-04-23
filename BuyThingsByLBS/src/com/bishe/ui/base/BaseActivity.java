package com.bishe.ui.base;

import com.bishe.MyApplication;
import com.bishe.config.Constant;
import com.bishe.utils.Sputil;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
/**
 * @author robin
 * @date 2015-4-20
 * Copyright 2015 The robin . All rights reserved
 */
public abstract class BaseActivity extends FragmentActivity implements
		OnSharedPreferenceChangeListener {

	protected static String TAG ;
	
	protected MyApplication mMyApplication;
	protected Sputil sputil;
	protected Resources mResources;
	protected Context mContext;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		TAG = this.getClass().getSimpleName();
		initConfigure();
	}
	
	private void initConfigure() {
		mContext = this;
		if(null == mMyApplication){
			mMyApplication = MyApplication.getInstance();
		}
		mMyApplication.addActivity(this);
		if(null == sputil){
			sputil = new Sputil(this, Constant.SP_NAME);
		}
		sputil.getInstance().registerOnSharedPreferenceChangeListener(this);
		mResources = getResources();
	}
    
	/**
	 * Activity跳转
	 * @param context
	 * @param targetActivity
	 * @param bundle
	 */
	public void redictToActivity(Context context,Class<?> targetActivity,Bundle bundle){
		Intent intent = new Intent(context, targetActivity);
		if(null != bundle){
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

	}

}
