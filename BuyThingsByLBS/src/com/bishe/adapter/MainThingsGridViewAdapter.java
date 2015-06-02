package com.bishe.adapter;

import java.util.List;

import com.bishe.MyApplication;
import com.bishe.buythingsbylbs.R;
import com.bishe.config.DeviceSize;
import com.bishe.model.ThingsImage;
import com.bishe.utils.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * @author robin
 * @date 2015-6-2
 * Copyright 2015 The robin . All rights reserved
 */
public class MainThingsGridViewAdapter extends BaseContentAdapter<ThingsImage> {

	
	public static final String TAG = "MainThingsGridViewAdapter";
	private List<ThingsImage> mImages;

	public MainThingsGridViewAdapter(Context context, List<ThingsImage> list) {
		super(context, list);
		this.mImages = list;
	}

	@Override
	public View getConvertView(int position, View convertView, ViewGroup parent) {
		CommodityImageGridViewHolder viewHolder ;
		if (null == convertView) {
			viewHolder = new CommodityImageGridViewHolder();
			convertView = mInflater.inflate(R.layout.commodity_image_gridview_item,null);
			viewHolder.imgv = (ImageView) convertView.findViewById(R.id.img_publish_image);
			DeviceSize ds = new DeviceSize(mContext);
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.imgv
					.getLayoutParams(); // 取控件mGrid当前的布局参数
			layoutParams.width = ds.getmWidth()/4;
			layoutParams.height = ds.getmWidth()/4;
			viewHolder.imgv.setLayoutParams(layoutParams);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (CommodityImageGridViewHolder)convertView.getTag();
		}
		String url = mImages.get(position).getFileUrl(mContext);
		ImageLoader.getInstance().displayImage(
				mImages.get(position).getFileUrl(mContext) == null ? ""
						: mImages.get(position).getFileUrl(mContext),
						viewHolder.imgv,
				MyApplication.getInstance().getOptions(
						R.drawable.bg_pic_loading),
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri,
							View view, Bitmap loadedImage) {
						super.onLoadingComplete(imageUri, view, loadedImage);
						LogUtils.i(TAG, "加载图片" + imageUri + "成功");
					}
				});
		
		return convertView;
	}

	private class CommodityImageGridViewHolder
	{
		private ImageView imgv;
	}

}
