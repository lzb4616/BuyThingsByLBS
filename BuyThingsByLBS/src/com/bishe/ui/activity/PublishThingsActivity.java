package com.bishe.ui.activity;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.bishe.buythingsbylbs.R;
import com.bishe.model.Things;
import com.bishe.model.ThingsImage;
import com.bishe.model.User;
import com.bishe.ui.base.BasePageActivity;
import com.bishe.utils.ActivityUtils;
import com.bishe.utils.LogUtils;

/**
 * @author robin
 * @date 2015-4-25
 * Copyright 2015 The robin . All rights reserved
 */
public class PublishThingsActivity extends BasePageActivity {

	private GridView gvCommodityImage;
	private EditText mEditThingsDdescription;
	private EditText mEditThingsPrice;
	private ImageView mImageView;
	private EditText mEditThingsPublisherPhone;
	private String mImagePath;
	
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
				
				
			}
		});
	}

	@Override
	protected void fetchData() {
		// TODO Auto-generated method stub

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
			
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	
	public void publishThings() {
		// TODO Auto-generated method stub
		String commitContent = mEditThingsDdescription.getText().toString().trim(); 
		if(TextUtils.isEmpty(commitContent)){
			ActivityUtils.toastShowCenter(this, "内容不能为空");
			return;
		}
		if(mImagePath == null){
			publishWithoutFigure(commitContent, null);
		}else{
			publish(commitContent);
		}
	}
	
	private void publishWithoutFigure(final String commitContent,
			final ThingsImage thingsImage) {
		User user = BmobUser.getCurrentUser(mContext, User.class);

		final Things things = new Things();
		things.setAuthor(user);
		things.setContent(commitContent);
		things.setPrice(Integer.valueOf(mEditThingsPrice.getText().toString()));
		if(thingsImage!=null){
			things.setThingsImage(thingsImage);
		}
		things.setShare(0);
		things.setComment(0);
		things.setPass(true);
		things.save(mContext, new SaveListener() {
			
			@Override
			public void onSuccess() {
				ActivityUtils.toastShowBottom(PublishThingsActivity.this, "发表成功！");
				LogUtils.i(TAG,"创建成功。");
				setResult(RESULT_OK);
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				ActivityUtils.toastShowBottom(PublishThingsActivity.this, "发表失败！yg"+arg1);
				LogUtils.i(TAG,"创建失败。"+arg1);
			}
		});
	}
	
	/*
	 * 发表带图片
	 */
	private void publish(final String commitContent){
		
		final ThingsImage figureFile = new ThingsImage( new File(mImagePath));
		figureFile.upload(mContext, new UploadFileListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				LogUtils.i(TAG, "上传文件成功。"+figureFile.getFileUrl(mContext));	
				publishWithoutFigure(commitContent, figureFile);
			}
			@Override
			public void onProgress(Integer arg0) {
				
			}
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				LogUtils.i(TAG, "上传文件失败。"+arg1);
			}
		});
	
	}
}
