package ysn.com.stock.paint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;

/**
 * @Author yangsanning
 * @ClassName LazyTextPaint
 * @Description 懒人文本画笔
 * @Date 2020/7/6
 */
public class LazyTextPaint {

    public Paint textPaint;
    public Rect textRect;
    public String latestMeasureText;

    public LazyTextPaint() {
        textPaint = new Paint();
        // 设置抗锯齿
        textPaint.setAntiAlias(true);
        // 设置画笔模式为填充
        textPaint.setStyle(Paint.Style.FILL);
        // 设置字体居左
        textPaint.setTextAlign(Paint.Align.LEFT);

        textRect = new Rect();
    }

    /**
     * 设置字体颜色
     *
     * @return
     */
    public LazyTextPaint setColor(@ColorInt int color) {
        textPaint.setColor(color);
        return this;
    }

    /**
     * 设置字体大小
     */
    public void setTextSize(float textSize) {
        textPaint.setTextSize(textSize);
    }

    /**
     * 测量文本
     * 实际调用 {@link Paint#getTextBounds(String, int, int, Rect)}
     */
    public LazyTextPaint measure(String text) {
        this.latestMeasureText = text;
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
     * 获取文字在指定容器水平居中的X坐标
     *
     * @param containerWidth 容器宽
     */
    public float centerX(float containerWidth) {
        return (containerWidth - textRect.width()) / 2f;
    }

    /**
     * 获取文字在指定容器垂直居中的Y坐标
     *
     * @param containerHeight 容器高
     */
    public float centerY(float containerHeight) {
        return (containerHeight + textRect.height()) / 2f;
    }

    /**
     * 绘制表格横向起始文本
     *
     * @param tableMinX 表格最小 X 坐标
     * @param xMargin   x 坐标间距
     * @param y         y 坐标
     */
    public void drawTableStartText(Canvas canvas, float tableMinX, float xMargin, float y) {
        drawText(canvas, (tableMinX + xMargin), y);
    }

    /**
     * 绘制表格横向居中文本
     *
     * @param tableMinX      表格最小 X 坐标
     * @param containerWidth 容器宽
     * @param y              y 坐标
     */
    public void drawTableCenterText(Canvas canvas, float tableMinX, float containerWidth, float y) {
        drawText(canvas, (tableMinX + centerX(containerWidth)), y);
    }

    /**
     * 绘制表格横向末端文本
     *
     * @param tableMaxX 表格最大 X 坐标
     * @param xMargin   x 坐标间距
     * @param y         y 坐标
     */
    public void drawTableEndText(Canvas canvas, float tableMaxX, float xMargin, float y) {
        drawText(canvas, (tableMaxX - width() - xMargin), y);
    }

    /**
     * 绘制文本
     */
    public void drawText(Canvas canvas, float x, float y) {
        canvas.drawText(latestMeasureText, x, y, textPaint);
    }
}
