package ysn.com.stockview.widget.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author yangsanning
 * @ClassName RecyclerViewFlipper
 * @Description view 走马灯效果
 * @Date 2019/6/13
 * @History 2019/6/13 author: description:
 */
public class FlipperRecyclerView extends RecyclerView {

    AtomicBoolean isContinue = new AtomicBoolean(false);
    Thread thread = null;
    Handler handler;

    public FlipperRecyclerView(Context context) {
        super(context);
    }

    public FlipperRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlipperRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init() {
        handler = new Handler(msg -> {
            if (msg.what == 1) {
                //竖直滚动或者水平滚动，偏移值
                FlipperRecyclerView.this.scrollBy(1, 1);
            }
            return false;
        });

        if (thread == null) {
            thread = new Thread() {

                @Override
                public void run() {
                    while (isContinue.get()) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        msg.sendToTarget();
                    }
                    //退出循环时清理handler
                    handler = null;
                }
            };
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    /**
     * 开始滚动
     */
    public void start() {
        isContinue.set(true);
        init();
        thread.start();
    }

    /**
     * 停止滚动
     */
    public void stop() {
        isContinue.set(false);
        thread = null;
    }
}
