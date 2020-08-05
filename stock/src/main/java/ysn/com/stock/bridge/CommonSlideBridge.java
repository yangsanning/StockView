package ysn.com.stock.bridge;

import android.graphics.RectF;

import ysn.com.stock.view.base.StockView;

/**
 * @Author yangsanning
 * @ClassName CommonSlideBridge
 * @Description 一句话概括作用
 * @Date 2020/8/4
 */
public class CommonSlideBridge extends BaseSlideBridge {

    private float originAbsoluteY;
    private float maxX, minX;
    private float slideRectHeight, slideRectHalfHeight;
    private float topTableHeight, topTableMinY, topTableMaxY;
    private float topTableWidth, topTableMaxX, topTableMinX;
    private float bottomTableHeight, bottomTableMinY, bottomTableMaxY;
    private float textMargin;

    public int slidePosition;
    public float slideLineY;
    public float slideValue;
    public RectF slideRectF = new RectF();

    public CommonSlideBridge(OnSlideBridgeListener onSlideBridgeListener) {
        super(onSlideBridgeListener);
    }

    @Override
    public float getOriginAbsoluteY() {
        return originAbsoluteY;
    }

    /**
     * 初始化一些参数
     * 在控件构建完成后调用（必须调用）
     */
    public void bindView(StockView stockView) {
        this.originAbsoluteY = stockView.getTopTableHeight();
        this.maxX = stockView.getTopTableMaxX();
        this.minX = stockView.getTopTableMinX();
        this.topTableMaxX = stockView.getTopTableMaxX();
        this.topTableMinY = stockView.getTopTableMinY();
        this.topTableMinX = stockView.getTopTableMinX();
        this.topTableMaxY = stockView.getTopTableMaxY();
        this.bottomTableMinY = stockView.getBottomTableMinY();
        this.bottomTableMaxY = stockView.getBottomTableMaxY();
        this.textMargin = stockView.getXYTextMargin();
        this.slideRectHeight = stockView.getTimeTableHeight();

        this.topTableWidth = topTableMaxX - topTableMinX;
        this.topTableHeight = topTableMaxY - topTableMinY;
        this.bottomTableHeight = bottomTableMaxY - bottomTableMinY;
        this.slideRectHalfHeight = slideRectHeight / 2;
    }

    /**
     * 计算出当前滑动位置，滑动值，滑动线的 y 坐标
     */
    public void convert(int dataSize, int totalCount, float topTableMaxValue, float topTableMinValue,
                        float bottomTableMaxValue, float bottomTableMinValue) {
        if (slideX > minX) {
            if (slideX < maxX) {
                slidePosition = (int) ((slideX - minX) / (maxX - minX) * totalCount);
            } else {
                slidePosition = dataSize - 1;
            }
        } else {
            slidePosition = 0;
        }

        if (slidePosition >= dataSize) {
            slidePosition = dataSize - 1;
        }

        if (slideY > 0) {
            // 下表格滑动线Y坐标
            if (slideY <= bottomTableMinY) {
                slideLineY = bottomTableMinY;
                slideValue = bottomTableMaxValue;
            } else if (slideY >= bottomTableMaxY) {
                slideLineY = bottomTableMaxY;
                slideValue = bottomTableMinValue;
            } else {
                slideLineY = slideY;
                slideValue = (Math.abs(slideY) * (bottomTableMaxValue - bottomTableMinValue)) / bottomTableHeight + bottomTableMinValue;
            }
        } else {
            // 上表格滑动线Y坐标
            if (slideY <= topTableMinY) {
                slideLineY = topTableMinY;
                slideValue = topTableMaxValue;
            } else if (slideY >= topTableMaxY) {
                slideLineY = topTableMaxY;
                slideValue = topTableMinValue;
            } else {
                slideLineY = slideY;
                slideValue = (Math.abs(slideY) * (topTableMaxValue - topTableMinValue)) / topTableHeight + topTableMinValue;
            }
        }
    }

    /**
     * 根据文本宽度获取滑动区域  RectF
     *
     * @param textWidth 文本宽度
     */
    public RectF getSlideRectF(float textWidth) {
        if (slideY > 0) {
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
        if (slideX < topTableWidth / 3f) {
            slideRectF.right = topTableMaxX - 1;
            slideRectF.left = slideRectF.right - textWidth - textMargin * 4;
        } else {
            slideRectF.left = topTableMinX + 1;
            slideRectF.right = slideRectF.left + textWidth + textMargin * 4;
        }
        return slideRectF;
    }

    /**
     * 根据文本高度获取滑动值的 Y 坐标
     *
     * @param textHeight 文本高度
     */
    public float getSlideValueY(float textHeight) {
        return slideRectF.bottom - (slideRectHeight - textHeight) / 2;
    }
}
