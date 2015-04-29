package com.bishe.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.bmob.v3.datatype.BmobFile;

import com.bishe.MyApplication;
import com.bishe.buythingsbylbs.R;
import com.bishe.config.Constant;
import com.bishe.logic.ThingsImageLogic;
import com.bishe.logic.ThingsImageLogic.IsUploadImageListener;
import com.bishe.logic.UserLogic;
import com.bishe.logic.UserLogic.IsUpdateUserListener;
import com.bishe.model.Location;
import com.bishe.model.ThingsImage;
import com.bishe.model.User;
import com.bishe.ui.activity.LoginAndRegisterActivity;
import com.bishe.ui.activity.MyLoactionActivity;
import com.bishe.ui.base.BaseHomeFragment;
import com.bishe.utils.ActivityUtils;
import com.bishe.utils.CreateBmpFactory;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * @author robin
 * @date 2015-4-28 Copyright 2015 The robin . All rights reserved
 */
public class PersonalInfoEditFragment extends BaseHomeFragment implements
		OnClickListener, IsUpdateUserListener, IsUploadImageListener {

	private ImageView mUserIcon;
	private EditText mUserName;
	private EditText mUserSignature;
	private CheckBox mSexSwitch;
	private TextView mUpdatetv;
	private EditText mUserPhone;

	private UserLogic mUserLogic;
	private User mCurrentUser;
	private CreateBmpFactory mBmpFactory;
	private ThingsImageLogic mImageLogic;

	private RelativeLayout mLocationLayout;
	private TextView mLocationNameTv;
	private String mImagePath;

	private Location mLocation;
	private String mLocationName;
	
	public static final int GO_LOGIN = 13;


	@Override
	protected int getLayoutId() {
		return R.layout.fragment_personal_info;
	}

	@Override
	protected void findViews(View view) {
		mUserIcon = (ImageView) view.findViewById(R.id.user_icon_image);
		mUserName = (EditText) view.findViewById(R.id.user_nick_edv);
		mUserSignature = (EditText) view.findViewById(R.id.user_sign_edv);
		mSexSwitch = (CheckBox) view.findViewById(R.id.sex_choice_switch);
		mUpdatetv = (TextView) view.findViewById(R.id.user_info_update);
		mUserPhone = (EditText) view.findViewById(R.id.user_phone_edv);
		mLocationLayout = (RelativeLayout) view
				.findViewById(R.id.user_location_rl);
		mLocationNameTv = (TextView) view.findViewById(R.id.user_location_tv);
	}

	@Override
	protected void setupViews(Bundle bundle) {
		initPersonalInfo();
	}

	@Override
	protected void initListeners() {
		mUpdatetv.setOnClickListener(this);
		mUserLogic.setOnUpdateUserListener(this);
		mUserIcon.setOnClickListener(this);
		mImageLogic.setIsUploadImageListener(this);
		mLocationLayout.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		mUserLogic = new UserLogic(mContext);
		mBmpFactory = new CreateBmpFactory(this);
		mImageLogic = new ThingsImageLogic(mContext);

		mCurrentUser = mUserLogic.getCurrentUser();
		if (null == mCurrentUser) {
			// 前往登录注册界面
			ActivityUtils.toastShowBottom((Activity) mContext, "收藏前请先登录。");
			Intent intent = new Intent();
			intent.setClass(mContext, LoginAndRegisterActivity.class);
			MyApplication.getInstance().getTopActivity()
					.startActivityForResult(intent, GO_LOGIN);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case GO_LOGIN:
				initPersonalInfo();
				break;
			case Constant.GET_LOCATION:
				mLocationName= (String) data.getStringExtra("locationName");
				if (null != mLocationName) {
					mLocationNameTv.setText(mLocationName);
				}
				mLocation = (Location) data.getSerializableExtra("location");
				break;
			default:
				break;
			}
		}
		mImagePath = mBmpFactory.getBitmapFilePath(requestCode, resultCode,
				data);
		if (null != mImagePath) {
			mUserIcon.setImageBitmap(mBmpFactory.getBitmapByOpt(mImagePath));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initPersonalInfo() {
		mUserName.setText(mCurrentUser.getUsername());
		if (null != mCurrentUser.getPhoneNum()) {
			mUserPhone.setText("" + mCurrentUser.getPhoneNum());
		}
		mUserSignature.setText(mCurrentUser.getSignature());
		if (mCurrentUser.getSex().equals(Constant.SEX_FEMALE)) {
			mSexSwitch.setChecked(true);
		} else {
			mSexSwitch.setChecked(false);
		}
		if (null != mCurrentUser.getUserLoactionName()) {
			mLocationNameTv.setText(mCurrentUser.getUserLoactionName());
		}
		BmobFile avatarFile = mCurrentUser.getAvatar();
		if (null != avatarFile) {
			ImageLoader.getInstance().displayImage(
					avatarFile.getFileUrl(mContext),
					mUserIcon,
					MyApplication.getInstance().getOptions(
							R.drawable.user_icon_default_main),
					new SimpleImageLoadingListener() {

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							super.onLoadingComplete(imageUri, view, loadedImage);
						}
					});
		}
	}

	private void uplaodImage(String imagePath) {
		if (null == imagePath) {
			updateUserInfo(initUserInfo(null));
			return;
		}
		mImageLogic.upLoadImageWithPath(imagePath);
	}

	private User initUserInfo(ThingsImage image) {
		User user = mUserLogic.getCurrentUser();
		user.setPhoneNum(Long.valueOf(mUserPhone.getText().toString()));
		user.setSex(mSexSwitch.isChecked() ? Constant.SEX_FEMALE
				: Constant.SEX_MALE);
		user.setSignature(mUserSignature.getText().toString());
		user.setUsername(mUserName.getText().toString());
		if (null != image) {
			user.setAvatar(image);
		}
		if (null != mLocation) {
			user.setLocation(mLocation);
		}
		if (null != mLocationName) {
			user.setUserLoactionName(mLocationNameTv.getText().toString());
		}
		return user;
	}

	private void updateUserInfo(User user) {
		if (null == user) {
			return;
		}
		mUserLogic.updateUser(user);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_info_update:
			uplaodImage(mImagePath);
			break;
		case R.id.user_icon_image:
			mBmpFactory.OpenGallery();
			break;
		case R.id.user_location_rl:
			Intent intent = new Intent(mContext, MyLoactionActivity.class);
			mContext.startActivityForResult(intent, Constant.GET_LOCATION);
		default:
			break;
		}
	}

	@Override
	public void onUpdateSuccess() {
		ActivityUtils.toastShowBottom(mContext, "修改个人信息成功");
		mContext.finish();
	}

	@Override
	public void onUpdateFailure(String msg) {

	}

	@Override
	public void onUploadImageSuccess(ThingsImage image) {
		updateUserInfo(initUserInfo(image));
	}

	@Override
	public void onUploadImageProgress(Integer arg0) {

	}

	@Override
	public void onUploadImageFailure(String msg) {
		ActivityUtils.toastShowBottom(mContext, "上传头像失败" + msg);
	}

}
