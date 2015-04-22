package com.bishe.model;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;

public class User extends BmobUser{
	
	private static final long serialVersionUID = -2426363854941257434L;
	
	private String signature;
	private BmobFile avatar;
	private BmobRelation favorite;
	private BmobGeoPoint location;
	private String sex;
	
	
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public BmobFile getAvatar() {
		return avatar;
	}
	public void setAvatar(BmobFile avatar) {
		this.avatar = avatar;
	}
	public BmobRelation getFavorite() {
		return favorite;
	}
	public void setFavorite(BmobRelation favorite) {
		this.favorite = favorite;
	}
	public BmobGeoPoint getLocation() {
		return location;
	}
	public void setLocation(BmobGeoPoint location) {
		this.location = location;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Override
	public String toString() {
		return  "User [signature=" + signature + ", avatar=" + avatar
				+ ", favorite=" + favorite + ", location=" + location
				+ ", sex=" + sex +"]";
	}
}
