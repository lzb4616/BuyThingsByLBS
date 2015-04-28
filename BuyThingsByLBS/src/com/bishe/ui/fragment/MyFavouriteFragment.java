package com.bishe.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.bishe.adapter.ThingsContentAdapter;
import com.bishe.buythingsbylbs.R;
import com.bishe.config.Constant;
import com.bishe.logic.ThingsLogic;
import com.bishe.logic.ThingsLogic.OnGetMyFavoutiteListener;
import com.bishe.model.Things;
import com.bishe.pulltorefresh.library.PullToRefreshBase;
import com.bishe.pulltorefresh.library.PullToRefreshListView;
import com.bishe.pulltorefresh.library.PullToRefreshBase.Mode;
import com.bishe.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.bishe.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.bishe.pulltorefresh.library.PullToRefreshBase.State;
import com.bishe.ui.activity.ThingsDetailActivity;
import com.bishe.ui.base.BaseFragment;
import com.bishe.ui.base.BaseHomeFragment;
import com.bishe.utils.ActivityUtils;
import com.bishe.utils.LogUtils;

/**
 * @author robin
 * @date 2015-4-28
 * Copyright 2015 The robin . All rights reserved
 */
public class MyFavouriteFragment extends BaseHomeFragment implements OnGetMyFavoutiteListener {



	private int pageNum;
	private String lastItemTime;//当前列表结尾的条目的创建时间，
	
	private ArrayList<Things> mListItems;
	private PullToRefreshListView mPullRefreshListView;
	private ThingsContentAdapter mAdapter;
	private ListView actualListView;
	private ThingsLogic mThingsLogic;
	
	private TextView networkTips;
	private ProgressBar progressbar;
	
	public enum RefreshType{
		REFRESH,LOAD_MORE
	}
	private RefreshType mRefreshType = RefreshType.LOAD_MORE;
	
	private static final int LOADING = 1;
	private static final int LOADING_COMPLETED = 2;
	private static final int LOADING_FAILED =3;
	private static final int NORMAL = 4;
	
	public static BaseFragment newInstance(int index){
		BaseFragment fragment = new MyFavouriteFragment();
		return fragment;
	}
	
	private String getCurrentTime(){
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     String times = formatter.format(new Date(System.currentTimeMillis()));
	     return times;
	}

	@Override
	protected int getLayoutId() {
		
		return R.layout.fragment_listview_main;
	}

	@Override
	protected void findViews(View view) {
		mPullRefreshListView = (PullToRefreshListView)view
				.findViewById(R.id.pull_refresh_list);
		networkTips = (TextView)view.findViewById(R.id.networkTips);
		progressbar = (ProgressBar)view.findViewById(R.id.progressBar);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				mPullRefreshListView.setMode(Mode.BOTH);
				mRefreshType = RefreshType.REFRESH;
				pageNum = 0;
				lastItemTime = getCurrentTime();
				getMyFavourite();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				mRefreshType = RefreshType.LOAD_MORE;
				getMyFavourite();
			}
		});
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				
			}
		});
		
		actualListView = mPullRefreshListView.getRefreshableView();
		mListItems = new ArrayList<Things>();
		mAdapter = new ThingsContentAdapter(mContext, mListItems);
		actualListView.setAdapter(mAdapter);
	
		mPullRefreshListView.setState(State.RELEASE_TO_REFRESH, true);
		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ThingsDetailActivity.class);
				intent.putExtra("data", mListItems.get(position-1));
				startActivity(intent);
			}
		});
	}
	
	public void setState(int state){
		switch (state) {
		case LOADING:
			if(mListItems.size() == 0){
				mPullRefreshListView.setVisibility(View.GONE);
				progressbar.setVisibility(View.VISIBLE);
			}
			networkTips.setVisibility(View.GONE);
			
			break;
		case LOADING_COMPLETED:
			networkTips.setVisibility(View.GONE);
			progressbar.setVisibility(View.GONE);
			
		    mPullRefreshListView.setVisibility(View.VISIBLE);
		    mPullRefreshListView.setMode(Mode.BOTH);

			
			break;
		case LOADING_FAILED:
			if(mListItems.size()==0){
				mPullRefreshListView.setVisibility(View.VISIBLE);
				mPullRefreshListView.setMode(Mode.PULL_FROM_START);
				networkTips.setVisibility(View.VISIBLE);
			}
			progressbar.setVisibility(View.GONE);
			break;
		case NORMAL:
			
			break;
		default:
			break;
		}
	}

	
	private void getMyFavourite(){
		mThingsLogic.getMyFavouriteThings(pageNum++);
	}
	
	@Override
	protected void setupViews(Bundle bundle) {
		pageNum = 0;
		lastItemTime = getCurrentTime();
		if(mListItems.size() == 0){
			getMyFavourite();
		}
		LogUtils.i(TAG,"curent time:"+lastItemTime);
	}

	@Override
	protected void initListeners() {
		mThingsLogic.setOnGetMyFavoutiteListener(this);
	}

	@Override
	protected void initData() {
		mThingsLogic = new ThingsLogic(mContext);
	}

	@Override
	public void onGetMyFavSuccess(List<Things> data) {
		LogUtils.i(TAG,"find success."+data.size());
		if(data.size()!=0&&data.get(data.size()-1)!=null){
			if(mRefreshType==RefreshType.REFRESH){
				mListItems.clear();
			}
			if(data.size()<Constant.NUMBERS_PER_PAGE){
				LogUtils.i(TAG,"已加载完所有数据~");
			}
			for (Things things : data) {
				things.setMyFav(true);
			}
			mListItems.addAll(data);
			mAdapter.notifyDataSetChanged();
			
			setState(LOADING_COMPLETED);
			mPullRefreshListView.onRefreshComplete();
		}else{
			ActivityUtils.toastShowBottom(getActivity(), "暂无更多数据~");
			pageNum--;
			setState(LOADING_COMPLETED);
			mPullRefreshListView.onRefreshComplete();
		}
	}

	@Override
	public void onGetMyFavFailure(String msg) {
		LogUtils.i(TAG,"find failed."+msg);
		pageNum--;
		setState(LOADING_FAILED);
		mPullRefreshListView.onRefreshComplete();
	}
}
