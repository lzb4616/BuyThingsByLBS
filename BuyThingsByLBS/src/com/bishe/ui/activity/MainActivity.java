package com.bishe.ui.activity;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Toast;


import com.bishe.buythingsbylbs.R;
import com.bishe.logic.UserLogic;
import com.bishe.model.User;
import com.bishe.ui.base.BasePageActivity;
import com.bishe.utils.LogUtils;

/**
 * @author robin
 * @date 2015-4-22
 * Copyright 2015 The robin . All rights reserved
 */
public class MainActivity extends BasePageActivity {

	private UserLogic mUserLogic;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_action_menu, menu);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        //当前用户登录
        User currentUser = mUserLogic.getCurrentUser();
        if (currentUser != null) {
            // 允许用户使用应用,即有了用户的唯一标识符，可以作为发布内容的字段
            String name = currentUser.getUsername();
            String email = currentUser.getEmail();
            LogUtils.i(TAG,"username:"+name+",email:"+email);
            redictToActivity(mContext, PublishThingsActivity.class, null);
        } else {
            // 缓存用户对象为空时， 可打开用户注册界面…
            Toast.makeText(MainActivity.this, "请先登录。",
                    Toast.LENGTH_SHORT).show();
            redictToActivity(mContext, LoginAndRegisterActivity.class, null);
        }
		return super.onOptionsItemSelected(item);
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
	protected void setLayoutView() {
		setContentView(R.layout.activity_main);
		mUserLogic = new UserLogic(mContext);
		setOverflowShowingAlways();		
	}

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setupViews(Bundle bundle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void fetchData() {
		// TODO Auto-generated method stub
		
	}

}
