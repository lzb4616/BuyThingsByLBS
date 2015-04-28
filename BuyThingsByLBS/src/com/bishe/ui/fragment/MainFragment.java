package com.bishe.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

import com.bishe.MyApplication;
import com.bishe.adapter.ThingsContentAdapter;
import com.bishe.buythingsbylbs.R;
import com.bishe.config.Constant;
import com.bishe.logic.ThingsLogic;
import com.bishe.logic.ThingsLogic.OnGetAllThingsListener;
import com.bishe.logic.ThingsLogic.OnGetMyFavoutiteListener;
import com.bishe.logic.UserLogic;
import com.bishe.model.Things;
import com.bishe.model.User;
import com.bishe.pulltorefresh.library.PullToRefreshBase;
import com.bishe.pulltorefresh.library.PullToRefreshBase.Mode;
import com.bishe.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.bishe.pulltorefresh.library.PullToRefreshBase.State;
import com.bishe.pulltorefresh.library.PullToRefreshListView;
import com.bishe.ui.activity.LoginAndRegisterActivity;
import com.bishe.ui.activity.ThingsDetailActivity;
import com.bishe.ui.base.BaseFragment;
import com.bishe.ui.base.BaseHomeFragment;
import com.bishe.utils.ActivityUtils;
import com.bishe.utils.LogUtils;
import com.bishe.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
/**
 * @author robin
 * @date 2015-4-26
 * Copyright 2015 The robin . All rights reserved
 */
public class MainFragment extends BaseHomeFragment implements OnGetMyFavoutiteListener,OnGetAllThingsListener{

	private int pageNum;
	private String lastItemTime;//当前列表结尾的条目的创建时间，
	
	private ArrayList<Things> mListItems;
	private PullToRefreshListView mPullRefreshListView;
	private ThingsContentAdapter mAdapter;
	private ListView actualListView;
	private UserLogic mUserLogic;
	private ThingsLogic mThingsLogic;
	
	private TextView networkTips;
	private ProgressBar progressbar;
	private List<Things> mMyCollectThings;
	
	public enum RefreshType{
		REFRESH,LOAD_MORE
	}
	private RefreshType mRefreshType = RefreshType.LOAD_MORE;
	
	private static final int LOADING = 1;
	private static final int LOADING_COMPLETED = 2;
	private static final int LOADING_FAILED =3;
	private static final int NORMAL = 4;
	
	public static BaseFragment newInstance(int index){
		BaseFragment fragment = new MainFragment();
		Bundle args = new Bundle();
		args.putInt("page",index);
		fragment.setArguments(args);
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
	
	public void fetchData(){
		setState(LOADING);
		LogUtils.i(TAG,"SIZE:"+Constant.NUMBERS_PER_PAGE*pageNum);
		mThingsLogic.getAllThings(pageNum++);
		LogUtils.i(TAG,"SIZE:"+Constant.NUMBERS_PER_PAGE*pageNum);
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
	
	private void isCollect(List<Things> collectThings,List<Things> allThings)
	{
		if (null == collectThings || collectThings.size() == 0) {
			return;
		}
		
		for (Things thing : allThings) {
			for (Things collectThing : collectThings) {
				if (collectThing.getObjectId().equals(thing.getObjectId())) {
					thing.setMyFav(true);
				}
			}
		}
	}
	
	private void getMyFavourite(){

		mThingsLogic.getMyFavouriteThings(-1);
	}
	
	@Override
	protected void setupViews(Bundle bundle) {
		//currentIndex = getArguments().getInt("page");
		pageNum = 0;
		lastItemTime = getCurrentTime();
		if(mListItems.size() == 0){
			getMyFavourite();
		}
		LogUtils.i(TAG,"curent time:"+lastItemTime);
	}

	@Override
	protected void initListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		mUserLogic = new UserLogic(mContext);
		mThingsLogic = new ThingsLogic(mContext);
		mThingsLogic.setOnGetMyFavoutiteListener(this);
		mThingsLogic.setOnGetAllThingsListener(this);
	}

	@Override
	public void onGetMyFavSuccess(List<Things> data) {
		LogUtils.i(TAG,"get fav success!"+data.size());
		if (null != data) {
			mMyCollectThings = data;
		}
		fetchData();
		
	}

	@Override
	public void onGetMyFavFailure(String msg) {
		ActivityUtils.toastShowBottom((Activity) mContext, "获取数据失败，请检查网络~");
		fetchData();
	}

	@Override
	public void onGetAllThingsSuccess(List<Things> data) {
		LogUtils.i(TAG,"find success."+data.size());
	
		if(data.size()!=0&&data.get(data.size()-1)!=null){
			if(mRefreshType==RefreshType.REFRESH){
				mListItems.clear();
			}
			if(data.size()<Constant.NUMBERS_PER_PAGE){
				LogUtils.i(TAG,"已加载完所有数据~");
			}
			if(mUserLogic.getCurrentUser()!=null){
				isCollect(mMyCollectThings, data);
			}
			mListItems.addAll(data);
			mAdapter.notifyDataSetChanged();
			
			setState(LOADING_COMPLETED);
			mPullRefreshListView.onRefreshComplete();
		}else{
			if(mRefreshType==RefreshType.REFRESH){
				mListItems.clear();
				mAdapter.notifyDataSetChanged();
			}
			ActivityUtils.toastShowBottom(getActivity(), "暂无更多数据~");
			pageNum--;
			setState(LOADING_COMPLETED);
			mPullRefreshListView.onRefreshComplete();
		}
	}

	@Override
	public void onGetAllThingsFailure(String msg) {
		LogUtils.i(TAG,"find failed."+msg);
		pageNum--;
		setState(LOADING_FAILED);
		mPullRefreshListView.onRefreshComplete();
	}

}
