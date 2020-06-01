package ysn.com.stock.config;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.text.DecimalFormat;

import ysn.com.stock.R;

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

    public int lineColor, textColor, priceLineColor;

    public Paint linePaint, textPaint, priceLinePaint;
    public Path linePath, priceLinePath;
    public Rect textRect;
    public DecimalFormat decimalFormat;

    public int viewWidth, viewHeight;
    public int circleX, circleY;

    public float leftTableWidth;
    public float titleTableHeight;
    public float timeTableHeight;
    public float topTableWidth, topTableHeight;

    public float xYTextSize;

    /**
     * 横线间距
     */
    public float rowSpacing;

    public ProfitLossConfig(Context context, @Nullable AttributeSet attrs) {
        initAttr(context, attrs);
        initPaint();
    }

    public void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProfitLossView);

        lineColor = typedArray.getColor(R.styleable.ProfitLossView_plv_text_color, Color.parseColor("#aaaaaa"));
        textColor = typedArray.getColor(R.styleable.ProfitLossView_plv_text_color, Color.parseColor("#666666"));
        priceLineColor = typedArray.getColor(R.styleable.ProfitLossView_plv_price_color, Color.parseColor("#f27100"));

        typedArray.recycle();
    }

    public void initPaint() {
        linePaint = new Paint();
        linePaint.setStrokeWidth(1f);
        linePaint.setStyle(Paint.Style.STROKE);

        linePath = new Path();

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textRect = new Rect();

        priceLinePath = new Path();
        priceLinePaint = new Paint();
        priceLinePaint.setColor(priceLineColor);
        priceLinePaint.setAntiAlias(true);
        priceLinePaint.setStyle(Paint.Style.STROKE);
        priceLinePaint.setStrokeWidth(2f);

        decimalFormat = new DecimalFormat("0.00");
    }

    public void onSizeChanged(int witdh, int heigt, int oldWidth, int oldheight) {
        viewWidth = witdh;
        viewHeight = heigt;

        leftTableWidth = viewWidth / 6f;
        titleTableHeight = timeTableHeight = viewHeight * 0.07f;
        topTableWidth = viewWidth - leftTableWidth;
        topTableHeight = viewHeight - titleTableHeight - timeTableHeight;

        circleX = (int) leftTableWidth;
        circleY = (int) (viewHeight - timeTableHeight);

        xYTextSize = timeTableHeight * 0.65f;
        textPaint.setTextSize(xYTextSize);

        rowSpacing = topTableHeight / ProfitLossConfig.TOP_ROW_COUNT;
    }
}
