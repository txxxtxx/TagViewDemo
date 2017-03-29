package com.zhe.library.tagview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.zhe.library.tagview.interfaces.ILayout;

/**
 * Created by zhe on 2017/3/28.
 */
public class TagsLayout extends FrameLayout implements ILayout {
    private Bitmap mBackground;

    public TagsLayout(@NonNull Context context) {
        this(context, null);
    }

    public TagsLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagsLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int currentWidth = 0;
        int currentHeight = 0;
        float scaleX = 1f;
        float scaleY = 1f;
        CutImageUtil cutImageUtil = new CutImageUtil();
        mBackground = cutImageUtil.getLocalBitmap("/storage/emulated/0/Pictures/JPEG_20170322_143934_.jpg");
        if (mBackground != null) {
            scaleX = widthMeasureSpec / mBackground.getWidth();
            scaleY = heightMeasureSpec / mBackground.getHeight();

            float scale = Math.min((float) heightMeasureSpec / (float) mBackground.getHeight(),
                    (float) widthMeasureSpec / (float) mBackground.getWidth());

            if (widthMeasureSpec > heightMeasureSpec) {
                currentHeight = heightMeasureSpec;
                currentWidth = (int) (mBackground.getWidth() * scale);
            }
        }
        setMeasuredDimension(
                Math.max(getSuggestedMinimumWidth(),
                        resolveSize(currentWidth,
                                widthMeasureSpec)),
                Math.max(getSuggestedMinimumHeight(),
                        resolveSize(currentHeight,
                                heightMeasureSpec)));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
