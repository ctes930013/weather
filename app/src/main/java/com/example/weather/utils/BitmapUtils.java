package com.example.weather.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * 圖片Bitmap處理相關
 *
 */
public class BitmapUtils {

    /**
     * 旋轉圖片
     *
     * @param srcBitmap  目標來源Bitmap
     * @param degree  旋轉角度
     * @return Bitmap
     */
    public static Bitmap rotateBitmap(Bitmap srcBitmap, float
            degree) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        return Bitmap.createBitmap(srcBitmap, 0, 0,
                srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
    }
}