package ysn.com.stock.helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import ysn.com.stock.config.ProfitLossConfig;
import ysn.com.stock.interceptor.ProfitLossUnitInterceptor;
import ysn.com.stock.manager.ProfitLossDataManager;
import ysn.com.stock.paint.LazyPaint;
import ysn.com.stock.utils.LogUtils;
import ysn.com.stock.view.ProfitLossView;

/**
 * @Author yangsanning
 * @ClassName ProfitLossSlideHelper
 * @Description {@link ysn.com.stock.view.ProfitLossView} 滑动辅助类
 * @Date 2020/6/2
 */
public class ProfitLossSlideHelper {

    /**
     * 滑动的阈值
     */
    private static final int TOUCH_SLOP = 20;

    private ProfitLossView profitLossView;
    protected ProfitLossConfig config;
    protected ProfitLossDataManager dataManager;
    protected ProfitLossUnitInterceptor unitInterceptor;

    protected float slideX, slideY;
    protected int slideNum;
    protected Point point = new Point();

    private boolean isLongPress;
    private Runnable longPressRunnable = () -> {
        isLongPress = true;
        profitLossView.postInvalidate();
    };

    public ProfitLossSlideHelper(@NonNull ProfitLossView profitLossView) {
        this.config = profitLossView.getConfig();
        this.dataManager = profitLossView.getDataManager();
        this.profitLossView = profitLossView;
    }

    public void dispatchTouchEvent(MotionEvent event) {
        profitLossView.getParent().requestDisallowInterceptTouchEvent(isLongPress);
    }

    public boolean onTouchEvent(MotionEvent event) {
        /**
         * 为了方便计算, 这里也以圆点为中心
         * 圆点坐标更改: {@link ysn.com.stock.view.ProfitLossView#onDraw(Canvas)}
         */
        float x = event.getX() - config.circleX;
        float y = event.getY() - config.circleY;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                slideX = x;
                slideY = y;
                profitLossView.postDelayed(longPressRunnable, 800);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_MOVE:
                if (isLongPress) {
                    slideX = x;
                    slideY = y;
                    profitLossView.postInvalidate();
                } else {
                    if (Math.abs(slideX - x) > TOUCH_SLOP || Math.abs(slideY - y) > TOUCH_SLOP) {
                        profitLossView.removeCallbacks(longPressRunnable);
                        isLongPress = false;
                        profitLossView.postInvalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_OUTSIDE:
                isLongPress = false;
                profitLossView.postInvalidate();
                break;
            default:
                break;
        }
        return true;
    }

    public void draw(Canvas canvas) {
        if (isLongPress && dataManager.isNotEmpty()) {
            LogUtils.d("slideX: " + slideX + " slideY" + slideY + " slideNum" + slideNum);

            // 初始化滑动数据
            initSlideData();

            // 绘制点
            drawPoint(canvas);

            // 绘制文本区域
            drawSlideRectF(canvas);
        }
    }

    /**
     * 初始化滑动数据
     */
    private void initSlideData() {

        int dataSize = dataManager.dataSize();
        if (slideX > 0 && slideX < config.viewWidth) {
            slideNum = (int) (slideX / config.topTableWidth * dataSize);
        } else if (slideX <= 0) {
            slideNum = 0;
        } else if (slideX >= config.viewWidth) {
            slideNum = dataSize - 1;
        }
        if (slideNum >= dataSize) {
            slideNum = dataSize - 1;
        }

        point.x = (int) profitLossView.getX(slideNum);
        point.y = (int) profitLossView.getY(slideNum);
    }

    /**
     * 绘制点
     */
    private void drawPoint(Canvas canvas) {
        config.lazyPaint.setLineStyle(Paint.Style.FILL)
                .drawCircle(canvas, point.x, point.y, (config.pointRadius + 3), Color.WHITE)
                .drawCircle(canvas, point.x, point.y, (config.pointRadius), config.valueLineColor);
    }

    /**
     * 绘制文本区域
     *
     * @param canvas
     */
    private void drawSlideRectF(Canvas canvas) {
        LazyPaint lazyPaint = config.lazyPaint.setTextColor(config.slideTextColor);

        String time = dataManager.timesList.get(slideNum);
        String price;
        if (unitInterceptor == null) {
            price = String.valueOf(dataManager.getValue(slideNum));
        } else {
            price = unitInterceptor.slideValue(dataManager.getValue(slideNum));
        }

        int rectWidth = Math.max(lazyPaint.width(time), lazyPaint.width(price));

        RectF slideRectF = config.slideRectF;
        float textSize = config.xYTextSize;
        float haftTextSize = textSize / 2;

        float textX;
        // 当触摸点小于控件宽度中间值时，绘制区域在右边，反之同理
        if (Math.abs(point.x) > config.viewWidth / 2f) {
            slideRectF.right = point.x - haftTextSize;
            slideRectF.left = slideRectF.right - rectWidth - textSize * 1.5f;
            textX = slideRectF.left + haftTextSize;
        } else {
            slideRectF.left = point.x + haftTextSize;
            slideRectF.right = slideRectF.left + rectWidth + textSize * 1.5f;
            textX = slideRectF.left + haftTextSize;
        }

        float priceY, timeY;
        // 当触摸点大于控件高度中间值时，绘制区域在下方，反之同理
        if (Math.abs(point.y) > config.topTableHeight / 2) {
            slideRectF.top = point.y - haftTextSize;
            slideRectF.bottom = slideRectF.top + textSize * 3.5f;
            timeY = slideRectF.bottom - haftTextSize;
            priceY = slideRectF.bottom - 2 * textSize;
        } else {
            slideRectF.bottom = point.y + haftTextSize;
            slideRectF.top = slideRectF.bottom - textSize * 3.5f;
            timeY = slideRectF.bottom - haftTextSize;
            priceY = slideRectF.bottom - 2 * textSize;
        }

        canvas.drawRoundRect(slideRectF, 10, 10, config.slideBgPaint);
        canvas.drawText(time, textX, priceY, lazyPaint.textPaint);
        canvas.drawText(price, textX, timeY, lazyPaint.textPaint);
    }

    public void setUnitInterceptor(ProfitLossUnitInterceptor unitInterceptor) {
        this.unitInterceptor = unitInterceptor;
    }
}
