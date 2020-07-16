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
import ysn.com.stock.bean.ICapitalData;
import ysn.com.stock.manager.CapitalDataManager;
import ysn.com.stock.paint.LazyTextPaint;
import ysn.com.stock.utils.NumberUtils;
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
    private static final int DEFAULT_IN_FLOW_UNIT = 10000;
    private static final int DEFAULT_PRICE_DIGITS = 3;
    private static final int DEFAULT_IN_FLOW_DIGITS = 2;
    private static final String DEFAULT_LEFT_TITLE = "股价(元)";
    private static final String DEFAULT_RIGHT_TITLE = "今日资金净流入(万元)";

    private Paint areaPaint;
    private Path financeInFlowPath;
    private Paint financeInFlowPaint;

    private Path mainInFlowPath;
    private Paint mainInFlowPaint;

    private Path retailInFlowPath;
    private Paint retailInFlowPaint;

    private Path pricePath;
    private Paint pricePaint;

    /**
     * inFlowUnit: 净流入单位
     * priceDigits: 价格保留的位数
     * inFlowDigits: 净流入保留的位数
     * leftTitle: 左上角标题(用于标注左边价格坐标)
     * rightTitle: 右上角标题(用于标注右边inFlow坐标)
     */
    private int inFlowUnit;
    private int priceDigits;
    private int inFlowDigits;
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

        inFlowUnit = typedArray.getInteger(R.styleable.CapitalView_cv_in_flow_unit, DEFAULT_IN_FLOW_UNIT);
        priceDigits = typedArray.getInteger(R.styleable.CapitalView_cv_price_digits, DEFAULT_PRICE_DIGITS);
        inFlowDigits = typedArray.getInteger(R.styleable.CapitalView_cv_in_flow_digits, DEFAULT_IN_FLOW_DIGITS);
        leftTitle = typedArray.getString(R.styleable.CapitalView_cv_left_title);
        rightTitle = typedArray.getString(R.styleable.CapitalView_cv_right_title);

        priceColor = typedArray.getColor(R.styleable.CapitalView_cv_price_color, getColor(R.color.capital_price));
        financeInFlowColor = typedArray.getColor(R.styleable.CapitalView_cv_finance_in_flow_color, getColor(R.color.capital_finance_in_flow));
        mainInFlowColor = typedArray.getColor(R.styleable.CapitalView_cv_main_in_flow_color, getColor(R.color.capital_main_in_flow));
        retailInFlowColor = typedArray.getColor(R.styleable.CapitalView_cv_retail_in_flow_color, getColor(R.color.capital_retail_in_flow));

        textColor = typedArray.getColor(R.styleable.CapitalView_cv_text_color, getColor(R.color.capital_text));
        bgColor = typedArray.getColor(R.styleable.CapitalView_cv_bg_color, getColor(R.color.capital_bg));

        typedArray.recycle();

        if (TextUtils.isEmpty(leftTitle)) {
            leftTitle = DEFAULT_LEFT_TITLE;
        }
        if (TextUtils.isEmpty(rightTitle)) {
            rightTitle = DEFAULT_RIGHT_TITLE;
        }
    }

    @Override
    protected void initPaint() {
        super.initPaint();

        areaPaint = new Paint();
        areaPaint.setAntiAlias(true);
        areaPaint.setColor(bgColor);
        areaPaint.setStyle(Paint.Style.FILL);

        financeInFlowPath = new Path();
        financeInFlowPaint = new Paint();
        financeInFlowPaint.setColor(financeInFlowColor);
        financeInFlowPaint.setAntiAlias(true);
        financeInFlowPaint.setStyle(Paint.Style.STROKE);
        financeInFlowPaint.setStrokeWidth(DEFAULT_PRICE_STROKE_WIDTH);

        mainInFlowPath = new Path();
        mainInFlowPaint = new Paint();
        mainInFlowPaint.setColor(mainInFlowColor);
        mainInFlowPaint.setAntiAlias(true);
        mainInFlowPaint.setStyle(Paint.Style.STROKE);
        mainInFlowPaint.setStrokeWidth(DEFAULT_PRICE_STROKE_WIDTH);

        retailInFlowPath = new Path();
        retailInFlowPaint = new Paint();
        retailInFlowPaint.setColor(retailInFlowColor);
        retailInFlowPaint.setAntiAlias(true);
        retailInFlowPaint.setStyle(Paint.Style.STROKE);
        retailInFlowPaint.setStrokeWidth(DEFAULT_PRICE_STROKE_WIDTH);

        pricePath = new Path();
        pricePaint = new Paint();
        pricePaint.setColor(priceColor);
        pricePaint.setAntiAlias(true);
        pricePaint.setStyle(Paint.Style.STROKE);
        pricePaint.setStrokeWidth(DEFAULT_PRICE_STROKE_WIDTH);
    }

    @Override
    public boolean hasTitleTable() {
        return true;
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
        lazyPaint.moveTo((0), getTopTableMaxY())
                .lineTo((0), getTopTableMinY())
                .lineTo(viewWidth, getTopTableMinY())
                .lineToClose(canvas, viewWidth, getTopTableMaxY(), areaPaint)
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
            float y = getTimeTableMinY() + (lazyTextPaint.height() + getTimeTableHeight()) / 2f;
            lazyTextPaint.drawText(canvas, getTableMargin(), y);
        }).measure(TIME_TEXT[1], lazyTextPaint -> {
            // 绘制中间区域时间值
            float x = getTimeTableMinX() + ((getTopTableWidth() - lazyTextPaint.width()) / 2f);
            float y = getTimeTableMinY() + (lazyTextPaint.height() + getTimeTableHeight()) / 2f;
            lazyTextPaint.drawText(canvas, x, y);
        }).measure(TIME_TEXT[2], lazyTextPaint -> {
            // 绘制结束区域时间值
            float x = getTopTableMaxX() - lazyTextPaint.width();
            float y = getTimeTableMinY() + (lazyTextPaint.height() + getTimeTableHeight()) / 2f;
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
        float rowSpacing = getTopRowSpacing();
        int topRowCount = getPartTopHorizontal();
        for (int i = 0; i < (topRowCount + 1); i++) {
            float defaultY = getTopRowY(rowSpacing, topRowCount - i);
            int position = i;
            lazyPaint.measure(NumberUtils.numberFormat(getPriceCoordinate(((float) i / topRowCount)), priceDigits), lazyTextPaint -> {
                // 价格坐标
                float x = getTableMargin() + xYTextMargin;
                float y = getCoordinateY(position, topRowCount, defaultY, lazyTextPaint);
                lazyTextPaint.drawText(canvas, x, y);
            }).measure(NumberUtils.numberFormat(getInfoFlowCoordinate(((float) i / topRowCount)), inFlowDigits), lazyTextPaint -> {
                // inFlow坐标
                float x = getTopTableMaxX() - lazyTextPaint.width() - xYTextMargin;
                float y = getCoordinateY(position, topRowCount, defaultY, lazyTextPaint);
                lazyTextPaint.drawText(canvas, x, y);
            });
        }
    }

    private Float getPriceCoordinate(float ratio) {
        return getCoordinateValue(dataManager.getPriceMaximum(), dataManager.getPriceMinimum(), ratio);
    }

    private Float getInfoFlowCoordinate(float ratio) {
        return getCoordinateValue(dataManager.getInFlowMaximum(), dataManager.getInFlowMinimum(), ratio) / inFlowUnit;
    }

    private Float getCoordinateValue(float maxValue, float mixValue, float ratio) {
        return mixValue + (maxValue - mixValue) * ratio;
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
        if (isDrawMainInFlow) {
            if (isDrawRetailInFlow) {
                drawAllLine(canvas);
            } else {
                drawMainInflowLine(canvas);
            }
        } else if (isDrawRetailInFlow) {
            drawRetailInFlowLine(canvas);
        } else {
            drawBaseLine(canvas);
        }
    }

    /**
     * 绘制趋势线
     */
    private void drawAllLine(Canvas canvas) {
        financeInFlowPath.moveTo(tableMargin, getInFlowY(dataManager.getFinanceInFlow(0)));
        mainInFlowPath.moveTo(tableMargin, getInFlowY(dataManager.getMainInFlow(0)));
        retailInFlowPath.moveTo(tableMargin, getInFlowY(dataManager.getRetailInFlow(0)));
        pricePath.moveTo(tableMargin, getPriceY(dataManager.getPrice(0)));
        for (int i = 1; i < dataManager.size(); i++) {
            financeInFlowPath.lineTo(getX(i), getInFlowY(dataManager.getFinanceInFlow(i)));
            mainInFlowPath.lineTo(getX(i), getInFlowY(dataManager.getMainInFlow(i)));
            retailInFlowPath.lineTo(getX(i), getInFlowY(dataManager.getRetailInFlow(i)));
            pricePath.lineTo(getX(i), getPriceY(dataManager.getPrice(i)));
        }
        canvas.drawPath(financeInFlowPath, financeInFlowPaint);
        canvas.drawPath(mainInFlowPath, mainInFlowPaint);
        canvas.drawPath(retailInFlowPath, retailInFlowPaint);
        canvas.drawPath(pricePath, pricePaint);

        financeInFlowPath.reset();
        mainInFlowPath.reset();
        retailInFlowPath.reset();
        pricePath.reset();
    }

    /**
     * 不绘制 RetailInFlow
     */
    private void drawMainInflowLine(Canvas canvas) {
        financeInFlowPath.moveTo(tableMargin, getInFlowY(dataManager.getFinanceInFlow(0)));
        mainInFlowPath.moveTo(tableMargin, getInFlowY(dataManager.getMainInFlow(0)));
        pricePath.moveTo(tableMargin, getPriceY(dataManager.getPrice(0)));
        for (int i = 1; i < dataManager.size(); i++) {
            financeInFlowPath.lineTo(getX(i), getInFlowY(dataManager.getFinanceInFlow(i)));
            mainInFlowPath.lineTo(getX(i), getInFlowY(dataManager.getMainInFlow(i)));
            pricePath.lineTo(getX(i), getPriceY(dataManager.getPrice(i)));
        }
        canvas.drawPath(financeInFlowPath, financeInFlowPaint);
        canvas.drawPath(mainInFlowPath, mainInFlowPaint);
        canvas.drawPath(pricePath, pricePaint);

        financeInFlowPath.reset();
        mainInFlowPath.reset();
        pricePath.reset();
    }

    /**
     * 不绘制 MainInFlow
     */
    private void drawRetailInFlowLine(Canvas canvas) {
        financeInFlowPath.moveTo(tableMargin, getInFlowY(dataManager.getFinanceInFlow(0)));
        retailInFlowPath.moveTo(tableMargin, getInFlowY(dataManager.getRetailInFlow(0)));
        pricePath.moveTo(tableMargin, getPriceY(dataManager.getPrice(0)));
        for (int i = 1; i < dataManager.size(); i++) {
            financeInFlowPath.lineTo(getX(i), getInFlowY(dataManager.getFinanceInFlow(i)));
            retailInFlowPath.lineTo(getX(i), getInFlowY(dataManager.getRetailInFlow(i)));
            pricePath.lineTo(getX(i), getPriceY(dataManager.getPrice(i)));
        }
        canvas.drawPath(financeInFlowPath, financeInFlowPaint);
        canvas.drawPath(retailInFlowPath, retailInFlowPaint);
        canvas.drawPath(pricePath, pricePaint);

        financeInFlowPath.reset();
        retailInFlowPath.reset();
        pricePath.reset();
    }

    /**
     * 绘制基础趋势线
     */
    private void drawBaseLine(Canvas canvas) {
        financeInFlowPath.moveTo(tableMargin, getInFlowY(dataManager.getFinanceInFlow(0)));
        pricePath.moveTo(tableMargin, getPriceY(dataManager.getPrice(0)));
        for (int i = 1; i < dataManager.size(); i++) {
            financeInFlowPath.lineTo(getX(i), getInFlowY(dataManager.getFinanceInFlow(i)));
            pricePath.lineTo(getX(i), getPriceY(dataManager.getPrice(i)));
        }
        canvas.drawPath(financeInFlowPath, financeInFlowPaint);
        canvas.drawPath(pricePath, pricePaint);

        financeInFlowPath.reset();
        pricePath.reset();
    }

    /**
     * 获取y轴坐标
     *
     * @param inFlowValue 当前值
     * @return y轴坐标
     */
    private float getInFlowY(float inFlowValue) {
        return getY(inFlowValue, dataManager.getInFlowMinimum(), dataManager.getInFlowMaximum());
    }

    /**
     * 获取价格y轴坐标
     *
     * @param price 当前值
     * @return 价格的y轴坐标
     */
    private float getPriceY(float price) {
        return getY(price, dataManager.getPriceMinimum(), dataManager.getPriceMaximum());
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
}
