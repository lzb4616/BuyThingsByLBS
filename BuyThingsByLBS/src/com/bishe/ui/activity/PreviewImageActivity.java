package com.bishe.ui.activity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.bishe.adapter.ZoomDetailImagePagerAdapter;
import com.bishe.buythingsbylbs.R;
import com.bishe.model.NativeImageAlbum;
import com.bishe.model.NativeImageItem;
import com.bishe.ui.base.BasePageActivity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.CheckBox;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;

public class PreviewImageActivity extends BasePageActivity {

	protected RelativeLayout mRlImageSelect;
	protected TextView mTvSelectImage;
	protected CheckBox mChkSelectCommodityImage;
	protected ViewPager mVpPriviewNativeImage;

	protected NativeImageItem mImageItem;
	private ZoomDetailImagePagerAdapter mPagerAdapter;
	private NativeImageAlbum mImageAlbum;
	private int mPosition;
	private List<NativeImageItem> mImageItems = new ArrayList<NativeImageItem>();
	private MenuItem mShowImgaeNumItem;

	
	@Override
	protected void setLayoutView() {
		setContentView(R.layout.activity_preview_native_image_layout);
		setOverflowShowingAlways();
		getDatdFromOthersActivity();		
	}

	@Override
	protected void findViews() {
		
		mRlImageSelect = (RelativeLayout) findViewById(R.id.rl_image_select);
		mTvSelectImage = (TextView) findViewById(R.id.tv_select_image);
		mChkSelectCommodityImage = (CheckBox) findViewById(R.id.chk_select_commodity_image);
		mVpPriviewNativeImage = (android.support.v4.view.ViewPager) findViewById(R.id.vp_priview_native_image);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
//		mTvPublishNavigationbarTitle.setText((mPosition + 1) + " / "
//				+ mImageAlbum.getBitList().size());

	}

	@Override
	protected void setupViews(Bundle bundle) {
		mPagerAdapter = new ZoomDetailImagePagerAdapter(mContext, mImageAlbum);
		mVpPriviewNativeImage.setAdapter(mPagerAdapter);
		mVpPriviewNativeImage.setCurrentItem(mPosition);
		mVpPriviewNativeImage.setEnabled(false);		
	}

	@Override
	protected void setListener() {
		mVpPriviewNativeImage.setOnPageChangeListener(mOnPageChangeListener);
		mChkSelectCommodityImage
				.setOnCheckedChangeListener(mCheckedChangeListener);		
	}

	@Override
	protected void fetchData() {
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.image_show_menu, menu);
		mShowImgaeNumItem = menu.findItem(R.id.action_show_imagenum);
		mShowImgaeNumItem.setTitle((mPosition + 1) + " / "
				+ mImageAlbum.getBitList().size());
		return true;
	}
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	private void setOverflowShowingAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			sendSelectAlbumToOtherActivity(NativeImageActivity.class);
			break;
		case R.id.action_things_publish:
			sendSelectAlbumToOtherActivity(PublishThingsActivity.class);
			break;
		}
		return super.onMenuItemSelected(featureId, item);
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
			mShowImgaeNumItem.setTitle((arg0 + 1) + "/"
					+ mImageAlbum.getBitList().size());
//			mTvPublishNavigationbarTitle.setText((arg0 + 1) + "/"
//					+ mImageAlbum.getBitList().size());
			mChkSelectCommodityImage.setChecked(mImageAlbum.getBitList()
					.get(arg0).isSelect());
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
