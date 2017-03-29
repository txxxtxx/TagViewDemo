package com.zhe.library.tagview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by zhe on 2016/11/15.
 */

public class CutImageUtil {

    /**
     * 根据文件路径获取图片的bitmap
     *
     * @param url
     * @return
     */
    public Bitmap getLocalBitmap(String url) {
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            opt.inPurgeable = true;
            opt.inInputShareable = true;
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis, null, opt);  ///把流转化为Bitmap图片
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存bitmap到文件
     *
     * @param context
     * @param saveUri
     * @param croppedImage
     * @param quality
     * @return
     */
    public boolean saveOutput(Context context, Uri saveUri, Bitmap croppedImage, int quality) {
        if (saveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = context.getContentResolver().openOutputStream(saveUri);
                if (outputStream != null) {
                    croppedImage.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                }
            } catch (FileNotFoundException e) {
                return false;
            } finally {
                closeSilently(outputStream);
            }
            return true;
        }
        return false;
    }

    /**
     * 关闭流
     *
     * @param c
     */
    private void closeSilently(Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (Throwable t) {
            // Do nothing
        }
    }

    /**
     * 获取随机文件名
     *
     * @return
     */
    public String getRandomFileName() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        Random random = new Random();
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数
        return "YIC" + str + rannum + ".jpg";// 当前时间
    }

    private float getRatio(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int rate = width - height;
        if (rate > 0) {
            return 4f / 3f;
        } else if (rate < 0) {
            return 3f / 4f;
        } else {
            return 1f;
        }
    }

    public int[] getRatios(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int rate = width - height;
        if (rate > 0) {
            return new int[]{4, 3};
        } else if (rate < 0) {
            return new int[]{3, 4};
        } else {
            return new int[]{1, 1};
        }
    }

    /**
     * 按正方形裁切图片
     *
     * @param bitmap
     * @param isRecycled
     * @return
     */
    public Bitmap ImageCrop(Bitmap bitmap, boolean isRecycled) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

        int retX = w > h ? (w - h) / 2 : 0;// 基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;

        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null,
                false);
        if (isRecycled && !bitmap.equals(bmp) && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bmp;
    }

    /**
     * 按照长宽比例裁剪
     *
     * @param bitmap
     * @param h
     * @param w
     * @param isRecycled
     * @return
     */
    public Bitmap ImageCrop(Bitmap bitmap, int w, int h, boolean isRecycled) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth(); // 得到图片的宽
        int height = bitmap.getHeight(); // 得到图片的高
        int retX, retY;
        int nw, nh;
        if (width > height) {
            if (height > width * w / h) {
                nw = width;
                nh = width * w / h;
                retX = 0;
                retY = (height - nh) / 2;
            } else {
                nw = height * h / w;
                nh = height;
                retX = (width - nw) / 2;
                retY = 0;
            }
        } else {
            if (width > height * w / h) {
                nh = height;
                nw = height * w / h;
                retY = 0;
                retX = (width - nw) / 2;
            } else {
                nh = width * h / w;
                nw = width;
                retY = (height - nh) / 2;
                retX = 0;
            }
        }
        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null, false);
        if (isRecycled && !bitmap.equals(bmp) && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bmp;
    }

    /**
     * 按照最大图片宽度缩放
     *
     * @param bitmap
     * @param width
     * @param isRecycled
     * @return
     */
    public Bitmap scale(Bitmap bitmap, int width, boolean isRecycled) {
        int bmpWidth = bitmap.getWidth();
        float scale = (float) width / (float) bmpWidth;
        return scaleBitmap(bitmap, scale, scale, isRecycled);
    }

    /**
     * 根据比例缩放bitmap
     *
     * @param bitmap
     * @param scaleX
     * @param scaleY
     * @param isRecycled
     * @return
     */
    public Bitmap scaleBitmap(Bitmap bitmap, float scaleX, float scaleY, boolean isRecycled) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY); //长和宽放大缩小的比例
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (isRecycled && !bitmap.equals(bmp) && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bmp;
    }
}
