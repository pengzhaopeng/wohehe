package com.messoft.gaoqin.wanyiyuan.utils.dialog;

import android.content.Context;

/**
 *
 * HttpDialog
 */
public class HttpDialogUtils {

	/**
	 * 显示网络请求的dialog
	 *
	 * @param context
	 * @param canceledOnTouchOutside
	 * @param messageText
	 */
	public static void showDialog(Context context, boolean canceledOnTouchOutside, String messageText) {
		if (context != null) {
			CustomWaitDialogUtil.showWaitDialog(context, messageText, canceledOnTouchOutside);
//			if (TextUtils.isEmpty(messageText)) {
//				//菊花转圈
////				CustomWaitDialogUtil.showWaitDialog(context, canceledOnTouchOutside);
//				CustomWaitDialogUtil.showWaitDialog(context, messageText, canceledOnTouchOutside);
//			} else {
//				//美团转圈（不用）
////				CustomWaitDialogUtil.showWaitDialog(context, canceledOnTouchOutside);
//				CustomWaitDialogUtil.showWaitDialog(context, messageText, canceledOnTouchOutside);
//			}
		}
	}

	public static void dismissDialog() {
		CustomWaitDialogUtil.stopWaitDialog();
	}

}
