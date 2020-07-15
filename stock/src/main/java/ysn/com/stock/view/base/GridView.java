package ysn.com.stock.view.base;

import android.content.Context;
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
     * 列数+1
     */
    private int columnCount = 4;

    /**
     * 上表横数+1
     */
    private int topRowCount = 4;

    /**
     * 下表横数+1
     */
    private int bottomRowCount = 2;

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
    protected void onBaseDraw(Canvas canvas) {
        super.onBaseDraw(canvas);
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
        float xSpace = (viewWidth - 2 * tableMargin) / getColumnCount();
        for (int i = 1; i < getColumnCount(); i++) {
            float x = getColumnX(xSpace, i);
            lazyPaint.drawPath(canvas, x, getTopTableMaxY(), x, getTopTableMinY());
        }

        // 绘制下表竖线
        if (isEnabledBottomTable()) {
            for (int i = 1; i < getColumnCount(); i++) {
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
        for (int i = 1; i < getTopRowCount(); i++) {
            float y = getTopRowY(rowSpacing, i);
            int color = getColor(i != getTopRowCount() / 2 ? R.color.stock_dotted_column_line : R.color.stock_dotted_row_line);
            lazyPaint.setLineColor(color)
                    .drawPath(canvas, tableMargin, y, (viewWidth - tableMargin), y);
        }

        // 绘制下表横线
        if (isEnabledBottomTable()) {
            rowSpacing = getBottomRowSpacing();
            lazyPaint.setLineColor(getColor(R.color.stock_dotted_column_line));
            for (int i = 1; i < getBottomRowCount(); i++) {
                float y = getBottomRowY(rowSpacing, i);
                lazyPaint.drawPath(canvas, tableMargin, y, (viewWidth - tableMargin), y);
            }
        }
    }

    protected int getColumnCount() {
        return columnCount;
    }

    protected int getTopRowCount() {
        return topRowCount;
    }

    protected float getTopRowSpacing() {
        return topTableHeight / getTopRowCount();
    }

    protected int getBottomRowCount() {
        return bottomRowCount;
    }

    protected float getBottomRowSpacing() {
        return bottomTableHeight / getBottomRowCount();
    }

    /**
     * 获取上表格点间距
     */
    protected float getTopXSpace() {
        return getTopTableWidth() / getColumnCount();
    }
}
