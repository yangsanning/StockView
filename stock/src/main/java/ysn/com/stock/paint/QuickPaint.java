package ysn.com.stock.paint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;

import ysn.com.stock.function.Call;

/**
 * @Author yangsanning
 * @ClassName QuickPaint
 * @Description 懒人封装画笔
 * @Date 2020/6/23
 */
public class QuickPaint {

    public Paint textPaint, linePaint;
    public Rect textRect;
    public String text;

    private Measure measure;

    public QuickPaint() {
        textPaint = new Paint();
        textRect = new Rect();
        // 设置抗锯齿
        textPaint.setAntiAlias(true);
        // 设置画笔模式为填充
        textPaint.setStyle(Paint.Style.FILL);
        // 设置字体居左
        textPaint.setTextAlign(Paint.Align.LEFT);
        measure = new Measure(textPaint, textRect);

        linePaint = new Paint();
        linePaint.setStrokeWidth(1f);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
    }

    /**
     * 获取测量后的文本宽度
     */
    public int width(String text) {
        return measure.measure(text).width();
    }

    /**
     * 获取测量后的文本高度
     */
    public int height(String text) {
        return measure.measure(text).height();
    }

    /**
     * 测量文本
     */
    public void measure(String text, Call<Measure> call) {
        call.call(measure.measure(text));
    }

    /**
     * 设置字体颜色
     */
    public QuickPaint setTextColor(@ColorInt int color) {
        textPaint.setColor(color);
        return this;
    }

    /**
     * 设置字体大小
     */
    public QuickPaint setTextSize(float textSize) {
        textPaint.setTextSize(textSize);
        return this;
    }

    /**
     * 设置线颜色
     */
    public QuickPaint setLineColor(@ColorInt int color) {
        linePaint.setColor(color);
        return this;
    }

    /**
     * 绘制横线
     */
    public QuickPaint drawLine(Canvas canvas, float startX, float startY, float stopX, float stopY) {
        canvas.drawLine(startX, startY, stopX, stopY, linePaint);
        return this;
    }

    /**
     * 测量工具
     * 为保证调用 width()、height()时经过测量，创建了此类
     */
    public static class Measure {

        public Paint textPaint;
        public Rect textRect;
        public String text;

        public Measure(Paint textPaint, Rect textRect) {
            this.textPaint = textPaint;
            this.textRect = textRect;
        }

        /**
         * 测量文本
         * 实际调用 {@link Paint#getTextBounds(String, int, int, Rect)}
         */
        public Measure measure(String text) {
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
        public Measure drawText(Canvas canvas, float x, float y) {
            canvas.drawText(text, x, y, textPaint);
            return this;
        }
    }
}
