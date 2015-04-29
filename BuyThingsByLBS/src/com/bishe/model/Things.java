package com.bishe.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

public class Things extends BmobObject {

	private static final long serialVersionUID = -6280656428527540320L;

	private User author;
	private String content;
	private ThingsImage thingsImage;
	private int share;
	private int comment;
	private boolean isBuy;
	private boolean myFav;
	private int price;
	private Location thingsLocation;
	private String locationName;
	private BmobRelation relation;

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price =price;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ThingsImage getThingsImage() {
		return thingsImage;
	}

	public void setThingsImage(ThingsImage thingsImage) {
		this.thingsImage = thingsImage;
	}

	public int getShare() {
		return share;
	}

	public void setShare(int share) {
		this.share = share;
	}

	public int getComment() {
		return comment;
	}

	public void setComment(int comment) {
		this.comment = comment;
	}

	public boolean isMyFav() {
		return myFav;
	}

	public void setMyFav(boolean myFav) {
		this.myFav = myFav;
	}

	public BmobRelation getRelation() {
		return relation;
	}

	public void setRelation(BmobRelation relation) {
		this.relation = relation;
	}

	public boolean isBuy() {
		return isBuy;
	}

	public void setBuy(boolean isBuy) {
		this.isBuy = isBuy;
	}
	public Location getThingsLocation() {
		return thingsLocation;
	}

	public void setThingsLocation(Location thingsLocation) {
		this.thingsLocation = thingsLocation;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	@Override
	public String toString() {
		return "Things [author=" + author.toString() + ", content=" + content
				+ ", thingsImage=" + thingsImage + ", price=" + price
				+ ", share=" + share + ", comment=" + comment + ", isBuy="
				+ isBuy + ", myFav=" + myFav + ", relation=" + relation + "]";
	}


}
