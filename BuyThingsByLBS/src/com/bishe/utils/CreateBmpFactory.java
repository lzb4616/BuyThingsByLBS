package com.bishe.utils;

import java.io.File;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.widget.Toast;

public class CreateBmpFactory {

	public static final String TAG = "CreateBmpFactory";
	// ----------相机图片的业务相关
	private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择

	private Fragment fragment;
	private Activity activity;
	private File tempFile;

	/**
	 * 通过Fragment来实例化这个类，主要是初始化界面的高度。
	 * 
	 * @param fragment 
	 * 
	 * */
	public CreateBmpFactory(Fragment fragment) {
		super();
		this.fragment = fragment;
		WindowManager wm = (WindowManager) fragment.getActivity()
				.getSystemService("window");
		windowHeight = wm.getDefaultDisplay().getHeight();
		windowWidth = wm.getDefaultDisplay().getWidth();
	}

	int windowHeight;//当前视窗的高度
	int windowWidth;//当前视窗的宽度
	/**
	 * 通过Fragment来实例化这个类，主要是初始化界面的高度。
	 * 
	 * @param activity 
	 * 
	 * */
	public CreateBmpFactory(Activity activity) {
		super();
		this.activity = activity;
		WindowManager wm = (WindowManager) activity.getSystemService("window");
		windowHeight = wm.getDefaultDisplay().getHeight();
		windowWidth = wm.getDefaultDisplay().getWidth();
	}

	/**
	 * 打开本地图库
	 * */
	public void OpenGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		//通过startActivityForResult的方法打开系统程序，返回数据的标志是PHOTO_REQUEST_GALLERY
		if (fragment != null) {
			fragment.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
		} else {
			activity.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
		}
	}

	/**
	 * 打开照相机
	 * */
	public void OpenCamera() {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		//判断内存卡是否存在
		if (hasSdcard()) {
			//将图片保存在本地路径
			tempFile = new File(Environment.getExternalStorageDirectory(), UUID
					.randomUUID().toString() + ".png");
			Uri uri = Uri.fromFile(tempFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		}
		//通过startActivityForResult的方法打开系统程序，返回数据的标志是PHOTO_REQUEST_CAREMA
		if (fragment != null) {
			fragment.startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
		} else {
			activity.startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
		}
	}

	/**
	 * 将返回数据保存在本地路径
	 * @param requestCode 返回的请求码
	 * @param resultCode
	 * @param data 返回数据
	 * 
	 * @return filepath
	 * */
	public String getBitmapFilePath(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			//通过MediaStore类将数据的内容解析，具体选取哪个字段，可以查看MediaStore类
			if (data != null) {
				Uri uri = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = null;
				if (fragment != null) {
					cursor = fragment.getActivity().getContentResolver()
							.query(uri, filePathColumn, null, null, null);
				} else {
					cursor = activity.getContentResolver().query(uri,
							filePathColumn, null, null, null);
				}
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				return picturePath;
			}
		} else if (requestCode == PHOTO_REQUEST_CAREMA) {
			if (hasSdcard()) {
				return tempFile.getAbsolutePath();
			} else {
				if (fragment != null) {
					Toast.makeText(fragment.getActivity(), "未找到存储卡，无法存储照片！", 0)
							.show();
				} else {
					Toast.makeText(activity, "未找到存储卡，无法存储照片！", 0).show();
				}
			}
		}
		return null;
	}

	/**
	 * 将图片按显示器大小进行缩放
	 * @param picturePath 图片路径
	 * 
	 * @return 返回一个bitmap对象
	 * */
	public Bitmap getBitmapByOpt(String picturePath) {
		Options opt = new Options();
		//这个属性能够让BitmapFactory只是返回图片的属性，不会返回一个bitmap对象
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(picturePath, opt);
		int imgHeight = opt.outHeight;
		int imgWidth = opt.outWidth;
		int scaleX = imgWidth / windowWidth;
		int scaleY = imgHeight / windowHeight;
		int scale = 1;
		if (scaleX > scaleY & scaleY >= 1) {
			scale = scaleX;
		}
		if (scaleY > scaleX & scaleX >= 1) {
			scale = scaleY;
		}
		//设为false的时候就会返回一个bitmap对象了
		opt.inJustDecodeBounds = false;
		//设定缩放比
		opt.inSampleSize = scale;
		//设置解码的类型，这个类型比默认的节省一半内存，默认Config.ARGB_8888是有透明度解析
		opt.inPreferredConfig = Config.RGB_565;
		opt.inDither = true;
		//是否需要回收Bitmap对象所占用的堆内存
		opt.inPurgeable = true;
		//是否需要复用bitmap对象的所占用的内存
		opt.inInputShareable = true;
		return BitmapFactory.decodeFile(picturePath, opt);
	}

	public boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
}
