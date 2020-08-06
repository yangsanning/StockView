package ysn.com.stock.view.base;

import android.graphics.Canvas;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

/**
 * @Author LongPressSlideHelper
 * @ClassName LongPressHelper
 * @Description 处理长按滑动事件
 * @Date 2020/8/3
 */
public class LongPressSlideHelper {

    /**
     * 滑动的阈值
     */
    private static final int TOUCH_SLOP = 20;

    /**
     * 长按默认时长
     */
    private static final int LONG_PRESS_DELAY_MILLIS = 800;

    private float circleY;
    public float slideX, slideY;
    public boolean isLongPress;

    private Handler handler = new Handler();
    private Runnable longPressRunnable = () -> {
        isLongPress = true;
        onLongPressSlide();
    };

    private OnLongPressSlideListener onLongPressSlideListener;

    public LongPressSlideHelper(OnLongPressSlideListener onLongPressSlideListener) {
        this.onLongPressSlideListener = onLongPressSlideListener;
    }

    public void dispatchTouchEvent(View view, MotionEvent event) {
        view.getParent().requestDisallowInterceptTouchEvent(isLongPress);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        /**
         * 为了方便计算, 这里也以原点为中心
         * {@link ysn.com.stock.view.base.StockView#onDraw(Canvas)} 已经进行了原点坐标修改
         */
        float y = event.getY() - circleY;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                slideX = x;
                slideY = y;
                handler.postDelayed(longPressRunnable, LONG_PRESS_DELAY_MILLIS);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_MOVE:
                if (isLongPress) {
                    slideX = x;
                    slideY = y;
                    onLongPressSlide();
                } else {
                    if (Math.abs(slideX - x) > TOUCH_SLOP || Math.abs(slideY - y) > TOUCH_SLOP) {
                        handler.removeCallbacks(longPressRunnable);
                        isLongPress = false;
                        onLongPressSlide();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_OUTSIDE:
                isLongPress = false;
                onLongPressSlide();
                break;
            default:
                break;
        }
        return true;
    }

    public void setCircleY(float circleY) {
        this.circleY = circleY;
    }

    public void onLongPressSlide() {
        onLongPressSlideListener.onLongPressSlide(isLongPress, slideX, slideY);
    }

    public interface OnLongPressSlideListener {

        /**
         * 长按滑动回调
         *
         * @param isLongPress 是否长按
         * @param slideX      滑动x坐标
         * @param slideY      滑动y坐标（经过原点计算）
         */
        void onLongPressSlide(boolean isLongPress, float slideX, float slideY);
    }
}
