package com.bishe.utils;



import com.bishe.view.CommodtiyImageGridItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.support.v4.util.LruCache;

public class ImageAsynTask extends AsyncTask<Integer, Void, Bitmap> {
	public static final String TAG = "ImageAsynTask";
	
	private CommodtiyImageGridItem mItem;
	private Context mContext;
	private LruCache<Integer, Bitmap> mBitmapCache ;
	private Integer mImageId;
	
	
	public ImageAsynTask(Context context, Integer imageId,
			LruCache<Integer, Bitmap> imageCache,
			CommodtiyImageGridItem item) {
		this.mItem = item;
		this.mContext = context;
		this.mImageId = imageId;
		this.mBitmapCache = imageCache;
	}
	@Override
	protected Bitmap doInBackground(Integer... params) {
		Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(
				mContext.getContentResolver(),
				mImageId,
				Thumbnails.MICRO_KIND, null);
		mBitmapCache.put(mImageId,bitmap);
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if ((mItem.mImageId.equals(mImageId))) {
			mItem.SetBitmap(result);
		}
		super.onPostExecute(result);
	}

}