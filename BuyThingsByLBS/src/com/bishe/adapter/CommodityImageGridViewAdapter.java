package com.bishe.adapter;

import java.util.List;

import com.bishe.buythingsbylbs.R;
import com.bishe.config.DeviceSize;


import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class CommodityImageGridViewAdapter extends BaseAdapter {
	
	private static final String TAG = "CommodityImageGridViewAdapter";
	
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Bitmap> mCommodityImageList;
	private List<string> mCommodityImagePathList;
	
	 
	
	public CommodityImageGridViewAdapter(Context context,List<Bitmap> imageList) {
		super();
		this.mContext = context;
		this.mCommodityImageList = imageList;
		mInflater=LayoutInflater.from(context);
	}
	public CommodityImageGridViewAdapter(Context context,List<Bitmap> imageList,List<string> imagePathList) {
		super();
		this.mContext = context;
		this.mCommodityImageList = imageList;
		this.mCommodityImagePathList = imagePathList;
		mInflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return mCommodityImageList.size()+1;
	}

	@Override
	public Object getItem(int position) {
		return mCommodityImageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommodityIamgeGridViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new CommodityIamgeGridViewHolder();
			convertView = mInflater.inflate(R.layout.commodity_image_gridview_item,null);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.img_publish_image);
			DeviceSize ds = new DeviceSize(mContext);
			Log.i(TAG, "======"+ds.getmWidth()/4);
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.img
					.getLayoutParams(); // 取控件mGrid当前的布局参数
			layoutParams.width = ds.getmWidth()/4;
			layoutParams.height = ds.getmWidth()/4;
			viewHolder.img.setLayoutParams(layoutParams);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (CommodityIamgeGridViewHolder)convertView.getTag();
		}
		if (position < (mCommodityImageList.size())) {
			viewHolder.img.setImageBitmap(mCommodityImageList.get(position));
		}else {
			Resources res = mContext.getResources();
			Bitmap bmp=BitmapFactory.decodeResource(res, R.drawable.image_add_commodity);
			viewHolder.img.setImageBitmap(bmp);
		}
		return convertView;
	}

	private class CommodityIamgeGridViewHolder{
		private ImageView img;
	}
}
