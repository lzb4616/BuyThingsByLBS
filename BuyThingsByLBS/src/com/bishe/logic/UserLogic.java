package com.bishe.logic;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.bishe.config.Constant;
import com.bishe.model.Location;
import com.bishe.model.User;
import com.bishe.utils.LogUtils;

import android.app.Activity;
import android.content.Context;

/**
 * @author robin
 * @date 2015-4-21
 * Copyright 2015 The robin . All rights reserved
 */
public class UserLogic {
	
	public static final String TAG = "UserLogic";
	
	private Context mContext;

	public UserLogic(Context context) {
		this.mContext = context;
	}
	
	/**
	 * 注册模块
	 * */
	public interface ISignUpListener{
		void onSignUpSuccess();
		void onSignUpFailure(String msg);
	}
	private ISignUpListener signUpLister;
	public void setOnSignUpListener(ISignUpListener signUpLister){
		this.signUpLister = signUpLister;
	}
	
	public void singUp(String userName,String passWord,String email)
	{
		User user = new User();
		user.setUsername(userName);
		user.setPassword(passWord);
		user.setEmail(email);
		user.setSex(Constant.SEX_MALE);
		user.setSignature("来吧，写上的心灵鸡汤");
		
		user.signUp(mContext, new SaveListener() {
			
			@Override
			public void onSuccess() {
				if (null != signUpLister) {
					signUpLister.onSignUpSuccess();
					
				}else {
					LogUtils.i(TAG, "signup listener is null,you must set one!");
				}
			}
			@Override
			public void onFailure(int arg0, String arg1) {
				if(signUpLister != null){
					signUpLister.onSignUpFailure(arg1);
				}else{
					LogUtils.i(TAG,"signup listener is null,you must set one!");
				}
			}
		});
	}
	
	public User getCurrentUser()
	{
		User user = BmobUser.getCurrentUser(mContext, User.class);
		if (null == user) {
			LogUtils.i(TAG, "user is null");
		} else {
			LogUtils.i(TAG, "用户详细信息:"+user.toString());
		}
		return user;
	}
	
	/**
	 *登录模块 
	 * */
	
	public interface ILoginListener{
		void onLoginSuccess();
		void onLoginFailure(String msg);
	}
	private ILoginListener loginListener;
	public void setOnLoginListener(ILoginListener loginListener){
		this.loginListener  = loginListener;
	}
	public void login(String userName,String passWord)
	{
		User user = new User();
		user.setUsername(userName);
		user.setPassword(passWord);
		user.login(mContext, new SaveListener() {
			
			@Override
			public void onSuccess() {
				if (null == loginListener) {
					LogUtils.i(TAG, "login listener is null,you must set one!");
				} else {
					loginListener.onLoginSuccess();
				}
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				if (null == loginListener) {
					LogUtils.i(TAG, "login listener is null,you must set one!");
				} else {
					loginListener.onLoginFailure(arg1);
				}
			}
		});
	}
	
	/**
	 * 退出登录模块
	 * */
	public void loginOut()
	{
		BmobUser.logOut(mContext);
		LogUtils.i(TAG, "logOut reult:"+(null == getCurrentUser()));
	}
	
	/**
	 * 更新用户信息模块
	 * @param
	 * args[0]:userName;
	 * args[1]:passWord;
	 * args[2]:email;
	 * args[3]:sex;
	 * args[4]:singnature;
	 * 
	 * */
	public void updateUser(String...args)
	{
		User user = getCurrentUser();
		user.setUsername(args[0]);
		user.setEmail(args[2]);
		user.setPassword(args[1]);
		user.setSex(args[3]);
		user.setSignature(args[4]);
		user.update(mContext, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if(mUpdateListener != null){
					mUpdateListener.onUpdateSuccess();
				}else{
					LogUtils.i(TAG,"update listener is null,you must set one!");
				}
			}

			@Override
			public void onFailure(int arg0, String msg) {
				// TODO Auto-generated method stub
				if(mUpdateListener != null){
					mUpdateListener.onUpdateFailure(msg);
				}else{
					LogUtils.i(TAG,"update listener is null,you must set one!");
				}
			}
		});
	}
	
	public interface IsUpdateUserListener
	{
		public void onUpdateSuccess();
		public void onUpdateFailure(String msg);
	}
	
	private IsUpdateUserListener mUpdateListener;
	
	public void setOnUpdateUserListener(IsUpdateUserListener listener)
	{
		this.mUpdateListener = listener;
	}
	
	/**
	 * 重置密码
	 * */
	public void resetPassword(String email){
		BmobUser.resetPassword(mContext, email, new ResetPasswordListener() {
			
			@Override
			public void onSuccess() {
				if(resetPasswordListener != null){
					resetPasswordListener.onResetSuccess();
				}else{
					LogUtils.i(TAG,"reset listener is null,you must set one!");
				}
			}

			@Override
			public void onFailure(int arg0, String msg) {
				if(resetPasswordListener != null){
					resetPasswordListener.onResetFailure(msg);
				}else{
					LogUtils.i(TAG,"reset listener is null,you must set one!");
				}
			}
		});
	}
	public interface IResetPasswordListener{
		void onResetSuccess();
		void onResetFailure(String msg);
	}
	private IResetPasswordListener resetPasswordListener;
	public void setOnResetPasswordListener(IResetPasswordListener resetPasswordListener){
		this.resetPasswordListener = resetPasswordListener;
	}
	

	
}
