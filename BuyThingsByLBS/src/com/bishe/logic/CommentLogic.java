package com.bishe.logic;

import java.util.List;

import com.bishe.config.Constant;
import com.bishe.model.Comment;
import com.bishe.model.Things;
import com.bishe.utils.LogUtils;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import android.content.Context;

/**
 * @author robin
 * @date 2015-4-24 Copyright 2015 The robin . All rights reserved
 */
public class CommentLogic {

	public static final String TAG = "CommentLogic";

	private Context mContext;

	private UserLogic mUserLogic;

	public CommentLogic(Context context) {
		this.mContext = context;
		this.mUserLogic = new UserLogic(context);
	}

	public interface OnFetchCommnetListener {
		void onFetchCommnetSuccess(List<Comment> data);

		void onFetchCommnetFailure(String msg);
	}

	private OnFetchCommnetListener mFetchCommnetListener;

	public void setOnFetchCommnetListener(OnFetchCommnetListener commnetListener) {
		this.mFetchCommnetListener = commnetListener;
	}

	public void fetchComment(int pageNum, Things things) {
		BmobQuery<Comment> query = new BmobQuery<Comment>();
		query.addWhereRelatedTo("relation", new BmobPointer(things));
		query.include("user");
		query.order("-createdAt");
		query.setLimit(Constant.NUMBERS_PER_PAGE);
		query.setSkip(Constant.NUMBERS_PER_PAGE * (pageNum));
		query.findObjects(mContext, new FindListener<Comment>() {

			@Override
			public void onSuccess(List<Comment> data) {
				if (null == mFetchCommnetListener) {
					LogUtils.i(TAG,
							"FetchCommnetListener is null,you must set one!");
					return;
				}
				mFetchCommnetListener.onFetchCommnetSuccess(data);
			}

			@Override
			public void onError(int arg0, String arg1) {
				if (null == mFetchCommnetListener) {
					LogUtils.i(TAG,
							"FetchCommnetListener is null,you must set one!");
					return;
				}
				mFetchCommnetListener.onFetchCommnetFailure(arg1);
			}
		});
	}

	public interface OnPublishCommnetListener
	{
		void onPublishCommnetSuccess(Comment comment);
		void onPublishCommnetFailure(String msg);
	}
	
	private OnPublishCommnetListener mPublishCommnetListener;
	
	public void setOnPublishCommnetListener(OnPublishCommnetListener onPublishCommnetListener)
	{
		this.mPublishCommnetListener = onPublishCommnetListener;
	}
	
	public void publishComment(String content) {
		final Comment comment = new Comment();
		comment.setUser(mUserLogic.getCurrentUser());
		comment.setCommentContent(content);
		comment.save(mContext, new SaveListener() {

			@Override
			public void onSuccess() {
				if (null == mPublishCommnetListener) {
					LogUtils.i(TAG,
							"onPublishCommnetListener is null,you must set one!");
					return;
				}
				mPublishCommnetListener.onPublishCommnetSuccess(comment);
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				if (null == mPublishCommnetListener) {
					LogUtils.i(TAG,
							"onPublishCommnetListener is null,you must set one!");
					return;
				}
				mPublishCommnetListener.onPublishCommnetFailure(arg1);
			}
		});
	}
	
	
}
