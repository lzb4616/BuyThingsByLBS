package com.bishe.adapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bishe.buythingsbylbs.R;
import com.bishe.model.NativeImageAlbum;
import com.bishe.model.NativeImageItem;
import com.bishe.utils.ImageAsynTask;
import com.bishe.view.CommodtiyImageGridItem;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.AbsListView.LayoutParams;

public class NativeImageGridViewAdapter extends BaseAdapter {
	public static final String TAG = "NativeImageGridViewAdapter";

	private static final int MAC_CHACHE_NUM = 36;
	private Context mContext;
	private NativeImageAlbum mAlbum;
	private LruCache<Integer, Bitmap> mBitmapCache;
	private Bitmap mDefaultBitmap;
	private LayoutParams mLayoutParams = new LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

	//线程池的模型
	private static ExecutorService SINGLE_TASK_EXECUTOR;
	private static ExecutorService LIMITED_TASK_EXECUTOR;
	private static ExecutorService FULL_TASK_EXECUTOR;
	static {
		SINGLE_TASK_EXECUTOR = (ExecutorService) Executors
				.newSingleThreadExecutor();
		LIMITED_TASK_EXECUTOR = (ExecutorService) Executors
				.newFixedThreadPool(2);
		FULL_TASK_EXECUTOR = (ExecutorService) Executors.newCachedThreadPool();
	}

	public NativeImageGridViewAdapter(Context context, NativeImageAlbum mAlbum) {
		this.mContext = context;
		this.mAlbum = mAlbum;
		this.mBitmapCache = new LruCache<Integer, Bitmap>(MAC_CHACHE_NUM);
		this.mDefaultBitmap = BitmapFactory.decodeResource(
				mContext.getResources(), R.drawable.ic_stub);
	}

	public void updateAlbum(NativeImageAlbum album) {
		this.mAlbum = album;
		this.mBitmapCache = new LruCache<Integer, Bitmap>(MAC_CHACHE_NUM);
	}


	@Override
	public int getCount() {
		return mAlbum.getBitList().size();

	}

	@Override
	public NativeImageItem getItem(int position) {
		return mAlbum.getBitList().get(position);

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommodtiyImageGridItem item;
		if (convertView == null) {
			item = new CommodtiyImageGridItem(mContext);
			item.setLayoutParams(mLayoutParams);
		} else {
			item = (CommodtiyImageGridItem) convertView;
		}
		Bitmap bitmap = mBitmapCache.get(mAlbum.getBitList().get(position)
				.getimageID());
		item.mImageId = mAlbum.getBitList().get(position).getimageID();
		if (bitmap == null) {
			item.SetBitmap(mDefaultBitmap);
			ImageAsynTask task = new ImageAsynTask(mContext, mAlbum
					.getBitList().get(position).getimageID(), mBitmapCache,
					item);
			task.executeOnExecutor(SINGLE_TASK_EXECUTOR, position);
		} else {
			item.SetBitmap(bitmap);
		}
		boolean flag = mAlbum.getBitList().get(position).isSelect();
		if (flag) {
			item.setSelectOrder(mAlbum.getBitList().get(position).getSelecNum());
		}
		item.setChecked(flag);
		return item;
	}
}
