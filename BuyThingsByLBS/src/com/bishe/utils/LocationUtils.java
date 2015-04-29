package com.bishe.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.tencent.map.geolocation.TencentGeofence;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.mapsdk.raster.model.GeoPoint;

/**
 * 一些工具方法.
 * 
 */
public class LocationUtils {

	/**
	 * 返回坐标系名称
	 */
	public static String toString(int coordinateType) {
		if (coordinateType == TencentLocationManager.COORDINATE_TYPE_GCJ02) {
			return "国测局坐标(火星坐标)";
		} else if (coordinateType == TencentLocationManager.COORDINATE_TYPE_WGS84) {
			return "WGS84坐标(GPS坐标, 地球坐标)";
		} else {
			return "非法坐标";
		}
	}

	/**
	 * 返回 manifest 中的 key
	 */
	public static String getKey(Context context) {
		String key = null;
		try {
			ApplicationInfo appInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			Bundle metaData = appInfo.metaData;
			if (metaData != null) {
				key = metaData.getString("TencentMapSDK");
			}
		} catch (NameNotFoundException e) {
			Log.e("TencentLocation",
					"Location Manager: no key found in manifest file");
			key = "";
		}
		return key;
	}
	public static GeoPoint of(TencentLocation location) {
		GeoPoint ge = new GeoPoint((int) (location.getLatitude() * 1E6),
				(int) (location.getLongitude() * 1E6));
		return ge;
	}

	public static GeoPoint ofBmobLocation(com.bishe.model.Location location) {
		GeoPoint ge = new GeoPoint((int) (location.getLatitude() * 1E6),
				(int) (location.getLongitude() * 1E6));
		return ge;
	}

	public static GeoPoint of(double latitude, double longitude) {
		GeoPoint ge = new GeoPoint((int) (latitude * 1E6),
				(int) (longitude * 1E6));
		return ge;
	}

	public static void d(String tag, String msg) {
		Log.i(tag, msg);
	}

	public static double fmt(double d) {
		long i = (long) (d * 1e6);
		return i / 1e6;
	}

	public static String toString(TencentGeofence geofence) {
		return geofence.getTag() + " " + geofence.getLatitude() + ","
				+ geofence.getLongitude();
	}

	public static String toString(Location location) {
		return location.getLatitude() + "," + location.getLongitude();
	}
}
