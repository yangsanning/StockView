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

import java.util.ArrayList;

import ysn.com.stock.R;
import ysn.com.stock.bean.Capital;
import ysn.com.stock.bean.CapitalData;
import ysn.com.stock.utils.NumberUtils;

/**
 * @Author yangsanning
 * @ClassName CapitalView
 * @Description 资金趋势图
 * @Date 2019/5/23
 * @History 2019/5/23 author: description:
 */
public class CapitalView extends StockView {

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
    private int columnLineColor;
    private int rowLineColor;

    private boolean isDrawMainInFlow;
    private boolean isDrawRetailInFlow;

    private Capital capital;
    private ArrayList<CapitalData> data;

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
        inFlowDigits = typedArray.getInteger(R.styleable.CapitalView_cv_in_flow_digits, DEFAULT_IN_FLOW_DIGITS);
        priceDigits = typedArray.getInteger(R.styleable.CapitalView_cv_price_digits, DEFAULT_PRICE_DIGITS);
        leftTitle = typedArray.getString(R.styleable.CapitalView_cv_left_title);
        rightTitle = typedArray.getString(R.styleable.CapitalView_cv_right_title);

        priceColor = typedArray.getColor(R.styleable.CapitalView_cv_price_color, getColor(R.color.capital_price));
        financeInFlowColor = typedArray.getColor(R.styleable.CapitalView_cv_finance_in_flow_color, getColor(R.color.capital_finance_in_flow));
        mainInFlowColor = typedArray.getColor(R.styleable.CapitalView_cv_main_in_flow_color, getColor(R.color.capital_main_in_flow));
        retailInFlowColor = typedArray.getColor(R.styleable.CapitalView_cv_retail_in_flow_color, getColor(R.color.capital_retail_in_flow));

        textColor = typedArray.getColor(R.styleable.CapitalView_cv_text_color, getColor(R.color.capital_text));
        bgColor = typedArray.getColor(R.styleable.CapitalView_cv_bg_color, getColor(R.color.capital_bg));
        columnLineColor = typedArray.getColor(R.styleable.CapitalView_cv_column_line_color, getColor(R.color.capital_column_line));
        rowLineColor = typedArray.getColor(R.styleable.CapitalView_cv_row_line_color, getColor(R.color.capital_row_line));

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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        titleTableHeight = viewHeight * 0.108f;
        titleTextSize = titleTableHeight * 0.534f;
    }

    @Override
    protected int getTopRowCount() {
        return 4;
    }

    @Override
    protected void onBaseDraw(Canvas canvas) {
        drawBackGround(canvas);
        super.onBaseDraw(canvas);
    }

    /**
     * 绘制背景
     */
    private void drawBackGround(Canvas canvas) {
        linePath.reset();
        linePath.moveTo((0), getTopTableMinY());
        linePath.lineTo((0), getTopTableMaxY());
        linePath.lineTo(viewWidth, getTopTableMaxY());
        linePath.lineTo(viewWidth, getTopTableMinY());
        linePath.close();
        canvas.drawPath(linePath, areaPaint);
        linePath.reset();
    }

    @Override
    protected void onBordersDraw(Canvas canvas) {
    }

    /**
     * 绘制竖线
     */
    @Override
    protected void onColumnLineDraw(Canvas canvas) {
        // 绘制上表竖线
        dottedLinePaint.setColor(columnLineColor);
        float xSpace = (viewWidth - 2 * tableMargin) / getColumnCount();
        for (int i = 1; i < getColumnCount(); i++) {
            linePath.reset();
            float x = getColumnX(xSpace, i);
            linePath.moveTo(x, getTopTableMinY());
            linePath.lineTo(x, getTopTableMaxY());
            canvas.drawPath(linePath, dottedLinePaint);
        }
    }

    @Override
    protected int getColumnCount() {
        return 2;
    }

    /**
     * 绘制横线
     */
    @Override
    protected void onRowLineDraw(Canvas canvas) {
        linePaint.setColor(rowLineColor);
        float rowSpacing = getRowSpacing();
        for (int i = 1; i < getTopRowCount(); i++) {
            linePath.reset();
            float y = getRowY(rowSpacing, i);
            linePath.moveTo(tableMargin, y);
            linePath.lineTo((viewWidth - tableMargin), y);
            canvas.drawPath(linePath, linePaint);
        }
    }

    /**
     * 绘制时间坐标
     */
    @Override
    protected void onTimeTextDraw(Canvas canvas) {
        super.onTimeTextDraw(canvas);
        textPaint.setColor(textColor);
        textPaint.setTextSize(xYTextSize);

        // 绘制开始区域时间值
        textPaint.getTextBounds(TIME_TEXT[0], (0), TIME_TEXT[0].length(), textRect);
        canvas.drawText(TIME_TEXT[0], tableMargin, getTimeTextY(), textPaint);

        // 绘制中间区域时间值
        textPaint.getTextBounds(TIME_TEXT[1], (0), TIME_TEXT[1].length(), textRect);
        canvas.drawText(TIME_TEXT[1], (((viewWidth - textRect.right) >> 1) - tableMargin), getTimeTextY(), textPaint);

        // 绘制结束区域时间值
        textPaint.getTextBounds(TIME_TEXT[2], (0), TIME_TEXT[2].length(), textRect);
        canvas.drawText(TIME_TEXT[2], (viewWidth - textRect.right - tableMargin), getTimeTextY(), textPaint);
    }

    @Override
    protected void onChildDraw(Canvas canvas) {
        super.onChildDraw(canvas);

        // 绘制头部文本
        drawTitleText(canvas);

        if (capital == null) {
            return;
        }

        // 绘制坐标值
        drawCoordinate(canvas);

        // 绘制趋势线
        drawLine(canvas);
    }

    /**
     * 绘制头部文本
     *
     * @param canvas
     */
    private void drawTitleText(Canvas canvas) {
        textPaint.setTextSize(titleTextSize);

        textPaint.getTextBounds(leftTitle, (0), leftTitle.length(), textRect);
        float titleTextY = getTopTableMaxY() - textRect.height();
        canvas.drawText(leftTitle, (0), titleTextY, textPaint);

        textPaint.getTextBounds(rightTitle, (0), rightTitle.length(), textRect);
        canvas.drawText(rightTitle, (viewWidth - textRect.width()), titleTextY, textPaint);
    }

    /**
     * 绘制坐标值
     */
    private void drawCoordinate(Canvas canvas) {
        textPaint.setTextSize(xYTextSize);

        float rowSpacing = getRowSpacing();
        int topRowCount = getTopRowCount();
        for (int i = 0; i < (topRowCount + 1); i++) {
            float defaultY = getRowY(rowSpacing, topRowCount - i);

            // 价格坐标
            String text = NumberUtils.numberFormat(getPriceCoordinate(((float) i / topRowCount)), priceDigits);
            textPaint.getTextBounds(text, (0), text.length(), textRect);
            float textMargin = getTextMargin();
            canvas.drawText(text, textMargin,
                    getCoordinateY(i, topRowCount, defaultY, textMargin), textPaint);

            // inFlow坐标
            text = NumberUtils.numberFormat(getInfoFlowCoordinate(((float) i / topRowCount)), inFlowDigits);
            textPaint.getTextBounds(text, (0), text.length(), textRect);
            textMargin = getTextMargin();
            canvas.drawText(text, (viewWidth - textRect.width() - textMargin),
                    getCoordinateY(i, topRowCount, defaultY, textMargin), textPaint);
        }
    }

    private Float getPriceCoordinate(float ratio) {
        return getCoordinateValue(capital.getMaxPrice(), capital.getMixPrice(), ratio);
    }

    private Float getInfoFlowCoordinate(float ratio) {
        return getCoordinateValue(capital.getMaxInFlow(), capital.getMixInFlow(), ratio) / inFlowUnit;
    }

    private Float getCoordinateValue(float maxValue, float mixValue, float ratio) {
        return mixValue + (maxValue - mixValue) * ratio;
    }

    private float getCoordinateY(int position, int topRowCount, float defaultY, float textMargin) {
        float coordinateY;
        if (position == 0) {
            coordinateY = -textMargin;
        } else if (position == topRowCount) {
            coordinateY = defaultY + textRect.height() + textMargin;
        } else {
            coordinateY = defaultY + textRect.height() / 2f;
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
        financeInFlowPath.moveTo(tableMargin, getInFlowY(data.get(0).getFinanceInFlow()));
        mainInFlowPath.moveTo(tableMargin, getInFlowY(data.get(0).getMainInFlow()));
        retailInFlowPath.moveTo(tableMargin, getInFlowY(data.get(0).getRetailInFlow()));
        pricePath.moveTo(tableMargin, getPriceY(data.get(0).getPrice()));
        for (int i = 1; i < data.size(); i++) {
            financeInFlowPath.lineTo(getX(i), getInFlowY(data.get(i).getFinanceInFlow()));
            mainInFlowPath.lineTo(getX(i), getInFlowY(data.get(i).getMainInFlow()));
            retailInFlowPath.lineTo(getX(i), getInFlowY(data.get(i).getRetailInFlow()));
            pricePath.lineTo(getX(i), getPriceY(data.get(i).getPrice()));
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
        financeInFlowPath.moveTo(tableMargin, getInFlowY(data.get(0).getFinanceInFlow()));
        mainInFlowPath.moveTo(tableMargin, getInFlowY(data.get(0).getMainInFlow()));
        pricePath.moveTo(tableMargin, getPriceY(data.get(0).getPrice()));
        for (int i = 1; i < data.size(); i++) {
            financeInFlowPath.lineTo(getX(i), getInFlowY(data.get(i).getFinanceInFlow()));
            mainInFlowPath.lineTo(getX(i), getInFlowY(data.get(i).getMainInFlow()));
            pricePath.lineTo(getX(i), getPriceY(data.get(i).getPrice()));
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
        financeInFlowPath.moveTo(tableMargin, getInFlowY(data.get(0).getFinanceInFlow()));
        retailInFlowPath.moveTo(tableMargin, getInFlowY(data.get(0).getRetailInFlow()));
        pricePath.moveTo(tableMargin, getPriceY(data.get(0).getPrice()));
        for (int i = 1; i < data.size(); i++) {
            financeInFlowPath.lineTo(getX(i), getInFlowY(data.get(i).getFinanceInFlow()));
            retailInFlowPath.lineTo(getX(i), getInFlowY(data.get(i).getRetailInFlow()));
            pricePath.lineTo(getX(i), getPriceY(data.get(i).getPrice()));
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
        financeInFlowPath.moveTo(tableMargin, getInFlowY(data.get(0).getFinanceInFlow()));
        pricePath.moveTo(tableMargin, getPriceY(data.get(0).getPrice()));
        for (int i = 1; i < data.size(); i++) {
            financeInFlowPath.lineTo(getX(i), getInFlowY(data.get(i).getFinanceInFlow()));
            pricePath.lineTo(getX(i), getPriceY(data.get(i).getPrice()));
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
        return getY(inFlowValue, capital.getMixInFlow(), capital.getMaxInFlow());
    }

    /**
     * 获取价格y轴坐标
     *
     * @param price 当前值
     * @return 价格的y轴坐标
     */
    private float getPriceY(float price) {
        return getY(price, capital.getMixPrice(), capital.getMaxPrice());
    }

    /**
     * 直接设置 capital
     *
     * @see Capital
     */
    public void setNewData(Capital capital) {
        this.capital = capital;
        this.data = capital.getData();
        postInvalidate();
    }

    /**
     * @param isDrawMainInFlow 是否绘制主力净流入曲线
     */
    public CapitalView setDrawMainInFlow(boolean isDrawMainInFlow) {
        this.isDrawMainInFlow = isDrawMainInFlow;
        postInvalidate();
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
        postInvalidate();
        return this;
    }

    /**
     * @return 当前是否绘制散户净流入曲线
     */
    public boolean isDrawRetailInFlow() {
        return isDrawRetailInFlow;
    }
}
