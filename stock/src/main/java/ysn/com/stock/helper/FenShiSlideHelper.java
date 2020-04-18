package ysn.com.stock.helper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
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

    /**
     * FenShiView 相关参数
     */
    private FenShiView fenShiView;
    private float viewWidth, viewHeight;
    private float timeTableHeight;
    private float textMargin, tableMargin;
    private float topTableHeight, topTableMaxY;
    private float bottomTableHeight, bottomTableMaxY, bottomTableMinY;
    private boolean hasBottomTable;
    private int totalCount;
    private Paint textPaint;
    private Rect textRect;

    private List<Float> priceList;
    private List<String> timeList;
    private float maxStockPrice = 0.0f;
    private float minStockPrice = 0.0f;
    private float maxStockVolume = 0.0f;

    private Paint slidePaint;
    private Paint slideAreaPaint;
    private Path path;

    private float slideX, slideY;
    private float textRectHalfHeight;
    private float slideLineY;
    private String slideValue;
    private int slideNum;

    private boolean isLongPress;
    private Runnable longPressRunnable = () -> {
        isLongPress = true;
        fenShiView.postInvalidate();
    };

    public FenShiSlideHelper(FenShiView fenShiView) {
        this.fenShiView = fenShiView;
        initPaint();
    }

    private void initPaint() {
        slidePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        slidePaint.setColor(getColor(R.color.stock_slide_line));
        slidePaint.setStrokeWidth(2.0f);
        slidePaint.setStyle(Paint.Style.STROKE);

        slideAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        slideAreaPaint.setColor(getColor(R.color.stock_area_fq));
        slideAreaPaint.setStyle(Paint.Style.FILL);

        path = new Path();
    }

    public void dispatchTouchEvent(MotionEvent ev) {
        fenShiView.getParent().requestDisallowInterceptTouchEvent(isLongPress);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        /**
         * 因圆点是(0,topTableHeight), 为了方便计算, 这里也以圆点为中心
         * 圆点坐标更改: {@link ysn.com.stock.view.StockView#onDraw(Canvas)}
         */
        float y = event.getY() - fenShiView.getTopTableHeight();
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
            // 初始化FenShiView相关参数
            initFenShiViewParam();

            // 初始化滑动数据
            initSlideData();

            // 绘制滑动线
            drawSlideLine(canvas);

            // 绘制滑动时间
            drawSlideTime(canvas);

            // 绘制滑动值
            drawSlideValue(canvas);
        }
    }

    private void initFenShiViewParam() {
        viewWidth = fenShiView.getViewWidth();
        viewHeight = fenShiView.getViewHeight();
        topTableHeight = fenShiView.getTopTableHeight();
        topTableMaxY = fenShiView.getTopTableMaxY();
        timeTableHeight = fenShiView.getTimeTableHeight();
        tableMargin = fenShiView.getTableMargin();
        textMargin = fenShiView.getXYTextMargin();

        hasBottomTable = fenShiView.hasBottomTable();
        if (hasBottomTable) {
            bottomTableHeight = fenShiView.getBottomTableHeight();
            bottomTableMaxY = fenShiView.getBottomTableMaxY();
            bottomTableMinY = fenShiView.getBottomTableMinY();
        }

        totalCount = fenShiView.getTotalCount();
        textPaint = fenShiView.getTextPaint();
        textRect = fenShiView.getTextRect();
    }

    /**
     * 初始化滑动数据
     */
    private void initSlideData() {
        // 文本框一半高度
        textRectHalfHeight = timeTableHeight / 2;

        if (slideX - tableMargin > 0 && slideX < tableMargin + viewWidth) {
            slideNum = (int) ((slideX - tableMargin) / viewWidth * totalCount);
        } else if (slideX - tableMargin - 2 < 0) {
            slideNum = 0;
        } else if (slideX > tableMargin + 1 + viewWidth) {
            slideNum = priceList.size() - 1;
        }
        if (slideNum >= priceList.size()) {
            slideNum = priceList.size() - 1;
        }

        // 分别对有下表格情况以及没有下表格情况进程处理
        if (hasBottomTable && slideY > 0) {
            // 滑动线Y坐标
            if (slideY <= bottomTableMinY + textRectHalfHeight) {
                slideLineY = bottomTableMinY + textRectHalfHeight;
            } else {
                slideLineY = Math.min(slideY, bottomTableMaxY - textRectHalfHeight);
            }

            // 滑动显示的值
            slideValue = getSlipPriceVolume();
        } else {
            // 滑动线Y坐标
            if (slideY > -textRectHalfHeight) {
                slideLineY = -textRectHalfHeight;
            } else {
                slideLineY = Math.max(slideY, topTableMaxY + textRectHalfHeight);
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
        canvas.drawLine(lineX, -topTableHeight, lineX, viewHeight, slidePaint);
        // 绘制横线
        canvas.drawLine(tableMargin, slideLineY, (viewWidth - tableMargin), slideLineY, slidePaint);
    }

    /**
     * 绘制滑动时间
     */
    private void drawSlideTime(Canvas canvas) {
        textPaint.setColor(getColor(R.color.stock_text_title));
        String timeText = timeList.get(slideNum);
        textPaint.getTextBounds(timeText, 0, timeText.length(), textRect);

        float rectWidth = textRect.width() + textMargin * 4;
        float rectHalfWidth = rectWidth / 2;

        float slideRectLeft = getX(slideNum) - rectHalfWidth;
        if (slideX < tableMargin + rectHalfWidth) {
            slideRectLeft = tableMargin;
        } else if (slideX > viewWidth - tableMargin - rectHalfWidth) {
            slideRectLeft = viewWidth - tableMargin - rectWidth;
        }

        float slipPriceTop = 0;
        float slideRectBottom = timeTableHeight;
        float slideRectRight = slideRectLeft + rectWidth;

        canvas.drawRect(slideRectLeft, slipPriceTop, slideRectRight, slideRectBottom, slideAreaPaint);
        path.reset();
        path.moveTo(slideRectLeft, slipPriceTop);
        path.lineTo(slideRectRight, slipPriceTop);
        path.lineTo(slideRectRight, slideRectBottom);
        path.lineTo(slideRectLeft, slideRectBottom);
        path.lineTo(slideRectLeft, slipPriceTop);
        canvas.drawPath(path, textPaint);

        canvas.drawText(timeText, (slideRectLeft + textMargin * 2), ((slideRectBottom + textRect.height()) / 2f), textPaint);
    }

    /**
     * 绘制成滑动值
     */
    private void drawSlideValue(Canvas canvas) {
        textPaint.getTextBounds(slideValue, 0, slideValue.length(), textRect);
        float slideRectTop = slideLineY - textRectHalfHeight;
        float slideRectBottom = slideLineY + textRectHalfHeight;
        float slideRectLeft;
        float slideRectRight;
        if (slideX < viewWidth / 3f) {
            slideRectLeft = viewWidth - textRect.width() - (tableMargin + 1) * 11;
            slideRectRight = viewWidth - tableMargin - 1f;
        } else {
            slideRectLeft = tableMargin + 1;
            slideRectRight = slideRectLeft + textRect.width() + (tableMargin + 1f) * 11;
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
        canvas.drawText(slideValue, (slideRectLeft + (tableMargin + 1) * 4), (slideLineY + textRect.height() / 2f), textPaint);
    }

    /**
     * 获取滑动价格
     */
    private String getSlipPriceValue() {
        if (slideY < topTableMaxY) {
            return NumberUtils.decimalFormat(maxStockPrice);
        } else if (slideY > -textRectHalfHeight) {
            return NumberUtils.decimalFormat(minStockPrice);
        }
        float volumeY = (Math.abs(slideY) * (maxStockPrice - minStockPrice)) / topTableHeight + minStockPrice;
        return NumberUtils.decimalFormat(volumeY);
    }

    /**
     * 滑动成交量
     */
    private String getSlipPriceVolume() {
        float max = maxStockVolume / 100;
        if (slideY < bottomTableMinY) {
            return NumberUtils.decimalFormat(max);
        } else if (slideY > bottomTableMaxY) {
            return NumberUtils.decimalFormat(0);
        }
        float volumeY = (bottomTableHeight - (slideY - bottomTableMinY)) / bottomTableHeight * max;
        return NumberUtils.decimalFormat(volumeY);
    }

    /**
     * 获取颜色
     */
    private int getColor(@ColorRes int colorRes) {
        return fenShiView.getContext().getResources().getColor(colorRes);
    }

    /**
     * 根据点获取X坐标
     */
    private float getX(int slideNum) {
        return fenShiView.getX(slideNum);
    }

    public FenShiSlideHelper setPrice(List<Float> priceList, float maxStockPrice, float minStockPrice) {
        this.priceList = priceList;
        this.maxStockPrice = maxStockPrice;
        this.minStockPrice = minStockPrice;
        return this;
    }

    public FenShiSlideHelper setTimeList(List<String> timeList) {
        this.timeList = timeList;
        return this;
    }

    public FenShiSlideHelper setMaxStockVolume(float maxStockVolume) {
        this.maxStockVolume = maxStockVolume;
        return this;
    }
}
