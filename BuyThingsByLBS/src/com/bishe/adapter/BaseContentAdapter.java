package com.bishe.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


/**
 * @author robin
 * 2015-4-6
 * Copyright 2015 The robin . All rights reserved
 */
public abstract class BaseContentAdapter<T> extends BaseAdapter{

	protected Context mContext;
	protected List<T> dataList ;
	protected LayoutInflater mInflater;
	
	
	
	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public BaseContentAdapter(Context context,List<T> list){
		mContext = context;
		dataList = list;
		mInflater = LayoutInflater.from(mContext);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		return getConvertView(position,convertView,parent);
	}
	
	public abstract View getConvertView(int position, View convertView, ViewGroup parent);

}