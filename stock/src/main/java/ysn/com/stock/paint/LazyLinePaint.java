package ysn.com.stock.paint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
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
     * 闭合并绘制路径，绘制完毕重置路径
     */
    public void finishPath(Canvas canvas) {
        closePath();
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
     * 画圆
     */
    public void drawCircle(Canvas canvas, float cx, float cy, float radius, @ColorInt int color) {
        linePaint.setColor(color);
        canvas.drawCircle(cx, cy, radius, linePaint);
    }
}
