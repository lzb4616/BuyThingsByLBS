package com.bishe.ui.activity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import com.bishe.MyApplication;
import com.bishe.adapter.CommodityImageGridViewAdapter;
import com.bishe.adapter.MainThingsGridViewAdapter;
import com.bishe.buythingsbylbs.R;
import com.bishe.config.Constant;
import com.bishe.config.DeviceSize;
import com.bishe.logic.ThingsImageLogic;
import com.bishe.logic.ThingsImageLogic.IsUploadImageListener;
import com.bishe.logic.ThingsLogic.IsPublishListener;
import com.bishe.logic.ThingsLogic;
import com.bishe.logic.ThingsLogic.IsUpdateListener;
import com.bishe.logic.UserLogic;
import com.bishe.model.Location;
import com.bishe.model.NativeImageAlbum;
import com.bishe.model.NativeImageItem;
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

	private GridView mGvCommodityImage;
	private EditText mEditThingsDdescription;
	private EditText mEditThingsPrice;
	private TextView mThingsPublisherLocationTv;
	private UserLogic mUserLogic;
	private ThingsLogic mThingsLogic;
	private ThingsImageLogic mImageLogic;
	private ScrollView mScrollView;
	
	private FrameLayout mFrameLayout;
	private Things mThings;
	private Location mLocation;
	private String mLocationName;

	private List<NativeImageItem> mNativeImageItems = new ArrayList<NativeImageItem>();
	private int keyBoardHeight = 0;
	private List<String> mImagePathList = new ArrayList<String>();
	private List<Bitmap> mCommodityImageList = new ArrayList<Bitmap>();
	private List<ThingsImage> mThingsImages = new ArrayList<ThingsImage>();
	private int mGridViewHeight;
	private CommodityImageGridViewAdapter mImageAdapter;
	
	private CreateBmpFactory mCreateBmpFactory;
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
		mGvCommodityImage =(GridView) findViewById(R.id.gv_publish_commodityimage_show);
		mThingsPublisherLocationTv = (TextView) findViewById(R.id.tv_publish_commodity_location);
		mFrameLayout = (FrameLayout) findViewById(R.id.framelayout_publish_location);
		mScrollView = (ScrollView) findViewById(R.id.sv_publich);
	}

	@Override
	protected void setupViews(Bundle bundle) {

		mUserLogic = new UserLogic(mContext);
		mThingsLogic = new ThingsLogic(mContext);
		mImageLogic = new ThingsImageLogic(mContext);
		mCreateBmpFactory = new CreateBmpFactory(this);
		setGridViewCommodityImage();
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		if (null != mThings) {
			mEditThingsDdescription.setText(mThings.getContent());
			mEditThingsPrice.setText("" + mThings.getPrice());
			mThingsPublisherLocationTv.setText(mThings.getLocationName());
			if (null != mThings.getThingsImages()) {
				mGvCommodityImage.setVisibility(View.VISIBLE);
				mThingsImages = mThings.getThingsImages();
				//mGvCommodityImage.setAdapter(new MainThingsGridViewAdapter(mContext, mThings.getThingsImages()));
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
		setGridViewOnClickListener();

		mFrameLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						MyLoactionActivity.class);
				startActivityForResult(intent, Constant.GET_LOCATION);
			}
		});
	}
	private void setGridViewOnClickListener() {
		mGvCommodityImage
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (position == mCommodityImageList.size()) {
							if (mCreateBmpFactory.hasSdcard()) {
								Intent intent = new Intent(mContext,
										NativeImageActivity.class);
								Bundle bundle = new Bundle();
								NativeImageAlbum album = new NativeImageAlbum();
								album.setBitList(mNativeImageItems);
								bundle.putSerializable("mAblum", album);
								intent.putExtras(bundle);
								startActivity(intent);
							} else {
								Toast.makeText(mContext, "SdCard不存在",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Intent intent = new Intent(mContext,
									ShowBigImageActivity.class);
							NativeImageAlbum album = new NativeImageAlbum();
							album.setBitList(mNativeImageItems);
							Bundle bundle = new Bundle();
							bundle.putSerializable("imageAlbum", album);
							bundle.putInt("position", position);
							intent.putExtras(bundle);
							startActivity(intent);
						}
					}
				});

		mGvCommodityImage
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {

						return false;
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
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void fetchData() {
		
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		getImageFromOtherActivity();
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
			uploadImage();
		} else if (0 == mImagePathList.size()) {
			updateThings(mThings);
		} else {
			uploadImage();
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
			final String price, final List<ThingsImage> images) {
		User user = mUserLogic.getCurrentUser();
		final Things things = new Things();
		things.setAuthor(user);
		things.setContent(thingsDescription);
		things.setPrice(Integer.valueOf(price));
		if (null != images) {
			things.setThingsImages(images);
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

	private void uploadImage() {
		String path = mImagePathList.get(0);
		mImagePathList.remove(path);
		mImageLogic.upLoadImageWithPath(path);
	}

	private void publishNewThings(Things things) {

		mThingsLogic.publishThings(things);
	}

	@Override
	public void onUploadImageSuccess(ThingsImage image) {
		if (null != image) { 
			mThingsImages.add(image);
		}
		if (mImagePathList.size() == 0) {
			if (null == mThings) {
				Things things = initThings(mEditThingsDdescription.getText()
						.toString(), mEditThingsPrice.getText().toString(), mThingsImages);
				publishNewThings(things);
			} else {
				mThings.setThingsImages(mThingsImages);
				updateThings(mThings);
			}
		} else {
			uploadImage();
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
	private void getImageFromOtherActivity() {
		Bundle bundle = getIntent().getExtras();
		if (bundle == null)
			return;

		NativeImageAlbum album = (NativeImageAlbum) bundle
				.getSerializable("mAblum");
		if (album == null)
			return;

		setNativeImageItemList(album);

		setThumbnailBitmapList(mNativeImageItems);

		resetGridViewHeight();
		mImageAdapter.notifyDataSetChanged();
	}
	
	private void setNativeImageItemList(NativeImageAlbum album) {
		mNativeImageItems = album.getBitList();
		mImagePathList.removeAll(mImagePathList);
		for (int i = 0; i < mNativeImageItems.size(); i++) {
			mImagePathList.add(mNativeImageItems.get(i).getPath());
		}
	}
	private void setGridViewCommodityImage() {
		mImageAdapter = new CommodityImageGridViewAdapter(mContext,
				mCommodityImageList);
		mGvCommodityImage.setAdapter(mImageAdapter);
	}
	private void resetGridViewHeight() {
		if (mGridViewHeight == 0) {
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			mGridViewHeight = dm.widthPixels / 4;
		}
		int i = mCommodityImageList.size() / 4 + 1;
		RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) mGvCommodityImage
				.getLayoutParams(); // 取控件mGrid当前的布局参数
		linearParams.height = mGridViewHeight * i;// 当控件的高强制设成80象素
		mGvCommodityImage.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件

	}
	
	private void getKeyBoardHeight() {
		final RelativeLayout content = (RelativeLayout) findViewById(R.id.relativeLayout_publish);
		content.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (keyBoardHeight <= 100) {
							Rect r = new Rect();
							content.getWindowVisibleDisplayFrame(r);

							int screenHeight = content.getRootView()
									.getHeight();
							int heightDifference = screenHeight
									- (r.bottom - r.top);
							int resourceId = getResources().getIdentifier(
									"status_bar_height", "dimen", "android");
							if (resourceId > 0) {
								heightDifference -= getResources()
										.getDimensionPixelSize(resourceId);
							}
							if (heightDifference > 100) {
								keyBoardHeight = heightDifference;
							}
						}
						if (isHideEditText()) {
							Rect r = new Rect();
							content.getWindowVisibleDisplayFrame(r);
							mScrollView.scrollTo(0, r.bottom);
						}
					}
				});
	}
	
	private Boolean isHideEditText() {
		View v = getCurrentFocus();
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			v.getLocationInWindow(leftTop);
			DeviceSize deviceSize = new DeviceSize(mContext);
			int top = deviceSize.getmHeight() - leftTop[1] - v.getHeight();
			if (top < keyBoardHeight) {
				return true;
			}
		}
		return false;
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
		// 商品图片
		if (mCommodityImageList.size() == 0) {
				return false;
		}

		if (null == mLocationName && null == mThings.getLocationName()) {
			return false;
		}

		return true;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
			return super.dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}
	
	private boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			// 获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域，保留点击EditText的事件
				return false;
			} else {
				return true;
			}
		}
		return false;
	}
	
	private void setThumbnailBitmapList(List<NativeImageItem> items) {
		mCommodityImageList.removeAll(mCommodityImageList);
		for (int i = 0; i < items.size(); i++) {
			Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(
					mContext.getContentResolver(), items.get(i).getimageID(),
					Thumbnails.MICRO_KIND, null);
			if (bitmap != null) {
				mCommodityImageList.add(bitmap);
			}
		}
	}


}
