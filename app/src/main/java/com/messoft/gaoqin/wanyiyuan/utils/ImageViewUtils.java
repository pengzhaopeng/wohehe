package com.messoft.gaoqin.wanyiyuan.utils;

import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2017/8/25 0025.
 * imageview处理
 */

public class ImageViewUtils {

    /**
     * imageview背景变灰
     */

    public static void setImgBgGray(ImageView imageView) {
        if (imageView == null) {
            return;
        }
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(colorFilter);
    }

    /**
     * 设置缩略图
     * @param bmp
     * @param needRecycle
     * @return
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
