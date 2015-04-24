package com.bishe.logic;

import android.content.Context;

import com.bishe.model.Location;
import com.bishe.model.User;

/**
 * @author robin
 * @date 2015-4-24
 * Copyright 2015 The robin . All rights reserved
 */
public class LocationLogic {
	
	private  UserLogic mUserLogic;
	
	private Context mContext;
	
	public LocationLogic(Context context) {
		this.mContext = context;
		this.mUserLogic = new UserLogic(context);
	}
	
	/**
	 * 获取当前用户的地理位置
	 * */
	public Location getCurrentUserLoaction()
	{
		User user = mUserLogic.getCurrentUser();
		if (null == user) {
			return null;
		}
		return user.getLocation();
	}
	
}
