package ysn.com.stock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import java.util.ArrayList;

import ysn.com.stock.R;
import ysn.com.stock.bean.Capital;
import ysn.com.stock.bean.CapitalData;

/**
 * @Author yangsanning
 * @ClassName CapitalView
 * @Description 资金趋势图
 * @Date 2019/5/23
 * @History 2019/5/23 author: description:
 */
public class CapitalView extends StockView {

    private static final int COUNT_DEFAULT = 240;
    private static final String[] TIME_TEXT = new String[]{"09:30", "11:30/13:00", "15:00"};
    private static final float DEFAULT_PRICE_STROKE_WIDTH = 2.5f;

    /**
     * totalCount: 总点数
     */
    private int totalCount = COUNT_DEFAULT;

    private Paint areaPaint;
    private Path financeInFlowPath;
    private Paint financeInFlowPaint;

    private Path mainInFlowPath;
    private Paint mainInFlowPaint;

    private Path retailInFlowPath;
    private Paint retailInFlowPaint;

    private Path pricePath;
    private Paint pricePaint;

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
    protected void init(AttributeSet attrs) {
        super.init(attrs);

        areaPaint = new Paint();
        areaPaint.setAntiAlias(true);
        areaPaint.setColor(getColor(R.color.capital_bg));
        areaPaint.setStyle(Paint.Style.FILL);

        financeInFlowPath = new Path();
        financeInFlowPaint = new Paint();
        financeInFlowPaint.setColor(getColor(R.color.capital_finance_in_flow));
        financeInFlowPaint.setAntiAlias(true);
        financeInFlowPaint.setStyle(Paint.Style.STROKE);
        financeInFlowPaint.setStrokeWidth(DEFAULT_PRICE_STROKE_WIDTH);

        mainInFlowPath = new Path();
        mainInFlowPaint = new Paint();
        mainInFlowPaint.setColor(getColor(R.color.capital_main_in_flow));
        mainInFlowPaint.setAntiAlias(true);
        mainInFlowPaint.setStyle(Paint.Style.STROKE);
        mainInFlowPaint.setStrokeWidth(DEFAULT_PRICE_STROKE_WIDTH);

        retailInFlowPath = new Path();
        retailInFlowPaint = new Paint();
        retailInFlowPaint.setColor(getColor(R.color.capital_retail_in_flow));
        retailInFlowPaint.setAntiAlias(true);
        retailInFlowPaint.setStyle(Paint.Style.STROKE);
        retailInFlowPaint.setStrokeWidth(DEFAULT_PRICE_STROKE_WIDTH);

        pricePath = new Path();
        pricePaint = new Paint();
        pricePaint.setColor(getColor(R.color.capital_price));
        pricePaint.setAntiAlias(true);
        pricePaint.setStyle(Paint.Style.STROKE);
        pricePaint.setStrokeWidth(DEFAULT_PRICE_STROKE_WIDTH);
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
        dottedLinePaint.setColor(getColor(R.color.capital_column_line));
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
        // 绘制上表横线
        linePaint.setColor(getColor(R.color.capital_row_line));
        float rowSpacing = topTableHeight / getTopRowCount();
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
        xYTextPaint.setColor(getColor(R.color.capital_text));

        // 绘制开始区域时间值
        xYTextPaint.getTextBounds(TIME_TEXT[0], (0), TIME_TEXT[0].length(), textRect);
        canvas.drawText(TIME_TEXT[0], tableMargin, getTimeTextY(), xYTextPaint);

        // 绘制中间区域时间值
        xYTextPaint.getTextBounds(TIME_TEXT[1], (0), TIME_TEXT[1].length(), textRect);
        canvas.drawText(TIME_TEXT[1], (((viewWidth - textRect.right) >> 1) - tableMargin), getTimeTextY(), xYTextPaint);

        // 绘制结束区域时间值
        xYTextPaint.getTextBounds(TIME_TEXT[2], (0), TIME_TEXT[2].length(), textRect);
        canvas.drawText(TIME_TEXT[2], (viewWidth - textRect.right - tableMargin), getTimeTextY(), xYTextPaint);
    }

    @Override
    protected void onChildDraw(Canvas canvas) {
        super.onChildDraw(canvas);
        if (capital == null) {
            return;
        }

        // 绘制曲线
        drawLine(canvas);
    }

    /**
     * 绘制曲线
     */
    private void drawLine(Canvas canvas) {
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
    }
}
