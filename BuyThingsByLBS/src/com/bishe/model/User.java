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
	private BmobRelation publish;
	private BmobRelation buyThing;
	private Location location;
	private Long phoneNum;
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
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Long getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(Long phoneNum) {
		this.phoneNum = phoneNum;
	}
	public BmobRelation getPublish() {
		return publish;
	}
	public void setPublish(BmobRelation publish) {
		this.publish = publish;
	}
	public BmobRelation getBuyThing() {
		return buyThing;
	}
	public void setBuyThing(BmobRelation buyThing) {
		this.buyThing = buyThing;
	}
	
	@Override
	public String toString() {
		return  "User [signature=" + signature + ", avatar=" + avatar
				+ ", favorite=" + favorite + ", location=" + location
				+ ", sex=" + sex +", phoneNum="+phoneNum+"]";
	}

}
