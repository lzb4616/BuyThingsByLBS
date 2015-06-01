package com.bishe.ui.activity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bishe.MyApplication;
import com.bishe.buythingsbylbs.R;
import com.bishe.config.Constant;
import com.bishe.logic.ThingsImageLogic;
import com.bishe.logic.ThingsImageLogic.IsUploadImageListener;
import com.bishe.logic.ThingsLogic.IsPublishListener;
import com.bishe.logic.ThingsLogic;
import com.bishe.logic.ThingsLogic.IsUpdateListener;
import com.bishe.logic.UserLogic;
import com.bishe.model.Location;
import com.bishe.model.Things;
import com.bishe.model.ThingsImage;
import com.bishe.model.User;
import com.bishe.ui.base.BasePageActivity;
import com.bishe.utils.ActivityUtils;
import com.bishe.utils.CreateBmpFactory;
import com.bishe.utils.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * @author robin
 * @date 2015-4-25 Copyright 2015 The robin . All rights reserved
 */
public class PublishThingsActivity extends BasePageActivity implements
		IsUploadImageListener, IsPublishListener, IsUpdateListener {

	private GridView gvCommodityImage;
	private EditText mEditThingsDdescription;
	private EditText mEditThingsPrice;
	private ImageView mImageView;
	private TextView mThingsPublisherLocationTv;
	private String mImagePath;
	private UserLogic mUserLogic;
	private ThingsLogic mThingsLogic;
	private ThingsImageLogic mImageLogic;
	private CreateBmpFactory mBmpFactory;

	private FrameLayout mFrameLayout;
	private Things mThings;
	private Location mLocation;
	private String mLocationName;

	@Override
	protected void setLayoutView() {
		setContentView(R.layout.activity_publish_layout);
		setOverflowShowingAlways();
		mThings = (Things) getIntent().getSerializableExtra("data");
	}

	@Override
	protected void findViews() {
		mEditThingsDdescription = (EditText) findViewById(R.id.edit_publish_commodity_description);
		mEditThingsPrice = (EditText) findViewById(R.id.edit_publish_commodity_price);
		mImageView = (ImageView) findViewById(R.id.gv_publish_commodityimage_show);
		mThingsPublisherLocationTv = (TextView) findViewById(R.id.tv_publish_commodity_location);
		mFrameLayout = (FrameLayout) findViewById(R.id.framelayout_publish_location);
	}

	@Override
	protected void setupViews(Bundle bundle) {

		mUserLogic = new UserLogic(mContext);
		mThingsLogic = new ThingsLogic(mContext);
		mImageLogic = new ThingsImageLogic(mContext);
		mBmpFactory = new CreateBmpFactory(PublishThingsActivity.this);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		if (null != mThings) {
			mEditThingsDdescription.setText(mThings.getContent());
			mEditThingsPrice.setText("" + mThings.getPrice());
			mThingsPublisherLocationTv.setText(mThings.getLocationName());
			if (null != mThings.getThingsImage()) {
				mImageView.setVisibility(View.VISIBLE);
				ImageLoader
						.getInstance()
						.displayImage(
								mThings.getThingsImage().getFileUrl(mContext) == null ? ""
										: mThings.getThingsImage().getFileUrl(
												mContext),
								mImageView,
								MyApplication.getInstance().getOptions(
										R.drawable.bg_pic_loading),
								new SimpleImageLoadingListener() {
									@Override
									public void onLoadingComplete(
											String imageUri, View view,
											Bitmap loadedImage) {
										super.onLoadingComplete(imageUri, view,
												loadedImage);
										LogUtils.i(TAG, "加载图片" + imageUri
												+ "成功");

									}

								});
			}
			actionBar.setTitle("修改物品");

		} else {
			actionBar.setTitle("发布物品");
		}

	}

	@Override
	protected void setListener() {
		mThingsLogic.setOnIsUpdateListener(this);
		mImageLogic.setIsUploadImageListener(this);
		mThingsLogic.setOnIsPublishListener(this);
		mImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mBmpFactory.OpenGallery();
			}
		});

		mFrameLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						MyLoactionActivity.class);
				startActivityForResult(intent, Constant.GET_LOCATION);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Constant.GET_LOCATION:
				mLocationName = (String) data.getStringExtra("locationName");
				if (null != mLocationName) {
					mThingsPublisherLocationTv.setText(mLocationName);
				}
				mLocation = (Location) data.getSerializableExtra("location");
				break;

			default:
				break;
			}
		}
		if (null != mBmpFactory
				.getBitmapFilePath(requestCode, resultCode, data)) {
			mImagePath = mBmpFactory.getBitmapFilePath(requestCode, resultCode,
					data);
			mImageView.setImageBitmap(mBmpFactory.getBitmapByOpt(mImagePath));
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void fetchData() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.publish_action_menu, menu);
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
			finish();
			break;
		case R.id.action_things_publish:
			publishThings();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	public void publishThings() {
		if (!isShouldPublish()) {
			ActivityUtils.toastShowCenter(this, "内容不能为空");
			return;
		}
		if (null == mThings) {
			uploadImage(mImagePath);
		} else if (null == mImagePath) {
			updateThings(mThings);
		} else {
			uploadImage(mImagePath);
		}

	}

	private void updateThings(Things things) {
		things.setContent(mEditThingsDdescription.getText().toString());
		things.setPrice(Integer.valueOf(mEditThingsPrice.getText().toString()));
		if (null != mLocation) {
			things.setThingsLocation(mLocation);
		}
		if (null != mLocationName) {
			things.setLocationName(mLocationName);
		}
		mThingsLogic.updateThings(things);
	}

	private Things initThings(final String thingsDescription,
			final String price, final ThingsImage image) {
		User user = mUserLogic.getCurrentUser();
		final Things things = new Things();
		things.setAuthor(user);
		things.setContent(thingsDescription);
		things.setPrice(Integer.valueOf(price));
		if (image != null) {
			List<ThingsImage> images = new ArrayList<ThingsImage>();
			images.add(image);
			images.add(image);
			things.setThingsImages(images);
			//things.setThingsImage(image);
		}
		if (null != mLocation) {
			things.setThingsLocation(mLocation);
		}
		if (null != mLocationName) {
			things.setLocationName(mLocationName);
		}
		things.setShare(0);
		things.setComment(0);
		things.setBuy(false);
		return things;
	}

	private void uploadImage(String path) {

		mImageLogic.upLoadImageWithPath(path);
	}

	private void publishNewThings(Things things) {

		mThingsLogic.publishThings(things);
	}

	@Override
	public void onUploadImageSuccess(ThingsImage image) {
		if (null == mThings) {
			Things things = initThings(mEditThingsDdescription.getText()
					.toString(), mEditThingsPrice.getText().toString(), image);
			publishNewThings(things);
		} else {
			mThings.setThingsImage(image);
			updateThings(mThings);
		}

	}

	@Override
	public void onUploadImageProgress(Integer arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUploadImageFailure(String msg) {
		ActivityUtils.toastShowBottom(PublishThingsActivity.this, "上传文件失败！yg"
				+ msg);
		LogUtils.i(TAG, "上传文件失败。" + msg);

	}

	@Override
	public void onPublichSuccess() {
		ActivityUtils.toastShowBottom(PublishThingsActivity.this, "发表成功！");
		LogUtils.i(TAG, "创建成功。");
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onPublsihFailure(String msg) {
		ActivityUtils.toastShowBottom(PublishThingsActivity.this, "发表失败！yg"
				+ msg);
		LogUtils.i(TAG, "创建失败。" + msg);
	}

	private boolean isShouldPublish() {

		// 商品描述
		if (mEditThingsDdescription.getText().toString().equals("")) {
			return false;
		}

		// 商品价格
		if (mEditThingsPrice.getText().toString().equals("")) {
			return false;
		}
		// 谁可以看到的设置
		if (null == mImageView && null == mThings
				&& null == mThings.getThingsImage()) {
			return false;
		}

		if (null == mLocationName && null == mThings.getLocationName()) {
			return false;
		}

		return true;
	}

	@Override
	public void onUpdateSuccess() {
		ActivityUtils.toastShowBottom(PublishThingsActivity.this, "修改成功！");
		LogUtils.i(TAG, "创建成功。");
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onUpdateFailure(String msg) {
		ActivityUtils.toastShowBottom(PublishThingsActivity.this, "修改失败！yg"
				+ msg);
		LogUtils.i(TAG, "创建失败。" + msg);
	}

}
