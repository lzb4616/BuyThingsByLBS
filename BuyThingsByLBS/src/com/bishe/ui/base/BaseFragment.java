package com.bishe.ui.base;


import com.bishe.config.Constant;
import com.bishe.utils.Sputil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * @author robin
 * @date 2015-4-21
 * Copyright 2015 The robin . All rights reserved
 */
public class BaseFragment extends Fragment {
	public static String TAG;
	protected Activity mContext;
	protected Sputil sputil;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TAG = this.getClass().getSimpleName();
		mContext = getActivity();
		if(null == sputil){
			sputil = new Sputil(mContext, Constant.SP_NAME);
		}
	}
	
}
