package com.bishe.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bishe.MyApplication;
import com.bishe.adapter.CommentAdapter;
import com.bishe.buythingsbylbs.R;
import com.bishe.config.Constant;
import com.bishe.logic.CommentLogic;
import com.bishe.logic.CommentLogic.OnPublishCommnetListener;
import com.bishe.logic.ThingsLogic;
import com.bishe.logic.ThingsLogic.IsDeleteListener;
import com.bishe.logic.ThingsLogic.IsUpdateListener;
import com.bishe.logic.ThingsLogic.OnThingsRelatedCommnetListener;
import com.bishe.logic.UserLogic;
import com.bishe.logic.CommentLogic.OnFetchCommnetListener;
import com.bishe.logic.UserLogic.OnBuyThingsListener;
import com.bishe.logic.UserLogic.OnCollectMyFavouriteListener;
import com.bishe.model.Comment;
import com.bishe.model.Things;
import com.bishe.model.User;
import com.bishe.ui.activity.LoginAndRegisterActivity;
import com.bishe.ui.activity.PersonalThingsActivity;
import com.bishe.ui.activity.PublishThingsActivity;
import com.bishe.ui.activity.ThingsDetailActivity;
import com.bishe.ui.activity.ThingsDetailActivity.OnThingsDetailListener;
import com.bishe.ui.base.BaseFragment;
import com.bishe.ui.base.BaseHomeFragment;
import com.bishe.utils.ActivityUtils;
import com.bishe.utils.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * @author robin
 * @date 2015-4-27 Copyright 2015 The robin . All rights reserved
 */
public class ThingsDetailFragment extends BaseHomeFragment implements
		OnItemClickListener, OnCollectMyFavouriteListener,
		OnFetchCommnetListener, OnPublishCommnetListener,
		OnThingsRelatedCommnetListener, OnThingsDetailListener,OnBuyThingsListener,IsUpdateListener,IsDeleteListener{

	private ImageView mUserLogo;
	private TextView mUserName;
	private TextView mContentText;
	private ImageView mContentImage;
	private TextView mThingsDistance;
	private TextView mThingsLocation;
	private TextView mThingsPrice;
	private TextView mThingsPhone;
	private ImageView mFavMark;
	private TextView mShare;
	private TextView mComment;
	private EditText mCommentContent;
	private Button mCommentCommit;
	private ImageView mBuytagView;

	private ListView mCommentList;
	private TextView mFooter;

	private CommentAdapter mCommentAdapter;
	private List<Comment> mComments = new ArrayList<Comment>();
	private Things mThings;
	private int mPageNum;

	private UserLogic mUserLogic;
	private ThingsLogic mThingsLogic;
	private CommentLogic mCommentLogic;
	private Boolean mIsCollect;

	public static BaseFragment newInstance() {
		BaseFragment fragment = new ThingsDetailFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_things_detail;
	}

	@Override
	protected void findViews(View view) {
		mUserName = (TextView) view.findViewById(R.id.user_name);
		mUserLogo = (ImageView) view.findViewById(R.id.user_logo);
		mFavMark = (ImageView) view.findViewById(R.id.item_action_fav);
		mContentText = (TextView) view.findViewById(R.id.content_text);
		mContentImage = (ImageView) view.findViewById(R.id.content_image);
		mThingsDistance = (TextView) view
				.findViewById(R.id.things_distance_text);
		mThingsLocation = (TextView) view
				.findViewById(R.id.things_location_text);
		mThingsPhone = (TextView) view.findViewById(R.id.user_phoneNum_text);
		mThingsPrice = (TextView) view.findViewById(R.id.things_price_text);
		mShare = (TextView) view.findViewById(R.id.item_action_share);
		mComment = (TextView) view.findViewById(R.id.item_action_comment);
		mCommentList = (ListView) view.findViewById(R.id.comment_list);
		mFooter = (TextView) view.findViewById(R.id.loadmore);
		mCommentContent = (EditText) view.findViewById(R.id.comment_content);
		mCommentCommit = (Button) view.findViewById(R.id.comment_commit);
		mBuytagView = (ImageView) view.findViewById(R.id.content_buy_tag);
	}

	@Override
	protected void setupViews(Bundle bundle) {
		mContext.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		mThings = (Things) mContext.getIntent().getSerializableExtra("data");

		mPageNum = 0;
		mCommentAdapter = new CommentAdapter(mContext, mComments);
		mCommentList.setAdapter(mCommentAdapter);
		setListViewHeightBasedOnChildren(mCommentList);
		mCommentList.setCacheColorHint(0);
		mCommentList.setScrollingCacheEnabled(false);
		mCommentList.setScrollContainer(false);
		mCommentList.setFastScrollEnabled(true);
		mCommentList.setSmoothScrollbarEnabled(true);

		initThingsView(mThings);
		fetchComment(mThings);
	}

	private void initThingsView(final Things entity) {
		LogUtils.i("user", entity.toString());
		User user = entity.getAuthor();
		if (user == null) {
			LogUtils.i("user", "USER IS NULL");
		}
		if (user.getAvatar() == null) {
			LogUtils.i("user", "USER avatar IS NULL");
		}

		mUserName.setText(entity.getAuthor().getUsername());
		mContentText.setText(entity.getContent());
		mThingsDistance.setText("200米");
		mThingsLocation.setText("广州");
		mThingsPhone.setText("" + entity.getAuthor().getPhoneNum());
		mThingsPrice.setText(String.valueOf(entity.getPrice()));
		mComment.setText("评论：" + entity.getComment());
		mBuytagView.setImageBitmap(((BitmapDrawable)mContext.getResources().getDrawable(R.drawable.icon_tag_2x)).getBitmap());
		mBuytagView.setVisibility(entity.isBuy()?View.VISIBLE:View.GONE);
		String avatarUrl = null;
		if (user.getAvatar() != null) {
			avatarUrl = user.getAvatar().getFileUrl(mContext);
		}
		ImageLoader.getInstance().displayImage(
				avatarUrl,
				mUserLogo,
				MyApplication.getInstance().getOptions(
						R.drawable.user_icon_default_main),
				new SimpleImageLoadingListener() {

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {

						super.onLoadingComplete(imageUri, view, loadedImage);
					}

				});
		mUserLogo.setOnClickListener(new OnClickListener() {

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
			mContentImage.setVisibility(View.GONE);
		} else {
			mContentImage.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					entity.getThingsImage().getFileUrl(mContext) == null ? ""
							: entity.getThingsImage().getFileUrl(mContext),
					mContentImage,
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

		mShare.setOnClickListener(new OnClickListener() {

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

		if (entity.isMyFav()) {
			mFavMark.setImageResource(R.drawable.ic_action_fav_choose);
		} else {
			mFavMark.setImageResource(R.drawable.ic_action_fav_normal);
		}
		mFavMark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 收藏
				ActivityUtils.toastShowBottom((Activity) mContext, "收藏");
				onClickFav(v, entity);
			}
		});

		mCommentContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickComment();
			}
		});

		mCommentCommit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				User currentUser = mUserLogic.getCurrentUser();
				if (currentUser != null) {// 已登录
					String commentEdit = mCommentContent.getText().toString()
							.trim();
					if (TextUtils.isEmpty(commentEdit)) {
						ActivityUtils.toastShowBottom(mContext, "评论内容不能为空。");
						return;
					}
					mCommentLogic.publishComment(commentEdit);
				} else {// 未登录
					ActivityUtils.toastShowBottom(mContext, "发表评论前请先登录。");
					Intent intent = new Intent();
					intent.setClass(mContext, LoginAndRegisterActivity.class);
					startActivityForResult(intent, Constant.PUBLISH_COMMENT);
				}
			}
		});

		mFooter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fetchComment(mThings);
			}
		});
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

	private void fetchComment(Things things) {

		mCommentLogic.fetchComment(mPageNum++, things);
	}

	@Override
	protected void initListeners() {
		mCommentList.setOnItemClickListener(this);
		mUserLogic.setOnCollectMyFavouriteListener(this);
		mCommentLogic.setOnFetchCommnetListener(this);
		mCommentLogic.setOnPublishCommnetListener(this);
		mThingsLogic.setOnThingsRelatedCommnetListener(this);
		((ThingsDetailActivity) mContext).setOnThingsDetailListener(this);
		mThingsLogic.setOnIsDeleteListener(this);
		mUserLogic.setOnBuyThingsListener(this);
		mThingsLogic.setOnIsUpdateListener(this);
	}

	@Override
	protected void initData() {
		mUserLogic = new UserLogic(mContext);
		mThingsLogic = new ThingsLogic(mContext);
		mCommentLogic = new CommentLogic(mContext);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ActivityUtils.toastShowBottom(mContext, "" + position);
	}

	/***
	 * 动态设置listview的高度 item 总布局必须是linearLayout
	 * 
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1))
				+ 15;
		listView.setLayoutParams(params);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case Constant.PUBLISH_COMMENT:
				// 登录完成
				mCommentCommit.performClick();
				break;
			case Constant.SAVE_FAVOURITE:
				mFavMark.performClick();
				break;
			case Constant.GO_SETTINGS:
				mUserLogo.performClick();
				break;
			default:
				break;
			}
		}

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
		} else {
			/**
			 * 这个到时需要数据库
			 * */
			// DatabaseUtil.getInstance(mContext).deleteFav(qiangYu);
			ActivityUtils.toastShowBottom((Activity) mContext, "取消收藏成功。");
			LogUtils.i(TAG, "取消收藏。");
		}
	}

	@Override
	public void onCollectFailure(String msg) {
		LogUtils.i(TAG, "收藏失败。请检查网络~");
		ActivityUtils.toastShowBottom((Activity) mContext, "收藏失败。请检查网络~" + msg);
	}

	@Override
	public void onFetchCommnetSuccess(List<Comment> data) {
		LogUtils.i(TAG, "get comment success!" + data.size());
		if (data.size() != 0 && data.get(data.size() - 1) != null) {

			if (data.size() < Constant.NUMBERS_PER_PAGE) {
				ActivityUtils.toastShowBottom(mContext, "已加载完所有评论~");
				mFooter.setText("暂无更多评论~");
			}
			mCommentAdapter.getDataList().addAll(data);
			mCommentAdapter.notifyDataSetChanged();
			setListViewHeightBasedOnChildren(mCommentList);
			LogUtils.i(TAG, "refresh");
		} else {
			ActivityUtils.toastShowBottom(mContext, "暂无更多评论~");
			mFooter.setText("暂无更多评论~");
			mPageNum--;
		}
	}

	@Override
	public void onFetchCommnetFailure(String msg) {
		ActivityUtils.toastShowBottom(mContext, "获取评论失败。请检查网络~");
		mPageNum--;
	}

	@Override
	public void onPublishCommnetSuccess(Comment comment) {
		ActivityUtils.toastShowBottom(mContext, "评论成功。");
		mCommentAdapter.getDataList().add(comment);
		mCommentAdapter.notifyDataSetChanged();
		setListViewHeightBasedOnChildren(mCommentList);
		mCommentContent.setText("");
		mComment.setText("评论：" + mComments.size());
		hideSoftInput();
		mThingsLogic.publishCommetWithThings(mThings, comment);
	}

	@Override
	public void onPublishCommnetFailure(String msg) {
		ActivityUtils.toastShowBottom(mContext, "评论失败。请检查网络~");
	}

	private void onClickComment() {
		mCommentContent.requestFocus();

		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.showSoftInput(mCommentContent, 0);
	}

	private void hideSoftInput() {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(mCommentContent.getWindowToken(), 0);
	}

	@Override
	public void onRelatedCommentSuccess() {
		LogUtils.i(TAG, "更新评论成功。");

	}

	@Override
	public void onRelatedCommnetFailure(String msg) {
		ActivityUtils.toastShowBottom(mContext, "关联评论失败。请检查网络~");
	}

	@Override
	public void buyThings() {
		if (mThings.isBuy()) {
			ActivityUtils.toastShowCenter(mContext, "这个东西已经售出，如果需要请联系买家");
			return;
		}
		mUserLogic.buyThings(mThings, true);
	}

	@Override
	public void deleThings() {
		mThingsLogic.deleteThings(mThings);
	}

	@Override
	public void updateThings() {
		Intent intent = new Intent();
		intent.setClass(mContext, PublishThingsActivity.class);
		intent.putExtra("data", mThings);
		mContext.startActivity(intent);
		mContext.finish();
	}

	@Override
	public void onBuyThingsSuccess() {
		mThings.setBuy(true);
		mThingsLogic.updateThings(mThings);
	}

	@Override
	public void onBuyThingsFailure(String msg) {
		ActivityUtils.toastShowBottom(mContext, "购买失败"+msg);
	}


	@Override
	public void onDeleteSuccess() {
		ActivityUtils.toastShowBottom(mContext, "删除成功");
		mContext.finish();
	}

	@Override
	public void onDeleteFailure(String msg) {
		ActivityUtils.toastShowBottom(mContext, "删除失败"+msg);
	}

	@Override
	public void onUpdateSuccess() {
		mBuytagView.setVisibility(mThings.isBuy()?View.VISIBLE:View.GONE);		
		ActivityUtils.toastShowBottom(mContext, "购买成功");
	}

	@Override
	public void onUpdateFailure(String msg) {
		ActivityUtils.toastShowBottom(mContext, "购买失败"+msg);		
	}

}
