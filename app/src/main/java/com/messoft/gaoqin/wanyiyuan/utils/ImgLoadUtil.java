package com.messoft.gaoqin.wanyiyuan.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.messoft.gaoqin.wanyiyuan.R;


/**
 * Created by Administrator on 2017/10/23 0023.
 * 图片加载工具类
 */

public class ImgLoadUtil {

    private static ImgLoadUtil instance;

    private ImgLoadUtil() {

    }

    private static ImgLoadUtil getInstance() {
        if (instance == null) {
            instance = new ImgLoadUtil();
        }
        return instance;
    }




    /**
     * 加载圆角图,暂时用到显示头像
     */
    public static void displayCircle(ImageView imageView, String imageUrl) {
        final RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.default_header)// 正在加载中的图片
                .error(R.drawable.default_header)  // 加载失败的图片
                .transform(new GlideCircleTransform(imageView.getContext()));

        Glide.with(imageView.getContext())
                .load(imageUrl)
                .apply(options)
                .into(imageView);
    }

    /**
     * 加载圆角图,暂时用到显示头像
     */
    public static void displayCircleById(ImageView imageView, int imageId) {
        final RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.default_header)// 正在加载中的图片
                .error(R.drawable.default_header) // 加载失败的图片
                .transform(new GlideCircleTransform(imageView.getContext()));
        Glide.with(imageView.getContext())
                .load(imageId)
                .apply(options)
                .into(imageView);
    }

    public static void displayEspImage(int RseId, ImageView imageView, int type) {
        final RequestOptions options = new RequestOptions()
                .placeholder(getDefaultPic(type))// 正在加载中的图片
                .error(getDefaultPic(type));
        Glide.with(imageView.getContext())
                .load(RseId)
                .apply(options)
                .into(imageView);
    }

    public static void displayEspImage(int RseId, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(RseId)
                .into(imageView);
    }

    public static void displayEspImage(String RseId, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(RseId)
                .into(imageView);
    }

    public static void displayEspImage(String url, ImageView imageView, int type) {
        final RequestOptions options = new RequestOptions()
                .placeholder(getDefaultPic(type))// 正在加载中的图片
                .error(getDefaultPic(type));
        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .into(imageView);
    }

    /**
     * 动态更新缓存
     */
    public static void displayEspImage(String url, ImageView imageView, int type,long modified) {
        final RequestOptions options = new RequestOptions()
                .placeholder(getDefaultPic(type))// 正在加载中的图片
                .error(getDefaultPic(type));
        ImgUpdateOptions.updateOptions(options,url,modified);
        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .into(imageView);
    }


    private static int getDefaultPic(int type) {
        switch (type) {
            case 0:// 默认头像
                return R.drawable.default_header;
            case 1:// 默认 短
                return R.drawable.img_load_error_0;
            case 2:// 默认 长
                return R.drawable.img_load_error_1;
            case 3:// 彩色头像(同意头像把)
                return R.drawable.default_header;
        }
        return R.drawable.img_load_error_0;
    }

}
