package com.bishe.utils;

import java.util.ArrayList;

import android.app.Activity;

/**
 * @author robin
 * @date 2015-4-20
 * Copyright 2015 The robin . All rights reserved
 */
public class ActivityManagerUtils {

	private ArrayList<Activity> activityList = new ArrayList<Activity>();
	
	private static ActivityManagerUtils activityManagerUtils;
	
	private ActivityManagerUtils(){
		
	}
	
	public static ActivityManagerUtils getInstance(){
		if(null == activityManagerUtils){
			activityManagerUtils = new ActivityManagerUtils();
		}
		return activityManagerUtils;
	}
	
	public Activity getTopActivity(){
		return activityList.get(activityList.size()-1);
	}
	
	public void addActivity(Activity ac){
		activityList.add(ac);
	}
	
	public void removeAllActivity(){
		for(Activity ac:activityList){
			if(null != ac){
				if(!ac.isFinishing()){
					ac.finish();
				}
				ac = null;
			}
		}
		activityList.clear();
	}
}
