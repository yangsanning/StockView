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

/**
 * @Author yangsanning
 * @ClassName GridView
 * @Description 网格控件
 * @Date 2020/7/14
 */
public class GridView extends StockView {

    /**
     * 垂直方向分成几部分（竖虚线+1）
     */
    private int tableVerticalPart;

    /**
     * 上表格水平方向分成几部分（上表格横虚线+1）
     */
    private int topTableHorizontalPart;

    /**
     * 下表格水平方向分成几部分（下表格横虚线+1）
     */
    private int bottomTableHorizontalPart;

    /**
     * 虚线效果
     */
    protected DashPathEffect dottedPathEffect = new DashPathEffect(new float[]{2, 2, 2, 2}, 1);

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

        tableVerticalPart = typedArray.getInt(R.styleable.GridView_tableVerticalPart, 4);
        topTableHorizontalPart = typedArray.getInt(R.styleable.GridView_topTableHorizontalPart, 4);
        bottomTableHorizontalPart = typedArray.getInt(R.styleable.GridView_bottomTableHorizontalPart, 2);

        typedArray.recycle();
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
                .moveTo(getTopTableMinX(), getTopTableMaxY())
                .lineTo(getTopTableMinX(), getTopTableMinY())
                .lineTo(getTableMaxX(), getTopTableMinY())
                .lineTo(getTableMaxX(), getTopTableMaxY())
                .finishPath(canvas);

        // 下表边框
        if (isEnabledBottomTable()) {
            lazyPaint.moveTo(getBottomTableMinX(), getBottomTableMinY())
                    .lineTo(getBottomTableMinX(), getBottomTableMaxY())
                    .lineTo(getBottomTableMaxX(), getBottomTableMaxY())
                    .lineTo(getBottomTableMaxX(), getBottomTableMinY())
                    .finishPath(canvas);
        }
    }

    /**
     * 绘制竖线
     */
    protected void onColumnLineDraw(Canvas canvas) {
        // 绘制上表竖线
        lazyPaint.setLineColor(getColor(R.color.stock_dotted_column_line));
        float xSpace = (viewWidth - 2 * tableMargin) / getTableVerticalPart();
        for (int i = 1; i < getTableVerticalPart(); i++) {
            float x = getColumnX(xSpace, i);
            lazyPaint.drawPath(canvas, x, getTopTableMaxY(), x, getTopTableMinY());
        }

        // 绘制下表竖线
        if (isEnabledBottomTable()) {
            for (int i = 1; i < getTableVerticalPart(); i++) {
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
        for (int i = 1; i < getTopTableHorizontalPart(); i++) {
            float y = getTopRowY(rowSpacing, i);
            int color = getColor(i != getTopTableHorizontalPart() / 2 ? R.color.stock_dotted_column_line : R.color.stock_dotted_row_line);
            lazyPaint.setLineColor(color)
                    .drawPath(canvas, tableMargin, y, (viewWidth - tableMargin), y);
        }

        // 绘制下表横线
        if (isEnabledBottomTable()) {
            rowSpacing = getBottomRowSpacing();
            lazyPaint.setLineColor(getColor(R.color.stock_dotted_column_line));
            for (int i = 1; i < getBottomTableHorizontalPart(); i++) {
                float y = getBottomRowY(rowSpacing, i);
                lazyPaint.drawPath(canvas, tableMargin, y, (viewWidth - tableMargin), y);
            }
        }
    }

    protected int getTableVerticalPart() {
        return tableVerticalPart;
    }

    protected int getTopTableHorizontalPart() {
        return topTableHorizontalPart;
    }

    protected float getTopRowSpacing() {
        return topTableHeight / getTopTableHorizontalPart();
    }

    protected int getBottomTableHorizontalPart() {
        return bottomTableHorizontalPart;
    }

    protected float getBottomRowSpacing() {
        return bottomTableHeight / getBottomTableHorizontalPart();
    }

    /**
     * 获取上表格点间距
     */
    protected float getTopXSpace() {
        return getTopTableWidth() / getTableVerticalPart();
    }
}
