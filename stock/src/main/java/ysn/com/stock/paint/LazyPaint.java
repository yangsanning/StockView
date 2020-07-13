package ysn.com.stock.paint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;

import ysn.com.stock.function.OnSomeOneCallBack;

/**
 * @Author yangsanning
 * @ClassName LazyPaint
 * @Description 懒人画笔
 * @Date 2020/6/23
 */
public class LazyPaint {

    private LazyTextPaint lazyTextPaint = new LazyTextPaint();
    private LazyLinePaint lazyLinePaint = new LazyLinePaint();

    /**
     * 获取测量后的文本宽度
     */
    public int width(String text) {
        return lazyTextPaint.measure(text).width();
    }

    /**
     * 获取测量后的文本高度
     */
    public int height(String text) {
        return lazyTextPaint.measure(text).height();
    }

    /**
     * 测量文本
     */
    public void measure(String text, OnSomeOneCallBack<LazyTextPaint> callBack) {
        callBack.onCallBack(lazyTextPaint.measure(text));
    }

    /**
     * 设置字体颜色
     */
    public LazyPaint setTextColor(@ColorInt int color) {
        lazyTextPaint.setColor(color);
        return this;
    }

    /**
     * 设置字体大小
     */
    public LazyPaint setTextSize(float textSize) {
        lazyTextPaint.setTextSize(textSize);
        return this;
    }

    /**
     * 设置线颜色
     */
    public LazyPaint setLineColor(@ColorInt int color) {
        lazyLinePaint.setColor(color);
        return this;
    }

    /**
     * 设置线 Style
     */
    public LazyPaint setLineStyle(Paint.Style style) {
        lazyLinePaint.setStyle(style);
        return this;
    }

    /**
     * 设置线宽
     */
    public LazyPaint setLineStrokeWidth(float width) {
        lazyLinePaint.setStrokeWidth(width);
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
        lazyLinePaint.moveTo(x, y);
        return this;
    }

    /**
     * 设置下一个点
     */
    public LazyPaint lineTo(float x, float y) {
        lazyLinePaint.lineTo(x, y);
        return this;
    }

    /**
     * 重置路径
     */
    public LazyPaint resetPath() {
        lazyLinePaint.resetPath();
        return this;
    }

    /**
     * 绘制横线
     */
    public LazyPaint drawLine(Canvas canvas, float startX, float startY, float stopX, float stopY) {
        lazyLinePaint.drawLine(canvas, startX, startY, stopX, stopY);
        return this;
    }

    /**
     * 画圆
     */
    public LazyPaint drawCircle(Canvas canvas, float cx, float cy, float radius, @ColorInt int color) {
        lazyLinePaint.drawCircle(canvas, cx, cy, radius, color);
        return this;
    }

    /**
     * 绘制路径
     */
    public LazyPaint drawPath(Canvas canvas) {
        lazyLinePaint.drawPath(canvas);
        return this;
    }

    public Paint getTextPaint() {
        return lazyTextPaint.textPaint;
    }

    public LazyLinePaint getLazyLinePaint() {
        return lazyLinePaint;
    }
}
