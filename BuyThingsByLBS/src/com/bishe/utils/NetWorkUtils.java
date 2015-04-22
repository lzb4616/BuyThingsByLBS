package com.bishe.utils;

import com.bishe.config.Constant;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * @author robin
 * @date 2015-4-20 
 * Copyright 2015 The robin . All rights reserved
 */
public class NetWorkUtils {

	/**
	 * 返回网络是否可用。需要权限：
	 * <p>
	 * <b> < uses-permission
	 * android:name="android.permission.ACCESS_NETWORK_STATE" /> </b>
	 * </p>
	 * 
	 * @param context
	 *            上下文
	 * @return 网络可用则返回true，否则返回false
	 */
	public static Boolean isAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = cm.getActiveNetworkInfo();
		return info != null && info.isAvailable();
	}

	/**
	 * 判断网络状态
	 * */
	public static String getNetType(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (cm == null) {
				return Constant.NETWORK_TYPE_ERROR;
			}

			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info == null || info.isConnected()) {
				return Constant.NETWORK_TYPE_ERROR;
			}

			switch (info.getType()) {
			case ConnectivityManager.TYPE_WIFI:
				return Constant.NETWORK_TYPE_WIFI;
			case ConnectivityManager.TYPE_MOBILE:
				return Constant.NETWORK_TYPE_MOBILE;
			}

		} catch (Exception e) {
			return Constant.NETWORK_TYPE_ERROR;
		}
		return Constant.NETWORK_TYPE_ERROR;
	}

	/**
	 * 判断wifi是否启用
	 * */
	public static Boolean isWifiActivate(Context context) {
		return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
				.isWifiEnabled();
	}

	/**
	 * 修改wifi状态
	 * */
	public static void changeWifiStatus(Context context, Boolean status) {
		((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
				.setWifiEnabled(status);
	}
}
