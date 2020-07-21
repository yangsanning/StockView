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
        resetStyle();
        linePaint.setAntiAlias(true);

        path = new Path();
    }

    private void resetStyle() {
        linePaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 设置线颜色
     */
    public LazyLinePaint setColor(@ColorInt int color) {
        linePaint.setColor(color);
        return this;
    }

    /**
     * 设置线 Style
     */
    public LazyLinePaint setStyle(Paint.Style style) {
        linePaint.setStyle(style);
        return this;
    }

    /**
     * 设置线宽
     *
     * @return
     */
    public LazyLinePaint setStrokeWidth(float width) {
        linePaint.setStrokeWidth(width);
        return this;
    }

    /**
     * 设置PathEffect
     */
    public LazyLinePaint setPathEffect(PathEffect pathEffect) {
        linePaint.setPathEffect(pathEffect);
        return this;
    }

    /**
     * 重置PathEffect
     */
    public LazyLinePaint resetPathEffect() {
        linePaint.setPathEffect(null);
        return this;
    }

    /**
     * 设置圆点为起点
     */
    public LazyLinePaint moreToCircle() {
        moveTo(0, 0);
        return this;
    }

    /**
     * 设置起点
     */
    public LazyLinePaint moveTo(float x, float y) {
        path.moveTo(x, y);
        return this;
    }

    /**
     * 设置下一个点
     */
    public LazyLinePaint lineTo(float x, float y) {
        path.lineTo(x, y);
        return this;
    }

    /**
     * 绘制曲线（横线）
     */
    public LazyLinePaint drawPath(Canvas canvas, float startX, float startY, float stopX, float stopY) {
        moveTo(startX, startY);
        return lineToEnd(canvas, stopX, stopY);
    }

    /**
     * 设置最后一个点并闭合曲线，设置完毕后进行绘制，绘制完成后进行路径重置
     */
    public LazyLinePaint lineToClose(Canvas canvas, float x, float y) {
        return lineToClose(canvas, x, y, linePaint);
    }

    /**
     * 设置最后一个点并闭合曲线，设置完毕后进行绘制，绘制完成后进行路径重置
     */
    public LazyLinePaint lineToClose(Canvas canvas, float x, float y, Paint linePaint) {
        path.lineTo(x, y);
        return finishLinePath(canvas, linePaint);
    }

    /**
     * 设置最后一个点，设置完毕后进行绘制，绘制完成后进行路径重置
     */
    public LazyLinePaint lineToEnd(Canvas canvas, float x, float y) {
        path.lineTo(x, y);
        return drawPathAndReset(canvas);
    }

    /**
     * 闭合并绘制路径，绘制完毕重置路径
     */
    public LazyLinePaint finishLinePath(Canvas canvas) {
        closePath();
        drawPath(canvas);
        return resetPath();
    }

    /**
     * 闭合并绘制路径，绘制完毕重置路径
     */
    public LazyLinePaint finishLinePath(Canvas canvas, Paint linePaint) {
        closePath();
        drawPath(canvas, linePaint);
        return resetPath();
    }

    /**
     * 绘制路径，绘制完毕重置路径
     */
    public LazyLinePaint drawPathAndReset(Canvas canvas) {
        drawPath(canvas);
        return resetPath();
    }

    /**
     * 闭合路径
     */
    public LazyLinePaint closePath() {
        path.close();
        return this;
    }

    /**
     * 绘制路径
     */
    public LazyLinePaint drawPath(Canvas canvas) {
        return drawPath(canvas, linePaint);
    }

    /**
     * 绘制路径
     */
    public LazyLinePaint drawPath(Canvas canvas, Paint linePaint) {
        canvas.drawPath(path, linePaint);
        return this;
    }

    /**
     * 重置路径
     */
    public LazyLinePaint resetPath() {
        path.reset();
        return this;
    }

    /**
     * 绘制横线
     */
    public LazyLinePaint drawLine(Canvas canvas, float startX, float startY, float stopX, float stopY) {
        canvas.drawLine(startX, startY, stopX, stopY, linePaint);
        return this;
    }

    /**
     * 绘制横线
     */
    public LazyLinePaint drawLines(Canvas canvas, float[] pts) {
        canvas.drawLines(pts, linePaint);
        return this;
    }

    /**
     * 画圆
     * 注意：这里已经设置了 Paint.Style.FILL ，绘制完成后会重置 Style
     */
    public LazyLinePaint drawCircle(Canvas canvas, float cx, float cy, float radius, @ColorInt int color) {
        setStyle(Paint.Style.FILL).setColor(color);
        canvas.drawCircle(cx, cy, radius, linePaint);
        resetStyle();
        return this;
    }

    /**
     * 绘制矩形
     * 注意：这里已经设置了 Paint.Style.FILL ，绘制完成后会重置 Style
     */
    public LazyLinePaint drawRect(Canvas canvas, float left, float top, float right, float bottom, @ColorInt int color) {
        setStyle(Paint.Style.FILL).setColor(color);
        canvas.drawRect(left, top, right, bottom, linePaint);
        resetStyle();
        return this;
    }

    /**
     * 绘制虚线
     * 注意：绘制完后会重置 PathEffect 和 路径
     */
    public LazyLinePaint drawDotted(Canvas canvas, float startX, float startY, float stopX, float stopY, PathEffect pathEffect) {
        return setPathEffect(pathEffect)
                .moveTo(startX, startY)
                .lineToEnd(canvas, stopX, stopY)
                .setPathEffect(null);
    }
}
