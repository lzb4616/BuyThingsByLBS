package com.bishe.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bishe.buythingsbylbs.R;
import com.bishe.ui.base.BaseFragment;
import com.bishe.ui.base.BaseHomeFragment;
import com.bishe.utils.ActivityUtils;

/**
 * @author robin
 * @date 2015-4-23
 * Copyright 2015 The robin . All rights reserved
 */
public class TestFram extends BaseHomeFragment {



	@Override
	protected int getMenuRes() {
		// TODO Auto-generated method stub
		return R.menu.main_action_menu;
	}

	@Override
	protected void onMenuItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			((Activity)mContext).finish();
			break;
		case R.id.action_about:
			ActivityUtils.toastShowCenter(getActivity(), "关于");
		default:
			break;
		}
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_main;
	}

	@Override
	protected void findViews(View view) {
		
	}

	@Override
	protected void setupViews(Bundle bundle) {
	}

	@Override
	protected void initListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}
}
