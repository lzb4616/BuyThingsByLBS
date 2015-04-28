package com.bishe.logic;

import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.bishe.MyApplication;
import com.bishe.config.Constant;
import com.bishe.model.Comment;
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
 * @date 2015-4-24 Copyright 2015 The robin . All rights reserved
 */
public class ThingsLogic {

	public static final String TAG = "ThingsLogic";

	private UserLogic mUserLogic;
	private Context mContext;

	public ThingsLogic(Context context) {
		this.mContext = context;
		this.mUserLogic = new UserLogic(context);
	}

	/**
	 * 
	 * 发布模块
	 * */
	public interface IsPublishListener {
		void onPublichSuccess();

		void onPublsihFailure(String msg);
	}

	private IsPublishListener mIsPublishListener;

	public void setOnIsPublishListener(IsPublishListener isPublishListener) {
		this.mIsPublishListener = isPublishListener;
	}

	/**
	 * 发布东西
	 * 
	 * @param things
	 *            != null
	 */
	public void publishThings(final Things things) {
		things.save(mContext, new SaveListener() {

			@Override
			public void onSuccess() {
				if (null == mIsPublishListener) {
					LogUtils.i(TAG,
							"IsPublishListener is null,you must set one!");
					return;
				}
				mIsPublishListener.onPublichSuccess();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				if (null == mIsPublishListener) {
					LogUtils.i(TAG,
							"IsPublishListener is null,you must set one!");
					return;
				}
				mIsPublishListener.onPublsihFailure(arg1);
			}
		});
	}

	/**
	 * 
	 * 更新模块
	 * */
	public interface IsUpdateListener {
		void onUpdateSuccess();

		void onUpdateFailure(String msg);
	}

	private IsUpdateListener mIsUpdateListener;

	public void setOnIsUpdateListener(IsUpdateListener isUpdateListener) {
		this.mIsUpdateListener = isUpdateListener;
	}

	public void updateThings(Things things) {
		things.update(mContext, new UpdateListener() {

			@Override
			public void onSuccess() {
				if (null == mIsUpdateListener) {
					LogUtils.i(TAG,
							"IsUpdateListener is null,you must set one!");
					return;
				}
				mIsUpdateListener.onUpdateSuccess();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				if (null == mIsUpdateListener) {
					LogUtils.i(TAG,
							"IsUpdateListener is null,you must set one!");
					return;
				}
				mIsUpdateListener.onUpdateFailure(arg1);
			}
		});
	}

	/**
	 * 
	 * 删除模块
	 * */
	public interface IsDeleteListener {
		void onDeleteSuccess();

		void onDeleteFailure(String msg);
	}

	private IsDeleteListener mIsDeleteListener;

	public void setOnIsDeleteListener(IsDeleteListener isDeleteListener) {
		this.mIsDeleteListener = isDeleteListener;
	}

	public void deleteThings(Things things) {
		things.delete(mContext, new DeleteListener() {

			@Override
			public void onSuccess() {
				if (null == mIsDeleteListener) {
					LogUtils.i(TAG,
							"IsDeleteListener is null,you must set one!");
					return;
				}
				mIsDeleteListener.onDeleteSuccess();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				if (null == mIsDeleteListener) {
					LogUtils.i(TAG,
							"IsDeleteListener is null,you must set one!");
					return;
				}
				mIsDeleteListener.onDeleteFailure(arg1);
			}
		});

	}

	/**
	 * 获取我的收藏列表
	 * 
	 * */
	public interface OnGetMyFavoutiteListener {
		void onGetMyFavSuccess(List<Things> data);

		void onGetMyFavFailure(String msg);
	}

	private OnGetMyFavoutiteListener mMyFavoutiteListener;

	public void setOnGetMyFavoutiteListener(
			OnGetMyFavoutiteListener favoutiteListener) {
		this.mMyFavoutiteListener = favoutiteListener;
	}

	public void getMyFavouriteThings() {
		User user = mUserLogic.getCurrentUser();
		if (user != null) {
			BmobQuery<Things> query = new BmobQuery<Things>();
			query.addWhereRelatedTo("favorite", new BmobPointer(user));
			query.include("user");
			query.order("createdAt");
			query.setLimit(Constant.NUMBERS_PER_PAGE);
			query.findObjects(mContext, new FindListener<Things>() {

				@Override
				public void onSuccess(List<Things> data) {
					if (null == mMyFavoutiteListener) {
						LogUtils.i(TAG,
								"mMyFavoutiteListener is null,you must set one!");
						return;
					}
					mMyFavoutiteListener.onGetMyFavSuccess(data);
				}

				@Override
				public void onError(int arg0, String arg1) {
					if (null == mMyFavoutiteListener) {
						LogUtils.i(TAG,
								"mMyFavoutiteListener is null,you must set one!");
						return;
					}
					mMyFavoutiteListener.onGetMyFavFailure(arg1);
				}
			});
		} else {
			// 前往登录注册界面
			ActivityUtils.toastShowBottom((Activity) mContext, "获取收藏前请先登录。");
			Intent intent = new Intent();
			intent.setClass(mContext, LoginAndRegisterActivity.class);
			MyApplication.getInstance().getTopActivity()
					.startActivityForResult(intent, Constant.GET_FAVOURITE);
		}
	}
	
	/**
	 * 获取所有Things列表
	 * */
	public interface OnGetAllThingsListener
	{
		void onGetAllThingsSuccess(List<Things> data);
		void onGetAllThingsFailure(String msg);
	}
	
	private OnGetAllThingsListener mGetAllThingsListener;
	
	public void setOnGetAllThingsListener(OnGetAllThingsListener allThingsListener)
	{
		this.mGetAllThingsListener = allThingsListener;
	}
	
	public void getAllThings(int pageNum)
	{
		BmobQuery<Things> query = new BmobQuery<Things>();
		query.order("-createdAt");
		query.setCachePolicy(CachePolicy.NETWORK_ONLY);
		query.setLimit(Constant.NUMBERS_PER_PAGE);
		BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
		query.addWhereLessThan("createdAt", date);
		query.setSkip(Constant.NUMBERS_PER_PAGE*(pageNum));
		query.include("author");
		query.findObjects(mContext, new FindListener<Things>() {
			@Override
			public void onSuccess(List<Things> list) {
				if (null == mGetAllThingsListener) {
					LogUtils.i(TAG,
							"mGetAllThingsListener is null,you must set one!");
					return;
				}
				mGetAllThingsListener.onGetAllThingsSuccess(list);
			}

			@Override
			public void onError(int arg0, String arg1) {
				if (null == mGetAllThingsListener) {
					LogUtils.i(TAG,
							"mGetAllThingsListener is null,you must set one!");
					return;
				}
				mGetAllThingsListener.onGetAllThingsFailure(arg1);
			}
		});
	}
	
	public interface OnThingsRelatedCommnetListener
	{
		void onRelatedCommentSuccess();
		void onRelatedCommnetFailure(String msg);
	}
	
	private OnThingsRelatedCommnetListener mOnRelatedCommnetListener;
	
	public void setOnThingsRelatedCommnetListener(OnThingsRelatedCommnetListener commnetListener)
	{
		this.mOnRelatedCommnetListener = commnetListener;
	}
	
	
	public void publishCommetWithThings(Things things,Comment comment)
	{
		BmobRelation relation = new BmobRelation();
		relation.add(comment);
		things.setRelation(relation);
		things.setComment(things.getComment()+1);
		things.setMyFav(false);
		things.update(mContext, new UpdateListener() {
			@Override
			public void onSuccess() {
				if (null == mOnRelatedCommnetListener) {
					LogUtils.i(TAG,
							"mOnRelatedCommnetListener is null,you must set one!");
					return;
				}
				mOnRelatedCommnetListener.onRelatedCommentSuccess();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				if (null == mOnRelatedCommnetListener) {
					LogUtils.i(TAG,
							"mOnRelatedCommnetListener is null,you must set one!");
					return;
				}
				mOnRelatedCommnetListener.onRelatedCommnetFailure(arg1);
			}
		});
	}
	
	/**
	 * 获取用户的东西列表
	 * 
	 * */
	public interface OnGetUserThingsListener {
		void onGetUserThingsSuccess(List<Things> data);

		void onGetUserThingsFailure(String msg);
	}

	private OnGetUserThingsListener mOnGetUserThingsListener;

	public void setOnGetUserThingsListener(
			OnGetUserThingsListener getUserThingsListener) {
		this.mOnGetUserThingsListener = getUserThingsListener;
	}

	/**
	 *获取用户发布的东西列表
	 * */
	public void getUserPublishThings(User user,int pageNum) {
		if (user != null) {
			BmobQuery<Things> query = new BmobQuery<Things>();
			query.setLimit(Constant.NUMBERS_PER_PAGE);
			query.setSkip(Constant.NUMBERS_PER_PAGE*(pageNum));
			query.order("-createdAt");
			query.include("author");
			query.addWhereEqualTo("author", user);
			query.findObjects(mContext, new FindListener<Things>() {
				
				@Override
				public void onSuccess(List<Things> data) {
					if (null == mOnGetUserThingsListener) {
						LogUtils.i(TAG,
								"mOnGetUserThingsListener is null,you must set one!");
						return;
					}
					mOnGetUserThingsListener.onGetUserThingsSuccess(data);
				}

				@Override
				public void onError(int arg0, String msg) {
					if (null == mOnGetUserThingsListener) {
						LogUtils.i(TAG,
								"mOnGetUserThingsListener is null,you must set one!");
						return;
					}
					mOnGetUserThingsListener.onGetUserThingsFailure(msg);
				}
			});
		} else {
			// 前往登录注册界面
			ActivityUtils.toastShowBottom((Activity) mContext, "获取前请先登录。");
			Intent intent = new Intent();
			intent.setClass(mContext, LoginAndRegisterActivity.class);
			MyApplication.getInstance().getTopActivity()
					.startActivity(intent);
		}
	}
	
	/**
	 * 获取我的已购东西列表
	 * 
	 * */
	public interface OnGetMyHadBuyThingsListener {
		void onGetMyHadBuyThingsSuccess(List<Things> data);

		void onGetMyHadBuyThingsFailure(String msg);
	}

	private  OnGetMyHadBuyThingsListener mBuyThingsListener;

	public void setOnGetMyHadBuyThingsListener(
			OnGetMyHadBuyThingsListener buyThingsListener) {
		this.mBuyThingsListener = buyThingsListener;
	}

	public void getMyHadBuyThings() {
		User user = mUserLogic.getCurrentUser();
		if (user != null) {
			BmobQuery<Things> query = new BmobQuery<Things>();
			query.addWhereRelatedTo("buyThing", new BmobPointer(user));
			query.include("user");
			query.order("createdAt");
			query.setLimit(Constant.NUMBERS_PER_PAGE);
			query.findObjects(mContext, new FindListener<Things>() {

				@Override
				public void onSuccess(List<Things> data) {
					if (null == mBuyThingsListener) {
						LogUtils.i(TAG,
								"mBuyThingsListener is null,you must set one!");
						return;
					}
					mBuyThingsListener.onGetMyHadBuyThingsSuccess(data);
				}

				@Override
				public void onError(int arg0, String arg1) {
					if (null == mBuyThingsListener) {
						LogUtils.i(TAG,
								"mBuyThingsListener is null,you must set one!");
						return;
					}
					mBuyThingsListener.onGetMyHadBuyThingsFailure(TAG);
				}
			});
		} else {
			// 前往登录注册界面
			ActivityUtils.toastShowBottom((Activity) mContext, "获取前请先登录。");
			Intent intent = new Intent();
			intent.setClass(mContext, LoginAndRegisterActivity.class);
			MyApplication.getInstance().getTopActivity()
					.startActivity(intent);
		}
	}
	
}
