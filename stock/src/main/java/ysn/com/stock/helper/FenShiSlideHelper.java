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
    private float maxStockVolume = 0.0f;

    private Paint slidePaint;
    private Paint slideAreaPaint;
    private Path path;

    private boolean isLongPress;
    private float slideX, slideY;
    private float textRectHalfHeight;
    private float slideLineY;
    private String slideValue;

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
            drawSlideLine(canvas);

            // 绘制滑动值
            drawSlideValue(canvas);
        }
    }

    /**
     * 初始化滑动数据
     */
    private void initSlideData() {
        // 文本框一半高度
        textRectHalfHeight = getTimeTableHeight() / 2;

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

        // 分别对有下表格情况以及没有下表格情况进程处理
        if (hasBottomTable() && slideY - getTopTableHeight() > 0) {
            // 滑动线Y坐标
            float realSlideY = slideY - getTopTableHeight();
            if (realSlideY <= getBottomTableMinY() + textRectHalfHeight) {
                slideLineY = getBottomTableMinY() + textRectHalfHeight;
            } else {
                slideLineY = Math.min(realSlideY, getBottomTableMaxY() - textRectHalfHeight);
            }

            // 滑动显示的值
            slideValue = getSlipPriceVolume();
        } else {
            // 滑动线Y坐标
            if (slideY <= textRectHalfHeight) {
                slideLineY = textRectHalfHeight - getTopTableHeight();
            } else if (slideY + textRectHalfHeight - getTopTableHeight() >= 0) {
                slideLineY = -textRectHalfHeight - 1;
            } else {
                slideLineY = slideY - getTopTableHeight();
            }

            // 滑动显示的值
            slideValue = getSlipPriceValue();
        }
    }

    /**
     * 绘制滑动线
     */
    private void drawSlideLine(Canvas canvas) {
        float lineX = getX(slideNum);
        //和绘制竖线
        canvas.drawLine(lineX, -getTopTableHeight(), lineX, getViewHeight(), slidePaint);
        // 绘制横线
        canvas.drawLine(getTableMargin(), slideLineY, (getViewWidth() - getTableMargin()), slideLineY, slidePaint);
    }

    /**
     * 绘制成滑动值
     */
    private void drawSlideValue(Canvas canvas) {
        Paint textPaint = getTextPaint();
        Rect textRect = new Rect();
        textPaint.getTextBounds(slideValue, 0, slideValue.length(), textRect);
        float slideRectTop = slideLineY - textRectHalfHeight;
        float slideRectBottom = slideLineY + textRectHalfHeight;
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
        path.reset();
        path.moveTo(slideRectLeft, slideRectTop);
        path.lineTo(slideRectRight, slideRectTop);
        path.lineTo(slideRectRight, slideRectBottom);
        path.lineTo(slideRectLeft, slideRectBottom);
        path.lineTo(slideRectLeft, slideRectTop);
        canvas.drawPath(path, textPaint);

        // 绘制文本
        canvas.drawText(slideValue, (slideRectLeft + (getTableMargin() + 1) * 4), (slideLineY + textRect.height() / 2f), textPaint);
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
     * 滑动成交量
     */
    private String getSlipPriceVolume() {
        float max = maxStockVolume / 100;
        if (slideY - getTopTableHeight() < getBottomTableMinY()) {
            return NumberUtils.decimalFormat(max);
        } else if (slideY >= getViewHeight()) {
            return NumberUtils.decimalFormat(0);
        }
        float bottomTableHeight = getBottomTableHeight();
        float h1 = bottomTableHeight - (slideY - getTopTableHeight() - getTimeTableHeight());
        float volumeY = h1 / bottomTableHeight;
        return NumberUtils.decimalFormat(volumeY * max);
    }

    /**
     * 获取表格宽
     */
    private int getViewWidth() {
        return fenShiView.getViewWidth();
    }

    /**
     * 获取表格高
     */
    private int getViewHeight() {
        return fenShiView.getViewHeight();
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
     * 获取下表格最小高度
     */
    private float getBottomTableMinY() {
        return fenShiView.getBottomTableMinY();
    }

    /**
     * 获取下表格最大高度
     */
    private float getBottomTableMaxY() {
        return fenShiView.getBottomTableMaxY();
    }

    public float getBottomTableHeight() {
        return fenShiView.getBottomTableHeight();
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

    public boolean hasBottomTable() {
        return fenShiView.hasBottomTable();
    }

    public void setPrice(List<Float> priceList, float maxStockPrice, float minStockPrice) {
        this.priceList = priceList;
        this.maxStockPrice = maxStockPrice;
        this.minStockPrice = minStockPrice;
    }

    public void setMaxStockVolume(float maxStockVolume) {
        this.maxStockVolume = maxStockVolume;
    }
}
