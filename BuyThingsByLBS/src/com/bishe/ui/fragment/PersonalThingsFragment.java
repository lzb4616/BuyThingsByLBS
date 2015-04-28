package com.bishe.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import cn.bmob.v3.BmobUser;

import com.bishe.MyApplication;
import com.bishe.adapter.ThingsContentAdapter;
import com.bishe.buythingsbylbs.R;
import com.bishe.config.Constant;
import com.bishe.logic.ThingsLogic;
import com.bishe.logic.ThingsLogic.OnGetUserThingsListener;
import com.bishe.model.Things;
import com.bishe.model.User;
import com.bishe.pulltorefresh.library.PullToRefreshBase;
import com.bishe.pulltorefresh.library.PullToRefreshBase.Mode;
import com.bishe.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.bishe.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.bishe.pulltorefresh.library.PullToRefreshListView;
import com.bishe.ui.activity.ThingsDetailActivity;
import com.bishe.ui.base.BaseHomeFragment;
import com.bishe.utils.ActivityUtils;
import com.bishe.utils.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * @author robin
 * @date 2015-4-28
 * Copyright 2015 The robin . All rights reserved
 */
public class PersonalThingsFragment extends BaseHomeFragment implements OnGetUserThingsListener{

	private ImageView mPersonalIcon;
	private TextView mPersonalName;
	private TextView mPersonalSign;
	private TextView mPersonalTitle;
	
	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	
	private ArrayList<Things> mThingsList;
	private ThingsContentAdapter mContentAdapter;
	
	private ThingsLogic mThingsLogic;
	private User mUser;
	
	private int pageNum ;
	
	public static final int EDIT_USER = 1;
	
	public enum RefreshType{
		REFRESH,LOAD_MORE
	}
	private RefreshType mRefreshType = RefreshType.LOAD_MORE;
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_personal_things;
	}

	@Override
	protected void findViews(View view) {
		mPersonalIcon = (ImageView)view.findViewById(R.id.personal_icon);
		mPersonalName = (TextView)view.findViewById(R.id.personl_name);
		mPersonalSign = (TextView)view.findViewById(R.id.personl_signature);
		mPersonalTitle = (TextView)view.findViewById(R.id.personl_title);
		mPullToRefreshListView = (PullToRefreshListView)view.findViewById(R.id.pull_refresh_list_personal);
	}

	@Override
	protected void setupViews(Bundle bundle) {
		initViews();
		getUserThings();
	}

	@Override
	protected void initListeners() {
		mThingsLogic.setOnGetUserThingsListener(this);
	}

	@Override
	protected void initData() {
		mThingsLogic = new ThingsLogic(mContext);
		mUser = (User) mContext.getIntent().getSerializableExtra("user");
	}

	private void initViews()
	{
		if(isCurrentUser(mUser)){
			mPersonalTitle.setText("我发表过的");
		}else{
			if(mUser !=null && mUser.getSex().equals(Constant.SEX_FEMALE)){
				mPersonalTitle.setText("她发表过的");
			}else if(mUser !=null && mUser.getSex().equals(Constant.SEX_MALE)){
				mPersonalTitle.setText("他发表过的");
			}
		}
		
		String avatarUrl = null;
		if (mUser.getAvatar() != null) {
			avatarUrl = mUser.getAvatar().getFileUrl(mContext);
		}
		ImageLoader.getInstance().displayImage(
				avatarUrl,
				mPersonalIcon,
				MyApplication.getInstance().getOptions(
						R.drawable.user_icon_default_main),
				new SimpleImageLoadingListener() {

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {

						super.onLoadingComplete(imageUri, view, loadedImage);
					}

				});
		mPersonalName.setText(mUser.getUsername());
		mPersonalSign.setText(mUser.getSignature());
		mPullToRefreshListView.setMode(Mode.BOTH);
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				mRefreshType = RefreshType.REFRESH;
				pageNum = 0;
				getUserThings();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				mRefreshType = RefreshType.LOAD_MORE;
				getUserThings();
			}
		});
		mPullToRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				
			}
		});
		mListView = mPullToRefreshListView.getRefreshableView();
		mThingsList = new ArrayList<Things>();
		mContentAdapter = new ThingsContentAdapter(mContext, mThingsList);
		mListView.setAdapter(mContentAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {

				Intent intent = new Intent();
				intent.setClass(getActivity(), ThingsDetailActivity.class);
				intent.putExtra("data", mThingsList.get(position-1));
				startActivity(intent);
			}
		});
	}
	
	private void getUserThings()
	{
		mThingsLogic.getUserPublishThings(mUser, pageNum++);
	}
	
	/**
	 * 判断点击条目的用户是否是当前登录用户
	 * @return
	 */
	private boolean isCurrentUser(User user){
		if(null != user){
			User cUser = BmobUser.getCurrentUser(mContext, User.class);
			if(cUser != null && cUser.getObjectId().equals(user.getObjectId())){
				return true;
			}
		}
		return false;
	}

	@Override
	public void onGetUserThingsSuccess(List<Things> data) {
		if(data.size()!=0&&data.get(data.size()-1)!=null){
			if(mRefreshType == RefreshType.REFRESH){
				mThingsList.clear();
			}
			
			if(data.size()<Constant.NUMBERS_PER_PAGE){
				ActivityUtils.toastShowBottom(mContext, "已加载完所有数据~");
			}
			
			mThingsList.addAll(data);
			mContentAdapter.notifyDataSetChanged();
			mPullToRefreshListView.onRefreshComplete();
		}else{
			ActivityUtils.toastShowBottom(mContext, "暂无更多数据~");
			pageNum--;
			mPullToRefreshListView.onRefreshComplete();
		}		
	}

	@Override
	public void onGetUserThingsFailure(String msg) {
		LogUtils.i(TAG,"find failed."+msg);
		pageNum--;
		mPullToRefreshListView.onRefreshComplete();		
	}
}
