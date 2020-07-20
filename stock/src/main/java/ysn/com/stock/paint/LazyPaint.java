package ysn.com.stock.paint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PathEffect;
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
    public LazyPaint measure(String text, OnSomeOneCallBack<LazyTextPaint> callBack) {
        callBack.onCallBack(lazyTextPaint.measure(text));
        return this;
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
     * 设置线宽
     */
    public LazyPaint setPathEffect(PathEffect pathEffect) {
        lazyLinePaint.setPathEffect(pathEffect);
        return this;
    }

    /**
     * 重置PathEffect
     */
    public LazyPaint resetPathEffect() {
        lazyLinePaint.resetPathEffect();
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
     * 绘制曲线（横线）
     */
    public LazyPaint drawPath(Canvas canvas, float startX, float startY, float stopX, float stopY) {
        lazyLinePaint.drawPath(canvas, startX, startY, stopX, stopY);
        return this;
    }

    /**
     * 设置最后一个点并闭合曲线，设置完毕后进行绘制，绘制完成后进行路径重置
     */
    public LazyPaint lineToClose(Canvas canvas, float x, float y) {
        lazyLinePaint.lineToClose(canvas, x, y);
        return this;
    }

    /**
     * 设置最后一个点并闭合曲线，设置完毕后进行绘制，绘制完成后进行路径重置
     */
    public LazyPaint lineToClose(Canvas canvas, float x, float y, Paint linePaint) {
        lazyLinePaint.lineToClose(canvas, x, y, linePaint);
        return this;
    }

    /**
     * 设置最后一个点，设置完毕后进行绘制，绘制完成后进行路径重置
     */
    public LazyPaint lineToEnd(Canvas canvas, float x, float y) {
        lazyLinePaint.lineToEnd(canvas, x, y);
        return this;
    }

    /**
     * 闭合并绘制路径，绘制完毕重置路径
     */
    public LazyPaint finishLinePath(Canvas canvas) {
        lazyLinePaint.finishLinePath(canvas);
        return this;
    }

    /**
     * 绘制路径，绘制完毕重置路径
     */
    public LazyPaint drawAndRestPath(Canvas canvas) {
        lazyLinePaint.drawPathAndReset(canvas);
        return this;
    }

    /**
     * 闭合路径
     */
    public LazyPaint closePath() {
        lazyLinePaint.closePath();
        return this;
    }

    /**
     * 绘制路径
     */
    public LazyPaint drawPath(Canvas canvas) {
        lazyLinePaint.drawPath(canvas);
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
     * 绘制横线
     */
    public LazyPaint drawLines(Canvas canvas, float[] pts) {
        lazyLinePaint.drawLines(canvas, pts);
        return this;
    }

    /**
     * 画圆
     */
    public LazyPaint drawCircle(Canvas canvas, float cx, float cy, float radius, @ColorInt int color) {
        lazyLinePaint.drawCircle(canvas, cx, cy, radius, color);
        return this;
    }

    public Paint getTextPaint() {
        return lazyTextPaint.textPaint;
    }

    public LazyLinePaint getLazyLinePaint() {
        return lazyLinePaint;
    }
}
