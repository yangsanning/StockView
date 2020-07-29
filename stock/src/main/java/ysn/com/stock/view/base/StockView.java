package ysn.com.stock.view.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.text.DecimalFormat;

import ysn.com.stock.R;
import ysn.com.stock.bean.Extremum;
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

    protected Context context;

    protected LazyPaint lazyPaint = new LazyPaint();

    /**
     * 是否启用下表格
     */
    protected boolean enabledBottomTable;

    /**
     * totalCount: 总点数
     */
    private int totalCount = COUNT_DEFAULT;

    /**
     * 两边边距
     */
    protected float tableMargin = 1;

    protected int viewWidth, viewHeight;

    protected float titleTableHeight;
    protected float middleTableHeight;
    protected float topTableHeight;
    protected float bottomTableHeight;
    protected float timeTableHeight;

    protected float xYTextSize, xYTextMargin, titleTextSize;
    protected Paint textPaint;
    protected Rect textRect = new Rect();
    protected DecimalFormat decimalFormat;

    protected Paint linePaint;
    protected Path linePath;

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
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StockView);

        enabledBottomTable = typedArray.getBoolean(R.styleable.StockView_enabledBottomTable, Boolean.FALSE);

        typedArray.recycle();
    }

    protected void initPaint() {
        linePaint = new Paint();
        linePaint.setColor(getColor(R.color.stock_line));
        linePaint.setStrokeWidth(1f);
        linePaint.setStyle(Paint.Style.STROKE);

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

        if (isEnabledTitleTable()) {
            titleTableHeight = getTitleTableHeight();
            titleTextSize = getTitleTextSize();
        }

        if (isEnabledBottomTable()) {
            if (isEnabledMiddleTable()) {
                middleTableHeight = timeTableHeight;
            }
            topTableHeight = viewHeight * 0.7f - titleTableHeight - tableMargin;
            bottomTableHeight = viewHeight - titleTableHeight - topTableHeight - timeTableHeight - middleTableHeight - tableMargin * 2;
        } else {
            topTableHeight = viewHeight - titleTableHeight - middleTableHeight - timeTableHeight - tableMargin * 2;
        }

        xYTextMargin = xYTextSize / 5;
        textPaint.setTextSize(xYTextSize);
        lazyPaint.setTextSize(xYTextSize);
    }

    public boolean isEnabledTitleTable() {
        return false;
    }

    public boolean isEnabledMiddleTable() {
        return true;
    }

    public float getTitleTableHeight() {
        return middleTableHeight;
    }

    public float getTitleTextSize() {
        return xYTextSize;
    }

    public boolean isEnabledBottomTable() {
        return enabledBottomTable;
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
        // 绘制标题文本
        onTitleTextDraw(canvas);

        // 绘制时间文本
        onTimeTextDraw(canvas);
    }

    /**
     * 绘制标题文本
     */
    protected void onTitleTextDraw(Canvas canvas) {
    }

    /**
     * 绘制时间文本
     */
    protected void onTimeTextDraw(Canvas canvas) {
    }

    /**
     * 开放给子类自由绘制
     */
    protected void onChildDraw(Canvas canvas) {
    }

    /**
     * 获取上表格最小X
     */
    public float getTopTableMinX() {
        return getTableMinX();
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
     * 根据值进行上表格 y 坐标转换
     *
     * @param value    当前值
     * @param extremum 极值
     * @return 相应值 y 轴坐标
     */
    protected float getTopTableY(float value, Extremum extremum) {
        return ((getTopTableMinY()) * (value - extremum.getMinimum())) / extremum.getPeek();
    }

    /**
     * 获取上表格最小X
     */
    public float getBottomTableMinX() {
        return 0;
    }

    /**
     * 获取上表格最大X
     */
    public float getBottomTableMaxX() {
        return getTableMaxX() - getBottomTableMinX();
    }

    /**
     * 获取下表格最小Y
     */
    public float getBottomTableMinY() {
        return middleTableHeight;
    }

    /**
     * 获取下表格最大Y
     */
    public float getBottomTableMaxY() {
        return bottomTableHeight + middleTableHeight;
    }

    /**
     * 获取时间表格最小Y
     */
    public float getTimeTableMinY() {
        return middleTableHeight + bottomTableHeight;
    }

    /**
     * 获取时间表格最大Y
     */
    public float getTimeTableMaxY() {
        return middleTableHeight + bottomTableHeight + timeTableHeight;
    }

    /**
     * 获取时间表格最小X
     */
    public float getTimeTableMinX() {
        return 0;
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
     * 获取横线y轴坐标
     *
     * @param ySpace   横线y轴间隙
     * @param position 当前position
     * @return 横线y轴坐标
     */
    protected float getTopRowY(float ySpace, int position) {
        return getTopTableMaxY() - ySpace * position;
    }

    /**
     * 获取横线y轴坐标
     *
     * @param ySpace   横线y轴间隙
     * @param position 当前position
     * @return 横线y轴坐标
     */
    protected float getBottomRowY(float ySpace, int position) {
        return middleTableHeight + ySpace * position;
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
     * 获取表格最大Y
     */
    protected float getTableMaxY() {
        return middleTableHeight + bottomTableHeight;
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
        return viewWidth - tableMargin - getTableMinX();
    }

    /**
     * 获取表格最小X
     */
    protected float getTableMinX() {
        return tableMargin - getCircleX();
    }

    /**
     * 获取上表格宽
     */
    protected float getTopTableWidth() {
        return viewWidth - tableMargin * 2;
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

    public float getMiddleTableHeight() {
        return middleTableHeight;
    }

    public float getBottomTableHeight() {
        return bottomTableHeight;
    }

    public float getTimeTableHeight() {
        return timeTableHeight;
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
