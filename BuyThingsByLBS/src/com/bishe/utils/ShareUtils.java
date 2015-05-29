package com.bishe.utils;

import android.app.Activity;
import android.content.Context;
import cn.bmob.social.share.core.BMShareListener;
import cn.bmob.social.share.core.ErrorInfo;
import cn.bmob.social.share.core.data.BMPlatform;
import cn.bmob.social.share.core.data.ShareData;
import cn.bmob.social.share.view.BMShare;

import com.bishe.model.Things;

/**
 * @author robin
 * @date 2015-5-29
 * Copyright 2015 The robin . All rights reserved
 */
public class ShareUtils {

	
	public static void shareThings(final Context mContext,Things things) {
		// ShareData使用内容分享类型分享类型
		ShareData shareData = new ShareData();
		shareData.setTitle(things.getAuthor().getUsername());
		shareData.setDescription("BuyThingsByLBS分享");
 		shareData.setText(things.getContent());
		shareData.setTarget_url("http://buythingslbs.bmob.cn/");
		shareData.setImageUrl(things.getThingsImage().getFileUrl(mContext));
		
		BMShareListener whiteViewListener = new BMShareListener() {

			@Override
			public void onSuccess() {
				ActivityUtils.toastShowBottom((Activity)mContext, "分享成功");
			}

			@Override
			public void onPreShare() {
				ActivityUtils.toastShowBottom((Activity)mContext, "开始分享");
			}

			@Override
			public void onError(ErrorInfo error) {
				ActivityUtils.toastShowBottom((Activity)mContext, "分享失败"+error.getErrorMessage());
			}

			@Override
			public void onCancel() {
				ActivityUtils.toastShowBottom((Activity)mContext, "取消分享");
			}
		};
		
		BMShare share = new BMShare((Activity)mContext);
		share.setShareData(shareData);
		share.addListener(BMPlatform.PLATFORM_WECHAT, whiteViewListener);
		share.addListener(BMPlatform.PLATFORM_WECHATMOMENTS, whiteViewListener);
		share.addListener(BMPlatform.PLATFORM_SINAWEIBO, whiteViewListener);
		share.addListener(BMPlatform.PLATFORM_RENN, whiteViewListener);
		share.addListener(BMPlatform.PLATFORM_TENCENTWEIBO, whiteViewListener);
		share.addListener(BMPlatform.PLATFORM_QQ, whiteViewListener);
		share.addListener(BMPlatform.PLATFORM_QZONE, whiteViewListener);
		share.show();
	}
}
