package ysn.com.stock.paint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;

/**
 * @Author yangsanning
 * @ClassName LazyTextPaint
 * @Description 懒人文本画笔
 * @Date 2020/7/6
 */
public class LazyTextPaint {

    public Paint textPaint;
    public Rect textRect;
    public String latestMeasureText;

    public LazyTextPaint() {
        textPaint = new Paint();
        // 设置抗锯齿
        textPaint.setAntiAlias(true);
        // 设置画笔模式为填充
        textPaint.setStyle(Paint.Style.FILL);
        // 设置字体居左
        textPaint.setTextAlign(Paint.Align.LEFT);

        textRect = new Rect();
    }

    /**
     * 设置字体颜色
     */
    public void setColor(@ColorInt int color) {
        textPaint.setColor(color);
    }

    /**
     * 设置字体大小
     */
    public void setTextSize(float textSize) {
        textPaint.setTextSize(textSize);
    }

    /**
     * 测量文本
     * 实际调用 {@link Paint#getTextBounds(String, int, int, Rect)}
     */
    public LazyTextPaint measure(String text) {
        this.latestMeasureText = text;
        textPaint.getTextBounds(text, 0, text.length(), textRect);
        return this;
    }

    /**
     * 获取测量后的文本宽度
     */
    public int width() {
        return textRect.width();
    }

    /**
     * 获取测量后的文本高度
     */
    public int height() {
        return textRect.height();
    }

    /**
     * 绘制文本
     */
    public void drawText(Canvas canvas, float x, float y) {
        canvas.drawText(latestMeasureText, x, y, textPaint);
    }
}
