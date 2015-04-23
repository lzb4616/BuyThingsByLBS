package com.bishe.ui.base;



import com.bishe.buythingsbylbs.R;
import com.bishe.config.Constant;

import cn.bmob.v3.Bmob;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

public abstract class BaseHomeActivity extends BaseActivity{
	
	
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		Bmob.initialize(this, Constant.BMOB_APP_ID);
		//getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initFragment();
	}

	private void initFragment(){
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.content_frame_base, getFragment())
				.commit();
	}
	
	protected abstract BaseFragment getFragment();
	
}
