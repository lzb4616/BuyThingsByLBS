package com.bishe.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.bishe.buythingsbylbs.R;
import com.bishe.logic.UserLogic;
import com.bishe.logic.UserLogic.ILoginListener;
import com.bishe.ui.base.BaseFragment;
import com.bishe.utils.ActivityUtils;
import com.bishe.utils.LogUtils;
import com.bishe.view.DeletableEditText;
import com.bishe.view.SmoothProgressBar;

/**
 * @author robin
 * @date 2015-4-25
 * Copyright 2015 The robin . All rights reserved
 */
public class LoginFragment extends BaseFragment implements ILoginListener,OnClickListener {

	DeletableEditText mUserNameInput;
	DeletableEditText mUserPasswordInput;
	DeletableEditText mUserEmailInput;

	Button mRegisterBtn;
	SmoothProgressBar mProgressbar;

	UserLogic mUserLogic;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View contentView = inflater.inflate(R.layout.fragment_login_register,
				container, false);

		initViews(contentView);

		mUserLogic = new UserLogic(mContext);

		return contentView;
	}

	private void initViews(View view) {
		mUserNameInput = (DeletableEditText) view
				.findViewById(R.id.user_name_input);
		mUserPasswordInput = (DeletableEditText) view
				.findViewById(R.id.user_password_input);
		mUserEmailInput = (DeletableEditText) view
				.findViewById(R.id.user_email_input);

		mUserEmailInput.setVisibility(View.GONE);
		mRegisterBtn = (Button) view.findViewById(R.id.register_btn);
		mRegisterBtn.setText("登录");
		mRegisterBtn.setOnClickListener(this);
		
		mProgressbar = (SmoothProgressBar) view
				.findViewById(R.id.sm_progressbar);

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
			mUserLogic.setOnLoginListener(this);
			mProgressbar.setVisibility(View.VISIBLE);
			mUserLogic.login(mUserNameInput.getText().toString().trim(),
					mUserPasswordInput.getText().toString().trim());
		}
	}

	private void dimissProgressbar(){
		if(mProgressbar!=null&&mProgressbar.isShown()){
			mProgressbar.setVisibility(View.GONE);
		}
	}

	@Override
	public void onLoginSuccess() {
		dimissProgressbar();
		ActivityUtils.toastShowBottom(mContext, "登录成功");
		LogUtils.i(TAG,"login sucessed!");
		mContext.setResult(mContext.RESULT_OK);
		mContext.finish();		
	}

	@Override
	public void onLoginFailure(String msg) {
		dimissProgressbar();
		ActivityUtils.toastShowBottom(mContext, "登录失败。请确认网络连接后再重试。");
		LogUtils.i(TAG,"login failed!"+msg);		
	}
}
