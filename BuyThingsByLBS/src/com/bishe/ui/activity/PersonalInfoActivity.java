package com.bishe.ui.activity;

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

	@Override
	protected BaseFragment getFragment() {
		// TODO Auto-generated method stub
		return new PersonalInfoEditFragment();
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
	protected void initData() {
		setActionBar("个人信息", null, true);
	}

}
