package ysn.com.stock.bridge;

import android.graphics.Canvas;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

/**
 * @Author yangsanning
 * @ClassName BaseSlideBridge
 * @Description 处理滑动事件
 * @Date 2020/8/3
 */
public abstract class BaseSlideBridge {

    /**
     * 滑动的阈值
     */
    private static final int TOUCH_SLOP = 20;

    /**
     * 长按默认时长
     */
    private static final int LONG_PRESS_DELAY_MILLIS = 800;

    public float slideX, slideY;
    public boolean isLongPress;

    private Handler handler = new Handler();
    private Runnable longPressRunnable = () -> {
        isLongPress = true;
        onRePaint();
    };

    private OnSlideBridgeListener onSlideBridgeListener;

    public BaseSlideBridge(OnSlideBridgeListener onSlideBridgeListener) {
        this.onSlideBridgeListener = onSlideBridgeListener;
    }

    public void dispatchTouchEvent(View view, MotionEvent ev) {
        view.getParent().requestDisallowInterceptTouchEvent(isLongPress);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        /**
         * 为了方便计算, 这里也以原点为中心
         * 原点坐标更改: {@link ysn.com.stock.view.base.StockView#onDraw(Canvas)}
         */
        float y = event.getY() - getOriginAbsoluteY();
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
                    onRePaint();
                } else {
                    if (Math.abs(slideX - x) > TOUCH_SLOP || Math.abs(slideY - y) > TOUCH_SLOP) {
                        handler.removeCallbacks(longPressRunnable);
                        isLongPress = false;
                        onRePaint();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_OUTSIDE:
                isLongPress = false;
                onRePaint();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 坐标原点Y的绝对坐标（下面坐标计算以原点为中心）
     */
    public abstract float getOriginAbsoluteY();

    public void onRePaint() {
        onSlideBridgeListener.onRePaint();
    }

    public interface OnSlideBridgeListener {

        void onRePaint();
    }
}
