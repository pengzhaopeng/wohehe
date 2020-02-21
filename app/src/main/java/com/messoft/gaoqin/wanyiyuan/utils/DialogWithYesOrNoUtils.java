package com.messoft.gaoqin.wanyiyuan.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class DialogWithYesOrNoUtils {

    private static DialogWithYesOrNoUtils instance = null;

    public static DialogWithYesOrNoUtils getInstance() {
        if (instance == null) {
            instance = new DialogWithYesOrNoUtils();
        }
        return instance;
    }

    private DialogWithYesOrNoUtils() {
    }

    public void showDialog(Context context, String titleInfo, String msg, String sureStr, final DialogWithYesOrNoUtils.DialogCallBack callBack) {
        AlertDialog.Builder alterDialog = new AlertDialog.Builder(context);
        if (titleInfo != null) {
            alterDialog.setTitle(titleInfo);
        }
        alterDialog.setMessage(msg);
        alterDialog.setCancelable(true);
        alterDialog.setPositiveButton(sureStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.exectEvent(dialog);
            }
        });
//        alterDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
        alterDialog.show();
    }

    public void showDialog(Context context, String titleInfo, String msg, final DialogWithYesOrNoUtils.DialogCallBack callBack) {
        AlertDialog.Builder alterDialog = new AlertDialog.Builder(context);
        if (titleInfo != null) {
            alterDialog.setTitle(titleInfo);
        }
        alterDialog.setMessage(msg);
        alterDialog.setCancelable(true);
        alterDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.exectEvent(dialog);
            }
        });
        alterDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alterDialog.show();
    }

    public void showDialog(Context context,
                           String titleInfo,
                           String msg,
                           String positiveTitle,
                           String negativeTitle,
                           final DialogWithYesOrNoUtils.DialogCallBack callBack) {
        AlertDialog.Builder alterDialog = new AlertDialog.Builder(context);
        if (titleInfo != null) {
            alterDialog.setTitle(titleInfo);
        }
        alterDialog.setMessage(msg);
        alterDialog.setCancelable(true);
        alterDialog.setPositiveButton(positiveTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.exectEvent(dialog);
            }
        });
        alterDialog.setNegativeButton(negativeTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.exectCancel(dialog);
//                dialog.cancel();
            }
        });
        alterDialog.show();
    }


    public interface DialogCallBack {
        void exectEvent(DialogInterface dialog);
        void exectCancel(DialogInterface dialog);
    }
}
