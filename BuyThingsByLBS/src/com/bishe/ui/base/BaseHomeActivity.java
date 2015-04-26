package com.bishe.ui.base;



import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import com.bishe.buythingsbylbs.R;
import com.bishe.config.Constant;

import cn.bmob.v3.Bmob;
import android.R.bool;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
/**
 * @author robin
 * @date 2015-4-21
 * Copyright 2015 The robin . All rights reserved
 */
public abstract class BaseHomeActivity extends BaseActivity{
	
	protected ActionBar actionBar;
	protected FragmentManager mFragmentManager;
	protected FragmentTransaction mFragmentTransaction;
	
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		Bmob.initialize(this, Constant.BMOB_APP_ID);
		setContentView(R.layout.activity_main);
		initData();
		initFragment();
		setOverflowShowingAlways();
	}

	public void initFragment(){
		mFragmentManager= getSupportFragmentManager();
		mFragmentTransaction = mFragmentManager
				.beginTransaction();
		BaseFragment fragment = getFragment();
		List<Fragment> fList = mFragmentManager.getFragments();
		
		if ( (null == fList) || (!fList.contains(fragment))) {
			mFragmentTransaction.add(R.id.content_frame_base, fragment);
		}else
		{
			hideFragments(fragment);
			mFragmentTransaction.show(fragment);
		}
		
		mFragmentTransaction.commit();
	}
    /**
     * 将所有fragment都置为隐藏状态
     * 
     * @param transaction
     *            用于对Fragment执行操作的事务
     */
    private void hideFragments(BaseFragment baseFragment) {
    	List<Fragment> fList = mFragmentManager.getFragments();
    		
    	for (Fragment fragment : fList)
    	{
			if (!fragment.equals(baseFragment)) {
				mFragmentTransaction.hide(fragment);
			}
		}
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(getMenuRes(), menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		onMenuItemSelected(item);
		return super.onMenuItemSelected(featureId, item);
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
	
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}
	
	private void setOverflowShowingAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected abstract BaseFragment getFragment();
	protected abstract int getMenuRes();
	protected abstract void onMenuItemSelected(MenuItem item);
	protected abstract void initData();
}
