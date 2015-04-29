package com.bishe.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import com.bishe.buythingsbylbs.R;
import com.bishe.ui.base.BaseFragment;
import com.bishe.ui.base.BaseHomeActivity;
import com.bishe.ui.fragment.PersonalInfoEditFragment;

/**
 * @author robin
 * @date 2015-4-28
 * Copyright 2015 The robin . All rights reserved
 */
public class PersonalInfoActivity extends BaseHomeActivity {

	private PersonalInfoEditFragment mEditFragment;
	
	@Override
	protected BaseFragment getFragment() {
		mEditFragment = new PersonalInfoEditFragment();
		return mEditFragment;
	}

	@Override
	protected int getMenuRes() {
		// TODO Auto-generated method stub
		return R.menu.empty_menu;
	}

	@Override
	protected void onMenuItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mEditFragment.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void initData() {
		setActionBar("个人信息", null, true);
	}

}
