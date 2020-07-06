package ysn.com.stock.paint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * @Author yangsanning
 * @ClassName LazyTextPaint
 * @Description 文本测量工具, 为保证调用 width()、height()时经过测量，创建了此类
 * @Date 2020/7/6
 */
public class LazyTextPaint {

    public Paint textPaint;
    public Rect textRect;
    public String text;

    public LazyTextPaint(Paint textPaint, Rect textRect) {
        this.textPaint = textPaint;
        this.textRect = textRect;
    }

    /**
     * 测量文本
     * 实际调用 {@link Paint#getTextBounds(String, int, int, Rect)}
     */
    public LazyTextPaint measure(String text) {
        this.text = text;
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
    public LazyTextPaint drawText(Canvas canvas, float x, float y) {
        canvas.drawText(text, x, y, textPaint);
        return this;
    }
}
