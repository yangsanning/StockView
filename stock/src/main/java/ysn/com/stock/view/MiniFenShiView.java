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
import ysn.com.stock.view.base.StockView;

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
    public int getTotalCount() {
        return dataManager.totalCount == 0 ? super.getTotalCount() : dataManager.totalCount;
    }

    @Override
    protected void onBaseDraw(Canvas canvas) {
    }

    @Override
    protected void onChildDraw(Canvas canvas) {
        super.onChildDraw(canvas);

        if (dataManager.isPriceEmpty()) {
            // 绘制默认中线
            drawLastClose(canvas, getTopTableMinY() / 2);
            return;
        }

        if (config.enableGradientBottom) {
            //渐变效果
            LinearGradient gradient = new LinearGradient(
                    0,
                    getTopTableMaxY(),
                    0,
                    getTopTableMinY(),
                    config.gradientBottomColor,
                    config.currentColor,
                    Shader.TileMode.CLAMP);
            config.priceAreaPaint.setShader(gradient);
        }

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
        config.pricePath.moveTo(getTableMinX(), y);
        config.pricePath.lineTo(getTableMaxX(), y);
        canvas.drawPath(config.pricePath, config.dottedLinePaint);
        config.pricePath.reset();
    }

    /**
     * 绘制价格曲线
     */
    private void drawPriceLine(Canvas canvas) {
        float topTableMaxY = getTopTableMaxY();
        float price = dataManager.getPrice(0);
        float y = getY(price);
        config.pricePath.moveTo(getCircleX(), y);
        for (int i = 1; i < dataManager.priceSize(); i++) {
            price = dataManager.getPrice(i);
            y = getY(price);
            float x = getX(i);
            config.pricePath.lineTo(x, y);
        }
        canvas.drawPath(config.pricePath, config.pricePaint);

        config.pricePath.lineTo(getX(dataManager.getLastPricePosition()), topTableMaxY);
        config.pricePath.lineTo(getCircleX(), topTableMaxY);
        config.pricePath.close();

        if (config.enableGradientBottom) {
            canvas.drawPath(config.pricePath, config.priceAreaPaint);
        }

        config.pricePath.reset();
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
        if (fenShi == null) {
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