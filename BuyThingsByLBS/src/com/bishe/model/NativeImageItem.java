package com.bishe.model;

import java.io.Serializable;
/**
 * 本地相片实体类
 * 
 * */
public class NativeImageItem implements Serializable {
	private static final long serialVersionUID = 8682674788506891598L;
	private int  imageID;//相片id
	private boolean select;//相片是否被选择
	private String path;//相片的本地路径
	private long addDate;//相片添加时间
	private int selecNum;//第几个被选择
	public NativeImageItem(int id,String path) {
		imageID = id;
		select = false;
		this.path=path;
	}
	
	public NativeImageItem(int id,String path,long addTime){
		this.imageID = id;
		this.select = false;
		this.path = path;
		this.addDate = addTime;
	}
	
	public NativeImageItem(int id,boolean flag) {
		imageID = id;
		select = flag;
	}
	
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getimageID() {
		return imageID;
	}
	public void setimageID(int imageID) {
		this.imageID = imageID;
	}
	public boolean isSelect() {
		return select;
	}
	public void setSelect(boolean select) {
		this.select = select;
	}
	public long getAddDate() {
		return addDate;
	}

	public void setAddDate(long addDate) {
		this.addDate = addDate;
	}
	
	public int getSelecNum() {
		return selecNum;
	}

	public void setSelecNum(int selecNum) {
		this.selecNum = selecNum;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof NativeImageItem) {
			NativeImageItem item = (NativeImageItem) o;   
            return (this.imageID==(item.imageID))  
                    && this.path.equals(item.path); 
		}
		return super.equals(o);
	}

	@Override
	public String toString() {
		return "PhotoItem [imageID=" + imageID + ", select=" + select + "]";
	}


}
