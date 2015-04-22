package com.bishe.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.bishe.MyApplication;
import com.bishe.buythingsbylbs.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * @author robin
 * @date 2015-4-20
 * Copyright 2015 The robin . All rights reserved
 */
public class ActivityUtils {
	/**
	 * 获取屏幕宽高
	 * @return int[] { widthPixels, heightPixels };
	 */
	public static int[] getScreenSize() {
		int[] screens;
		DisplayMetrics dm = new DisplayMetrics();
		dm = MyApplication.getInstance().getResources().getDisplayMetrics();
		screens = new int[] { dm.widthPixels, dm.heightPixels };
		return screens;
	}
	
	/**
	 * 获取应用版本号
	 */
	public static String getVersionName(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * 通过外部浏览器打开页面
	 * 
	 */
	public static void openBrowser(Context context, String urlText) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri url = Uri.parse(urlText);
		intent.setData(url);
		context.startActivity(intent);
	}
	/**
	 * 切换全屏状态。
	 * 
	 * @param isFull
	 *            设置为true则全屏，否则非全屏
	 */
	public static void toggleFullScreen(Activity activity, boolean isFull) {
		hideTitleBar(activity);
		Window window = activity.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		if (isFull) {
			params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			window.setAttributes(params);
			window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			window.setAttributes(params);
			window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}
	/**
	 * 隐藏Activity的系统默认标题栏
	 * 
	 */
	public static void hideTitleBar(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	/**
	 * 设置为全屏
	 * 
	 */
	public static void setFullScreen(Activity activity) {
		toggleFullScreen(activity, true);
	}
	
	/**
	 * 获取系统状态栏高度
	 * 
	 * @return 状态栏高度
	 */
	public static int getStatusBarHeight(Activity activity) {
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			Field field = clazz.getField("status_bar_height");
			int dpHeight = Integer.parseInt(field.get(object).toString());
			return activity.getResources().getDimensionPixelSize(dpHeight);
		} catch (Exception e1) {
			e1.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 强制设置Actiity的显示方向为垂直方向。
	 * 
	 */
	public static void setScreenVertical(Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	/**
	 * 强制设置Activity的显示方向为横向。
	 * 
	 */
	public static void setScreenHorizontal(Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	
	/**
	 * 隐藏软件输入法
	 * 
	 */
	public static void hideSoftInput(Activity activity) {
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	/**
	 * 关闭已经显示的输入法窗口。
	 * 
	 * @param context
	 *            上下文对象，一般为Activity
	 * @param focusingView
	 *            输入法所在焦点的View
	 */
	public static void closeSoftInput(Context context, View focusingView) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(focusingView.getWindowToken(),
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}
	/**
	 * 使UI适配输入法
	 * 
	 * @param activity
	 *            Activity
	 */
	public static void adjustSoftInput(Activity activity) {
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	/**
	 * 短时间显示Toast消息,显示在品屏幕中间，并保证运行在UI线程中
	 * 
	 * @param activity
	 *            Activity
	 * @param message
	 *            消息内容
	 */
	public static void toastShowCenter(final Activity activity, final String message) {
		activity.runOnUiThread(new Runnable() {

			public void run() {
				Toast toast = ToastFactory.getToast(activity, message);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		});
	}

	/**
	 * 短时间显示Toast消息,显示在在屏幕底部，并保证运行在UI线程中
	 * 
	 * @param activity
	 *            Activity
	 * @param message
	 *            消息内容
	 */
	public static void toastShowBottom(final Activity activity,final String message) {
		activity.runOnUiThread(new Runnable() {

					public void run() {
						Toast toast = ToastFactory.getToast(MyApplication
								.getInstance().getTopActivity(), message);
						toast.setGravity(Gravity.BOTTOM, 0, 0);
						toast.show();
					}
				});
	}

	/**
	 * 长时间显示Toast消息，并保证运行在UI线程中
	 * 
	 * @param activity
	 *            Activity
	 * @param message
	 *            消息内容
	 */
	public static void toastShowLongTime(final Activity activity, final String message) {

		activity.runOnUiThread(new Runnable() {

			public void run() {
				// Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
				Toast toast = ToastFactory.getToast(activity, message);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		});
	}
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 * 
	 * @param context
	 *            上下文，一般为Activity
	 * @param dpValue
	 *            dp数据值
	 * @return px像素值
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 * 
	 * @param context
	 *            上下文，一般为Activity
	 * @param pxValue
	 *            px像素值
	 * @return dp数据值
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * activity切换动画
	 * 
	 */
	@SuppressLint("NewApi")
	public static void runActivityAnim(Activity m, boolean isEnd) {
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
			if (isEnd) {
				m.overridePendingTransition(R.anim.activity_close_enter,
						R.anim.activity_close_exit);
			} else {
				m.overridePendingTransition(R.anim.activity_open_enter,
						R.anim.activity_open_exit);
			}
		}
	}

	/**
	 * 快捷方式是否存在
	 * 
	 */
	public static boolean ifAddShortCut(Context context) {
		boolean isInstallShortCut = false;
		ContentResolver cr = context.getContentResolver();
		String authority = "com.android.launcher2.settings";
		Uri uri = Uri
				.parse("content://" + authority + "/favorites?notify=true");
		Cursor c = cr.query(uri, new String[] { "title", "iconResource" },
				"title=?",
				new String[] { context.getString(R.string.app_name) }, null);
		if (null != c && c.getCount() > 0) {
			isInstallShortCut = true;
		}
		return isInstallShortCut;
	}

	/**
	 * 创建快捷方式
	 * 
	 * @param mainActivty 第一个界面
	 */
	@SuppressWarnings("rawtypes")
	public static void addShortCut(Context context,Class mainActivity) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// 设置属性
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				context.getString(R.string.app_name));
		ShortcutIconResource resource = Intent.ShortcutIconResource
				.fromContext(context, R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, resource);
		// 是否允许重复创建
		shortcut.putExtra("duplicate", false);
		Intent intent = new Intent(Intent.ACTION_MAIN);// 标识Activity为一个程序的开始
		intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClass(context, mainActivity);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		context.sendBroadcast(shortcut);
	}

	/**
	 * 得到view高度
	 * 
	 */
	public static int getViewHeight(int w, int bmw, int bmh) {
		return w * bmh / bmw;
	}

	/**
	 * 获取屏幕宽高
	 * 
	 */
	public static int[] getScreenSize(Activity activity) {
		int[] screens;
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screens = new int[] { dm.widthPixels, dm.heightPixels };
		return screens;
	}

	/**
	 * 得到手机IMEI
	 * 
	 */
	public static String getImei(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		LogUtils.i("IMEI", tm.getDeviceId());
		return tm.getDeviceId();
	}
	
	/**
	 * 安装一个APK文件
	 * 
	 */
	public static void installApk(Context context, File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
	
	/**
	 * 获取当前的年、月、日 对应的时间
	 * 
	 */
	@SuppressLint("SimpleDateFormat")
	public static long getTime() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateNowStr = sdf.format(d);
		Date d2 = null;
		try {
			d2 = sdf.parse(dateNowStr);
			return d2.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
