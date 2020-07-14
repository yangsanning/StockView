package ysn.com.stock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.text.DecimalFormat;

import ysn.com.stock.R;
import ysn.com.stock.paint.LazyPaint;

/**
 * @Author yangsanning
 * @ClassName BaseView
 * @Description 一句话概括作用
 * @Date 2019/5/4
 * @History 2019/5/4 author: description:
 */
public class StockView extends View {

    private static final int COUNT_DEFAULT = 240;

    /**
     * 默认虚线效果
     */
    private static final PathEffect DEFAULT_DASH_EFFECT = new DashPathEffect(new float[]{2, 2, 2, 2}, 1);

    protected Context context;

    protected LazyPaint lazyPaint = new LazyPaint();

    /**
     * totalCount: 总点数
     */
    private int totalCount = COUNT_DEFAULT;

    /**
     * columnCount: 列数+1
     * topRowCount: 上表横数+1
     * bottomRowCount: 下表横数+1
     */
    private int columnCount = 4;
    private int topRowCount = 4;
    private int bottomRowCount = 2;

    /**
     * 两边边距
     */
    protected float tableMargin = 1;

    protected int viewWidth, viewHeight;

    protected float titleTableHeight;
    protected float timeTableHeight;
    protected float topTableHeight;
    protected float bottomTableHeight;

    protected float xYTextSize, xYTextMargin, titleTextSize;
    protected Paint textPaint;
    protected Rect textRect = new Rect();
    protected DecimalFormat decimalFormat;

    protected Paint linePaint, dottedLinePaint;
    protected Path linePath;

    /**
     * 虚线效果
     */
    protected PathEffect mDashEffect = DEFAULT_DASH_EFFECT;

    public StockView(Context context) {
        this(context, null);
    }

    public StockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
        initAttr(attrs);
        initPaint();
    }

    protected void initAttr(AttributeSet attrs) {

    }

    protected void initPaint() {
        linePaint = new Paint();
        linePaint.setColor(getColor(R.color.stock_line));
        linePaint.setStrokeWidth(1f);
        linePaint.setStyle(Paint.Style.STROKE);

        dottedLinePaint = new Paint();
        dottedLinePaint.setStyle(Paint.Style.STROKE);
        dottedLinePaint.setPathEffect(mDashEffect);
        dottedLinePaint.setStrokeWidth(1f);

        linePath = new Path();

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextAlign(Paint.Align.LEFT);

        decimalFormat = new DecimalFormat("0.00");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;

        timeTableHeight = viewHeight * 0.06f;
        xYTextSize = timeTableHeight * 0.8f;

        if (hasTitleTable()) {
            titleTableHeight = getTitleTableHeight();
            titleTextSize = getTitleTextSize();
        }

        if (hasBottomTable()) {
            topTableHeight = viewHeight * 0.7f - titleTableHeight - tableMargin;
            bottomTableHeight = viewHeight - titleTableHeight - topTableHeight - timeTableHeight - tableMargin * 2;
        } else {
            topTableHeight = viewHeight - titleTableHeight - timeTableHeight - tableMargin * 2;
        }

        xYTextMargin = xYTextSize / 5;
        textPaint.setTextSize(xYTextSize);
    }

    public boolean hasTitleTable() {
        return false;
    }

    public float getTitleTableHeight() {
        return timeTableHeight;
    }

    public float getTitleTextSize() {
        return xYTextSize;
    }

    public boolean hasBottomTable() {
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getCircleX(), getCircleY());

        // 基础绘制
        onBaseDraw(canvas);

        // 开放给子类自由绘制
        onChildDraw(canvas);

        canvas.restore();
    }

    /**
     * 圆心X坐标
     */
    protected int getCircleX() {
        return (int) tableMargin;
    }

    /**
     * 圆心X坐标
     */
    protected int getCircleY() {
        return (int) (topTableHeight + titleTableHeight + tableMargin);
    }

    /**
     * 基础绘制
     */
    protected void onBaseDraw(Canvas canvas) {
        // 绘制边框
        onBordersDraw(canvas);
        // 绘制竖线
        onColumnLineDraw(canvas);
        // 绘制横线
        onRowLineDraw(canvas);
        // 绘制时间坐标
        onTimeTextDraw(canvas);
    }

    /**
     * 绘制边框
     */
    protected void onBordersDraw(Canvas canvas) {
        // 上表边框
        lazyPaint.setLineColor(getColor(R.color.stock_line))
                .setLineStrokeWidth(1f)
                .moveTo(getTopTableMinX(), getTopTableMaxY())
                .lineTo(getTopTableMinX(), getTopTableMinY())
                .lineTo(getTableMaxX(), getTopTableMinY())
                .lineTo(getTableMaxX(), getTopTableMaxY())
                .finishPath(canvas);

        // 下表边框
        if (hasBottomTable()) {
            lazyPaint.moveTo(getBottomTableMinX(), getBottomTableMinY())
                    .lineTo(getBottomTableMinX(), getBottomTableMaxY())
                    .lineTo(getBottomTableMaxX(), getBottomTableMaxY())
                    .lineTo(getBottomTableMaxX(), getBottomTableMinY())
                    .finishPath(canvas);
        }
    }

    /**
     * 获取上表格最小X
     */
    public float getTopTableMinX() {
        return getCircleX();
    }

    /**
     * 获取上表格最大X
     */
    public float getTopTableMaxX() {
        return getTableMaxX() - getCircleX();
    }

    /**
     * 获取上表格最小Y
     */
    public float getTopTableMinY() {
        return -topTableHeight;
    }

    /**
     * 获取上表格最大Y（圆点即上表格最大高度）
     */
    public float getTopTableMaxY() {
        return 0;
    }

    /**
     * 获取上表格最小X
     */
    public float getBottomTableMinX() {
        return getCircleX();
    }

    /**
     * 获取上表格最大X
     */
    public float getBottomTableMaxX() {
        return getTableMaxX() - getBottomTableMinX();
    }

    /**
     * 获取上表格最小Y
     */
    public float getBottomTableMinY() {
        return timeTableHeight;
    }

    /**
     * 获取上表格最大Y
     */
    public float getBottomTableMaxY() {
        return bottomTableHeight + timeTableHeight;
    }

    /**
     * 绘制竖线
     */
    protected void onColumnLineDraw(Canvas canvas) {
        // 绘制上表竖线
        dottedLinePaint.setColor(getColor(R.color.stock_dotted_column_line));
        float xSpace = (viewWidth - 2 * tableMargin) / getColumnCount();
        for (int i = 1; i < getColumnCount(); i++) {
            linePath.reset();
            float x = getColumnX(xSpace, i);
            linePath.moveTo(x, getTopTableMaxY());
            linePath.lineTo(x, getTopTableMinY());
            canvas.drawPath(linePath, dottedLinePaint);
        }

        // 绘制下表竖线
        if (hasBottomTable()) {
            for (int i = 1; i < getColumnCount(); i++) {
                linePath.reset();
                float x = getColumnX(xSpace, i);
                linePath.moveTo(x, getBottomTableMinY());
                linePath.lineTo(x, getBottomTableMaxY());
                canvas.drawPath(linePath, dottedLinePaint);
            }
        }
    }

    protected int getColumnCount() {
        return columnCount;
    }

    /**
     * 获取竖线x轴坐标
     *
     * @param xSpace   竖线x轴间隙
     * @param position 当前position
     * @return 竖线x轴坐标
     */
    protected float getColumnX(float xSpace, int position) {
        return tableMargin + xSpace * position;
    }

    /**
     * 绘制横线
     */
    protected void onRowLineDraw(Canvas canvas) {
        // 绘制上表横线
        float rowSpacing = getTopRowSpacing();
        for (int i = 1; i < getTopRowCount(); i++) {
            linePath.reset();
            float y = getTopRowY(rowSpacing, i);
            linePath.moveTo(tableMargin, y);
            linePath.lineTo((viewWidth - tableMargin), y);
            dottedLinePaint.setColor(getColor(i != getTopRowCount() / 2 ?
                    R.color.stock_dotted_column_line : R.color.stock_dotted_row_line));
            canvas.drawPath(linePath, dottedLinePaint);
        }

        // 绘制下表横线
        if (hasBottomTable()) {
            rowSpacing = getBottomRowSpacing();
            dottedLinePaint.setColor(getColor(R.color.stock_dotted_column_line));
            for (int i = 1; i < getBottomRowCount(); i++) {
                linePath.reset();
                float y = getBottomRowY(rowSpacing, i);
                linePath.moveTo(tableMargin, y);
                linePath.lineTo((viewWidth - tableMargin), y);
                canvas.drawPath(linePath, dottedLinePaint);
            }
        }
    }

    protected int getTopRowCount() {
        return topRowCount;
    }

    protected float getTopRowSpacing() {
        return topTableHeight / getTopRowCount();
    }

    /**
     * 获取横线y轴坐标
     *
     * @param ySpace   横线y轴间隙
     * @param position 当前position
     * @return 横线y轴坐标
     */
    protected float getTopRowY(float ySpace, int position) {
        return getTopTableMinY() + ySpace * position;
    }

    protected int getBottomRowCount() {
        return bottomRowCount;
    }

    protected float getBottomRowSpacing() {
        return bottomTableHeight / getBottomRowCount();
    }

    /**
     * 获取横线y轴坐标
     *
     * @param ySpace   横线y轴间隙
     * @param position 当前position
     * @return 横线y轴坐标
     */
    protected float getBottomRowY(float ySpace, int position) {
        return timeTableHeight + ySpace * position;
    }

    /**
     * 绘制时间坐标
     */
    protected void onTimeTextDraw(Canvas canvas) {
    }

    /**
     * 注意: 要先进行测量文本 getTextBounds
     *
     * @return 时间文字的Y坐标
     */
    protected float getTimeTextY() {
        return getTextMargin() + textRect.height();
    }

    /**
     * 注意: 要先进行测量文本 getTextBounds
     *
     * @return 文本距离XY轴的Margin
     */
    protected float getTextMargin() {
        return (timeTableHeight - textRect.height()) / 2f;
    }

    /**
     * 获取x轴坐标
     *
     * @param position 当前position
     * @return x轴坐标
     */
    public float getX(int position) {
        return getColumnX(((viewWidth - tableMargin * 2) / (float) getTotalCount()), position);
    }

    /**
     * 获取y轴坐标
     *
     * @param value    当前值
     * @param minValue 最小值
     * @param maxValue 最大值
     * @return y轴坐标
     */
    protected float getY(float value, float minValue, float maxValue) {
        return ((getTopTableMinY()) * (value - minValue)) / (maxValue - minValue);
    }

    /**
     * 开放给子类自由绘制
     */
    protected void onChildDraw(Canvas canvas) {
    }

    /**
     * 获取表格最大Y
     */
    protected float getTableMaxY() {
        return timeTableHeight + bottomTableHeight;
    }

    /**
     * 获取表格最小Y
     */
    protected float getTableMinY() {
        return -(titleTableHeight + topTableHeight);
    }

    /**
     * 获取表格最大X
     */
    protected float getTableMaxX() {
        return viewWidth - tableMargin;
    }

    /**
     * 获取表格最小X
     */
    protected float getTableMinX() {
        return tableMargin;
    }

    /**
     * 获取上表格宽
     */
    protected float getTopTableWidth() {
        return viewWidth - tableMargin * 2;
    }

    /**
     * 获取上表格点间距
     */
    protected float getTopXSpace() {
        return getTopTableWidth() / getColumnCount();
    }

    protected int getColor(@ColorRes int colorRes) {
        return context.getResources().getColor(colorRes);
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public float getTopTableHeight() {
        return topTableHeight;
    }

    public float getTimeTableHeight() {
        return timeTableHeight;
    }

    public float getBottomTableHeight() {
        return bottomTableHeight;
    }

    public float getTableMargin() {
        return tableMargin;
    }

    public float getXYTextMargin() {
        return xYTextMargin;
    }

    public Paint getTextPaint() {
        return textPaint;
    }

    public Rect getTextRect() {
        return textRect;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        if (totalCount != 0) {
            this.totalCount = totalCount;
        }
    }
}
