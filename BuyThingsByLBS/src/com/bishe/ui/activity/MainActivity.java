package com.bishe.ui.activity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bishe.buythingsbylbs.R;
import com.bishe.logic.UserLogic;
import com.bishe.model.User;
import com.bishe.ui.base.BaseFragment;
import com.bishe.ui.base.BaseHomeActivity;
import com.bishe.ui.fragment.BuyThingsFragment;
import com.bishe.ui.fragment.MainFragment;
import com.bishe.ui.fragment.MyFavouriteFragment;
import com.bishe.utils.ActivityUtils;
import com.bishe.utils.LogUtils;

/**
 * @author robin
 * @date 2015-4-22 Copyright 2015 The robin . All rights reserved
 */
public class MainActivity extends BaseHomeActivity {

	private UserLogic mUserLogic;
	private MainFragment mMainFragment;
	private MyFavouriteFragment mFavouriteFragment;
	private BuyThingsFragment mBuyThingsFragment;
	private SelectFragmentType mSelectFragment;

	private enum SelectFragmentType {
		MAINFRAGMENT,FAVOURITEFRAGMENT,BUYTHINGSFRAGMENT
	}

	@Override
	protected BaseFragment getFragment() {
		switch (mSelectFragment) {
		case MAINFRAGMENT:
			setActionBar("主页", null, false);
			return mMainFragment;
		case FAVOURITEFRAGMENT:
			setActionBar("我的收藏", null, true);
			return mFavouriteFragment;
		case BUYTHINGSFRAGMENT:
			setActionBar("我购买的东西", null, true);
			return mBuyThingsFragment;
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
			mSelectFragment = SelectFragmentType.FAVOURITEFRAGMENT;
			break;
		case R.id.action_hadBuy:
			mSelectFragment = SelectFragmentType.BUYTHINGSFRAGMENT;
			break;
		case R.id.action_main:
			mSelectFragment = SelectFragmentType.MAINFRAGMENT;
			break;
		case R.id.action_my_info:
			redictToActivity(mContext, PersonalInfoActivity.class, null);
			break;
		case R.id.action_login:
			redictToActivity(mContext, LoginAndRegisterActivity.class, null);
			break;
		case R.id.action_login_out:
			mUserLogic.loginOut();
			ActivityUtils.toastShowBottom(this, "退出登录成功");
			invalidateOptionsMenu();
			break;
		case android.R.id.home:
			mSelectFragment = SelectFragmentType.MAINFRAGMENT;
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
		mFavouriteFragment = new MyFavouriteFragment();
		mBuyThingsFragment = new BuyThingsFragment();
		mSelectFragment = SelectFragmentType.MAINFRAGMENT;

	}
	
	@Override
	protected void onStart() {
		super.onStart();
		invalidateOptionsMenu();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem login_out_item = menu.findItem(R.id.action_login_out);
		MenuItem login_item = menu.findItem(R.id.action_login);
		if (null == mUserLogic.getCurrentUser()) {
			login_item.setVisible(true);
			login_out_item.setVisible(false);
		}else {
			login_item.setVisible(false);
			login_out_item.setVisible(true);
		}
		return true;
	}
	
}
