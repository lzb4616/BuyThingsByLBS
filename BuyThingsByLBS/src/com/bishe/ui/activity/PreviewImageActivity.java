package com.bishe.ui.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.bishe.adapter.ZoomDetailImagePagerAdapter;
import com.bishe.buythingsbylbs.R;
import com.bishe.model.NativeImageAlbum;
import com.bishe.model.NativeImageItem;

import android.content.Context;
import android.content.Intent;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.CheckBox;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.app.Activity;
import android.os.Bundle;

public class PreviewImageActivity extends Activity {

	protected Context mContext;

	
	protected RelativeLayout mRlImageSelect;
	protected TextView mTvSelectImage;
	protected CheckBox mChkSelectCommodityImage;
	protected ViewPager mVpPriviewNativeImage;

	protected NativeImageItem mImageItem;
	private ZoomDetailImagePagerAdapter mPagerAdapter;
	private NativeImageAlbum mImageAlbum;
	private int mPosition;
	private List<NativeImageItem> mImageItems = new ArrayList<NativeImageItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview_native_image_layout);
		getDatdFromOthersActivity();
		initViews();
	}

	private void initViews() {
		mContext = this;
	
		
		mRlImageSelect = (RelativeLayout) findViewById(R.id.rl_image_select);
		mTvSelectImage = (TextView) findViewById(R.id.tv_select_image);
		mChkSelectCommodityImage = (CheckBox) findViewById(R.id.chk_select_commodity_image);
		mVpPriviewNativeImage = (android.support.v4.view.ViewPager) findViewById(R.id.vp_priview_native_image);

//		mTvPublishNavigationbarTitle.setText((mPosition + 1) + " / "
//				+ mImageAlbum.getBitList().size());

		mVpPriviewNativeImage.setOnPageChangeListener(mOnPageChangeListener);
		mPagerAdapter = new ZoomDetailImagePagerAdapter(mContext, mImageAlbum);
		mVpPriviewNativeImage.setAdapter(mPagerAdapter);
		mVpPriviewNativeImage.setCurrentItem(mPosition);
		mVpPriviewNativeImage.setEnabled(false);


		mChkSelectCommodityImage
				.setOnCheckedChangeListener(mCheckedChangeListener);
	}

	private OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			boolean isSelect = mImageAlbum.getBitList().get(mPosition).isSelect();
			if (isChecked) {
				if (!isSelect) {
					mImageAlbum.getBitList().get(mPosition).setSelect(true);
					mImageItems.add(mImageAlbum.getBitList().get(mPosition));
					inite(mImageAlbum.getBitList().get(mPosition), mImageAlbum
							.getBitList().get(mPosition).isSelect());
				}
			} else {
				if (isSelect) {
					mImageAlbum.getBitList().get(mPosition).setSelect(false);
					mImageItems.remove(mImageAlbum.getBitList().get(mPosition));
					inite(mImageAlbum.getBitList().get(mPosition), mImageAlbum
							.getBitList().get(mPosition).isSelect());
				}
			}
		}
	};

	private void inite(NativeImageItem str, boolean isSeclect) {
		//item的选择顺序进行重新赋值
		for (int i = 0; i < mImageItems.size(); i++) {
			mImageItems.get(i).setSelecNum(i + 1);
		}
		if (!isSeclect) {str.setSelecNum(0);}
	}

//	protected View.OnClickListener mOnBtnClickListener = new View.OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			int viewId = v.getId();
//			if (viewId == R.id.ib_publish_navigationbar_left) {
//				sendSelectAlbumToOtherActivity(NativeImageActivity.class);
//			} else if (viewId == R.id.ib_publish_navigationbar_right) {
//				sendSelectAlbumToOtherActivity(PublishCommodityActivity.class);
//			}
//		}
//	};

	private void sendSelectAlbumToOtherActivity(Class<?> cls) {
		Intent intent = new Intent(mContext, cls);
		Bundle bundle = new Bundle();
		NativeImageAlbum album = new NativeImageAlbum();
		album.setBitList(mImageItems);
		bundle.putSerializable("mAblum", album);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}

	private void getDatdFromOthersActivity() {
		Bundle bundle = getIntent().getExtras();
		if (bundle == null)
			return;
		mImageAlbum = (NativeImageAlbum) bundle.getSerializable("imageAlbum");
		if (mImageAlbum == null)
			return;
		for (int i = 0; i < mImageAlbum.getBitList().size(); i++) {
			NativeImageItem item = mImageAlbum.getBitList().get(i);
			if (item.isSelect())
				mImageItems.add(item);
		}
		Collections.sort(mImageItems,
				new ComparatorSortItemList<NativeImageItem>());
		mPosition = bundle.getInt("position");
	}

	private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			mPosition = arg0;
//			mTvPublishNavigationbarTitle.setText((arg0 + 1) + "/"
//					+ mImageAlbum.getBitList().size());
//			mChkSelectCommodityImage.setChecked(mImageAlbum.getBitList()
//					.get(arg0).isSelect());
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	/**
	 * 通过选择顺序号进行排序
	 * */
	public class ComparatorSortItemList<T> implements Comparator<T> {

		@Override
		public int compare(T lhs, T rhs) {
			if (lhs instanceof NativeImageItem) {
				NativeImageItem item1 = (NativeImageItem) lhs;
				NativeImageItem item2 = (NativeImageItem) rhs;
				int num1 = item1.getSelecNum();
				int num2 = item2.getSelecNum();
				if (num1 < num2) {
					return -1;
				} else if (num1 == num2) {
					return 0;
				} else {
					return 1;
				}
			}
			return 0;
		}
	}
}
