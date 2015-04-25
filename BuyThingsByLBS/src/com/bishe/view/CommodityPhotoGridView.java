package com.bishe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;
/**
 * 自定义的gridview 为了使gridview不能滑动
 */
public class CommodityPhotoGridView extends GridView {

	public CommodityPhotoGridView(Context context) {
		super(context);
	}

	public CommodityPhotoGridView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public CommodityPhotoGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 禁止GridView滑动
	 * 
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}

}
