package ysn.com.stock.paint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
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
    public LazyTextPaint measure(String text) {
        return lazyTextPaint.measure(text);
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
     * 设置原点为曲线起点
     */
    public LazyPaint moreToOrigin() {
        lazyLinePaint.moreToOrigin();
        return this;
    }

    /**
     * 设置曲线起点
     */
    public LazyPaint moveTo(float x, float y) {
        lazyLinePaint.moveTo(x, y);
        return this;
    }

    /**
     * 设置曲线的下一个点
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
     * 设置最后一个点，并在设置完毕后进行曲线绘制，且绘制完成后进行路径重置
     */
    public LazyPaint lineToFinish(Canvas canvas, float x, float y) {
        lazyLinePaint.lineToFinish(canvas, x, y);
        return this;
    }

    /**
     * 绘制路径，并在绘制完成后进行路径重置
     */
    public LazyPaint finishPath(Canvas canvas) {
        lazyLinePaint.finishPath(canvas);
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
     * 设置最后一个点，并在设置完毕后闭合曲线以及曲线绘制，且绘制完成后进行路径重置
     */
    public LazyPaint lineToClose(Canvas canvas, float x, float y) {
        lazyLinePaint.lineToClose(canvas, x, y);
        return this;
    }

    /**
     * 设置最后一个点，并在设置完毕后闭合曲线以及曲线绘制，且绘制完成后进行路径重置
     */
    public LazyPaint lineToClose(Canvas canvas, float x, float y, Paint linePaint) {
        lazyLinePaint.lineToClose(canvas, x, y, linePaint);
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
     * 绘制矩形
     */
    public LazyPaint drawRect(Canvas canvas, float left, float top, float right, float bottom, @ColorInt int color) {
        lazyLinePaint.drawRect(canvas, left, top, right, bottom, color);
        return this;
    }

    /**
     * 绘制矩形
     * 须知：这里已经设置了 Paint.Style.FILL ，绘制完成后会重置 Style
     */
    public LazyPaint drawRect(Canvas canvas, RectF rect, @ColorInt int bgColor, @ColorInt int strokeColor) {
        lazyLinePaint.drawRect(canvas, rect, bgColor,strokeColor);
        return this;
    }

    /**
     * 绘制虚线
     * 注意：绘制完后会重置 PathEffect
     */
    public LazyPaint drawDotted(Canvas canvas, float startX, float startY, float stopX, float stopY, PathEffect pathEffect) {
        lazyLinePaint.drawDotted(canvas, startX, startY, stopX, stopY, pathEffect);
        return this;
    }

    public Paint getTextPaint() {
        return lazyTextPaint.textPaint;
    }

    public LazyLinePaint getLazyLinePaint() {
        return lazyLinePaint;
    }
}
