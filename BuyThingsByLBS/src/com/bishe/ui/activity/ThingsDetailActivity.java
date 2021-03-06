package com.bishe.ui.activity;

import android.view.MenuItem;

import com.bishe.buythingsbylbs.R;
import com.bishe.logic.UserLogic;
import com.bishe.model.Things;
import com.bishe.model.User;
import com.bishe.ui.base.BaseFragment;
import com.bishe.ui.base.BaseHomeActivity;
import com.bishe.ui.fragment.ThingsDetailFragment;

/**
 * @author robin
 * @date 2015-4-27 Copyright 2015 The robin . All rights reserved
 */
public class ThingsDetailActivity extends BaseHomeActivity {

	@Override
	protected BaseFragment getFragment() {
		return ThingsDetailFragment.newInstance();
	}

	@Override
	protected int getMenuRes() {
		return R.menu.detail_action_menu;
	}

	@Override
	protected void onMenuItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_buy:
			mDetailListener.buyThings();
			break;
		case R.id.action_cancel_buy:
			mDetailListener.cancelBuyThings();
			break;
		case R.id.action_delete:
			mDetailListener.deleThings();
			break;
		case R.id.action_update:
			mDetailListener.updateThings();
			break;
		default:
			break;
		}
	}

	private OnThingsDetailListener mDetailListener;

	public void setOnThingsDetailListener(OnThingsDetailListener detailListener) {
		this.mDetailListener = detailListener;
	}

	public interface OnThingsDetailListener {
		void buyThings();

		void cancelBuyThings();

		void deleThings();

		void updateThings();
	}

	@Override
	protected void initData() {
		setActionBar("东西详情", null, true);
	}

}
