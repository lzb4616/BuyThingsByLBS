package com.bishe.utils;

import com.bishe.config.Constant;

import android.util.Log;


/**
 * @author robin
 * @date 2015-4-20
 * Copyright 2015 The robin . All rights reserved
 */
public class LogUtils {
	public static void v(String tag, String msg) {
		if (Constant.DEBUG) {
			Log.v(tag, msg);
		}

	}

	public static void d(String tag, String msg) {
		if (Constant.DEBUG) {
			Log.d(tag, msg);
		}

	}

	public static void i(String tag, String msg) {
		if (Constant.DEBUG) {
			Log.i(tag, msg);
		}

	}

	public static void w(String tag, String msg) {
		if (Constant.DEBUG) {
			Log.w(tag, msg);
		}

	}

	public static void e(String tag, String msg) {
		if (Constant.DEBUG) {
			Log.e(tag, msg);
		}
	}

}
