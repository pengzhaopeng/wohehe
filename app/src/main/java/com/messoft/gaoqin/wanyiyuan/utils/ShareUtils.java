package com.messoft.gaoqin.wanyiyuan.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.wxapi.WechatShareManager;
import com.messoft.gaoqin.wanyiyuan.R;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;

import java.io.Serializable;

/**
 * Created by jingbin on 2016/12/28.
 */

public class ShareUtils implements Serializable {

    private static WechatShareManager mShareManager;

    public static void share(Context context, int stringRes) {
        share(context, context.getString(stringRes));
    }

    public static void shareImage(Context context, Uri uri, String title) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(shareIntent, title));
    }


    public static void share(Context context, String extraText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.action_share));
        intent.putExtra(Intent.EXTRA_TEXT, extraText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(
                Intent.createChooser(intent, context.getString(R.string.action_share)));
    }

    /**
     * 默认分享--web
     */
    public static void shareDefault(AppCompatActivity context, final String title,
                                    final String content, final String url, final int rsId,
                                    OperateListener operateListener) {
        mShareManager = WechatShareManager.getInstance(context);
        NiceDialog.init()
                .setLayoutId(R.layout.share_layout)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        //微信
                        holder.setOnClickListener(R.id.wechat, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //微信好友
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                                WechatShareManager.ShareContentWebpage shareContentWebpag =
                                        (WechatShareManager.ShareContentWebpage) mShareManager.getShareContentWebpag(title, content, url, rsId);
                                mShareManager.shareByWebchat(shareContentWebpag, WechatShareManager.WECHAT_SHARE_TYPE_TALK);
                            }
                        });
                        //朋友圈
                        holder.setOnClickListener(R.id.wechat_circle, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //朋友圈
//                                if (!mShareManager.isWebchatAvaliable()) {
//                                    ToastUtil.showShortToast("请先安装微信");
//                                    return;
//                                }
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                                WechatShareManager.ShareContentWebpage shareContentWebpag =
                                        (WechatShareManager.ShareContentWebpage) mShareManager.getShareContentWebpag(title, content, url, rsId);
                                mShareManager.shareByWebchat(shareContentWebpag, WechatShareManager.WECHAT_SHARE_TYPE_FRENDS);
                            }
                        });
                        //复制连接
                        holder.setOnClickListener(R.id.copy_link, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //复制连接
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                                if (operateListener != null) {
                                    if (StringUtils.isNoEmpty(url)) {
                                        operateListener.copyLink(url);
                                    }
                                }
                            }
                        });
                        //qq
                        holder.setOnClickListener(R.id.qq, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtil.showShortToast("分享功能敬请期待");
                            }
                        });
                        //qq空间
                        holder.setOnClickListener(R.id.qq_space, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtil.showShortToast("分享功能敬请期待");
                            }
                        });
                        //微博
                        holder.setOnClickListener(R.id.wb, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtil.showShortToast("分享功能敬请期待");
                            }
                        });
                    }
                })
                .setDimAmount(0.3f)
                .setShowBottom(true)
                .show(context.getSupportFragmentManager());

    }

    /**
     * 默认分享--web(带收藏和转发)
     */
    public static void shareDefaultMore(BaseActivity context, final String title, final String content, final String url, final int rsId ,
                                        int isCollection, //收藏状态 0-没有 1-有
                                        OperateNewsListener operateListener) {
        mShareManager = WechatShareManager.getInstance(context);
        com.othershe.nicedialog.NiceDialog.init()
                .setLayoutId(R.layout.share_news_layout)
                .setConvertListener(new com.othershe.nicedialog.ViewConvertListener() {
                    @Override
                    public void convertView(com.othershe.nicedialog.ViewHolder holder, final com.othershe.nicedialog.BaseNiceDialog dialog) {
                        @SuppressLint("CutPasteId") LinearLayout collect = holder.getConvertView().findViewById(R.id.collect);
                        //收藏图片
                        ImageView ivCollect = holder.getConvertView().findViewById(R.id.iv_collect);
                        TextView tvCollect = holder.getConvertView().findViewById(R.id.tv_collect);
                        if (isCollection==0) {
                            //未收藏
                            ivCollect.setBackgroundResource(R.drawable.collect);
                            tvCollect.setText("收藏");
                        }else {
                            //已收藏
                            ivCollect.setBackgroundResource(R.drawable.collect_selected);
                            tvCollect.setText("已收藏");
                        }
                        @SuppressLint("CutPasteId") LinearLayout zf = holder.getConvertView().findViewById(R.id.zf);
                        collect.setVisibility(View.VISIBLE);
                        zf.setVisibility(View.VISIBLE);
                        //微信
                        holder.setOnClickListener(R.id.wechat, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                                //微信好友
                                WechatShareManager.ShareContentWebpage shareContentWebpag =
                                        (WechatShareManager.ShareContentWebpage) mShareManager.getShareContentWebpag(title, content, url, rsId);
                                mShareManager.shareByWebchat(shareContentWebpag, WechatShareManager.WECHAT_SHARE_TYPE_TALK);
                            }
                        });
                        //朋友圈
                        holder.setOnClickListener(R.id.wechat_circle, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //朋友圈
//                                if (!mShareManager.isWebchatAvaliable()) {
//                                    ToastUtil.showShortToast("请先安装微信");
//                                    return;
//                                }
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                                WechatShareManager.ShareContentWebpage shareContentWebpag =
                                        (WechatShareManager.ShareContentWebpage) mShareManager.getShareContentWebpag(title, content, url, rsId);
                                mShareManager.shareByWebchat(shareContentWebpag, WechatShareManager.WECHAT_SHARE_TYPE_FRENDS);
                            }
                        });
                        //qq
                        holder.setOnClickListener(R.id.qq, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtil.showShortToast("分享功能敬请期待");
                            }
                        });
                        //qq空间
                        holder.setOnClickListener(R.id.qq_space, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtil.showShortToast("分享功能敬请期待");
                            }
                        });
                        //微博
                        holder.setOnClickListener(R.id.wb, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtil.showShortToast("分享功能敬请期待");
                            }
                        });
                        //收藏
                        holder.setOnClickListener(R.id.collect, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (operateListener != null) {
                                    String trim = tvCollect.getText().toString().trim();
                                    if (trim.equals("收藏")) {
                                        //未收藏
                                        operateListener.collect(ivCollect,tvCollect);
                                    }else {
                                        //已收藏
                                        operateListener.cancelCollect(ivCollect,tvCollect);
                                    }

                                }
                            }
                        });
                        //转发
                        holder.setOnClickListener(R.id.zf, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (operateListener != null) {
                                    operateListener.zf(dialog);
                                }
                            }
                        });
                    }
                })
                .setDimAmount(0.3f)
                .setShowBottom(true)
                .show(context.getSupportFragmentManager());

    }


    public interface OperateListener {
        void copyLink(String url);

    }

    public interface OperateNewsListener {

        void collect(ImageView iv, TextView tvCollect);

        void cancelCollect(ImageView iv, TextView tvCollect);

        void zf(com.othershe.nicedialog.BaseNiceDialog dialog);
    }


}
