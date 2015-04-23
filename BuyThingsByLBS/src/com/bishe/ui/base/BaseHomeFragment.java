package com.bishe.ui.base;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;

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
		initActionBar();
		setOverflowShowingAlways();
		findViews(contentView);
		setupViews(savedInstanceState);
		initListeners();
		initData();
		return contentView;
	}


	private void initActionBar() {
		
		actionBar = ((Activity)mContext).getActionBar();
		if (null != actionBarTitle()) {
			actionBar.setTitle(actionBarTitle());
		}
		actionBar.setDisplayHomeAsUpEnabled(isHomeAsUpEnabled()==true?true:false);

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
		return true;
	}
	
	private void setOverflowShowingAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(mContext);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected abstract String actionBarTitle();
	protected abstract int getMenuRes();
	protected abstract boolean isHomeAsUpEnabled();
	protected abstract void onMenuItemSelected(MenuItem item);
	protected abstract int getLayoutId();
	protected abstract void findViews(View view);
	protected abstract void setupViews(Bundle bundle);
	protected abstract void initListeners();  
	protected abstract void initData();  
    

}
