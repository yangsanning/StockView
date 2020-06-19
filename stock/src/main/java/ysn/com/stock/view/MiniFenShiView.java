package ysn.com.stock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import ysn.com.stock.bean.IFenShi;
import ysn.com.stock.config.MiniFenShiConfig;
import ysn.com.stock.manager.MiniFenShiDataManager;

/**
 * @Author yangsanning
 * @ClassName FenShiView
 * @Description 普通分时图
 * @Date 2019/5/4
 * @History 2019/5/4 author: description:
 */
public class MiniFenShiView extends StockView {

    private MiniFenShiConfig config;
    private MiniFenShiDataManager dataManager;

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
        dataManager = new MiniFenShiDataManager(config);
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

        if (dataManager.isPriceEmpty()) {
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
        drawLastClose(canvas, getY(dataManager.lastClose));

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

        float price = dataManager.getPrice(0);
        config.pricePath.moveTo(tableMargin, getY(price));
        config.priceAreaPath.moveTo(tableMargin, getTopTableMinY());
        config.priceAreaPath.lineTo(tableMargin, getY(price));
        for (int i = 1; i < dataManager.priceSize(); i++) {
            price = dataManager.getPrice(i);
            config.pricePath.lineTo(getX(i), getY(price));
            config.priceAreaPath.lineTo(getX(i), getY(price));
        }
        config.priceAreaPath.lineTo(getX(dataManager.getLastPricePosition()), getTopTableMinY());
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
        return getY(price, dataManager.minStockPrice, dataManager.maxStockPrice);
    }

    /**
     * 设置数据
     */
    public <T extends IFenShi> void setNewData(T fenShi) {
        if (fenShi==null) {
            return;
        }
        dataManager.setNewData(fenShi);
        invalidate();
    }

    /**
     * 获取迷你分时参数配置
     */
    public MiniFenShiConfig getConfig() {
        return config;
    }

    /**
     * 获取迷你分时数据管理
     */
    public MiniFenShiDataManager getDataManager() {
        return dataManager;
    }
}