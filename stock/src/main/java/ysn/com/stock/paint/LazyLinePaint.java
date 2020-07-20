package ysn.com.stock.paint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.annotation.ColorInt;

/**
 * @Author yangsanning
 * @ClassName LazyLinePaint
 * @Description 一句话概括作用
 * @Date 2020/7/13
 */
public class LazyLinePaint {

    public Paint linePaint;
    public Path path;

    public LazyLinePaint() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);

        path = new Path();
    }

    /**
     * 设置线颜色
     */
    public void setColor(@ColorInt int color) {
        linePaint.setColor(color);
    }

    /**
     * 设置线 Style
     */
    public void setStyle(Paint.Style style) {
        linePaint.setStyle(style);
    }

    /**
     * 设置线宽
     */
    public void setStrokeWidth(float width) {
        linePaint.setStrokeWidth(width);
    }

    /**
     * 设置PathEffect
     */
    public void setPathEffect(PathEffect pathEffect) {
        linePaint.setPathEffect(pathEffect);
    }

    /**
     * 重置PathEffect
     */
    public void resetPathEffect() {
        linePaint.setPathEffect(null);
    }

    /**
     * 设置圆点为起点
     */
    public void moreToCircle() {
        moveTo(0, 0);
    }

    /**
     * 设置起点
     */
    public void moveTo(float x, float y) {
        path.moveTo(x, y);
    }

    /**
     * 设置下一个点
     */
    public void lineTo(float x, float y) {
        path.lineTo(x, y);
    }

    /**
     * 绘制曲线（横线）
     */
    public void drawPath(Canvas canvas, float startX, float startY, float stopX, float stopY) {
        moveTo(startX, startY);
        lineToEnd(canvas, stopX, stopY);
    }

    /**
     * 设置最后一个点并闭合曲线，设置完毕后进行绘制，绘制完成后进行路径重置
     */
    public void lineToClose(Canvas canvas, float x, float y) {
        lineToClose(canvas, x, y, linePaint);
    }

    /**
     * 设置最后一个点并闭合曲线，设置完毕后进行绘制，绘制完成后进行路径重置
     */
    public void lineToClose(Canvas canvas, float x, float y, Paint linePaint) {
        path.lineTo(x, y);
        finishLinePath(canvas, linePaint);
    }

    /**
     * 设置最后一个点，设置完毕后进行绘制，绘制完成后进行路径重置
     */
    public void lineToEnd(Canvas canvas, float x, float y) {
        path.lineTo(x, y);
        drawPathAndReset(canvas);
    }

    /**
     * 闭合并绘制路径，绘制完毕重置路径
     */
    public void finishLinePath(Canvas canvas) {
        closePath();
        drawPath(canvas);
        resetPath();
    }

    /**
     * 闭合并绘制路径，绘制完毕重置路径
     */
    public void finishLinePath(Canvas canvas, Paint linePaint) {
        closePath();
        drawPath(canvas, linePaint);
        resetPath();
    }

    /**
     * 绘制路径，绘制完毕重置路径
     */
    public void drawPathAndReset(Canvas canvas) {
        drawPath(canvas);
        resetPath();
    }

    /**
     * 闭合路径
     */
    public void closePath() {
        path.close();
    }

    /**
     * 绘制路径
     */
    public void drawPath(Canvas canvas) {
        drawPath(canvas, linePaint);
    }

    /**
     * 绘制路径
     */
    public void drawPath(Canvas canvas, Paint linePaint) {
        canvas.drawPath(path, linePaint);
    }

    /**
     * 重置路径
     */
    public void resetPath() {
        path.reset();
    }

    /**
     * 绘制横线
     */
    public void drawLine(Canvas canvas, float startX, float startY, float stopX, float stopY) {
        canvas.drawLine(startX, startY, stopX, stopY, linePaint);
    }

    /**
     * 绘制横线
     */
    public void drawLines(Canvas canvas, float[] pts) {
        canvas.drawLines(pts, linePaint);
    }

    /**
     * 画圆
     */
    public void drawCircle(Canvas canvas, float cx, float cy, float radius, @ColorInt int color) {
        linePaint.setColor(color);
        canvas.drawCircle(cx, cy, radius, linePaint);
    }
}
