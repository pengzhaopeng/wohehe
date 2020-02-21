package com.messoft.gaoqin.wanyiyuan.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;

public class ImgUpdateOptions {
    /**
     * 刷新RequestOptions，解決Glide图片缓存导致不刷新问题
     *
     * @param options
     * @param url       图片地址
     * @param modified  图片修改时间
     */
    @SuppressLint("CheckResult")
    public static void updateOptions(RequestOptions options, String url, long modified){
        if(!TextUtils.isEmpty(url) && url.contains(".")){
            try{
                String tail = url.substring(url.lastIndexOf(".") + 1).toLowerCase();
                String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(tail);
                options.signature(new MediaStoreSignature(type, modified, 0));
            }catch (Exception e){}
        }
    }
}
