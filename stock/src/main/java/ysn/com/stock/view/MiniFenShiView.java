package ysn.com.stock.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import ysn.com.stock.R;
import ysn.com.stock.bean.FenShi;
import ysn.com.stock.bean.FenShiData;

/**
 * @Author yangsanning
 * @ClassName FenShiView
 * @Description 普通分时图
 * @Date 2019/5/4
 * @History 2019/5/4 author: description:
 */
public class MiniFenShiView extends StockView {

    private List<Float> stockPriceList = new ArrayList<>();
    private float lastClose = 0.0f;
    private float maxStockPrice = Float.MIN_VALUE;
    private float minStockPrice = Float.MAX_VALUE;

    private Path pricePath;
    private Paint pricePaint;
    private Path priceAreaPath;
    private Paint priceAreaPaint;

    /**
     * upColor: 涨颜色
     * downColor: 跌颜色
     * equalColor: 不涨不跌颜色
     * gradientBottomColor: 渐变底部颜色
     */
    private int upColor;
    private int downColor;
    private int equalColor;
    private int gradientBottomColor;

    private int currentColor;

    private int strokeWidth;
    private int alpha;

    private PathEffect pathEffect;

    public MiniFenShiView(Context context) {
        super(context);
    }

    public MiniFenShiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MiniFenShiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MiniFenShiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initAttr(AttributeSet attrs) {
        super.initAttr(attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MiniFenShiView);

        upColor = typedArray.getColor(R.styleable.MiniFenShiView_mfsv_up, getColor(R.color.mini_fen_shi_up));
        downColor = typedArray.getColor(R.styleable.MiniFenShiView_mfsv_down, getColor(R.color.mini_fen_shi_down));
        equalColor = typedArray.getColor(R.styleable.MiniFenShiView_mfsv_equal, getColor(R.color.mini_fen_shi_equal));
        gradientBottomColor = typedArray.getColor(R.styleable.MiniFenShiView_mfsv_gradient_bottom, getColor(R.color.mini_fen_shi_gradient_bottom));

        strokeWidth = typedArray.getDimensionPixelSize(R.styleable.MiniFenShiView_mfsv_stroke_width, 4);
        alpha = typedArray.getDimensionPixelSize(R.styleable.MiniFenShiView_mfsv_alpha, 150);

        int dottedWidth = typedArray.getDimensionPixelSize(R.styleable.MiniFenShiView_mfsv_dotted_line_width, 20);
        int dottedSpace = typedArray.getDimensionPixelSize(R.styleable.MiniFenShiView_mfsv_dotted_line_space, 20);
        pathEffect = new DashPathEffect(new float[]{dottedWidth, dottedSpace}, 0);

        typedArray.recycle();
    }

    @Override
    protected void initPaint() {
        super.initPaint();
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
        priceAreaPaint.setAlpha(alpha);

        dottedLinePaint.setPathEffect(pathEffect);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;

        topTableHeight = viewHeight;
    }

    @Override
    protected void onBaseDraw(Canvas canvas) {
    }

    @Override
    protected void onChildDraw(Canvas canvas) {
        super.onChildDraw(canvas);

        if (stockPriceList.isEmpty()) {
            // 绘制默认中线
            drawLastClose(canvas, getTopTableMaxY() / 2);
            return;
        }

        //渐变效果
        LinearGradient gradient = new LinearGradient(
                0,
                getTopTableMinY(),
                0,
                getTopTableMaxY(),
                gradientBottomColor,
                currentColor,
                Shader.TileMode.CLAMP);
        priceAreaPaint.setShader(gradient);
        pricePaint.setColor(currentColor);
        dottedLinePaint.setColor(currentColor);

        // 绘制昨日收盘价线
        drawLastClose(canvas, getY(lastClose));

        // 绘制价格曲线
        drawPriceLine(canvas);
    }

    /**
     * 绘制昨日收盘价线
     */
    private void drawLastClose(Canvas canvas, float y) {
        pricePath.reset();
        pricePath.moveTo(tableMargin, y);
        pricePath.lineTo(viewWidth - tableMargin, y);
        canvas.drawPath(pricePath, dottedLinePaint);
    }

    /**
     * 绘制价格曲线
     */
    private void drawPriceLine(Canvas canvas) {
        pricePath.reset();
        priceAreaPath.reset();

        float price = stockPriceList.get(0);
        pricePath.moveTo(tableMargin, getY(price));
        priceAreaPath.moveTo(tableMargin, getTopTableMinY());
        priceAreaPath.lineTo(tableMargin, getY(price));
        for (int i = 1; i < stockPriceList.size(); i++) {
            price = stockPriceList.get(i);
            pricePath.lineTo(getX(i), getY(price));
            priceAreaPath.lineTo(getX(i), getY(price));
        }
        priceAreaPath.lineTo(getX((stockPriceList.size() - 1)), getTopTableMinY());
        priceAreaPath.close();

        canvas.drawPath(pricePath, pricePaint);
        canvas.drawPath(priceAreaPath, priceAreaPaint);
    }

    /**
     * 获取价格线的y轴坐标
     *
     * @param price 当前价格
     * @return 价格线的y轴坐标
     */
    private float getY(float price) {
        return getY(price, minStockPrice, maxStockPrice);
    }

    private void initCurrentColor() {
        if (stockPriceList.isEmpty()) {
            return;
        }
        Float lastPrice = stockPriceList.get(stockPriceList.size() - 1);
        if (lastPrice > lastClose) {
            currentColor = upColor;
        } else if (lastPrice < lastClose) {
            currentColor = downColor;
        } else {
            currentColor = equalColor;
        }
    }

    public void setNewData(FenShi fenShi) {
        if (fenShi != null) {
            stockPriceList.clear();
            for (FenShiData fenShiData : fenShi.getData()) {
                float trade = fenShiData.getTrade();
                stockPriceList.add(trade);
                if (maxStockPrice < trade) {
                    maxStockPrice = trade;
                } else if (minStockPrice > trade) {
                    minStockPrice = trade;
                }
            }
            lastClose = fenShi.getLastClose();
        }

        initCurrentColor();
        invalidate();
    }

    public void setNewData(ArrayList<Float> stockPriceList, Float lastClose) {
        this.stockPriceList = stockPriceList;
        this.lastClose = lastClose;
        initCurrentColor();
        invalidate();
    }

    public void setPathEffect(PathEffect pathEffect) {
        this.pathEffect = pathEffect;
        dottedLinePaint.setPathEffect(pathEffect);
        invalidate();
    }

    public int getCurrentColor() {
        return currentColor;
    }
}
