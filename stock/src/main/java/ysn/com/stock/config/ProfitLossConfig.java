package ysn.com.stock.config;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ysn.com.stock.R;
import ysn.com.stock.paint.QuickPaint;

/**
 * @Author yangsanning
 * @ClassName ProfitLossConfig
 * @Description {@link ysn.com.stock.view.ProfitLossView} 的参数信息
 * @Date 2020/6/1
 */
public class ProfitLossConfig {

    /**
     * 上表被分割成几份
     */
    public static final int TOP_ROW_COUNT = 4;

    public int lineColor, textColor, valueLineColor, slideTextColor, slideBgColor;

    public Paint valueLinePaint, slidePointPaint, slideBgPaint;
    public QuickPaint quickPaint;
    public Path valueLinePath;
    public RectF slideRectF = new RectF();

    public float pointRadius = 6.66f;
    public int viewWidth, viewHeight;
    public int circleX, circleY;

    public float leftTableWidth;
    public float titleTableHeight;
    public float timeTableHeight;
    public float topTableWidth, topTableHeight;

    public float xYTextSize;

    /**
     * rowSpacing: 横线间距
     * topTableMaxY: 上表格最大Y
     */
    public float rowSpacing;
    public float topTableMaxY;

    public ProfitLossConfig(Context context, @Nullable AttributeSet attrs) {
        initAttr(context, attrs);
        initPaint();
    }

    public void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProfitLossView);

        lineColor = typedArray.getColor(R.styleable.ProfitLossView_plv_text_color, Color.parseColor("#aaaaaa"));
        textColor = typedArray.getColor(R.styleable.ProfitLossView_plv_text_color, Color.parseColor("#666666"));
        valueLineColor = typedArray.getColor(R.styleable.ProfitLossView_plv_value_line_color, Color.parseColor("#f27100"));
        slideTextColor = typedArray.getColor(R.styleable.ProfitLossView_plv_value_line_color, Color.parseColor("#ffffff"));
        slideBgColor = typedArray.getColor(R.styleable.ProfitLossView_plv_value_line_color, Color.parseColor("#90000000"));

        typedArray.recycle();
    }

    public void initPaint() {
        quickPaint = new QuickPaint();

        valueLinePath = new Path();
        valueLinePaint = new Paint();
        valueLinePaint.setColor(valueLineColor);
        valueLinePaint.setAntiAlias(true);
        valueLinePaint.setStyle(Paint.Style.STROKE);
        valueLinePaint.setStrokeWidth(2f);

        slidePointPaint = new Paint();
        slidePointPaint.setAntiAlias(true);

        slideBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        slideBgPaint.setStyle(Paint.Style.FILL);
        slideBgPaint.setColor(slideBgColor);
    }

    public void onSizeChanged(int witdh, int heigt, int oldWidth, int oldheight) {
        viewWidth = witdh;
        viewHeight = heigt;

        leftTableWidth = viewWidth / 6f;
        titleTableHeight = timeTableHeight = viewHeight * 0.07f;
        topTableWidth = viewWidth - leftTableWidth - pointRadius;
        topTableHeight = viewHeight - titleTableHeight - timeTableHeight;

        circleX = (int) leftTableWidth;
        circleY = (int) (viewHeight - timeTableHeight);

        xYTextSize = timeTableHeight * 0.65f;
        quickPaint.setTextSize(xYTextSize);

        topTableMaxY = titleTableHeight - topTableHeight;
        rowSpacing = topTableHeight / ProfitLossConfig.TOP_ROW_COUNT;
    }
}
