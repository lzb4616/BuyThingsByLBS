package com.bishe.ui;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.view.Menu;
import android.view.ViewConfiguration;
import android.view.Window;

import com.bishe.buythingsbylbs.R;
import com.bishe.ui.base.BaseActivity;

/**
 * @author robin
 * @date 2015-4-22
 * Copyright 2015 The robin . All rights reserved
 */
public class MainActivity extends BaseActivity {

	@Override
	public void setContentView() {
		setContentView(R.layout.activity_main);
		setOverflowShowingAlways();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_action_menu, menu);
		return true;
		
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

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fetchData() {
		// TODO Auto-generated method stub
		
	}

}
