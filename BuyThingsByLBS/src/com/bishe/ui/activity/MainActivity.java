package com.bishe.ui.activity;

import android.view.MenuItem;
import android.widget.Toast;

import com.bishe.buythingsbylbs.R;
import com.bishe.logic.UserLogic;
import com.bishe.model.User;
import com.bishe.ui.base.BaseFragment;
import com.bishe.ui.base.BaseHomeActivity;
import com.bishe.ui.fragment.LoginFragment;
import com.bishe.ui.fragment.MainFragment;
import com.bishe.ui.fragment.RegisterFragment;
import com.bishe.ui.fragment.TestFragment;
import com.bishe.utils.LogUtils;

/**
 * @author robin
 * @date 2015-4-22 Copyright 2015 The robin . All rights reserved
 */
public class MainActivity extends BaseHomeActivity {

	private UserLogic mUserLogic;
	private MainFragment mMainFragment;
	private TestFragment mLoginFragment;
	private RegisterFragment mRegisterFragment;

	private SelectFragment mSelectFragment;

	private enum SelectFragment {
		MAINFRAGMENT, LOGINFRAGMENT, REGISTERFRAGMENT
	}

	@Override
	protected BaseFragment getFragment() {
		switch (mSelectFragment) {
		case MAINFRAGMENT:
			return mMainFragment;
		case LOGINFRAGMENT:
			return mLoginFragment;
		case REGISTERFRAGMENT:
			return mRegisterFragment;
		default:
			return mMainFragment;
		}
	}

	@Override
	protected int getMenuRes() {
		return R.menu.main_action_menu;
	}

	@Override
	protected void onMenuItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_publish:
			publishThingsActivtiy();
			break;
		case R.id.action_collection:

			break;
		case R.id.action_about:
			break;
		case R.id.action_main:
			mSelectFragment = SelectFragment.MAINFRAGMENT;
			break;
		case R.id.action_settings:
			mSelectFragment = SelectFragment.LOGINFRAGMENT;
			break;
		case R.id.action_login:
			redictToActivity(mContext, LoginAndRegisterActivity.class, null);
			break;
		case R.id.action_login_out:
			mUserLogic.loginOut();
			break;
		}
		initFragment();
	}

	private void publishThingsActivtiy() {
		// 当前用户登录
		User currentUser = mUserLogic.getCurrentUser();
		if (currentUser != null) {
			// 允许用户使用应用,即有了用户的唯一标识符，可以作为发布内容的字段
			String name = currentUser.getUsername();
			String email = currentUser.getEmail();
			LogUtils.i(TAG, "username:" + name + ",email:" + email);
			redictToActivity(mContext, PublishThingsActivity.class, null);
		} else {
			// 缓存用户对象为空时， 可打开用户注册界面…
			Toast.makeText(MainActivity.this, "请先登录。", Toast.LENGTH_SHORT)
					.show();
			redictToActivity(mContext, LoginAndRegisterActivity.class, null);
		}
	}

	@Override
	protected void initData() {
		mUserLogic = new UserLogic(mContext);
		mMainFragment = new MainFragment();
		mLoginFragment = new TestFragment();
		mRegisterFragment = new RegisterFragment();
		mSelectFragment = SelectFragment.MAINFRAGMENT;
	}

}
