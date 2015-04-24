package com.bishe.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class Things extends BmobObject {

	private static final long serialVersionUID = -6280656428527540320L;

	private User author;
	private String content;
	private ThingsImage thingsImage;
	private int share;
	private int comment;
	private boolean isPass;
	private boolean myFav;// 收藏
	private int mPrice;
	private BmobRelation relation;

	public int getmPrice() {
		return mPrice;
	}

	public void setmPrice(int mPrice) {
		this.mPrice = mPrice;
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

	public boolean isPass() {
		return isPass;
	}

	public void setPass(boolean isPass) {
		this.isPass = isPass;
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

	@Override
	public String toString() {
		return "Things [author=" + author.toString() + ", content=" + content
				+ ", thingsImage=" + thingsImage + ", price=" + mPrice
				+ ", share=" + share + ", comment=" + comment + ", isPass="
				+ isPass + ", myFav=" + myFav + ", relation=" + relation + "]";
	}
}
