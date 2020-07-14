package ysn.com.stock.helper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.view.MotionEvent;

import ysn.com.stock.R;
import ysn.com.stock.interceptor.FenShiUnitInterceptor;
import ysn.com.stock.manager.FenShiDataManager;
import ysn.com.stock.manager.FiveDayFenShiDataManager;
import ysn.com.stock.utils.NumberUtils;
import ysn.com.stock.view.FiveDayFenShiView;

/**
 * @Author yangsanning
 * @ClassName FiveDayFenShiSlideHelper
 * @Description 一句话概括作用
 * @Date 2020/5/11
 */
public class FiveDayFenShiSlideHelper {


    /**
     * 滑动的阈值
     */
    private static final int TOUCH_SLOP = 20;

    /**
     * FiveDayFenShiView 相关参数
     */
    private FiveDayFenShiView fiveDayFenShiView;
    private float viewWidth, viewHeight;
    private float timeTableHeight;
    private float textMargin, tableMargin;
    private float topTableHeight, topTableMaxY;
    private float bottomTableHeight, bottomTableMaxY, bottomTableMinY;
    private boolean hasBottomTable;
    private Paint textPaint;
    private Rect textRect;
    public float dataWidth;

    /**
     * 数据管理
     */
    private FiveDayFenShiDataManager fiveDayFenShiDataManager;
    private FenShiDataManager fenShiDataManager;

    private Paint slidePaint;
    private Paint slideAreaPaint;
    private Path path;

    private int slidePosition;
    private float slideX, slideY;
    private float textRectHalfHeight;
    private float slideLineY;
    private String slideValue;
    private int slideNum;

    private boolean isLongPress;
    private Runnable longPressRunnable = () -> {
        isLongPress = true;
        fiveDayFenShiView.postInvalidate();
    };

    /**
     * 分时单位转换拦截器
     */
    private FenShiUnitInterceptor fenShiUnitInterceptor;

    public FiveDayFenShiSlideHelper(FiveDayFenShiView fiveDayFenShiView, FiveDayFenShiDataManager fiveDayFenShiDataManager) {
        this.fiveDayFenShiView = fiveDayFenShiView;
        this.fiveDayFenShiDataManager = fiveDayFenShiDataManager;
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
        fiveDayFenShiView.getParent().requestDisallowInterceptTouchEvent(isLongPress);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();

        /**
         * 因圆点是(0,topTableHeight), 为了方便计算, 这里也以圆点为中心
         * 圆点坐标更改: {@link ysn.com.stock.view.StockView#onDraw(Canvas)}
         */
        float y = event.getY() - fiveDayFenShiView.getTopTableHeight();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                slideX = x;
                slideY = y;
                fiveDayFenShiView.postDelayed(longPressRunnable, 800);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_MOVE:
                if (isLongPress) {
                    slideX = x;
                    slideY = y;
                    fiveDayFenShiView.postInvalidate();
                } else {
                    if (Math.abs(slideX - x) > TOUCH_SLOP || Math.abs(slideY - y) > TOUCH_SLOP) {
                        fiveDayFenShiView.removeCallbacks(longPressRunnable);
                        isLongPress = false;
                        fiveDayFenShiView.postInvalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_OUTSIDE:
                isLongPress = false;
                fiveDayFenShiView.postInvalidate();
                break;
            default:
                break;
        }
        return true;
    }

    public void draw(Canvas canvas) {
        if (isLongPress) {
            // 初始化FenShiView相关参数
            initFenShiViewParam();

            // 初始化滑动数据
            initSlideData();

            if (fenShiDataManager.isPriceEmpty()) {
                return;
            }
            // 绘制滑动线
            drawSlideLine(canvas);

            // 绘制滑动时间
            drawSlideTime(canvas);

            // 绘制滑动值
            drawSlideValue(canvas);
        }
    }

    /**
     * 初始化FenShiView相关参数
     */
    private void initFenShiViewParam() {
        viewWidth = fiveDayFenShiView.getViewWidth();
        viewHeight = fiveDayFenShiView.getViewHeight();
        topTableHeight = fiveDayFenShiView.getTopTableHeight();
        topTableMaxY = fiveDayFenShiView.getTopTableMinY();
        timeTableHeight = fiveDayFenShiView.getTimeTableHeight();
        tableMargin = fiveDayFenShiView.getTableMargin();
        textMargin = fiveDayFenShiView.getXYTextMargin();

        hasBottomTable = fiveDayFenShiView.isEnabledBottomTable();
        if (hasBottomTable) {
            bottomTableHeight = fiveDayFenShiView.getBottomTableHeight();
            bottomTableMaxY = fiveDayFenShiView.getBottomTableMaxY();
            bottomTableMinY = fiveDayFenShiView.getBottomTableMinY();
        }

        textPaint = fiveDayFenShiView.getTextPaint();
        textRect = fiveDayFenShiView.getTextRect();

        // 滑动文本框一半高度
        textRectHalfHeight = timeTableHeight / 2;
    }

    /**
     * 初始化滑动数据
     */
    private void initSlideData() {
        int dataSize = fiveDayFenShiDataManager.dataManagerMap.size();

        if (slideX <= tableMargin) {
            slideX = 0;
            slideNum = 0;
            slidePosition = 0;
        } else {
            for (int i = 0; i < dataSize; i++) {
                if (i * dataWidth <= slideX && slideX < (i + 1) * dataWidth) {
                    slidePosition = i;
                    break;
                }
            }
        }

        for (int i = slidePosition; i < dataSize; i++) {
            fenShiDataManager = fiveDayFenShiDataManager.dataManagerMap.get(i);
            if (fenShiDataManager.isPriceNoEmpty()) {
                float minSlideArea = i * dataWidth;
                slideX = (i - slidePosition) == 0 ? slideX : minSlideArea;
                slideNum = (int) ((slideX - minSlideArea) / dataWidth * fiveDayFenShiView.getTotalCount(fenShiDataManager));
                slidePosition = i;
                break;
            }
        }

        int priceSize = fenShiDataManager.priceSize();

        if (slideNum >= priceSize) {
            slideNum = priceSize - 1;
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
            slideValue = getSlipVolume();
        } else {
            // 滑动线Y坐标
            if (slideY > -textRectHalfHeight) {
                slideLineY = -textRectHalfHeight;
            } else {
                slideLineY = Math.max(slideY, topTableMaxY + textRectHalfHeight);
            }

            // 滑动显示的值
            slideValue = getSlipPrice();
        }
    }

    /**
     * 绘制滑动线
     */
    private void drawSlideLine(Canvas canvas) {
        float lineX = Math.min(getX(slideNum), viewWidth - tableMargin);
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
        String timeText = fenShiDataManager.getTime(slideNum);
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
        canvas.drawPath(path, slidePaint);

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
        canvas.drawPath(path, slidePaint);

        // 绘制文本
        canvas.drawText(slideValue, (slideRectLeft + (tableMargin + 1) * 4), (slideLineY + textRect.height() / 2f), textPaint);
    }

    /**
     * 获取滑动价格
     */
    private String getSlipPrice() {
        float slipPrice;
        if (slideY < topTableMaxY) {
            slipPrice = fiveDayFenShiDataManager.maxPrice;
        } else if (slideY > -textRectHalfHeight) {
            slipPrice = fiveDayFenShiDataManager.minPrice;
        } else {
            slipPrice = (Math.abs(slideY) * (fiveDayFenShiDataManager.maxPrice
                    - fiveDayFenShiDataManager.minPrice)) / topTableHeight + fiveDayFenShiDataManager.minPrice;
        }
        return fenShiUnitInterceptor == null ? NumberUtils.decimalFormat(slipPrice) : fenShiUnitInterceptor.slipPrice(slipPrice);
    }

    /**
     * 滑动成交量
     */
    private String getSlipVolume() {
        float slipVolume;
        if (slideY < bottomTableMinY) {
            slipVolume = fiveDayFenShiDataManager.maxVolume;
        } else if (slideY > bottomTableMaxY) {
            slipVolume = 0;
        } else {
            slipVolume = (bottomTableHeight - (slideY - bottomTableMinY)) / bottomTableHeight * fiveDayFenShiDataManager.maxVolume;
        }
        return fenShiUnitInterceptor == null ? NumberUtils.decimalFormat(slipVolume) : fenShiUnitInterceptor.slipVolume(slipVolume);
    }

    /**
     * 获取颜色
     */
    private int getColor(@ColorRes int colorRes) {
        return fiveDayFenShiView.getContext().getResources().getColor(colorRes);
    }

    /**
     * 根据点获取X坐标
     */
    private float getX(int slideNum) {
        return fiveDayFenShiView.getPriceX(fenShiDataManager, slideNum, slidePosition);
    }

    /**
     * 设置分时单位转换拦截器
     */
    public void setFenShiUnitInterceptor(FenShiUnitInterceptor fenShiUnitInterceptor) {
        this.fenShiUnitInterceptor = fenShiUnitInterceptor;
    }
}
