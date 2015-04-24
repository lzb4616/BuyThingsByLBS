package com.bishe.ui;


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
