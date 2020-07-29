package ysn.com.stock.view.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import ysn.com.stock.R;
import ysn.com.stock.paint.LazyTextPaint;

/**
 * @Author yangsanning
 * @ClassName GridView
 * @Description 网格控件
 * @Date 2020/7/14
 */
public class GridView extends StockView {

    public static final String[] TIME_TEXT = new String[]{"09:30", "11:30/13:00", "15:00"};

    /**
     * verticalPart: 垂直方向分成几部分（竖虚线+1）
     * topHorizontalPart: 上表格水平方向分成几部分（上表格横虚线+1）
     * bottomHorizontalPart: 下表格水平方向分成几部分（下表格横虚线+1）
     */
    private int partVertical, partTopHorizontal, partBottomHorizontal;

    /**
     * verticalDottedColor: 表格垂直方向虚线颜色
     * horizontalDottedColor: 表格水平方向虚线颜色
     * horizontalDottedDeepenColor: 表格水平方向虚线加深颜色
     */
    private int colorVerticalDotted, colorHorizontalDotted, colorHorizontalDottedDeepen;

    /**
     * 虚线效果
     */
    protected DashPathEffect dottedPathEffect = new DashPathEffect(new float[]{2, 2, 2, 2}, 1);

    private float[] topTablePts, bottomTablePts;

    public GridView(Context context) {
        super(context);
    }

    public GridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initAttr(AttributeSet attrs) {
        super.initAttr(attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GridView);

        partVertical = typedArray.getInt(R.styleable.GridView_partVertical, 4);
        partTopHorizontal = typedArray.getInt(R.styleable.GridView_partTopHorizontal, 4);
        partBottomHorizontal = typedArray.getInt(R.styleable.GridView_partBottomHorizontal, 2);

        colorVerticalDotted = typedArray.getColor(R.styleable.GridView_colorVerticalDotted, getColor(R.color.stock_dotted_column_line));
        colorHorizontalDotted = typedArray.getColor(R.styleable.GridView_colorHorizontalDotted, getColor(R.color.stock_dotted_column_line));
        colorHorizontalDottedDeepen = typedArray.getColor(R.styleable.GridView_colorHorizontalDottedDeepen, getColor(R.color.stock_dotted_row_line));

        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float topTableMaxX = getTopTableMaxX();
        float topTableMinX = getTopTableMinX();
        float topTableMaxY = getTopTableMaxY();
        float topTableMinY = getTopTableMinY();
        topTablePts = new float[]{topTableMinX, topTableMinY, topTableMinX, topTableMaxY,
                topTableMinX, topTableMaxY, topTableMaxX, topTableMaxY,
                topTableMaxX, topTableMaxY, topTableMaxX, topTableMinY,
                topTableMaxX, topTableMinY, topTableMinX, topTableMinY};

        float bottomTableMaxX = getBottomTableMaxX();
        float bottomTableMinX = getBottomTableMinX();
        float bottomTableMaxY = getBottomTableMaxY();
        float bottomTableMinY = getBottomTableMinY();
        bottomTablePts = new float[]{bottomTableMinX, bottomTableMinY, bottomTableMinX, bottomTableMaxY,
                bottomTableMinX, bottomTableMaxY, bottomTableMaxX, bottomTableMaxY,
                bottomTableMaxX, bottomTableMaxY, bottomTableMaxX, bottomTableMinY,
                bottomTableMaxX, bottomTableMinY, bottomTableMinX, bottomTableMinY};
    }

    /**
     * 绘制默认时间坐标
     */
    @Override
    protected void onTimeTextDraw(Canvas canvas) {
        lazyPaint.setTextColor(getColor(R.color.stock_text_title))
                .measure(TIME_TEXT[0], lazyTextPaint -> {
                    // 绘制开始区域时间值
                    float y = getTimeTableMinY() + lazyTextPaint.centerY(getTimeTableHeight());
                    lazyTextPaint.drawText(canvas, getTableMargin(), y);
                })
                .measure(TIME_TEXT[1], lazyTextPaint -> {
                    // 绘制中间区域时间值
                    float x = getTimeTableMinX() + lazyTextPaint.centerX(getTopTableWidth());
                    float y = getTimeTableMinY() + lazyTextPaint.centerY(getTimeTableHeight());
                    lazyTextPaint.drawText(canvas, x, y);
                })
                .measure(TIME_TEXT[2], lazyTextPaint -> {
                    // 绘制结束区域时间值
                    float x = getTopTableMaxX() - lazyTextPaint.width() - getTableMargin();
                    float y = getTimeTableMinY() + lazyTextPaint.centerY(getTimeTableHeight());
                    lazyTextPaint.drawText(canvas, x, y);
                });
    }

    @Override
    protected void onChildDraw(Canvas canvas) {
        super.onChildDraw(canvas);
        // 绘制边框
        onBordersDraw(canvas);

        // 设置虚线效果
        lazyPaint.setPathEffect(dottedPathEffect);

        // 绘制竖线
        onColumnLineDraw(canvas);
        // 绘制横线
        onRowLineDraw(canvas);

        // 重置虚线效果
        lazyPaint.resetPathEffect();
    }

    /**
     * 绘制边框
     */
    protected void onBordersDraw(Canvas canvas) {
        // 上表边框
        lazyPaint.setLineColor(getColor(R.color.stock_line))
                .setLineStrokeWidth(1f)
                .drawLines(canvas, topTablePts);

        // 下表边框
        if (isEnabledBottomTable()) {
            lazyPaint.drawLines(canvas, bottomTablePts);
        }
    }

    /**
     * 绘制竖线
     */
    protected void onColumnLineDraw(Canvas canvas) {
        // 绘制上表竖线
        lazyPaint.setLineColor(colorVerticalDotted);
        float xSpace = (viewWidth - 2 * tableMargin) / getPartVertical();
        for (int i = 1; i < getPartVertical(); i++) {
            float x = getColumnX(xSpace, i);
            lazyPaint.drawPath(canvas, x, getTopTableMaxY(), x, getTopTableMinY());
        }

        // 绘制下表竖线
        if (isEnabledBottomTable()) {
            for (int i = 1; i < getPartVertical(); i++) {
                float x = getColumnX(xSpace, i);
                lazyPaint.drawPath(canvas, x, getBottomTableMinY(), x, getBottomTableMaxY());
            }
        }
    }

    /**
     * 绘制横线
     */
    protected void onRowLineDraw(Canvas canvas) {
        // 绘制上表横线
        float rowSpacing = getTopRowSpacing();
        for (int i = 1; i < getPartTopHorizontal(); i++) {
            float y = getTopRowY(rowSpacing, i);
            int color = i == getPartTopHorizontal() / 2 ? colorHorizontalDottedDeepen : colorHorizontalDotted;
            lazyPaint.setLineColor(color).drawPath(canvas, tableMargin, y, (viewWidth - tableMargin), y);
        }

        // 绘制下表横线
        if (isEnabledBottomTable()) {
            rowSpacing = getBottomRowSpacing();
            lazyPaint.setLineColor(getColor(R.color.stock_dotted_column_line));
            for (int i = 1; i < getPartBottomHorizontal(); i++) {
                float y = getBottomRowY(rowSpacing, i);
                lazyPaint.drawPath(canvas, tableMargin, y, (viewWidth - tableMargin), y);
            }
        }
    }

    /**
     * 根据当前 position 以及当前文本绘制的默认上表格 y 坐标，获取相应横线对应的坐标上表格 y 坐标
     * 首行文字绘制在横线上方，末行绘制在横线下方，其余居中横线
     *
     * @param position      当前 position
     * @param defaultY      计算出来的 Y 值
     * @param lazyTextPaint
     * @return 换算后的文本绘制上表格 y 坐标
     */
    protected float getTopCoordinateY(int position, float defaultY, LazyTextPaint lazyTextPaint) {
        return getCoordinateY(position, getPartTopHorizontal(), defaultY, lazyTextPaint);
    }

    /**
     * 根据当前 position 以及当前文本绘制的默认 y 坐标，获取相应横线对应的坐标 y 坐标
     * 首行文字绘制在横线上方，末行绘制在横线下方，其余居中横线
     *
     * @param position      当前 position
     * @param lastPosition  最后一条横线的 position
     * @param defaultY      计算出来的 Y 值
     * @param lazyTextPaint
     * @return 换算后的文本绘制 y 坐标
     */
    protected float getCoordinateY(int position, int lastPosition, float defaultY, LazyTextPaint lazyTextPaint) {
        float coordinateY;
        if (position == 0) {
            coordinateY = -xYTextMargin;
        } else if (position == lastPosition) {
            coordinateY = defaultY + lazyTextPaint.height() + xYTextMargin;
        } else {
            coordinateY = defaultY + lazyTextPaint.height() / 2f;
        }
        return coordinateY;
    }

    protected int getPartVertical() {
        return partVertical;
    }

    protected int getPartTopHorizontal() {
        return partTopHorizontal;
    }

    protected float getTopRowSpacing() {
        return topTableHeight / getPartTopHorizontal();
    }

    protected int getPartBottomHorizontal() {
        return partBottomHorizontal;
    }

    protected float getBottomRowSpacing() {
        return bottomTableHeight / getPartBottomHorizontal();
    }
}
