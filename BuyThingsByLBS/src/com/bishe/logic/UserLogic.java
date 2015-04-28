package com.bishe.logic;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.ResetPasswordListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.bishe.MyApplication;
import com.bishe.adapter.ThingsContentAdapter;
import com.bishe.config.Constant;
import com.bishe.model.Things;
import com.bishe.model.User;
import com.bishe.ui.activity.LoginAndRegisterActivity;
import com.bishe.utils.ActivityUtils;
import com.bishe.utils.LogUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * @author robin
 * @date 2015-4-21 Copyright 2015 The robin . All rights reserved
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
	public interface ISignUpListener {
		void onSignUpSuccess();

		void onSignUpFailure(String msg);
	}

	private ISignUpListener signUpLister;

	public void setOnSignUpListener(ISignUpListener signUpLister) {
		this.signUpLister = signUpLister;
	}

	public void singUp(String userName, String passWord, String email) {
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

				} else {
					LogUtils.i(TAG, "signup listener is null,you must set one!");
				}
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				if (signUpLister != null) {
					signUpLister.onSignUpFailure(arg1);
				} else {
					LogUtils.i(TAG, "signup listener is null,you must set one!");
				}
			}
		});
	}

	public User getCurrentUser() {
		User user = BmobUser.getCurrentUser(mContext, User.class);
		if (null == user) {
			LogUtils.i(TAG, "user is null");
		} else {
			LogUtils.i(TAG, "用户详细信息:" + user.toString());
		}
		return user;
	}

	/**
	 * 登录模块
	 * */

	public interface ILoginListener {
		void onLoginSuccess();

		void onLoginFailure(String msg);
	}

	private ILoginListener loginListener;

	public void setOnLoginListener(ILoginListener loginListener) {
		this.loginListener = loginListener;
	}

	public void login(String userName, String passWord) {
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
	public void loginOut() {
		BmobUser.logOut(mContext);
		LogUtils.i(TAG, "logOut reult:" + (null == getCurrentUser()));
	}

	/**
	 * 更新用户信息模块
	 * 
	 * 
	 * */
	public void updateUser(User user) {
		user.update(mContext, new UpdateListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if (mUpdateListener != null) {
					mUpdateListener.onUpdateSuccess();
				} else {
					LogUtils.i(TAG, "update listener is null,you must set one!");
				}
			}

			@Override
			public void onFailure(int arg0, String msg) {
				// TODO Auto-generated method stub
				if (mUpdateListener != null) {
					mUpdateListener.onUpdateFailure(msg);
				} else {
					LogUtils.i(TAG, "update listener is null,you must set one!");
				}
			}
		});
	}

	public interface IsUpdateUserListener {
		public void onUpdateSuccess();

		public void onUpdateFailure(String msg);
	}

	private IsUpdateUserListener mUpdateListener;

	public void setOnUpdateUserListener(IsUpdateUserListener listener) {
		this.mUpdateListener = listener;
	}

	/**
	 * 重置密码
	 * */
	public void resetPassword(String email) {
		BmobUser.resetPassword(mContext, email, new ResetPasswordListener() {

			@Override
			public void onSuccess() {
				if (resetPasswordListener != null) {
					resetPasswordListener.onResetSuccess();
				} else {
					LogUtils.i(TAG, "reset listener is null,you must set one!");
				}
			}

			@Override
			public void onFailure(int arg0, String msg) {
				if (resetPasswordListener != null) {
					resetPasswordListener.onResetFailure(msg);
				} else {
					LogUtils.i(TAG, "reset listener is null,you must set one!");
				}
			}
		});
	}

	public interface IResetPasswordListener {
		void onResetSuccess();

		void onResetFailure(String msg);
	}

	private IResetPasswordListener resetPasswordListener;

	public void setOnResetPasswordListener(
			IResetPasswordListener resetPasswordListener) {
		this.resetPasswordListener = resetPasswordListener;
	}

	public interface OnCollectMyFavouriteListener {
		void onCollectSuccess();

		void onCollectFailure(String msg);

	}

	private OnCollectMyFavouriteListener mCollectMyFavouriteListener;

	public void setOnCollectMyFavouriteListener(
			OnCollectMyFavouriteListener collectMyFavouriteListener) {
		this.mCollectMyFavouriteListener = collectMyFavouriteListener;

	}

	/**
	 * 收藏东西
	 * 
	 * @param things != nil
	 * @param isCollect = ture收藏。 = false 取消收藏
	 * */
	public void collectMyFav(Things things,Boolean isCollect) {
		User user = getCurrentUser();
		if (user != null && user.getSessionToken() != null) {
			BmobRelation favRelaton = new BmobRelation();
			if (isCollect) {
				favRelaton.add(things);
				user.setFavorite(favRelaton);
				user.update(mContext, new UpdateListener() {
					@Override
					public void onSuccess() {
						if (null == mCollectMyFavouriteListener) {
							LogUtils.i(TAG, "mCollectMyFavouriteListener is null,you must set one!");		
							return;
						}
						mCollectMyFavouriteListener.onCollectSuccess();
					}
					@Override
					public void onFailure(int arg0, String arg1) {
						if (null == mCollectMyFavouriteListener) {
							LogUtils.i(TAG, "mCollectMyFavouriteListener is null,you must set one!");	
							return;
						}
						mCollectMyFavouriteListener.onCollectFailure(arg1);
					}
				});

			} else {
				favRelaton.remove(things);
				user.setFavorite(favRelaton);
				user.update(mContext, new UpdateListener() {

					@Override
					public void onSuccess() {
						if (null == mCollectMyFavouriteListener) {
							LogUtils.i(TAG, "mCollectMyFavouriteListener is null,you must set one!");			
							return;
						}
						mCollectMyFavouriteListener.onCollectSuccess();
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						if (null == mCollectMyFavouriteListener) {
							LogUtils.i(TAG, "mCollectMyFavouriteListener is null,you must set one!");		
							return;
						}
						mCollectMyFavouriteListener.onCollectFailure(arg1);
					}
				});
			}

		} else {
			// 前往登录注册界面
			ActivityUtils.toastShowBottom((Activity) mContext, "收藏前请先登录。");
			Intent intent = new Intent();
			intent.setClass(mContext, LoginAndRegisterActivity.class);
			MyApplication.getInstance().getTopActivity()
					.startActivityForResult(intent, ThingsContentAdapter.SAVE_FAVOURITE);
		} 
	}

	
	public interface OnBuyThingsListener {
		void onBuyThingsSuccess();

		void onBuyThingsFailure(String msg);

	}
	
	private OnBuyThingsListener mOnBuyThingsListener;;

	public void setOnBuyThingsListener(
			OnBuyThingsListener onBuyThingsListener) {
		this.mOnBuyThingsListener = onBuyThingsListener;

	}

	/**
	 * 购买东西
	 * 
	 * @param things != nil
	 * @param isBuy = ture购买。 = false 取消购买
	 * */
	public void buyThings(Things things,Boolean isBuy) {
		User user = getCurrentUser();
		if (user != null && user.getSessionToken() != null) {
			BmobRelation buyRelaton = new BmobRelation();
			if (isBuy) {
				buyRelaton.add(things);
				user.setBuyThing(buyRelaton);
				user.update(mContext, new UpdateListener() {
					@Override
					public void onSuccess() {
						if (null == mOnBuyThingsListener) {
							LogUtils.i(TAG, "mOnBuyThingsListener is null,you must set one!");
							return;
						}
						mOnBuyThingsListener.onBuyThingsSuccess();
					}
					@Override
					public void onFailure(int arg0, String arg1) {
						if (null == mOnBuyThingsListener) {
							LogUtils.i(TAG, "mOnBuyThingsListener is null,you must set one!");
							return;
						}
						mOnBuyThingsListener.onBuyThingsFailure(arg1);
					}
				});

			} else {
				buyRelaton.remove(things);
				user.setBuyThing(buyRelaton);
				user.update(mContext, new UpdateListener() {

					@Override
					public void onSuccess() {
						if (null == mOnBuyThingsListener) {
							LogUtils.i(TAG, "mOnBuyThingsListener is null,you must set one!");	
							return;
						}
						mOnBuyThingsListener.onBuyThingsSuccess();
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						if (null == mOnBuyThingsListener) {
							LogUtils.i(TAG, "mOnBuyThingsListener is null,you must set one!");
							return;
						}
						mOnBuyThingsListener.onBuyThingsFailure(arg1);
					}
				});
			}

		} else {
			// 前往登录注册界面
			ActivityUtils.toastShowBottom((Activity) mContext, "收藏前请先登录。");
			Intent intent = new Intent();
			intent.setClass(mContext, LoginAndRegisterActivity.class);
			MyApplication.getInstance().getTopActivity()
					.startActivity(intent);
		} 
	}
	

}
