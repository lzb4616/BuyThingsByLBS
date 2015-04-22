package com.bishe.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * @author robin
 * @date 2015-4-20
 * Copyright 2015 The robin . All rights reserved
 */
public class BitmapUtils {
	
	public static float[] getBitmapConfiguration(Bitmap bitmap,
			ImageView imageView, float screenRadio) {
		int screenWidth = ActivityUtils.getScreenSize()[0];
		float rawWidth = 0;
		float rawHeight = 0;
		float width = 0;
		float height = 0;
		if (bitmap == null) {
			// rawWidth = sourceWidth;
			// rawHeight = sourceHeigth;
			width = (float) (screenWidth / screenRadio);
			height = (float) width;
			imageView.setScaleType(ScaleType.FIT_XY);
		} else {
			rawWidth = bitmap.getWidth();
			rawHeight = bitmap.getHeight();
			if (rawHeight > 10 * rawWidth) {
				imageView.setScaleType(ScaleType.CENTER);
			} else {
				imageView.setScaleType(ScaleType.FIT_XY);
			}
			float radio = rawHeight / rawWidth;

			width = (float) (screenWidth / screenRadio);
			height = (float) (radio * width);
		}
		return new float[] { width, height };
	}
	
	/**
	 * 根据图片大小得到合适的缩放率
	 * 
	 * @param value
	 * @return
	 */
	public static int getSimpleNumber(int value) {
		if (value > 30) {
			return 1 + getSimpleNumber(value / 4);
		} else {
			return 1;
		}
	}
}
