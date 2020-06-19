package ysn.com.stock.manager;

import android.support.annotation.IntRange;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ysn.com.stock.bean.IFenShi;
import ysn.com.stock.bean.IFenShiData;
import ysn.com.stock.config.MiniFenShiConfig;

/**
 * @Author yangsanning
 * @ClassName MiniFenShiDataManager
 * @Description 迷你分时数据管理
 * @Date 2020/6/19
 */
public class MiniFenShiDataManager {

    /**
     * 分时参数配置
     */
    private MiniFenShiConfig config;

    /**
     * priceList: 价格集合
     * lastClose: 收盘价
     * maxStockPrice: 最大价格
     * minStockPrice: 最小价格
     */
    public List<Float> priceList = new ArrayList<>();
    public float lastClose = 0.0f;
    public float maxStockPrice = Float.MIN_VALUE;
    public float minStockPrice = Float.MAX_VALUE;

    /**
     * 数据总数(即表格需要绘制的点总数)
     */
    public int totalCount;

    public MiniFenShiDataManager(MiniFenShiConfig config) {
        this.config = config;
    }

    /**
     * 价格是否为空
     */
    public boolean isPriceEmpty() {
        return priceList.isEmpty();
    }

    /**
     * 根据position获取价格
     */
    public float getPrice(@IntRange(from = 0) int position) {
        return position < priceSize() ? priceList.get(position) : 0;
    }

    /**
     * 价格集合大小
     */
    public int priceSize() {
        return priceList.size();
    }

    /**
     * 获取最后一个价格position
     */
    public int getLastPricePosition() {
        return priceSize() - 1;
    }

    /**
     * 设置数据
     */
    public <T extends IFenShi> void setNewData(T fenShi) {
        // 重置数据
        resetData();

        // 处理相应数据
        List<? extends IFenShiData> fenShiDataList = fenShi.getFenShiData();
        for (IFenShiData fenShiData : fenShiDataList) {
            float trade = fenShiData.getFenShiPrice();
            priceList.add(trade);
            maxStockPrice = Math.max(trade, maxStockPrice);
            minStockPrice = Math.min(trade, minStockPrice);
        }
        lastClose = fenShi.getFenShiLastClose();
        totalCount = fenShi.getTotalCount();

        // 重置最值
        resetPeakPrice();

        // 重置当前颜色
        resetCurrentColor();
    }

    /**
     * 重置数据
     */
    private void resetData() {
        priceList.clear();
        lastClose = 0.0f;
        maxStockPrice = Float.MIN_VALUE;
        minStockPrice = Float.MAX_VALUE;
    }

    /**
     * 重置最值
     */
    private void resetPeakPrice() {
        if (Objects.equals(maxStockPrice, minStockPrice)) {
            minStockPrice = maxStockPrice / 2f;
            maxStockPrice = maxStockPrice * 3f / 2f;
        }
    }

    /**
     * 重置当前颜色
     */
    private void resetCurrentColor() {
        if (priceList.isEmpty()) {
            return;
        }
        Float lastPrice = priceList.get(priceList.size() - 1);
        if (lastPrice > lastClose) {
            config.currentColor = config.upColor;
        } else if (lastPrice < lastClose) {
            config.currentColor = config.downColor;
        } else {
            config.currentColor = config.equalColor;
        }
    }
}
