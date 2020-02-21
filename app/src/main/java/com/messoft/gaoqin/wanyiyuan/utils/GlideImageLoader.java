package com.messoft.gaoqin.wanyiyuan.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.messoft.gaoqin.wanyiyuan.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by jingbin on 2016/11/30.
 * 首页轮播图
 */

public class GlideImageLoader extends ImageLoader {
    private long modified;

    public GlideImageLoader(long modified) {
        this.modified = modified;
    }

    @Override
    public void displayImage(Context context, Object url, ImageView imageView) {
        final RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.img_load_error_1)// 正在加载中的图片
                .error(R.drawable.img_load_error_1); // 加载失败的图片
        ImgUpdateOptions.updateOptions(options,String.valueOf(url) , modified);
        Glide.with(context).load(url)
                .apply(options)
                .into(imageView);
    }
}
