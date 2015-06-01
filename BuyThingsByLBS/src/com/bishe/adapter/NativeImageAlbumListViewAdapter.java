package com.bishe.adapter;

import java.util.List;

import com.bishe.buythingsbylbs.R;
import com.bishe.model.NativeImageAlbum;
import com.bishe.utils.BitmapUtils;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NativeImageAlbumListViewAdapter extends BaseAdapter {
	private List<NativeImageAlbum> mAlbumList;
	private Context context;
	private ViewHolder holder;

	private ImageLoadingListener animateFirstListener = new BitmapUtils.AnimateFirstDisplayListener();
	
	public NativeImageAlbumListViewAdapter(List<NativeImageAlbum> list, Context context ) {
		this.mAlbumList = list;
		this.context = context;		
	}
	
	@Override
	public int getCount() {
		return mAlbumList.size();
	}

	@Override
	public Object getItem(int position) {
		return mAlbumList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;   
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = (RelativeLayout)LayoutInflater.from(context).inflate(R.layout.native_image_album_listview_item, null);
			holder = new ViewHolder();
			holder.ivAlbum = (ImageView)convertView.findViewById(R.id.img_photoalbum_item_image);
			holder.tvAlbumName = (TextView)convertView.findViewById(R.id.tv_photoalbum_item_name);
			holder.tvAlbumImageNum = (TextView)convertView.findViewById(R.id.tv_photoalbum_item_image_num);
			holder.chkAlbumIsSelect = (CheckBox)convertView.findViewById(R.id.chk_photoalbum_isselect);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		 NativeImageAlbum CommodityImageAlbum = mAlbumList.get(position);
		/** 通过ID 获取缩略图*/
//		Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), mAlbumList.get(position).getBitmap(), Thumbnails.MICRO_KIND, null);
//		holder.ivAlbum.setImageBitmap(bitmap);
		//Constants.imageLoader.displayImage("file://" + CommodityImageAlbum.getPath(), holder.ivAlbum, Constants.image_display_options, animateFirstListener);
		holder.tvAlbumName.setText(mAlbumList.get(position).getName());
		holder.tvAlbumImageNum.setText(mAlbumList.get(position).getCount()+"张");
		if (!holder.chkAlbumIsSelect.isChecked()) {
			holder.chkAlbumIsSelect.setChecked(false);
		}
		return convertView;
	}
	
	private  class ViewHolder{
		ImageView ivAlbum;
		TextView tvAlbumName;
		TextView tvAlbumImageNum;
		CheckBox chkAlbumIsSelect;
	}
}
