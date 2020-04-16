package ysn.com.stock.helper;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.List;

import ysn.com.stock.R;
import ysn.com.stock.utils.NumberUtils;
import ysn.com.stock.view.FenShiView;

/**
 * @Author yangsanning
 * @ClassName FenShiSlideHelper
 * @Description 滑动辅助类
 * @Date 2020/4/16
 */
public class FenShiSlideHelper {

    /**
     * 滑动的阈值
     */
    private static final int TOUCH_SLOP = 20;

    private FenShiView fenShiView;
    public List<Float> priceList;
    private float maxStockPrice = 0.0f;
    private float minStockPrice = 0.0f;

    private Paint slidePaint;
    private Paint slideAreaPaint;
    private Path path;

    private boolean isLongPress;
    private float slideX, slideY;

    private int slideNum;

    private Runnable longPressRunnable = () -> {
        isLongPress = true;
        fenShiView.postInvalidate();
    };

    public FenShiSlideHelper(FenShiView fenShiView) {
        this.fenShiView = fenShiView;
        initPaint();
    }

    private void initPaint() {
        Resources resources = fenShiView.getContext().getResources();

        slidePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        slidePaint.setColor(resources.getColor(R.color.stock_slide_line));
        slidePaint.setStrokeWidth(2.0f);
        slidePaint.setStyle(Paint.Style.STROKE);

        slideAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        slideAreaPaint.setColor(resources.getColor(R.color.stock_area_fq));
        slideAreaPaint.setStyle(Paint.Style.FILL);

        path = new Path();
    }

    public void dispatchTouchEvent(MotionEvent ev) {
        fenShiView.getParent().requestDisallowInterceptTouchEvent(isLongPress);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                slideX = x;
                slideY = y;
                fenShiView.postDelayed(longPressRunnable, 800);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_MOVE:
                if (isLongPress) {
                    slideX = x;
                    slideY = y;
                    fenShiView.postInvalidate();
                } else {
                    if (Math.abs(slideX - x) > TOUCH_SLOP || Math.abs(slideY - y) > TOUCH_SLOP) {
                        fenShiView.removeCallbacks(longPressRunnable);
                        isLongPress = false;
                        fenShiView.postInvalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_OUTSIDE:
                isLongPress = false;
                fenShiView.postInvalidate();
                break;
            default:
                break;
        }
        return true;
    }

    public void draw(Canvas canvas) {
        if (isLongPress && priceList != null) {
            // 初始化滑动数据
            initSlideData();

            // 绘制滑动线
            drawSlipLine(canvas);

            // 绘制滑动横线以及滑动价格
            drawSlipPrice(canvas);
        }
    }

    /**
     * 初始化滑动数据
     */
    private void initSlideData() {
        if (slideX - getTableMargin() > 0 && slideX < getTableMargin() + getViewWidth()) {
            slideNum = (int) ((slideX - getTableMargin()) / getViewWidth() * getTotalCount());
        } else if (slideX - getTableMargin() - 2 < 0) {
            slideNum = 0;
        } else if (slideX > getTableMargin() + 1 + getViewWidth()) {
            slideNum = priceList.size() - 1;
        }

        if (slideNum >= priceList.size()) {
            slideNum = priceList.size() - 1;
        }
    }

    /**
     * 绘制滑动线
     */
    private void drawSlipLine(Canvas canvas) {
        float lineX = getX(slideNum);
        canvas.drawLine(lineX, -getTopTableHeight(), lineX, 0, slidePaint);
    }

    /**
     * 绘制滑动横线以及滑动价格
     */
    private void drawSlipPrice(Canvas canvas) {
        float lineY;
        float textRectHalfHeight = getTimeTableHeight() / 2;
        if (slideY <= textRectHalfHeight) {
            lineY = textRectHalfHeight - getTopTableHeight();
        } else if (slideY + textRectHalfHeight - getTopTableHeight() >= 0) {
            lineY = -textRectHalfHeight - 1;
        } else {
            lineY = slideY - getTopTableHeight();
        }
        // 绘制横线
        canvas.drawLine(getTableMargin(), lineY, (getViewWidth() - getTableMargin()), lineY, slidePaint);

        Paint textPaint = getTextPaint();
        Rect textRect = new Rect();
        String slipPriceValue = getSlipPriceValue();
        textPaint.getTextBounds(slipPriceValue, 0, slipPriceValue.length(), textRect);
        float slideRectTop = lineY - textRectHalfHeight;
        float slideRectBottom = lineY + textRectHalfHeight;
        float slideRectLeft;
        float slideRectRight;
        if (slideX < getViewWidth() / 3f) {
            slideRectLeft = getViewWidth() - textRect.width() - (getTableMargin() + 1) * 11;
            slideRectRight = getViewWidth() - getTableMargin() - 1f;
        } else {
            slideRectLeft = getTableMargin() + 1;
            slideRectRight = slideRectLeft + textRect.width() + (getTableMargin() + 1f) * 11;
        }

        // 绘制背景
        canvas.drawRect(slideRectLeft, slideRectTop, slideRectRight, slideRectBottom, slideAreaPaint);

        // 绘制边框
        path.moveTo(slideRectLeft, slideRectTop);
        path.lineTo(slideRectRight, slideRectTop);
        path.lineTo(slideRectRight, slideRectBottom);
        path.lineTo(slideRectLeft, slideRectBottom);
        path.lineTo(slideRectLeft, slideRectTop);
        canvas.drawPath(path, textPaint);
        path.reset();

        // 绘制文本
        canvas.drawText(slipPriceValue, (slideRectLeft + (getTableMargin() + 1) * 4), (lineY + textRect.height() / 2f), textPaint);
    }

    /**
     * 获取滑动价格
     */
    private String getSlipPriceValue() {
        if (slideY <= getTitleTableHeight()) {
            return NumberUtils.decimalFormat(maxStockPrice);
        } else if (slideY >= getTitleTableHeight() + getTopTableHeight()) {
            return NumberUtils.decimalFormat(minStockPrice);
        }
        return NumberUtils.decimalFormat((maxStockPrice - (slideY - getTitleTableHeight()) / getTopTableHeight() * (maxStockPrice - minStockPrice)));
    }

    /**
     * 获取表格宽
     */
    private int getViewWidth() {
        return fenShiView.getViewWidth();
    }

    /**
     * 获取表格标题表格高
     */
    private float getTitleTableHeight() {
        return fenShiView.getTitleTableHeight();
    }

    /**
     * 获取上表格高
     */
    private float getTopTableHeight() {
        return fenShiView.getTopTableHeight();
    }

    /**
     * 获取时间表格高
     */
    private float getTimeTableHeight() {
        return fenShiView.getTimeTableHeight();
    }

    /**
     * 获取表格边距
     */
    private float getTableMargin() {
        return fenShiView.getTableMargin();
    }

    /**
     * 表格总点数
     */
    private int getTotalCount() {
        return fenShiView.getTotalCount();
    }

    /**
     * 根据点获取X坐标
     */
    private float getX(int slideNum) {
        return fenShiView.getX(slideNum);
    }

    /**
     * 获取文本画笔
     */
    public Paint getTextPaint() {
        return fenShiView.getTextPaint();
    }

    public Rect getTextRect() {
        return fenShiView.getTextRect();
    }

    public Path getPath() {
        return path;
    }

    public void setPrice(List<Float> priceList, float maxStockPrice, float minStockPrice) {
        this.priceList = priceList;
        this.maxStockPrice = maxStockPrice;
        this.minStockPrice = minStockPrice;
    }
}
