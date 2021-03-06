package com.bishe.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.graphics.Bitmap;
import android.view.View;
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
	
	 //获取图片所在文件夹名称
    public static String getDir(String path)
    {
        String subString = path.substring(0, path.lastIndexOf('/'));
        return subString.substring(subString.lastIndexOf('/') + 1, subString.length());
    }

    public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener
    {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

}
