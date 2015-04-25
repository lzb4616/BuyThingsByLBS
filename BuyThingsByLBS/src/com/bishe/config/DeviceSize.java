package com.bishe.config;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;

public class DeviceSize {

	private int mHeight;
	private int mWidth;
	private float mDensity;
	private Context mContext;

	public DeviceSize(Context context) {
		this.mContext = context;
		getDeviceSizeAttrs();
	}

	private void getDeviceSizeAttrs() {
		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();;
		setmHeight(dm.heightPixels);
		setmWidth(dm.widthPixels);
		setmDensity(dm.density);
	}

	/**
	 * 获取屏幕高度 单位是px
	 * 
	 * */
	public int getmHeight() {
		return mHeight;
	}

	/**
	 * 获取屏幕密度
	 * 
	 * */
	public float getmDensity() {
		return mDensity;
	}

	/**
	 * 获取屏幕宽度 单位是px
	 * 
	 * */
	public int getmWidth() {
		return mWidth;
	}

	private void setmHeight(int mHeight) {
		this.mHeight = mHeight;
	}

	private void setmWidth(int mWidth) {
		this.mWidth = mWidth;
	}

	private void setmDensity(float mDensity) {
		this.mDensity = mDensity;
	}

}
