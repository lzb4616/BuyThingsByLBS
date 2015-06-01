package com.bishe.ui.activity;

import android.content.Context;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.GridView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bishe.adapter.NativeImageAlbumListViewAdapter;
import com.bishe.adapter.NativeImageGridViewAdapter;
import com.bishe.buythingsbylbs.R;
import com.bishe.config.DeviceSize;
import com.bishe.model.NativeImageAlbum;
import com.bishe.model.NativeImageItem;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class NativeImageActivity extends Activity {
	public static final String TAG = "NativeImageActivity";

	private GridView mGvNativeImage;
	private Context mContext;
	
	
	private RelativeLayout mRlBottom;
	private Button mBtnShowImage;
	private Button mBtnSure;
	protected ListView mLvImageAlbum;

	private ArrayList<NativeImageItem> mImageItems = new ArrayList<NativeImageItem>();
	private NativeImageAlbum mAlbum;
	private NativeImageGridViewAdapter mNativeImaegAdapter;
	private PopupWindow popupWindow;
	private DeviceSize mDeviceSize;
	private List<NativeImageAlbum> mAlbumList;

	// 设置获取图片的字段信息
	private static final String[] STORE_IMAGES = {
			MediaStore.Images.Media.DISPLAY_NAME, // 显示的名字
			MediaStore.Images.Media.DATA, MediaStore.Images.Media.LONGITUDE, // 经度
			MediaStore.Images.Media._ID, // id
			MediaStore.Images.Media.BUCKET_ID, // dir id 目录
			MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // dir name 目录名字
			MediaStore.Images.Media.DATE_ADDED // dir_date_add 添加时间

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_native_image_gridview);
		initViews();
		getIntentFromOthers();
	}

	private void initViews() {
		mContext = this.getApplicationContext();
		
		mGvNativeImage = (GridView) findViewById(R.id.gv_native_image);
		mRlBottom = (RelativeLayout) findViewById(R.id.rl_bottom);
		mBtnShowImage = (Button) findViewById(R.id.btn_show_image);
		mBtnSure = (Button) findViewById(R.id.btn_sure);

		mBtnShowImage.setOnClickListener(mOnBtnClickListener);
		mBtnSure.setOnClickListener(mOnBtnClickListener);

		mDeviceSize = new DeviceSize(mContext);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(
				R.layout.activity_native_image_album_listview, null);
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				(int) (mDeviceSize.getmHeight() * 0.7), true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setAnimationStyle(R.style.popupwindow_animation_style);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@SuppressLint("NewApi")
			@Override
			public void onDismiss() {
				// 设置背景颜色变亮
				mGvNativeImage.setAlpha(1.0f);
				popupWindow.setFocusable(true);
			}
		});
		// 做一个不在焦点外的处理事件监听
		popupWindow.getContentView().setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popupWindow.setFocusable(false);
				popupWindow.dismiss();
				return true;
			}
		});
		mLvImageAlbum = (ListView) view.findViewById(R.id.lv_image_album);
		mAlbumList = getPhotoAlbum();
		mLvImageAlbum.setAdapter(new NativeImageAlbumListViewAdapter(
				mAlbumList, this));
		mLvImageAlbum.setOnItemClickListener(albumClickListener);

		mAlbum = mAlbumList.get(0);
		mNativeImaegAdapter = new NativeImageGridViewAdapter(this, mAlbum);
		mGvNativeImage.setAdapter(mNativeImaegAdapter);
		mGvNativeImage.setOnItemClickListener(gvItemClickListener);
		mGvNativeImage.setOnItemLongClickListener(gvOnItemLongClickListener);
	}

	private void getIntentFromOthers() {
		Bundle bundle = getIntent().getExtras();
		if (bundle == null)
			return;
		NativeImageAlbum album = (NativeImageAlbum) bundle
				.getSerializable("mAblum");
		if (album == null)
			return;
		for (int i = 0; i < album.getBitList().size(); i++) {
			for (int j = 0; j < mAlbum.getBitList().size(); j++) {
				if (mAlbum.getBitList().get(j).getimageID() == album
						.getBitList().get(i).getimageID()) {
					mAlbum.getBitList()
							.get(j)
							.setSelecNum(
									album.getBitList().get(i).getSelecNum());
					mAlbum.getBitList().get(j)
							.setSelect(album.getBitList().get(i).isSelect());
				}
			}
		}
		mImageItems = (ArrayList<NativeImageItem>) album.getBitList();
	}

	protected View.OnClickListener mOnBtnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
//			int viewId = v.getId();
//			if (viewId == R.id.ib_publish_navigationbar_left) {
//				finish();
//			} else if (viewId == R.id.ib_publish_navigationbar_right) {
//				Intent intent = new Intent(NativeImageActivity.this,
//						PublishCommodityActivity.class);
//				Bundle bundle = new Bundle();
//				NativeImageAlbum album = new NativeImageAlbum();
//				album.setBitList(mImageItems);
//				bundle.putSerializable("mAblum", album);
//				intent.putExtras(bundle);
//				startActivity(intent);
//				finish();
//			} else if (viewId == R.id.btn_show_image) {
//				popwindowTOShowOrHide(v);
//			} else if (viewId == R.id.btn_sure) {
//				Intent intent = new Intent(NativeImageActivity.this,
//						PublishCommodityActivity.class);
//				Bundle bundle = new Bundle();
//				NativeImageAlbum album = new NativeImageAlbum();
//				album.setBitList(mImageItems);
//				bundle.putSerializable("mAblum", album);
//				intent.putExtras(bundle);
//				startActivity(intent);
//				finish();
//			}
		}
	};

	@SuppressLint("NewApi")
	private void popwindowTOShowOrHide(View v) {
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
		} else {
			int hr = (int) (mDeviceSize.getmHeight() * 0.3);
			int he = mRlBottom.getHeight();
			popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, 0, hr - he);
			// 设置背景颜色变暗
			mGvNativeImage.setAlpha(0.7f);
		}
	}

	/**
	 * 相册点击事件
	 */
	OnItemClickListener albumClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.i(TAG, "" + mAlbumList.get(position));
			popwindowTOShowOrHide(view);
			mAlbum = mAlbumList.get(position);
			mNativeImaegAdapter.updateAlbum(mAlbum);
			mNativeImaegAdapter.notifyDataSetChanged();
			mBtnShowImage.setText(mAlbum.getName());
			// ((CompoundButton)
			// view.findViewById(R.id.chk_photoalbum_isselect))
			// .setChecked(true);
		}
	};

	private void inite(NativeImageItem str, boolean isSeclect) {
		for (int i = 0; i < mImageItems.size(); i++) {
			mImageItems.get(i).setSelecNum(i + 1);
		}
		if (isSeclect) {
			mBtnSure.setText("确定(" + mImageItems.size() + ")");
		} else {
			mBtnSure.setText("确定(" + mImageItems.size() + ")");
			str.setSelecNum(0);
		}
	}

	private OnItemClickListener gvItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();
				return;
			}
			if (mAlbum.getBitList().get(position).isSelect()) {
				mAlbum.getBitList().get(position).setSelect(false);
				mImageItems.remove(mAlbum.getBitList().get(position));
				inite(mAlbum.getBitList().get(position), mAlbum.getBitList()
						.get(position).isSelect());
			} else {
				mAlbum.getBitList().get(position).setSelect(true);
				mImageItems.add(mAlbum.getBitList().get(position));
				inite(mAlbum.getBitList().get(position), mAlbum.getBitList()
						.get(position).isSelect());
			}
			mNativeImaegAdapter.notifyDataSetChanged();
		}
	};

	private OnItemLongClickListener gvOnItemLongClickListener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();
				return false;
			}
			Intent intent = new Intent(mContext, PreviewImageActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("imageAlbum", mAlbum);
			bundle.putInt("position", position);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
			return false;
		}
	};

	/**
	 * 方法描述：按相册获取图片信息
	 * 
	 */
	private List<NativeImageAlbum> getPhotoAlbum() {
		List<NativeImageAlbum> albumList = new ArrayList<NativeImageAlbum>();
		Cursor cursor = MediaStore.Images.Media.query(getContentResolver(),
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES);
		Map<String, NativeImageAlbum> countMap = new HashMap<String, NativeImageAlbum>();
		NativeImageAlbum pa = null;

		while (cursor.moveToNext()) {
			String path = cursor.getString(cursor
					.getColumnIndex(MediaStore.Video.Media.DATA));
			String id = cursor.getString(3);
			String dir_id = cursor.getString(4);
			String dir = cursor.getString(5);
			String date_add = cursor.getString(6);
			if (!countMap.containsKey(dir_id)) {
				pa = new NativeImageAlbum();
				pa.setName(dir);
				pa.setBitmap(Integer.parseInt(id));
				pa.setCount("1");
				pa.setAddDate(Long.parseLong(date_add));
				pa.getBitList().add(
						new NativeImageItem(Integer.valueOf(id), path, Long
								.parseLong(date_add)));
				countMap.put(dir_id, pa);
				pa.setPath(path);
			} else {
				pa = countMap.get(dir_id);
				pa.setCount(String.valueOf(Integer.parseInt(pa.getCount()) + 1));
				pa.getBitList().add(
						new NativeImageItem(Integer.valueOf(id), path, Long
								.parseLong(date_add)));
				pa.setPath(path);
			}
		}
		cursor.close();
		Iterable<String> it = countMap.keySet();
		Comparator comparator = new ComparatorSortAlbumList<NativeImageItem>();
		for (String key : it) {
			Collections.sort(countMap.get(key).getBitList(), comparator);
			countMap.get(key).setAddDate(
					countMap.get(key).getBitList().get(0).getAddDate());
			countMap.get(key).setBitmap(
					countMap.get(key).getBitList().get(0).getimageID());
			countMap.get(key).setPath(
					countMap.get(key).getBitList().get(0).getPath());
			albumList.add(countMap.get(key));
		}
		Comparator comparator2 = new ComparatorSortAlbumList<NativeImageAlbum>();
		Collections.sort(albumList, comparator2);
		List<NativeImageItem> imageItems = new ArrayList<NativeImageItem>();
		for (int i = 0; i < albumList.size(); i++) {
			imageItems.addAll(albumList.get(i).getBitList());
		}
		pa = new NativeImageAlbum();
		Collections.sort(imageItems, comparator);
		pa.setBitList(imageItems);
		pa.setCount(String.valueOf(imageItems.size()));
		Log.i(TAG, "所有图片第一张相片" + albumList.get(0).getBitmap());
		pa.setBitmap(albumList.get(0).getBitList().get(0).getimageID());
		pa.setPath(albumList.get(0).getBitList().get(0).getPath());
		pa.setName("所有图片");
		albumList.add(0, pa);
		return albumList;
	}

	/**
	 * 通过相片生成时间进行排序
	 * */
	public class ComparatorSortAlbumList<T> implements Comparator<T> {

		@Override
		public int compare(T lhs, T rhs) {
			if (lhs instanceof NativeImageItem) {
				NativeImageItem item1 = (NativeImageItem) lhs;
				NativeImageItem item2 = (NativeImageItem) rhs;
				long time1 = item1.getAddDate();
				long time2 = item2.getAddDate();
				if (time1 < time2) {
					return 1;
				} else if (time1 == time2) {
					return 0;
				} else {
					return -1;
				}
			}
			if (lhs instanceof NativeImageAlbum) {
				NativeImageAlbum item1 = (NativeImageAlbum) lhs;
				NativeImageAlbum item2 = (NativeImageAlbum) rhs;
				long time1 = item1.getAddDate();
				long time2 = item2.getAddDate();
				if (time1 < time2) {
					return 1;
				} else if (time1 == time2) {
					return 0;
				} else {
					return -1;
				}
			}
			return 0;
		}
	}
}
