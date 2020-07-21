package ysn.com.stock.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;

import java.util.List;

import ysn.com.stock.R;
import ysn.com.stock.adapter.BaseCurveAdapter;
import ysn.com.stock.bean.Extremum;
import ysn.com.stock.bean.ICapitalData;
import ysn.com.stock.bean.IExtremum;
import ysn.com.stock.interceptor.CapitalUnitInterceptor;
import ysn.com.stock.manager.CapitalDataManager;
import ysn.com.stock.paint.LazyLinePaint;
import ysn.com.stock.paint.LazyTextPaint;
import ysn.com.stock.view.base.GridView;

/**
 * @Author yangsanning
 * @ClassName CapitalView
 * @Description 资金趋势图
 * @Date 2019/5/23
 * @History 2019/5/23 author: description:
 */
public class CapitalView extends GridView {

    private static final String[] TIME_TEXT = new String[]{"09:30", "11:30/13:00", "15:00"};
    private static final float DEFAULT_PRICE_STROKE_WIDTH = 2.5f;
    private static final String DEFAULT_LEFT_TITLE = "股价(元)";
    private static final String DEFAULT_RIGHT_TITLE = "今日资金净流入(元)";

    /**
     * inFlowUnit: 净流入单位
     * leftTitle: 左上角标题(用于标注左边价格坐标)
     * rightTitle: 右上角标题(用于标注右边inFlow坐标)
     */
    private String leftTitle;
    private String rightTitle;

    /**
     * priceColor: 价格曲线颜色
     * financeInFlowColor: 总资金净流入曲线颜色
     * mainInFlowColor: 主力净流入曲线颜色
     * retailInFlowColor: 散户净流入曲线颜色
     */
    private int priceColor;
    private int financeInFlowColor;
    private int mainInFlowColor;
    private int retailInFlowColor;

    /**
     * textColor: 文本颜色
     * bgColor: 背景颜色
     * columnLineColor: 竖线颜色
     * rowLineColor: 横线颜色
     */
    private int textColor;
    private int bgColor;

    private boolean isDrawMainInFlow;
    private boolean isDrawRetailInFlow;

    private CapitalDataManager dataManager = new CapitalDataManager();
    private CapitalUnitInterceptor interceptor;

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
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CapitalView);

        leftTitle = typedArray.getString(R.styleable.CapitalView_leftTitle);
        rightTitle = typedArray.getString(R.styleable.CapitalView_rightTitle);

        priceColor = typedArray.getColor(R.styleable.CapitalView_priceColor, getColor(R.color.capital_price));
        financeInFlowColor = typedArray.getColor(R.styleable.CapitalView_financeInFlowColor, getColor(R.color.capital_finance_in_flow));
        mainInFlowColor = typedArray.getColor(R.styleable.CapitalView_mainInFlowColor, getColor(R.color.capital_main_in_flow));
        retailInFlowColor = typedArray.getColor(R.styleable.CapitalView_retailInFlowColor, getColor(R.color.capital_retail_in_flow));

        textColor = typedArray.getColor(R.styleable.CapitalView_textColor, getColor(R.color.capital_text));
        bgColor = typedArray.getColor(R.styleable.CapitalView_bgColor, getColor(R.color.capital_bg));

        typedArray.recycle();

        if (TextUtils.isEmpty(leftTitle)) {
            leftTitle = DEFAULT_LEFT_TITLE;
        }
        if (TextUtils.isEmpty(rightTitle)) {
            rightTitle = DEFAULT_RIGHT_TITLE;
        }
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
        lazyPaint.drawRect(canvas, 0, getTopTableMaxY(), viewWidth, getTopTableMinY(), bgColor)
                // 为后续操作统一设置字体大小以及文字颜色
                .setTextSize(xYTextSize)
                .setTextColor(textColor);
        super.onBaseDraw(canvas);
    }

    @Override
    protected void onTitleTextDraw(Canvas canvas) {
        super.onTitleTextDraw(canvas);
        lazyPaint.measure(leftTitle, lazyTextPaint -> {
            // 绘制左上角标题
            float y = getTopTableMinY() - lazyTextPaint.height();
            lazyTextPaint.drawText(canvas, getTopTableMinX(), y);
        }).measure(rightTitle, lazyTextPaint -> {
            // 绘制右上角标题
            float x = getTopTableMaxX() - lazyTextPaint.width();
            float y = getTopTableMinY() - lazyTextPaint.height();
            lazyTextPaint.drawText(canvas, x, y);
        });
    }

    /**
     * 绘制时间坐标
     */
    @Override
    protected void onTimeTextDraw(Canvas canvas) {
        super.onTimeTextDraw(canvas);
        lazyPaint.measure(TIME_TEXT[0], lazyTextPaint -> {
            // 绘制开始区域时间值
            float y = getTimeTableMinY() + lazyTextPaint.centerY(getTimeTableHeight());
            lazyTextPaint.drawText(canvas, getTableMargin(), y);
        }).measure(TIME_TEXT[1], lazyTextPaint -> {
            // 绘制中间区域时间值
            float x = getTimeTableMinX() + lazyTextPaint.centerX(getTopTableWidth());
            float y = getTimeTableMinY() + lazyTextPaint.centerY(getTimeTableHeight());
            lazyTextPaint.drawText(canvas, x, y);
        }).measure(TIME_TEXT[2], lazyTextPaint -> {
            // 绘制结束区域时间值
            float x = getTopTableMaxX() - lazyTextPaint.width();
            float y = getTimeTableMinY() + lazyTextPaint.centerY(getTimeTableHeight());
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
        for (int i = 0; i < (partTopHorizontal + 1); i++) {
            float defaultY = getTopRowY(topRowSpacing, partTopHorizontal - i);
            int position = i;
            lazyPaint.measure(getLeftCoordinateText(i), lazyTextPaint -> {
                // 价格坐标
                float x = getTableMargin() + xYTextMargin;
                float y = getCoordinateY(position, partTopHorizontal, defaultY, lazyTextPaint);
                lazyTextPaint.drawText(canvas, x, y);
            }).measure(getRightCoordinateText(i), lazyTextPaint -> {
                // inFlow坐标
                float x = getTopTableMaxX() - lazyTextPaint.width() - xYTextMargin;
                float y = getCoordinateY(position, partTopHorizontal, defaultY, lazyTextPaint);
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

    private float getCoordinateY(int position, int topRowCount, float defaultY, LazyTextPaint lazyTextPaint) {
        float coordinateY;
        if (position == 0) {
            coordinateY = -xYTextMargin;
        } else if (position == topRowCount) {
            coordinateY = defaultY + lazyTextPaint.height() + xYTextMargin;
        } else {
            coordinateY = defaultY + lazyTextPaint.height() / 2f;
        }
        return coordinateY;
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
            priceCurveAdapter.draw(canvas, lazyLinePaint.setColor(priceColor).linePaint, position);
            financeInFlowCurveAdapter.draw(canvas, lazyLinePaint.setColor(financeInFlowColor).linePaint, position);
            mainInFlowCurveAdapter.draw(canvas, lazyLinePaint.setColor(mainInFlowColor).linePaint, position);
            retailInFlowCurveAdapter.draw(canvas, lazyLinePaint.setColor(retailInFlowColor).linePaint, position);
        }
    }

    /**
     * 不绘制 RetailInFlow
     */
    private void drawMainInflowLine(Canvas canvas, LazyLinePaint lazyLinePaint) {
        for (int position = 0; position < dataManager.size() - 1; position++) {
            priceCurveAdapter.draw(canvas, lazyLinePaint.setColor(priceColor).linePaint, position);
            financeInFlowCurveAdapter.draw(canvas, lazyLinePaint.setColor(financeInFlowColor).linePaint, position);
            mainInFlowCurveAdapter.draw(canvas, lazyLinePaint.setColor(mainInFlowColor).linePaint, position);
        }
    }

    /**
     * 不绘制 MainInFlow
     */
    private void drawRetailInFlowLine(Canvas canvas, LazyLinePaint lazyLinePaint) {
        for (int position = 0; position < dataManager.size() - 1; position++) {
            priceCurveAdapter.draw(canvas, lazyLinePaint.setColor(priceColor).linePaint, position);
            financeInFlowCurveAdapter.draw(canvas, lazyLinePaint.setColor(financeInFlowColor).linePaint, position);
            retailInFlowCurveAdapter.draw(canvas, lazyLinePaint.setColor(retailInFlowColor).linePaint, position);
        }
    }

    /**
     * 绘制基础趋势线
     */
    private void drawBaseLine(Canvas canvas, LazyLinePaint lazyLinePaint) {
        for (int position = 0; position < dataManager.size() - 1; position++) {
            priceCurveAdapter.draw(canvas, lazyLinePaint.setColor(priceColor).linePaint, position);
            financeInFlowCurveAdapter.draw(canvas, lazyLinePaint.setColor(financeInFlowColor).linePaint, position);
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
