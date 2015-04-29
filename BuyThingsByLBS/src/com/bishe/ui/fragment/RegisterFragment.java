package com.bishe.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.bishe.buythingsbylbs.R;
import com.bishe.config.Constant;
import com.bishe.logic.UserLogic;
import com.bishe.logic.UserLogic.ISignUpListener;
import com.bishe.model.Location;
import com.bishe.model.User;
import com.bishe.ui.activity.MyLoactionActivity;
import com.bishe.ui.base.BaseFragment;
import com.bishe.utils.ActivityUtils;
import com.bishe.utils.LogUtils;
import com.bishe.utils.StringUtils;
import com.bishe.view.DeletableEditText;
import com.bishe.view.SmoothProgressBar;

/**
 * @author robin
 * @date 2015-4-25 Copyright 2015 The robin . All rights reserved
 */
public class RegisterFragment extends BaseFragment implements ISignUpListener, OnClickListener {

	DeletableEditText mUserNameInput;
	DeletableEditText mUserPasswordInput;
	DeletableEditText mUserEmailInput;
	DeletableEditText mUserLocationInput;
	
	Button mRegisterBtn;
	SmoothProgressBar mProgressbar;

	UserLogic mUserLogic;
	
	private Location mLocation;
	private String mLocationName;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View contentView = inflater.inflate(R.layout.fragment_login_register,
				container, false);

		mUserLogic = new UserLogic(mContext);
		initViews(contentView);
		return contentView;
	}

	private void initViews(View view) {
		mUserNameInput = (DeletableEditText) view
				.findViewById(R.id.user_name_input);
		mUserPasswordInput = (DeletableEditText) view
				.findViewById(R.id.user_password_input);
		mUserEmailInput = (DeletableEditText) view
				.findViewById(R.id.user_email_input);
		mUserLocationInput = (DeletableEditText) view.findViewById(R.id.user_location_input);
		
		mRegisterBtn = (Button) view.findViewById(R.id.register_btn);
		mRegisterBtn.setOnClickListener(this);
		mProgressbar = (SmoothProgressBar) view
				.findViewById(R.id.sm_progressbar);
		
		mUserLocationInput.setOnClickListener(this);

		mUserLogic.setOnSignUpListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.register_btn) {
			if (TextUtils.isEmpty(mUserNameInput.getText())) {
				mUserNameInput.setShakeAnimation();
				ActivityUtils.toastShowBottom(mContext, "请输入用户名");
				return;
			}
			if (TextUtils.isEmpty(mUserPasswordInput.getText())) {
				mUserPasswordInput.setShakeAnimation();
				ActivityUtils.toastShowBottom(mContext, "请输入密码");
				return;
			}
			if (TextUtils.isEmpty(mUserEmailInput.getText())) {
				mUserEmailInput.setShakeAnimation();
				ActivityUtils.toastShowBottom(mContext, "请输入邮箱地址");
				return;
			}
			if (!StringUtils.isValidEmail(mUserEmailInput.getText())) {
				mUserEmailInput.setShakeAnimation();
				ActivityUtils.toastShowBottom(mContext, "邮箱格式不正确");
				return;
			}
			if (null == mLocation) {
				ActivityUtils.toastShowBottom(mContext, "没有取得地址");
				return;
			}
			if (null == mLocationName) {
				ActivityUtils.toastShowBottom(mContext, "没有取得地址");
				return;
			}
			mProgressbar.setVisibility(View.VISIBLE);
			User user = new User();
			user.setUsername(mUserNameInput.getText().toString().trim());
			user.setPassword(mUserPasswordInput.getText().toString().trim());
			user.setEmail(mUserEmailInput.getText().toString().trim());
			user.setSex(Constant.SEX_MALE);
			user.setLocation(mLocation);
			user.setUserLoactionName(mLocationName);
			user.setSignature("来吧，写上的心灵鸡汤");
			mUserLogic.singUp(user);
		}
		
		if (v.getId() == R.id.user_location_input) {
			Intent intent = new Intent(mContext, MyLoactionActivity.class);
			startActivityForResult(intent, Constant.GET_LOCATION);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case Constant.GET_LOCATION:
				mLocationName= (String) data.getStringExtra("locationName");
				if (null != mLocationName) {
					mUserLocationInput.setText(mLocationName);
				}
				mLocation = (Location) data.getSerializableExtra("location");
				break;
			default:
				break;
			}
		}
	}
	
	private void dimissProgressbar(){
		if(mProgressbar!=null&&mProgressbar.isShown()){
			mProgressbar.setVisibility(View.GONE);
		}
	}
	
	
	public void onSignUpSuccess() {
		dimissProgressbar();
		ActivityUtils.toastShowBottom(getActivity(), "注册成功");
		LogUtils.i(TAG,"register successed！");
		mContext.setResult(mContext.RESULT_OK);
		mContext.finish();
	}

	@Override
	public void onSignUpFailure(String msg) {
		dimissProgressbar();
		ActivityUtils.toastShowBottom(mContext, "注册失败。请确认网络连接后再重试。");
		LogUtils.i(TAG,"register failed！");
	}

}
