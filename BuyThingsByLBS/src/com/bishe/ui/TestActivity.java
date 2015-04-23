package com.bishe.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;

import com.bishe.ui.base.BaseFragment;
import com.bishe.ui.base.BaseHomeActivity;

/**
 * @author robin
 * @date 2015-4-23
 * Copyright 2015 The robin . All rights reserved
 */
public class TestActivity extends BaseHomeActivity {
	
	@Override
	protected BaseFragment getFragment() {
		return new TestFram();
	}
}
