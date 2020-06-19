package ysn.com.stock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ysn.com.stock.bean.IFenShi;
import ysn.com.stock.bean.IFenShiData;
import ysn.com.stock.config.MiniFenShiConfig;

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

    MiniFenShiConfig config;

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
    protected void init(AttributeSet attrs) {
        super.init(attrs);
        config = new MiniFenShiConfig(context, attrs);
        config.dottedLinePaint.setPathEffect(config.pathEffect);
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
                config.gradientBottomColor,
                config.currentColor,
                Shader.TileMode.CLAMP);
        config.priceAreaPaint.setShader(gradient);
        config.pricePaint.setColor(config.currentColor);
        config.dottedLinePaint.setColor(config.currentColor);

        // 绘制昨日收盘价线
        drawLastClose(canvas, getY(lastClose));

        // 绘制价格曲线
        drawPriceLine(canvas);
    }

    /**
     * 绘制昨日收盘价线
     */
    private void drawLastClose(Canvas canvas, float y) {
        config.pricePath.reset();
        config.pricePath.moveTo(tableMargin, y);
        config.pricePath.lineTo(viewWidth - tableMargin, y);
        canvas.drawPath(config.pricePath, config.dottedLinePaint);
    }

    /**
     * 绘制价格曲线
     */
    private void drawPriceLine(Canvas canvas) {
        config.pricePath.reset();
        config.priceAreaPath.reset();

        float price = stockPriceList.get(0);
        config.pricePath.moveTo(tableMargin, getY(price));
        config.priceAreaPath.moveTo(tableMargin, getTopTableMinY());
        config.priceAreaPath.lineTo(tableMargin, getY(price));
        for (int i = 1; i < stockPriceList.size(); i++) {
            price = stockPriceList.get(i);
            config.pricePath.lineTo(getX(i), getY(price));
            config.priceAreaPath.lineTo(getX(i), getY(price));
        }
        config.priceAreaPath.lineTo(getX((stockPriceList.size() - 1)), getTopTableMinY());
        config.priceAreaPath.close();

        canvas.drawPath(config.pricePath, config.pricePaint);
        canvas.drawPath(config.priceAreaPath, config.priceAreaPaint);
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

    private void initData() {
        stockPriceList.clear();
        lastClose = 0.0f;
        maxStockPrice = Float.MIN_VALUE;
        minStockPrice = Float.MAX_VALUE;
    }

    private void initCurrentColor() {
        if (stockPriceList.isEmpty()) {
            return;
        }
        Float lastPrice = stockPriceList.get(stockPriceList.size() - 1);
        if (lastPrice > lastClose) {
            config.currentColor = config.upColor;
        } else if (lastPrice < lastClose) {
            config.currentColor = config.downColor;
        } else {
            config.currentColor = config.equalColor;
        }
    }

    private void initPeakPrice() {
        if (Objects.equals(maxStockPrice, minStockPrice)) {
            minStockPrice = maxStockPrice / 2f;
            maxStockPrice = maxStockPrice * 3f / 2f;
        }
    }

    public <T extends IFenShi> void setNewData(T fenShi) {
        initData();
        if (fenShi != null) {
            List<? extends IFenShiData> fenShiDataList = fenShi.getFenShiData();
            for (IFenShiData fenShiData : fenShiDataList) {
                float trade = fenShiData.getFenShiPrice();
                stockPriceList.add(trade);
                maxStockPrice = Math.max(trade, maxStockPrice);
                minStockPrice = Math.min(trade, minStockPrice);
            }
            lastClose = fenShi.getFenShiLastClose();
        }
        initPeakPrice();
        initCurrentColor();
        invalidate();
    }

    public void setNewData(ArrayList<Float> stockPriceList, Float lastClose) {
        initData();
        this.stockPriceList = stockPriceList;
        for (Float trade : stockPriceList) {
            maxStockPrice = Math.max(trade, maxStockPrice);
            minStockPrice = Math.min(trade, minStockPrice);
        }
        this.lastClose = lastClose;
        initPeakPrice();
        initCurrentColor();
        invalidate();
    }

    public void setNewData(ArrayList<Float> stockPriceList, Float lastClose, Float maxStockPrice, Float minStockPrice) {
        this.stockPriceList = stockPriceList;
        this.lastClose = lastClose;
        this.maxStockPrice = maxStockPrice;
        this.minStockPrice = minStockPrice;
        initPeakPrice();
        initCurrentColor();
        invalidate();
    }

    public List<Float> getStockPriceList() {
        return stockPriceList;
    }

    public float getLastClose() {
        return lastClose;
    }

    public float getMaxStockPrice() {
        return maxStockPrice;
    }

    public float getMinStockPrice() {
        return minStockPrice;
    }

    public MiniFenShiConfig getConfig() {
        return config;
    }
}