package ysn.com.stock.paint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.ColorInt;

import ysn.com.stock.function.OnSomeOneCallBack;

/**
 * @Author yangsanning
 * @ClassName LazyPaint
 * @Description 懒人画笔
 * @Date 2020/6/23
 */
public class LazyPaint {

    public Paint textPaint, linePaint;
    public Path path;
    public Rect textRect;
    public String text;

    private LazyTextPaint lazyTextHelper;

    public LazyPaint() {
        textPaint = new Paint();
        textRect = new Rect();
        // 设置抗锯齿
        textPaint.setAntiAlias(true);
        // 设置画笔模式为填充
        textPaint.setStyle(Paint.Style.FILL);
        // 设置字体居左
        textPaint.setTextAlign(Paint.Align.LEFT);
        lazyTextHelper = new LazyTextPaint(textPaint, textRect);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);

        path = new Path();
    }

    /**
     * 获取测量后的文本宽度
     */
    public int width(String text) {
        return lazyTextHelper.measure(text).width();
    }

    /**
     * 获取测量后的文本高度
     */
    public int height(String text) {
        return lazyTextHelper.measure(text).height();
    }

    /**
     * 测量文本
     */
    public void measure(String text, OnSomeOneCallBack<LazyTextPaint> callBack) {
        callBack.onCallBack(lazyTextHelper.measure(text));
    }

    /**
     * 设置字体颜色
     */
    public LazyPaint setTextColor(@ColorInt int color) {
        textPaint.setColor(color);
        return this;
    }

    /**
     * 设置字体大小
     */
    public LazyPaint setTextSize(float textSize) {
        textPaint.setTextSize(textSize);
        return this;
    }

    /**
     * 设置线颜色
     */
    public LazyPaint setLineColor(@ColorInt int color) {
        linePaint.setColor(color);
        return this;
    }

    /**
     * 设置线 Style
     */
    public LazyPaint setLineStyle(Paint.Style style) {
        linePaint.setStyle(style);
        return this;
    }

    /**
     * 设置线宽
     */
    public LazyPaint setLineStrokeWidth(float width) {
        linePaint.setStrokeWidth(width);
        return this;
    }


    /**
     * 设置圆点为起点
     */
    public LazyPaint moreToCircle() {
        return moveTo(0, 0);
    }

    /**
     * 设置起点
     */
    public LazyPaint moveTo(float x, float y) {
        path.moveTo(x, y);
        return this;
    }

    /**
     * 设置下一个点
     */
    public LazyPaint lineTo(float x, float y) {
        path.lineTo(x, y);
        return this;
    }

    /**
     * 重置路径
     */
    public LazyPaint resetPath() {
        path.reset();
        return this;
    }

    /**
     * 绘制横线
     */
    public LazyPaint drawLine(Canvas canvas, float startX, float startY, float stopX, float stopY) {
        canvas.drawLine(startX, startY, stopX, stopY, linePaint);
        return this;
    }

    /**
     * 画圆
     */
    public LazyPaint drawCircle(Canvas canvas, float cx, float cy, float radius, @ColorInt int color) {
        linePaint.setColor(color);
        canvas.drawCircle(cx, cy, radius, linePaint);
        return this;
    }

    /**
     * 绘制路径
     */
    public LazyPaint drawPath(Canvas canvas) {
        canvas.drawPath(path, linePaint);
        return this;
    }
}
