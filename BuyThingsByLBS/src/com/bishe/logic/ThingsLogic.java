package com.bishe.logic;

import java.util.List;

import junit.framework.Assert;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.bishe.model.Things;
import com.bishe.model.User;
import com.bishe.utils.LogUtils;

import android.content.Context;

/**
 * @author robin
 * @date 2015-4-24
 * Copyright 2015 The robin . All rights reserved
 */
public class ThingsLogic {

	public static final String TAG = "ThingsLogic";
	
	private UserLogic mUserLogic;
	private Context mContext;
	
	public  ThingsLogic(Context context) {
		this.mContext = context;
	}
	
	/**
	 * 
	 * 发布模块
	 * */
	public interface IsPublishListener
	{
		void onPublichSuccess();
		void onPublsihFailure(String msg);
	}
	
	private IsPublishListener mIsPublishListener;
	
	public void setOnIsPublishListener(IsPublishListener isPublishListener) {
		this.mIsPublishListener = isPublishListener;
	}
	
	/**
	 * 发布东西
	 * @param things != null
	 */
	public void publishThings(final Things things)
	{
		Assert.assertEquals(null, things);		
		
		things.save(mContext, new SaveListener() {
			
			@Override
			public void onSuccess() {
				if (null ==  mIsPublishListener ) {
					LogUtils.i(TAG, "IsPublishListener is null,you must set one!");
					return;
				}
				mIsPublishListener.onPublichSuccess();
			}
			@Override
			public void onFailure(int arg0, String arg1) {
				if (null ==  mIsPublishListener ) {
					LogUtils.i(TAG, "IsPublishListener is null,you must set one!");
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
	public interface IsUpdateListener
	{
		void onUpdateSuccess();
		void onUpdateFailure(String msg);
	}
	
	private IsUpdateListener mIsUpdateListener;
	
	public void setOnIsUpdateListener(IsUpdateListener isUpdateListener) {
		this.mIsUpdateListener = isUpdateListener;
	}
	
	public void updateThings(Things things)
	{
		Assert.assertEquals(null, things);	
		things.update(mContext, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				if (null ==  mIsUpdateListener ) {
					LogUtils.i(TAG, "IsUpdateListener is null,you must set one!");
					return;
				}
				mIsUpdateListener.onUpdateSuccess();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				if (null ==  mIsUpdateListener ) {
					LogUtils.i(TAG, "IsUpdateListener is null,you must set one!");
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
	public interface IsDeleteListener
	{
		void onDeleteSuccess();
		void onDeleteFailure(String msg);
	}
	
	private IsDeleteListener mIsDeleteListener;
	
	public void setOnIsDeleteListener(IsDeleteListener isDeleteListener) {
		this.mIsDeleteListener = isDeleteListener;
	}
	
	public void deleteThings(Things things)
	{
		Assert.assertEquals(null, things);	
		things.delete(mContext, new DeleteListener() {
			
			@Override
			public void onSuccess() {
				if (null ==  mIsDeleteListener ) {
					LogUtils.i(TAG, "IsDeleteListener is null,you must set one!");
					return;
				}			
				mIsDeleteListener.onDeleteSuccess();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				if (null ==  mIsDeleteListener ) {
					LogUtils.i(TAG, "IsDeleteListener is null,you must set one!");
					return;
				}	
				mIsDeleteListener.onDeleteFailure(arg1);
			}
		});
		
	}
	
}
