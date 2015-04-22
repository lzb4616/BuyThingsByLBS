package com.bishe.model;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {

	private static final long serialVersionUID = 875337170759011280L;

	private User user;
	private String commentContent;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	
	@Override
	public String toString() {
		return  "User [user=" + user.toString() + ", commentContent=" + commentContent +"]";
	}
}
