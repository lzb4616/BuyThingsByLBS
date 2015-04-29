package com.bishe.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.bishe.MyApplication;
import com.bishe.buythingsbylbs.R;
import com.bishe.config.Constant;
import com.bishe.logic.UserLogic;
import com.bishe.logic.UserLogic.OnCollectMyFavouriteListener;
import com.bishe.model.Things;
import com.bishe.model.User;
import com.bishe.ui.activity.LoginAndRegisterActivity;
import com.bishe.ui.activity.PersonalInfoActivity;
import com.bishe.ui.activity.PersonalThingsActivity;
import com.bishe.ui.activity.ThingsDetailActivity;
import com.bishe.utils.ActivityUtils;
import com.bishe.utils.BitmapUtils;
import com.bishe.utils.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * @author robin
 * @date 2015-4-26 Copyright 2015 The robin . All rights reserved
 */
public class ThingsContentAdapter extends BaseContentAdapter<Things> implements
		OnCollectMyFavouriteListener {

	public static final String TAG = "ThingsContentAdapter";
	public static final int SAVE_FAVOURITE = 2;
	private UserLogic mUserLogic;
	private Boolean mIsCollect;

	public ThingsContentAdapter(Context context, List<Things> list) {
		super(context, list);
		this.mUserLogic = new UserLogic(context);
		this.mUserLogic.setOnCollectMyFavouriteListener(this);
	}

	@Override
	public View getConvertView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();

			convertView = mInflater
					.inflate(R.layout.things_listview_item, null);

			viewHolder.userName = (TextView) convertView
					.findViewById(R.id.user_name);
			viewHolder.userLogo = (ImageView) convertView
					.findViewById(R.id.user_logo);
			viewHolder.favMark = (ImageView) convertView
					.findViewById(R.id.item_action_fav);
			viewHolder.contentText = (TextView) convertView
					.findViewById(R.id.content_text);
			viewHolder.contentImage = (ImageView) convertView
					.findViewById(R.id.content_image);
			viewHolder.thingsDistance = (TextView) convertView
					.findViewById(R.id.things_distance_text);
			viewHolder.thingsLocation = (TextView) convertView
					.findViewById(R.id.things_location_text);
			viewHolder.thingsPhone = (TextView) convertView
					.findViewById(R.id.user_phoneNum_text);
			viewHolder.thingsPrice = (TextView) convertView
					.findViewById(R.id.things_price_text);
			viewHolder.share = (TextView) convertView
					.findViewById(R.id.item_action_share);
			viewHolder.comment = (TextView) convertView
					.findViewById(R.id.item_action_comment);
			viewHolder.buytagView = (ImageView) convertView.findViewById(R.id.content_buy_tag);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final Things entity = dataList.get(position);
		LogUtils.i("user", entity.toString());
		User user = entity.getAuthor();
		if (user == null) {
			LogUtils.i("user", "USER IS NULL");
		}
		if (user.getAvatar() == null) {
			LogUtils.i("user", "USER avatar IS NULL");
		}
		viewHolder.userName.setText(entity.getAuthor().getUsername());
		viewHolder.contentText.setText(entity.getContent());
		if (null!= entity.getAuthor().getLocation()) {
			double distance = entity.getThingsLocation().distanceInKilometersTo(mUserLogic.getCurrentUser().getLocation());
			if (distance > 10.0) {
				viewHolder.thingsDistance.setText(""+(int)distance+" km");
			}
			else {
				distance *= 1000;
				viewHolder.thingsDistance.setText(""+(int)distance+" m");
			}
		}else {
			viewHolder.thingsDistance.setText("0米");
		}
		viewHolder.thingsLocation.setText(entity.getLocationName());
		if (null ==entity.getAuthor().getPhoneNum()) {
			viewHolder.thingsPhone.setText("");
		}else {
			viewHolder.thingsPhone.setText(""+entity.getAuthor().getPhoneNum());
		}
		viewHolder.thingsPrice.setText(String.valueOf(entity.getPrice()));
		viewHolder.comment.setText("评论:" + entity.getComment());
		viewHolder.buytagView.setImageBitmap(((BitmapDrawable)mContext.getResources().getDrawable(R.drawable.icon_tag_2x)).getBitmap());
		viewHolder.buytagView.setVisibility(entity.isBuy()?View.VISIBLE:View.GONE);
		String avatarUrl = null;
		if (user.getAvatar() != null) {
			avatarUrl = user.getAvatar().getFileUrl(mContext);
		}
		ImageLoader.getInstance().displayImage(
				avatarUrl,
				viewHolder.userLogo,
				MyApplication.getInstance().getOptions(
						R.drawable.user_icon_default_main),
				new SimpleImageLoadingListener() {

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {

						super.onLoadingComplete(imageUri, view, loadedImage);
					}

				});
		viewHolder.userLogo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mUserLogic.getCurrentUser() == null) {
					ActivityUtils.toastShowBottom((Activity) mContext, "请先登录。");
					Intent intent = new Intent();
					intent.setClass(mContext, LoginAndRegisterActivity.class);
					MyApplication.getInstance().getTopActivity()
							.startActivity(intent);
					return;
				}
				Intent intent = new Intent();
				intent.setClass(mContext, PersonalThingsActivity.class);
				intent.putExtra("user", entity.getAuthor());
				mContext.startActivity(intent);
			}
		});

		if (null == entity.getThingsImage()) {
			viewHolder.contentImage.setVisibility(View.GONE);
		} else {
			viewHolder.contentImage.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					entity.getThingsImage().getFileUrl(mContext) == null ? ""
							: entity.getThingsImage().getFileUrl(mContext),
					viewHolder.contentImage,
					MyApplication.getInstance().getOptions(
							R.drawable.bg_pic_loading),
					new SimpleImageLoadingListener() {

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							super.onLoadingComplete(imageUri, view, loadedImage);
							LogUtils.i(TAG, "加载图片" + imageUri + "成功");
						}

					});
		}

		viewHolder.share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// share to sociaty
				ActivityUtils.toastShowBottom((Activity) mContext, "分享给好友看哦~");
				// TODO Auto-generated method stub
				// final TencentShare tencentShare=new
				// TencentShare(MyApplication.getInstance().getTopActivity(),
				// getQQShareEntity(entity));
				// tencentShare.shareToQQ();
			}
		});
		viewHolder.comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mUserLogic.getCurrentUser() == null) {
					ActivityUtils.toastShowBottom((Activity) mContext, "请先登录。");
					Intent intent = new Intent();
					intent.setClass(mContext, LoginAndRegisterActivity.class);
					MyApplication.getInstance().getTopActivity()
							.startActivity(intent);
					return;
				}
				Intent intent = new Intent();
				intent.setClass(MyApplication.getInstance().getTopActivity(),
						ThingsDetailActivity.class);
				intent.putExtra("data", entity);
				mContext.startActivity(intent);
			}
		});

		if (entity.isMyFav()) {
			viewHolder.favMark
					.setImageResource(R.drawable.ic_action_fav_choose);
		} else {
			viewHolder.favMark
					.setImageResource(R.drawable.ic_action_fav_normal);
		}
		viewHolder.favMark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 收藏
				ActivityUtils.toastShowBottom((Activity) mContext, "收藏");
				onClickFav(v, entity);
			}
		});
		return convertView;
	}

	private void onClickFav(View v, final Things things) {

		mIsCollect = !things.isMyFav();
		if (mIsCollect) {
			((ImageView) v).setImageResource(R.drawable.ic_action_fav_choose);
		} else {
			((ImageView) v).setImageResource(R.drawable.ic_action_fav_normal);
		}
		mUserLogic.collectMyFav(things, mIsCollect);
	}

	private void getMyFavourite() {
		User user = BmobUser.getCurrentUser(mContext, User.class);
		if (user != null) {
			BmobQuery<Things> query = new BmobQuery<Things>();
			query.addWhereRelatedTo("favorite", new BmobPointer(user));
			query.include("user");
			query.order("createdAt");
			// query.setLimit(Constant.NUMBERS_PER_PAGE);
			query.findObjects(mContext, new FindListener<Things>() {

				@Override
				public void onSuccess(List<Things> data) {
					LogUtils.i(TAG, "get fav success!" + data.size());
					ActivityUtils.toastShowBottom((Activity) mContext,
							"fav size:" + data.size());
				}

				@Override
				public void onError(int arg0, String arg1) {
					ActivityUtils.toastShowBottom((Activity) mContext,
							"获取收藏失败。请检查网络~");
				}
			});
		} else {
			// 前往登录注册界面
			ActivityUtils.toastShowBottom((Activity) mContext, "获取收藏前请先登录。");
			Intent intent = new Intent();
			intent.setClass(mContext, LoginAndRegisterActivity.class);
			MyApplication.getInstance().getTopActivity()
					.startActivityForResult(intent, Constant.GET_FAVOURITE);
		}
	}

	public static class ViewHolder {
		public ImageView userLogo;
		public TextView userName;
		public TextView contentText;
		public ImageView contentImage;
		public TextView thingsDistance;
		public TextView thingsLocation;
		public TextView thingsPrice;
		public TextView thingsPhone;
		public ImageView favMark;
		public TextView share;
		public TextView comment;
		public ImageView buytagView; 
	}

	@Override
	public void onCollectSuccess() {
		if (mIsCollect) {
			// TODO Auto-generated method stub
			/**
			 * 这个到时需要数据库
			 * */
			// DatabaseUtil.getInstance(mContext).insertFav(qiangYu);
			ActivityUtils.toastShowBottom((Activity) mContext, "收藏成功。");
			LogUtils.i(TAG, "收藏成功。");

			// try get fav to see if fav success
			getMyFavourite();
		} else {
			/**
			 * 这个到时需要数据库
			 * */
			// DatabaseUtil.getInstance(mContext).deleteFav(qiangYu);
			ActivityUtils.toastShowBottom((Activity) mContext, "取消收藏成功。");
			LogUtils.i(TAG, "取消收藏。");
			// try get fav to see if fav success
			getMyFavourite();
		}
	}

	@Override
	public void onCollectFailure(String msg) {
		LogUtils.i(TAG, "收藏失败。请检查网络~");
		ActivityUtils.toastShowBottom((Activity) mContext, "收藏失败。请检查网络~" + msg);
	}

}
