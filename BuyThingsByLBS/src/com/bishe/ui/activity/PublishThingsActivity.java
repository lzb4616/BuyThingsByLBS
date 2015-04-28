package com.bishe.ui.activity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.bishe.buythingsbylbs.R;
import com.bishe.logic.ThingsImageLogic;
import com.bishe.logic.ThingsImageLogic.IsUploadImageListener;
import com.bishe.logic.ThingsLogic.IsPublishListener;
import com.bishe.logic.ThingsLogic;
import com.bishe.logic.UserLogic;
import com.bishe.model.Things;
import com.bishe.model.ThingsImage;
import com.bishe.model.User;
import com.bishe.ui.base.BasePageActivity;
import com.bishe.utils.ActivityUtils;
import com.bishe.utils.CreateBmpFactory;
import com.bishe.utils.LogUtils;

/**
 * @author robin
 * @date 2015-4-25 Copyright 2015 The robin . All rights reserved
 */
public class PublishThingsActivity extends BasePageActivity implements
		IsUploadImageListener, IsPublishListener {

	private GridView gvCommodityImage;
	private EditText mEditThingsDdescription;
	private EditText mEditThingsPrice;
	private ImageView mImageView;
	private EditText mEditThingsPublisherPhone;
	private String mImagePath;
	private UserLogic mUserLogic;
	private ThingsLogic mThingsLogic;
	private ThingsImageLogic mImageLogic;
	private CreateBmpFactory mBmpFactory;

	@Override
	protected void setLayoutView() {
		setContentView(R.layout.activity_publish_layout);
		setOverflowShowingAlways();
	}

	@Override
	protected void findViews() {
		mEditThingsDdescription = (EditText) findViewById(R.id.edit_publish_commodity_description);
		mEditThingsPrice = (EditText) findViewById(R.id.edit_publish_commodity_price);
		mImageView = (ImageView) findViewById(R.id.gv_publish_commodityimage_show);
		mEditThingsPublisherPhone = (EditText) findViewById(R.id.edit_publish_commodity_user_phoneNum);
	}

	@Override
	protected void setupViews(Bundle bundle) {
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("发布物品");
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	protected void setListener() {

		mImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mBmpFactory.OpenGallery();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mImagePath = mBmpFactory.getBitmapFilePath(requestCode, resultCode, data);
		mImageView.setImageBitmap(mBmpFactory.getBitmapByOpt(mImagePath));
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void fetchData() {
		mUserLogic = new UserLogic(mContext);
		mThingsLogic = new ThingsLogic(mContext);
		mImageLogic = new ThingsImageLogic(mContext);
		mBmpFactory = new CreateBmpFactory(PublishThingsActivity.this);
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
		uploadImage(mImagePath);
	}

	private Things initThings(final String thingsDescription,
			final String price, final ThingsImage image) {
		User user = mUserLogic.getCurrentUser();
		final Things things = new Things();
		things.setAuthor(user);
		things.setContent(thingsDescription);
		things.setPrice(Integer.valueOf(price));
		if (image != null) {
			things.setThingsImage(image);
		}
		things.setShare(0);
		things.setComment(0);
		things.setBuy(false);
		return things;
	}

	private void uploadImage(String path) {
		mImageLogic.setIsUploadImageListener(this);
		mImageLogic.upLoadImageWithPath(path);
	}

	private void publishNewThings(Things things) {
		mThingsLogic.setOnIsPublishListener(this);
		mThingsLogic.publishThings(things);
	}

	@Override
	public void onUploadImageSuccess(ThingsImage image) {
		Things things = initThings(
				mEditThingsDdescription.getText().toString(), mEditThingsPrice
						.getText().toString(), image);
		publishNewThings(things);
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
		if (null == mImagePath) {
			return false;
		}
		return true;
	}
}
