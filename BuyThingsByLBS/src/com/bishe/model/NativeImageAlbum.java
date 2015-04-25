package com.bishe.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 本地相册实体类
 * 
 */
public class NativeImageAlbum implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name; // 相册名字
	private String count; // 数量
	private int bitmap; // 相册第一张图片
	private String path; // 相册第一张图片path
	private long addDate; // 相册第一张图片添加的时间

	private List<NativeImageItem> bitList = new ArrayList<NativeImageItem>();

	public NativeImageAlbum() {
	}

	public NativeImageAlbum(String name, String count, int bitmap) {
		super();
		this.name = name;
		this.count = count;
		this.bitmap = bitmap;
	}

	public NativeImageAlbum(String name, String count,int bitmap,long addDate){
		this.name = name;
		this.count = count;
		this.bitmap = bitmap;
		this.addDate = addDate;
	}
	public long getAddDate() {
		return addDate;
	}

	public void setAddDate(long addDate) {
		this.addDate = addDate;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<NativeImageItem> getBitList() {
		return bitList;
	}

	public void setBitList(List<NativeImageItem> bitList) {
		this.bitList = bitList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public int getBitmap() {
		return bitmap;
	}

	public void setBitmap(int bitmap) {
		this.bitmap = bitmap;
	}

	@Override
	public String toString() {
		return "PhotoAibum [name=" + name + ", count=" + count + ", bitmap="
				+ bitmap + ", bitList=" + bitList +",addDate="+addDate+ "]";
	}
}
