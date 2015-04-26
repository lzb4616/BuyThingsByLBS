package com.bishe.logic;

import java.io.File;
import java.util.Vector;

import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.bishe.model.ThingsImage;
import com.bishe.utils.LogUtils;

import android.content.Context;

/**
 * @author robin
 * @date 2015-4-24 Copyright 2015 The robin . All rights reserved
 */
public class ThingsImageLogic {

	public static final String TAG = "ThingsImageLogic";

	public Context mContext;

	public ThingsImageLogic(Context context) {

		this.mContext = context;
	}

	/**
	 * 上传图片模块
	 * */
	public interface IsUploadImageListener {
		void onUploadImageSuccess(ThingsImage image);

		void onUploadImageProgress(Integer arg0);

		void onUploadImageFailure(String msg);
	}

	private IsUploadImageListener mUploadImageListener;

	public void setIsUploadImageListener(IsUploadImageListener imageListener) {
		this.mUploadImageListener = imageListener;
	}

	public void uploadImageWithPaths(Vector<String> imagePaths) {
		if (null != imagePaths) {
			Vector<ThingsImage> thingsImages = new Vector<ThingsImage>();
			for (String imagePath : imagePaths) {
				ThingsImage image = new ThingsImage(new File(imagePath));
				thingsImages.add(image);
			}
			uploadImage(thingsImages);
		}
	}

	
	public void upLoadImageWithPath(String imagePath)
	{
		if (null != imagePath) {
			ThingsImage image = new ThingsImage(new File(imagePath));
			uploadImage(image);
		}
	}
	
	public void uploadImage(Vector<ThingsImage> imgaes) {
		for (ThingsImage thingsImage : imgaes) {
			uploadImage(thingsImage);
		}
	}


	public void uploadImage(final ThingsImage image) {
		if (null == image) {
			LogUtils.i(TAG, "image is null,you must set one!");
			return;
		}

		image.upload(mContext, new UploadFileListener() {

			@Override
			public void onSuccess() {
				if (null ==  mUploadImageListener ) {
					LogUtils.i(TAG, "IsUploadImageListener is null,you must set one!");
					return;
				}
				mUploadImageListener.onUploadImageSuccess(image);
			}

			@Override
			public void onProgress(Integer arg0) {
				if (null ==  mUploadImageListener ) {
					LogUtils.i(TAG, "IsUploadImageListener is null,you must set one!");
					return;
				}
				mUploadImageListener.onUploadImageProgress(arg0);
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				if (null ==  mUploadImageListener ) {
					LogUtils.i(TAG, "IsUploadImageListener is null,you must set one!");
					return;
				}
				mUploadImageListener.onUploadImageFailure(arg1);
			}
		});
		
	}

	/**
	 * 删除图片模块
	 * */
	public interface IsdeleteImageListener {
		void onDeleteImageSuccess();

		void onDeleteImageFailure(String msg);
	}

	private IsdeleteImageListener mIsdeleteImageListener;

	public void deleteImage(Vector<ThingsImage> imgaes) {
		for (ThingsImage thingsImage : imgaes) {
			deleteImage(thingsImage);
		}
	}

	public void deleteImage(ThingsImage image) {
		if (null == image) {
			LogUtils.i(TAG, "image is null,you must set one!");
			return;
		}
		image.delete(mContext, new DeleteListener() {

			@Override
			public void onSuccess() {
				if (null ==  mIsdeleteImageListener ) {
					LogUtils.i(TAG, "IsdeleteImageListener is null,you must set one!");
					return;
				}
				mIsdeleteImageListener.onDeleteImageSuccess();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				if (null ==  mIsdeleteImageListener ) {
					LogUtils.i(TAG, "IsdeleteImageListener is null,you must set one!");
					return;
				}
				mIsdeleteImageListener.onDeleteImageFailure(arg1);
			}
		});
	}

}
