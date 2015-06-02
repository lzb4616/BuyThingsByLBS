package com.bishe.ui.activity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import com.bishe.adapter.ZoomDetailImagePagerAdapter;
import com.bishe.buythingsbylbs.R;
import com.bishe.model.NativeImageAlbum;
import com.bishe.model.NativeImageItem;
import com.bishe.ui.base.BasePageActivity;
import com.tencent.mapsdk.a.ac;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;

import android.os.Bundle;

public class ShowBigImageActivity extends BasePageActivity {

	protected android.support.v4.view.ViewPager mVpShowZoomDetailImage;

	protected NativeImageItem mImageItem;
	private NativeImageAlbum mImageAlbum;
	private int mPosition;
	private MenuItem mShowImgaeNumItem;
	
	@Override
	protected void setLayoutView() {
		setContentView(R.layout.activity_show_big_native_image_layout);
		getDatdFromOthersActivity();	
		setOverflowShowingAlways();
	}

	@Override
	protected void findViews() {
		mVpShowZoomDetailImage = (android.support.v4.view.ViewPager) findViewById(R.id.vp_show_zoom_detail_image);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void setupViews(Bundle bundle) {
		ZoomDetailImagePagerAdapter adapter = new ZoomDetailImagePagerAdapter(
				mContext, mImageAlbum);
		mVpShowZoomDetailImage.setAdapter(adapter);
		mVpShowZoomDetailImage.setCurrentItem(mPosition);
		mVpShowZoomDetailImage.setEnabled(false);		
	}

	@Override
	protected void setListener() {
		mVpShowZoomDetailImage.setOnPageChangeListener(mOnPageChangeListener);		
	}

	@Override
	protected void fetchData() {
		
	}

	private void getDatdFromOthersActivity() {
		Bundle bundle = getIntent().getExtras();
		if (bundle == null)
			return;
		mImageAlbum = (NativeImageAlbum) bundle.getSerializable("imageAlbum");
		if (mImageAlbum == null)
			return;
		mPosition = bundle.getInt("position");
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
			returnToPublishActivity();
			break;
		case R.id.action_things_publish:
			if (mImageAlbum.getBitList().size() > 0) {
				mImageAlbum.getBitList().remove(mPosition);
				if (mImageAlbum.getBitList().size() == 0) {
					returnToPublishActivity();
					break;
				}
				resetItemsSelectOrder(mImageAlbum.getBitList());
				ZoomDetailImagePagerAdapter adapter = new ZoomDetailImagePagerAdapter(
						mContext, mImageAlbum);
				mVpShowZoomDetailImage.setAdapter(adapter);
				if (mPosition > 0){
					mVpShowZoomDetailImage.setCurrentItem(--mPosition);
				}else {
					mVpShowZoomDetailImage.setCurrentItem(mPosition);
				}
				mShowImgaeNumItem.setTitle((mPosition + 1) + "/"
						+ mImageAlbum.getBitList().size());
			}
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	
	private void resetItemsSelectOrder(List<NativeImageItem> items){
		for (int i = 0; i < items.size(); i++) {
			items.get(i).setSelecNum(i + 1);
		}
	}
	
	private void returnToPublishActivity() {
		Intent intent = new Intent(mContext, PublishThingsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("mAblum", mImageAlbum);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}

	private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int arg0) {
			mShowImgaeNumItem.setTitle((arg0 + 1) + "/"
					+ mImageAlbum.getBitList().size());
			mPosition = arg0;
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	

}
