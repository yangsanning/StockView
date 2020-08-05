package ysn.com.stock.paint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.support.annotation.ColorInt;

/**
 * @Author yangsanning
 * @ClassName LazyLinePaint
 * @Description 曲线绘制、横线绘制
 * 须知：绘制 path 的衍生方法，在绘制完成后都会重置 path
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

    /**
     * 设置线 Style
     */
    public LazyLinePaint setStyle(Paint.Style style) {
        linePaint.setStyle(style);
        return this;
    }

    /**
     * 重置 Style
     *
     * @return
     */
    public LazyLinePaint resetStyle() {
        linePaint.setStyle(Paint.Style.STROKE);
        return this;
    }

    /**
     * 设置线颜色
     */
    public LazyLinePaint setColor(@ColorInt int color) {
        linePaint.setColor(color);
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
     * 设置原点为曲线起点
     */
    public LazyLinePaint moreToOrigin() {
        return moveTo(0, 0);
    }

    /**
     * 设置曲线起点
     */
    public LazyLinePaint moveTo(float x, float y) {
        path.moveTo(x, y);
        return this;
    }

    /**
     * 设置曲线的下一个点
     */
    public LazyLinePaint lineTo(float x, float y) {
        path.lineTo(x, y);
        return this;
    }

    /**
     * 绘制曲线（横线）
     */
    public LazyLinePaint drawPath(Canvas canvas, float startX, float startY, float stopX, float stopY) {
        return moveTo(startX, startY).lineToFinish(canvas, stopX, stopY);
    }

    /**
     * 设置最后一个点，并在设置完毕后进行曲线绘制，且绘制完成后进行路径重置
     */
    public LazyLinePaint lineToFinish(Canvas canvas, float x, float y) {
        return lineTo(x, y).finishPath(canvas);
    }

    /**
     * 绘制路径，并在绘制完成后进行路径重置
     */
    public LazyLinePaint finishPath(Canvas canvas) {
        return drawPath(canvas).resetPath();
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
     * 绘制路径，并在绘制完成后进行路径重置
     */
    public LazyLinePaint finishPath(Canvas canvas, Paint linePaint) {
        return drawPath(canvas, linePaint).resetPath();
    }

    /**
     * 设置最后一个点，并在设置完毕后闭合曲线以及曲线绘制，且绘制完成后进行路径重置
     */
    public LazyLinePaint lineToClose(Canvas canvas, float x, float y) {
        return lineToClose(canvas, x, y, linePaint);
    }

    /**
     * 设置最后一个点，并在设置完毕后闭合曲线以及曲线绘制，且绘制完成后进行路径重置
     */
    public LazyLinePaint lineToClose(Canvas canvas, float x, float y, Paint linePaint) {
        return lineTo(x, y).closeAndFinishPath(canvas, linePaint);
    }

    /**
     * 闭合并绘制路径，绘制完毕重置路径
     */
    public LazyLinePaint closeAndFinishPath(Canvas canvas, Paint linePaint) {
        return closePath().finishPath(canvas, linePaint);
    }

    /**
     * 闭合路径
     */
    public LazyLinePaint closePath() {
        path.close();
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
     * 绘制横线
     */
    public LazyLinePaint drawLine(Canvas canvas, float startX, float startY, float stopX, float stopY) {
        canvas.drawLine(startX, startY, stopX, stopY, linePaint);
        return this;
    }

    /**
     * 画圆
     * 须知：这里已经设置了 Paint.Style.FILL ，绘制完成后会重置 Style
     */
    public LazyLinePaint drawCircle(Canvas canvas, float cx, float cy, float radius, @ColorInt int color) {
        setStyle(Paint.Style.FILL).setColor(color);
        canvas.drawCircle(cx, cy, radius, linePaint);
        return resetStyle();
    }

    /**
     * 绘制矩形
     * 须知：这里已经设置了 Paint.Style.FILL ，绘制完成后会重置 Style
     */
    public LazyLinePaint drawRect(Canvas canvas, float left, float top, float right, float bottom, @ColorInt int color) {
        setStyle(Paint.Style.FILL).setColor(color);
        canvas.drawRect(left, top, right, bottom, linePaint);
        resetStyle();
        return this;
    }

    /**
     * 绘制矩形
     * 须知：这里已经设置了 Paint.Style.FILL ，绘制完成后会重置 Style
     */
    public LazyLinePaint drawRect(Canvas canvas, RectF rect, @ColorInt int bgColor, @ColorInt int strokeColor) {
        setStyle(Paint.Style.FILL).setColor(bgColor);
        canvas.drawRect(rect, linePaint);
        resetStyle().setColor(strokeColor);
        canvas.drawRect(rect, linePaint);
        return this;
    }

    /**
     * 绘制虚线
     * 须知：绘制完后会重置 PathEffect 和 路径
     */
    public LazyLinePaint drawDotted(Canvas canvas, float startX, float startY, float stopX, float stopY, PathEffect pathEffect) {
        return setPathEffect(pathEffect)
                .drawPath(canvas, startX, startY, stopX, stopY)
                .setPathEffect(null);
    }
}
