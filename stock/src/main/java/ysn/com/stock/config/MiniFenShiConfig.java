package ysn.com.stock.config;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import ysn.com.stock.R;
import ysn.com.stock.utils.ResUtils;

/**
 * @Author yangsanning
 * @ClassName MiniFenShiConfig
 * @Description 迷你分时参数配置
 * @Date 2020/6/18
 */
public class MiniFenShiConfig {

    /**
     * upColor: 涨颜色
     * downColor: 跌颜色
     * equalColor: 不涨不跌颜色
     * gradientBottomColor: 渐变底部颜色
     * strokeWidth: 实线宽
     * areaAlpha: 价格区域透明度
     * pathEffect: 路径效果
     */
    public int upColor;
    public int downColor;
    public int equalColor;
    public int gradientBottomColor;
    public int strokeWidth;
    public int areaAlpha;
    public PathEffect pathEffect;
    public boolean alwaysShowDottedLine;

    /**
     * 当前颜色
     */
    public int currentColor;

    public Path pricePath, priceAreaPath;
    public Paint pricePaint, priceAreaPaint, dottedLinePaint;

    public MiniFenShiConfig(Context context, @Nullable AttributeSet attrs) {
        initAttr(context, attrs);
        initPaint();
    }

    public void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MiniFenShiView);

        upColor = typedArray.getColor(R.styleable.MiniFenShiView_upColor, ResUtils.getColor(context, R.color.mini_fen_shi_up));
        downColor = typedArray.getColor(R.styleable.MiniFenShiView_downColor, ResUtils.getColor(context, R.color.mini_fen_shi_down));
        equalColor = typedArray.getColor(R.styleable.MiniFenShiView_equalColor, ResUtils.getColor(context, R.color.mini_fen_shi_equal));
        gradientBottomColor = typedArray.getColor(R.styleable.MiniFenShiView_gradientBottomColor, ResUtils.getColor(context, R.color.mini_fen_shi_gradient_bottom));

        strokeWidth = typedArray.getDimensionPixelSize(R.styleable.MiniFenShiView_strokeWidth, 4);
        areaAlpha = typedArray.getDimensionPixelSize(R.styleable.MiniFenShiView_areaAlpha, 150);

        int dottedWidth = typedArray.getDimensionPixelSize(R.styleable.MiniFenShiView_dottedLineWidth, 20);
        int dottedSpace = typedArray.getDimensionPixelSize(R.styleable.MiniFenShiView_dottedLineSpace, 20);
        pathEffect = new DashPathEffect(new float[]{dottedWidth, dottedSpace}, 0);

        alwaysShowDottedLine = typedArray.getBoolean(R.styleable.MiniFenShiView_alwaysShowDottedLine, false);

        typedArray.recycle();
    }

    public void initPaint() {
        pricePath = new Path();
        pricePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pricePaint.setAntiAlias(true);
        pricePaint.setStyle(Paint.Style.STROKE);
        pricePaint.setStrokeWidth(strokeWidth);
        pricePaint.setStrokeJoin(Paint.Join.ROUND);
        pricePaint.setStrokeCap(Paint.Cap.ROUND);

        priceAreaPath = new Path();
        priceAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        priceAreaPaint.setStyle(Paint.Style.FILL);
        priceAreaPaint.setAlpha(areaAlpha);

        dottedLinePaint = new Paint();
        dottedLinePaint.setStyle(Paint.Style.STROKE);
        dottedLinePaint.setPathEffect(pathEffect);
        dottedLinePaint.setStrokeWidth(1f);
    }
}
