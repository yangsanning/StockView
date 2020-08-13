package ysn.com.stock.view.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;

import ysn.com.stock.R;
import ysn.com.stock.paint.LazyTextPaint;

/**
 * @Author yangsanning
 * @ClassName GridSlideView
 * @Description 网格的滑动控件
 * @Date 2020/8/4
 */
public abstract class GridSlideView extends GridView implements LongPressSlideHelper.OnLongPressSlideListener {

    protected LongPressSlideHelper longPressHelper;

    protected float slideRectHeight, slideRectHalfHeight;
    protected float topTableMinY, topTableMaxY;
    protected float topTableWidth, topTableMaxX, topTableMinX;
    protected float bottomTableMinY, bottomTableMaxY;
    protected float timeTableMinY, timeTableMaxY;

    protected int slidePosition;
    protected float slideLineY;
    protected String slideText;
    protected RectF slideRectF = new RectF();
    protected RectF timeRectF = new RectF();

    public GridSlideView(Context context) {
        super(context);
    }

    public GridSlideView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GridSlideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GridSlideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init(AttributeSet attrs) {
        super.init(attrs);
        longPressHelper = new LongPressSlideHelper(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.topTableMaxX = getTopTableMaxX();
        this.topTableMinY = getTopTableMinY();
        this.topTableMinX = getTopTableMinX();
        this.topTableMaxY = getTopTableMaxY();
        this.bottomTableMinY = getBottomTableMinY();
        this.bottomTableMaxY = getBottomTableMaxY();
        this.timeTableMinY = getTimeTableMinY();
        this.timeTableMaxY = getTimeTableMaxY();
        this.slideRectHeight = getTimeTableHeight();

        this.topTableWidth = topTableMaxX - topTableMinX;
        this.topTableHeight = topTableMaxY - topTableMinY;
        this.slideRectHalfHeight = slideRectHeight / 2;

        longPressHelper.setCircleY(getCircleY());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isEnabledSlide()) {
            longPressHelper.dispatchTouchEvent(this, event);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabledSlide()) {
            return longPressHelper.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onLongPressSlide(boolean isLongPress, float slideX, float slideY) {
        invalidate();
    }

    /**
     * 绘制滑动相关
     */
    public void drawSlide(Canvas canvas, int dataSize, float topTableMaxValue, float topTableMinValue,
                          float bottomTableMaxValue, float bottomTableMinValue) {
        if (isEnabledSlide() && longPressHelper.isLongPress) {
            init(dataSize, getTotalCount(), topTableMaxValue, topTableMinValue, bottomTableMaxValue, bottomTableMinValue);

            // 绘制滑动线
            drawSlideLine(canvas);
            // 绘制滑动值
            drawSlideValue(canvas);
            // 绘制滑动时间
            drawSlideTime(canvas);
        }
    }

    /**
     * 计算出当前滑动位置，滑动值，滑动线的 y 坐标
     */
    public void init(int dataSize, int totalCount, float topTableMaxValue, float topTableMinValue,
                     float bottomTableMaxValue, float bottomTableMinValue) {
        if (longPressHelper.slideX > topTableMinX) {
            if (longPressHelper.slideX < topTableMaxX) {
                slidePosition = (int) ((longPressHelper.slideX - topTableMinX) / topTableWidth * totalCount);
            } else {
                slidePosition = dataSize - 1;
            }
        } else {
            slidePosition = 0;
        }

        if (slidePosition >= dataSize) {
            slidePosition = dataSize - 1;
        }

        float slideValue;
        if (longPressHelper.slideY > 0) {
            // 下表格滑动线Y坐标
            if (longPressHelper.slideY <= bottomTableMinY) {
                slideLineY = bottomTableMinY;
                slideValue = bottomTableMaxValue;
            } else if (longPressHelper.slideY >= bottomTableMaxY) {
                slideLineY = bottomTableMaxY;
                slideValue = bottomTableMinValue;
            } else {
                slideLineY = longPressHelper.slideY;
                slideValue = (bottomTableMaxValue - bottomTableMinValue) *
                        (bottomTableMaxY - slideLineY) / bottomTableHeight + bottomTableMinValue;
            }
            slideText = convertBottomSlideValue(slideValue);
        } else {
            // 上表格滑动线Y坐标
            if (longPressHelper.slideY <= topTableMinY) {
                slideLineY = topTableMinY;
                slideValue = topTableMaxValue;
            } else if (longPressHelper.slideY >= topTableMaxY) {
                slideLineY = topTableMaxY;
                slideValue = topTableMinValue;
            } else {
                slideLineY = longPressHelper.slideY;
                slideValue = (Math.abs(longPressHelper.slideY) * (topTableMaxValue - topTableMinValue)) / topTableHeight + topTableMinValue;
            }
            slideText = convertTopSlideValue(slideValue);
        }
    }

    /**
     * 绘制滑动线
     */
    private void drawSlideLine(Canvas canvas) {
        float lineX = Math.min(getX(slidePosition), getTopTableMaxX());
        lazyPaint.setLineColor(getColor(R.color.stock_slide_line))
                // 绘制竖线
                .drawLine(canvas, lineX, -topTableHeight, lineX, viewHeight)
                // 绘制横线
                .drawLine(canvas, tableMargin, slideLineY, (viewWidth - tableMargin), slideLineY);
    }

    /**
     * 绘制成滑动值
     */
    private void drawSlideValue(Canvas canvas) {
        LazyTextPaint lazyTextPaint = lazyPaint.measure(slideText);
        initSlideRectF(lazyTextPaint.width());
        // 绘制背景以及边框
        lazyPaint.drawRect(canvas, getColor(R.color.stock_area_fq), getColor(R.color.stock_slide_line), slideRectF);

        // 绘制相应值
        float containerWidth = slideRectF.right - slideRectF.left;
        float y = slideRectF.bottom - (slideRectHeight - lazyTextPaint.height()) / 2;
        lazyTextPaint.drawTableCenterText(canvas, getColor(R.color.stock_text_title), slideRectF.left, containerWidth, y);
    }

    /**
     * 根据文本宽度获取滑动区域  RectF
     *
     * @param textWidth 文本宽度
     */
    public void initSlideRectF(float textWidth) {
        if (longPressHelper.slideY > 0) {
            if (slideLineY < bottomTableMinY + slideRectHalfHeight) {
                slideRectF.top = bottomTableMinY;
            } else if (slideLineY > bottomTableMaxY - slideRectHalfHeight) {
                slideRectF.top = bottomTableMaxY - slideRectHeight;
            } else {
                slideRectF.top = slideLineY - slideRectHalfHeight;
            }
        } else {
            if (slideLineY < topTableMinY + slideRectHalfHeight) {
                slideRectF.top = topTableMinY;
            } else if (slideLineY > topTableMaxY - slideRectHalfHeight) {
                slideRectF.top = topTableMaxY - slideRectHeight;
            } else {
                slideRectF.top = slideLineY - slideRectHalfHeight;
            }
        }

        slideRectF.bottom = slideRectF.top + slideRectHeight;
        if (longPressHelper.slideX < topTableWidth / 3f) {
            slideRectF.right = topTableMaxX - 1;
            slideRectF.left = slideRectF.right - textWidth - xYTextMargin * 4;
        } else {
            slideRectF.left = topTableMinX + 1;
            slideRectF.right = slideRectF.left + textWidth + xYTextMargin * 4;
        }
    }

    /**
     * 绘制滑动时间
     */
    private void drawSlideTime(Canvas canvas) {
        LazyTextPaint lazyTextPaint = lazyPaint.measure(getSlideTime(slidePosition));
        float rectWidth = lazyTextPaint.width() + xYTextMargin * 4;
        float rectHalfWidth = rectWidth / 2;

        timeRectF.left = getX(slidePosition) - rectHalfWidth;
        if (longPressHelper.slideX < topTableMinX + rectHalfWidth) {
            timeRectF.left = tableMargin;
        } else if (longPressHelper.slideX > topTableMaxX - rectHalfWidth) {
            timeRectF.left = topTableMaxX - rectWidth;
        }

        timeRectF.top = timeTableMinY;
        timeRectF.bottom = timeTableMaxY;
        timeRectF.right = timeRectF.left + rectWidth;

        // 绘制背景以及边框
        lazyPaint.drawRect(canvas, getColor(R.color.stock_area_fq), getColor(R.color.stock_slide_line), timeRectF);

        // 绘制相应值
        float x = timeRectF.left + xYTextMargin * 2;
        float y = (timeRectF.top + ((timeTableHeight + lazyTextPaint.height()) / 2f));
        lazyTextPaint.drawText(canvas, getColor(R.color.stock_text_title), x, y);
    }

    /**
     * 是否启用滑动
     */
    public abstract boolean isEnabledSlide();

    /**
     * 将 slideValue 转换为 String 格式
     */
    public abstract String convertTopSlideValue(float slideValue);

    /**
     * 将 slideValue 转换为 String 格式
     */
    public abstract String convertBottomSlideValue(float slideValue);

    /**
     * 获取滑动值
     */
    public abstract String getSlideTime(int slidePosition);
}
