package com.bishe.view;

import com.bishe.buythingsbylbs.R;
import com.bishe.config.DeviceSize;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CommodtiyImageGridItem extends RelativeLayout implements Checkable {
	private Context mContext;
	private boolean mCheck;
	private ImageView mImageView;
	public Integer mImageId;
	private BadgeView mBadgeView;

	public CommodtiyImageGridItem(Context context) {
		this(context, null, 0);
	}

	public CommodtiyImageGridItem(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CommodtiyImageGridItem(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		LayoutInflater.from(mContext).inflate(
				R.layout.native_image_gridview_item, this);
		mImageView = (ImageView) findViewById(R.id.photo_img_view);
		mBadgeView = new BadgeView(mContext, mImageView);
		mBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		mBadgeView.setTextSize(16);
		mBadgeView.toggle(true);

		DeviceSize ds = new DeviceSize(mContext);
		FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mImageView
				.getLayoutParams(); // 取控件mGrid当前的布局参数
		layoutParams.width = ds.getmWidth() / 3;
		layoutParams.height = ds.getmWidth() / 3;
		mImageView.setLayoutParams(layoutParams);
	}

	@Override
	public void setChecked(boolean checked) {
		mCheck = checked;
		if (checked) {
			mBadgeView.show();
			// 设置背景颜色变暗
//			this.setAlpha(0.5f);
			changeLight(mImageView, -80);
		} else {
			mBadgeView.hide();
			// 设置背景颜色变亮
			changeLight(mImageView, 0);
		}
	}
	private void changeLight(ImageView imageview, int brightness) {
		ColorMatrix matrix = new ColorMatrix();
		matrix.set(new float[] { 1, 0, 0, 0, 0, 0, 1, 0, 0,
				0, 0, 0, 1, 0, 0, 0, 0, 0, 1, brightness});
		imageview.setColorFilter(new ColorMatrixColorFilter(matrix));

	}

	public void setSelectOrder(int order) {
		mBadgeView.setText(String.valueOf(order));
	}

	@Override
	public boolean isChecked() {
		return mCheck;
	}

	@Override
	public void toggle() {
		setChecked(!mCheck);
	}

	public void setImgResID(int id) {
		if (mImageView != null) {
			mImageView.setBackgroundResource(id);
		}
	}

	public void SetBitmap(Bitmap bit) {
		if (mImageView != null) {
			mImageView.setImageBitmap(bit);
		}
	}

	public ImageView getImageView() {
		return mImageView;
	}

}
