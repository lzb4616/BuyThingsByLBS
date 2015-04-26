package com.bishe.ui.activity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;

import com.bishe.buythingsbylbs.R;
import com.bishe.ui.base.BaseFragment;
import com.bishe.ui.base.BaseHomeActivity;
import com.bishe.ui.base.BasePageActivity;
import com.bishe.ui.fragment.LoginFragment;
import com.bishe.ui.fragment.RegisterFragment;
import com.bishe.view.PagerSlidingTabStrip;;


/**
 * @author robin
 * @date 2015-4-25
 * Copyright 2015 The robin . All rights reserved
 */
public class LoginAndRegisterActivity extends BasePageActivity {

	private LoginFragment mLoginFragment;
	private RegisterFragment mRegisterFragment;
	
	private PagerSlidingTabStrip mTabs;

	private DisplayMetrics mDisplayMetrics;

	@Override
	protected void setLayoutView() {
		setContentView(R.layout.activity_login_register);
		
	}

	@Override
	protected void findViews() {
		mDisplayMetrics = getResources().getDisplayMetrics();
		ViewPager pager = (ViewPager) findViewById(R.id.login_register_pager);
		mTabs = (PagerSlidingTabStrip) findViewById(R.id.login_register_tabs);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		mTabs.setViewPager(pager);	
		setOverflowShowingAlways();
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(R.string.login_register_title);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void setupViews(Bundle bundle) {
		// 设置Tab是自动填充满屏幕的
		mTabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		mTabs.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		mTabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, mDisplayMetrics));
		// 设置Tab Indicator的高度
		mTabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, mDisplayMetrics));
		// 设置Tab标题文字的大小
		mTabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, mDisplayMetrics));
		// 设置Tab Indicator的颜色
		mTabs.setIndicatorColor(Color.parseColor("#45c01a"));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		mTabs.setSelectedTextColor(Color.parseColor("#45c01a"));
		// 取消点击Tab时的背景色
		mTabs.setTabBackground(0);
		
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		private final String[] titles = { "登录", "注册" };

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (mLoginFragment == null) {
					mLoginFragment = new LoginFragment();
				}
				return mLoginFragment;
			case 1:
				if (mRegisterFragment == null) {
					mRegisterFragment = new RegisterFragment();
				}
				return mRegisterFragment;
			default:
				return null;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.empty_menu, menu);
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
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	protected void setListener() {
		
	}

	@Override
	protected void fetchData() {
		
	}


}
