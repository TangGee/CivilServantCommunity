package com.mdove.dependent.common.view.richspan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

/**
 * Created by MDove on 2019-11-2.
 */
public class CustomImageSpan extends ImageSpan {

    public static final int ALIGN_FONTCENTER = 2;
    private boolean useUnderLine = false;

    public CustomImageSpan(Drawable drawable, int verticalAlignment) {
        super(drawable, verticalAlignment);
    }

    public CustomImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }

    // 需要下划线，可以使用这个
    public CustomImageSpan(Drawable drawable, int verticalAlignment, boolean useUnderLine) {
        super(drawable, verticalAlignment);
        this.useUnderLine = useUnderLine;
    }

    public CustomImageSpan(Context context, int resourceId, int verticalAlignment) {
        this(createDrawable(context, resourceId), verticalAlignment);
    }

    private static Drawable createDrawable(Context context, int resourceId) {
        Drawable drawable = VectorDrawableCompat.create(context.getResources(), resourceId, context.getTheme());
        if (drawable == null) {
            drawable = ContextCompat.getDrawable(context, resourceId);
        }
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth() * 3 / 4, drawable.getIntrinsicHeight() * 3 / 4);

        return drawable;
    }

    int mColor = Color.BLUE;

    public void setTextColor(int color) {
        mColor = color;
    }

    public String mText = "";

    public String getmRealString() {
        return mRealString;
    }

    public void setmRealString(String mRealString) {
        this.mRealString = mRealString;
    }

    public String mRealString = "";

    public void setSpanText(String text) {
        mText = text;
    }

    Long id;

    private int spanBgColor = 0;                //设置span的背景颜色
    private boolean useSpanBgColor = false;     //是否设置背景色
    public void setSpanBgColor(int color) {
        useSpanBgColor = true;
        spanBgColor = color;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom,
                     Paint paint) {

        Drawable drawable = getDrawable();

        canvas.save();

        Paint.FontMetricsInt fm = paint.getFontMetricsInt();

        int transY = 0;

        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY = y - drawable.getBounds().bottom;
        } else if (mVerticalAlignment == ALIGN_FONTCENTER) {
            transY = ((y + fm.descent) + (y + fm.ascent)) / 2 - drawable.getBounds().bottom / 2;
        }
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();

        if(useSpanBgColor) {
            paint.setColor(spanBgColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect( x,
                y + paint.getFontMetrics().ascent,
                x + drawable.getBounds().right + paint.measureText(" " + mText + " "),
                y + paint.getFontMetrics().bottom + paint.getFontMetrics().leading, paint);
        }

        paint.setColor(mColor);

        if (useUnderLine) {
            int flags = paint.getFlags();
            paint.setFlags(flags | Paint.UNDERLINE_TEXT_FLAG);
            canvas.drawText(" " + mText, x + drawable.getBounds().right, y, paint);
            paint.setFlags(flags);
        } else {
            canvas.drawText(" " + mText, x + drawable.getBounds().right, y, paint);
        }
    }

    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        Drawable d = getDrawable();
        Rect rect = d.getBounds();
        return (int) (rect.width() + paint.measureText(" " + mText + " "));
    }
}
