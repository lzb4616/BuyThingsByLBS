package com.bishe.adapter;


import com.bishe.buythingsbylbs.R;
import com.bishe.model.NativeImageAlbum;
import com.bishe.utils.CreateBmpFactory;
import com.bishe.view.ZoomImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi")
public class ZoomDetailImagePagerAdapter extends PagerAdapter {

	public final static String TAG = "ZoomDetailImagePagerAdapter";

	private NativeImageAlbum mImageAlbum;
	private Context mContext;
	private CreateBmpFactory mBmpFactory;
	private LruCache<Integer, Bitmap> mViewCache;
	private View mView;
	private ZoomImageView mImageView;
	private final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	private final int cacheSize = maxMemory / 8;
	
	public ZoomDetailImagePagerAdapter(Context context, NativeImageAlbum album) {
		this.mImageAlbum = album;
		this.mContext = context;
		this.mBmpFactory = new CreateBmpFactory((Activity) mContext);
		this.mViewCache = new LruCache<Integer, Bitmap>(cacheSize);
	}

	public void updateAlbum(NativeImageAlbum album) {
		this.mImageAlbum = album;
	}

	@Override
	public int getCount() {
		return mImageAlbum.getBitList().size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		View view = (View) object;
		((ViewPager) container).removeView(view);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Bitmap bitmap = mViewCache.get(position);
		mView = LayoutInflater.from(mContext).inflate(
				R.layout.zoom_imageview_layout, null);
		mImageView = (ZoomImageView) mView
				.findViewById(R.id.iv_zoom_commodity_image);
		if (bitmap == null) {
			new AdvanceLoadBitmapAsynTask(mImageView).execute(position);
			mImageView.setBackgroundResource(R.drawable.default_none_bitmap_bg);
		} else {
			mImageView.setImageBitmap(bitmap);
		}
		container.addView(mView);
		return mView;
	}
	private class AdvanceLoadBitmapAsynTask extends
			AsyncTask<Integer, Void, Bitmap> {
		private ZoomImageView mImageView;

		public AdvanceLoadBitmapAsynTask(ZoomImageView view) {
			this.mImageView = view;
		}
		@Override
		protected Bitmap doInBackground(Integer... params) {
			Bitmap bitmap = mBmpFactory.getBitmapByOpt(mImageAlbum.getBitList()
					.get(params[0]).getPath());
			if (bitmap == null) {
				bitmap = BitmapFactory.decodeResource(mContext.getResources(),
						R.drawable.ic_stub);
			} else {
				mViewCache.put(params[0], bitmap);
			}
			return bitmap;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			mImageView.setBackgroundColor(mContext.getResources().getColor(
					R.color.black));
			mImageView.setImageBitmap(result);
			Log.i(TAG, "Bitmap 的大小：" + result.getByteCount());
		}
	}
}
