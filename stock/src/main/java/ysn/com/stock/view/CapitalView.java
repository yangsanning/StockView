package ysn.com.stock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import java.util.List;

import ysn.com.stock.adapter.BaseCurveAdapter;
import ysn.com.stock.bean.Extremum;
import ysn.com.stock.bean.ICapitalData;
import ysn.com.stock.bean.IExtremum;
import ysn.com.stock.config.CapitalConfig;
import ysn.com.stock.interceptor.CapitalUnitInterceptor;
import ysn.com.stock.manager.CapitalDataManager;
import ysn.com.stock.paint.LazyLinePaint;
import ysn.com.stock.view.base.GridView;

import static ysn.com.stock.config.CapitalConfig.DEFAULT_PRICE_STROKE_WIDTH;

/**
 * @Author yangsanning
 * @ClassName CapitalView
 * @Description 资金趋势图
 * @Date 2019/5/23
 * @History 2019/5/23 author: description:
 */
public class CapitalView extends GridView {

    private CapitalConfig config;
    private CapitalDataManager dataManager = new CapitalDataManager();
    private CapitalUnitInterceptor interceptor;

    private boolean isDrawMainInFlow;
    private boolean isDrawRetailInFlow;

    private PriceCurveAdapter priceCurveAdapter = new PriceCurveAdapter();
    private InFlowCurveAdapter financeInFlowCurveAdapter = new InFlowCurveAdapter() {
        @Override
        protected float getValue(int position) {
            return dataManager.getFinanceInFlow(position);
        }
    };
    private InFlowCurveAdapter mainInFlowCurveAdapter = new InFlowCurveAdapter() {
        @Override
        protected float getValue(int position) {
            return dataManager.getMainInFlow(position);
        }
    };
    private InFlowCurveAdapter retailInFlowCurveAdapter = new InFlowCurveAdapter() {
        @Override
        protected float getValue(int position) {
            return dataManager.getRetailInFlow(position);
        }
    };

    public CapitalView(Context context) {
        super(context);
    }

    public CapitalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CapitalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CapitalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initAttr(AttributeSet attrs) {
        super.initAttr(attrs);
        config = new CapitalConfig(context, attrs);
    }

    @Override
    public boolean isEnabledTitleTable() {
        return true;
    }

    @Override
    public boolean isEnabledMiddleTable() {
        return false;
    }

    @Override
    public float getTitleTableHeight() {
        return viewHeight * 0.108f;
    }

    @Override
    public float getTitleTextSize() {
        return titleTableHeight * 0.534f;
    }

    @Override
    protected void onBaseDraw(Canvas canvas) {
        // 绘制背景
        lazyPaint.drawRect(canvas, 0, getTopTableMaxY(), viewWidth, getTopTableMinY(), config.bgColor)
                // 为后续操作统一设置字体大小以及文字颜色
                .setTextColor(config.textColor);
        super.onBaseDraw(canvas);
    }

    @Override
    protected void onTitleTextDraw(Canvas canvas) {
        super.onTitleTextDraw(canvas);
        lazyPaint.measure(config.leftTitle, lazyTextPaint -> {
            // 绘制左上角标题
            float y = getTopTableMinY() - lazyTextPaint.height();
            lazyTextPaint.drawText(canvas, getTopTableMinX(), y);
        }).measure(config.rightTitle, lazyTextPaint -> {
            // 绘制右上角标题
            float x = getTopTableMaxX() - lazyTextPaint.width();
            float y = getTopTableMinY() - lazyTextPaint.height();
            lazyTextPaint.drawText(canvas, x, y);
        });
    }

    @Override
    protected void onChildDraw(Canvas canvas) {
        super.onChildDraw(canvas);

        if (dataManager.isEmpty()) {
            return;
        }

        // 绘制坐标值
        drawCoordinate(canvas);

        // 绘制趋势线
        drawLine(canvas);
    }

    /**
     * 绘制坐标值
     */
    private void drawCoordinate(Canvas canvas) {
        float topRowSpacing = getTopRowSpacing();
        int partTopHorizontal = getPartTopHorizontal();
        for (int i = 0; i <= partTopHorizontal; i++) {
            float defaultY = getTopRowY(topRowSpacing, i);
            int position = i;
            lazyPaint.measure(getLeftCoordinateText(i), lazyTextPaint -> {
                // 价格坐标
                float x = getTableMargin() + xYTextMargin;
                float y = getTopCoordinateY(position, defaultY, lazyTextPaint);
                lazyTextPaint.drawText(canvas, x, y);
            }).measure(getRightCoordinateText(i), lazyTextPaint -> {
                // inFlow坐标
                float x = getTopTableMaxX() - lazyTextPaint.width() - xYTextMargin;
                float y = getTopCoordinateY(position, defaultY, lazyTextPaint);
                lazyTextPaint.drawText(canvas, x, y);
            });
        }
    }

    /**
     * 根据 position 获取对应的左坐标文本
     */
    private String getLeftCoordinateText(float position) {
        Float value = getCoordinateValue(dataManager.getPriceExtremum(), position);
        return interceptor == null ? String.valueOf(value) : interceptor.leftCoordinate(value);
    }

    /**
     * 根据 position 获取对应的右坐标文本
     */
    private String getRightCoordinateText(float position) {
        Float value = getCoordinateValue(dataManager.getInFlowExtremum(), position);
        return interceptor == null ? String.valueOf(value) : interceptor.rightCoordinate(value);
    }

    /**
     * 根据极值和序号获取对应的坐标值
     */
    private Float getCoordinateValue(IExtremum iExtremum, float position) {
        return iExtremum.getMinimum() + iExtremum.getPeek() * position / getPartTopHorizontal();
    }

    /**
     * 绘制趋势线
     */
    private void drawLine(Canvas canvas) {
        LazyLinePaint lazyLinePaint = lazyPaint.getLazyLinePaint().setStrokeWidth(DEFAULT_PRICE_STROKE_WIDTH);
        if (isDrawMainInFlow) {
            if (isDrawRetailInFlow) {
                drawAllLine(canvas, lazyLinePaint);
            } else {
                drawMainInflowLine(canvas, lazyLinePaint);
            }
        } else if (isDrawRetailInFlow) {
            drawRetailInFlowLine(canvas, lazyLinePaint);
        } else {
            drawBaseLine(canvas, lazyLinePaint);
        }
    }

    /**
     * 绘制趋势线
     */
    private void drawAllLine(Canvas canvas, LazyLinePaint lazyLinePaint) {
        for (int position = 0; position < dataManager.size() - 1; position++) {
            priceCurveAdapter.draw(canvas, lazyLinePaint.setColor(config.priceColor).linePaint, position);
            financeInFlowCurveAdapter.draw(canvas, lazyLinePaint.setColor(config.financeInFlowColor).linePaint, position);
            mainInFlowCurveAdapter.draw(canvas, lazyLinePaint.setColor(config.mainInFlowColor).linePaint, position);
            retailInFlowCurveAdapter.draw(canvas, lazyLinePaint.setColor(config.retailInFlowColor).linePaint, position);
        }
    }

    /**
     * 不绘制 RetailInFlow
     */
    private void drawMainInflowLine(Canvas canvas, LazyLinePaint lazyLinePaint) {
        for (int position = 0; position < dataManager.size() - 1; position++) {
            priceCurveAdapter.draw(canvas, lazyLinePaint.setColor(config.priceColor).linePaint, position);
            financeInFlowCurveAdapter.draw(canvas, lazyLinePaint.setColor(config.financeInFlowColor).linePaint, position);
            mainInFlowCurveAdapter.draw(canvas, lazyLinePaint.setColor(config.mainInFlowColor).linePaint, position);
        }
    }

    /**
     * 不绘制 MainInFlow
     */
    private void drawRetailInFlowLine(Canvas canvas, LazyLinePaint lazyLinePaint) {
        for (int position = 0; position < dataManager.size() - 1; position++) {
            priceCurveAdapter.draw(canvas, lazyLinePaint.setColor(config.priceColor).linePaint, position);
            financeInFlowCurveAdapter.draw(canvas, lazyLinePaint.setColor(config.financeInFlowColor).linePaint, position);
            retailInFlowCurveAdapter.draw(canvas, lazyLinePaint.setColor(config.retailInFlowColor).linePaint, position);
        }
    }

    /**
     * 绘制基础趋势线
     */
    private void drawBaseLine(Canvas canvas, LazyLinePaint lazyLinePaint) {
        for (int position = 0; position < dataManager.size() - 1; position++) {
            priceCurveAdapter.draw(canvas, lazyLinePaint.setColor(config.priceColor).linePaint, position);
            financeInFlowCurveAdapter.draw(canvas, lazyLinePaint.setColor(config.financeInFlowColor).linePaint, position);
        }
    }

    /**
     * 获取y轴坐标
     *
     * @param inFlowValue 当前值
     * @return y轴坐标
     */
    private float getInFlowY(float inFlowValue) {
        Extremum inFlowExtremum = dataManager.getInFlowExtremum();
        return getY(inFlowValue, inFlowExtremum.getMinimum(), inFlowExtremum.getMaximum());
    }

    /**
     * 获取价格y轴坐标
     *
     * @param price 当前值
     * @return 价格的y轴坐标
     */
    private float getPriceY(float price) {
        Extremum priceExtremum = dataManager.getPriceExtremum();
        return getY(price, priceExtremum.getMinimum(), priceExtremum.getMaximum());
    }

    /**
     * 设置拦截器
     */
    public CapitalView setInterceptor(CapitalUnitInterceptor interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    /**
     * 设置数据
     */
    public <T extends ICapitalData> void setNewData(List<T> dataList) {
        dataManager.setNewData(dataList);
        invalidate();
    }

    /**
     * @param isDrawMainInFlow 是否绘制主力净流入曲线
     */
    public CapitalView setDrawMainInFlow(boolean isDrawMainInFlow) {
        this.isDrawMainInFlow = isDrawMainInFlow;
        invalidate();
        return this;
    }

    /**
     * @return 当前是否绘制主力净流入曲线
     */
    public boolean isDrawMainInFlow() {
        return isDrawMainInFlow;
    }

    /**
     * @param isDrawRetailInFlow 是否绘制散户净流入曲线
     */
    public CapitalView setDrawRetailInFlow(boolean isDrawRetailInFlow) {
        this.isDrawRetailInFlow = isDrawRetailInFlow;
        invalidate();
        return this;
    }

    /**
     * @return 当前是否绘制散户净流入曲线
     */
    public boolean isDrawRetailInFlow() {
        return isDrawRetailInFlow;
    }

    private abstract class InFlowCurveAdapter extends CapitalTopCurveAdapter {

        @Override
        protected float getDrawY(float value) {
            return getInFlowY(value);
        }
    }

    private class PriceCurveAdapter extends CapitalTopCurveAdapter {

        @Override
        protected float getValue(int position) {
            return dataManager.getPrice(position);
        }

        @Override
        protected float getDrawY(float value) {
            return getPriceY(value);
        }
    }

    private abstract class CapitalTopCurveAdapter extends BaseCurveAdapter {

        @Override
        protected float getMaxY() {
            return getTopTableMaxY();
        }

        @Override
        protected float getMinY() {
            return getTableMinY();
        }

        @Override
        protected float getDrawX(int position) {
            return getX(position);
        }
    }
}
