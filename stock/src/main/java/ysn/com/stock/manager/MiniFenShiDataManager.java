package ysn.com.stock.manager;

import android.support.annotation.IntRange;

import java.util.List;
import java.util.Objects;

import ysn.com.stock.bean.Extremum;
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
    private List<? extends IFenShiData> fenShiDataList;
    public float lastClose = 0.0f;
    public Extremum extremum = new Extremum();

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
        return fenShiDataList.isEmpty();
    }

    /**
     * 根据position获取价格
     */
    public float getPrice(@IntRange(from = 0) int position) {
        return position < priceSize() ? fenShiDataList.get(position).getFenShiPrice() : 0;
    }

    /**
     * 获取最后一个价格
     */
    public float getLastPrice() {
        return getPrice(getLastPricePosition());
    }

    /**
     * 价格集合大小
     */
    public int priceSize() {
        return fenShiDataList.size();
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
        lastClose = 0.0f;
        fenShiDataList = fenShi.getFenShiData();
        extremum.init(fenShiDataList.get(0).getFenShiPrice());

        for (IFenShiData fenShiData : fenShiDataList) {
            extremum.convert(fenShiData.getFenShiPrice());
        }
        lastClose = fenShi.getFenShiLastClose();
        totalCount = fenShi.getTotalCount();

        // 重置最值
        resetPeakPrice();

        // 重置当前颜色
        resetCurrentColor();
    }

    /**
     * 重置最值
     */
    private void resetPeakPrice() {
        if (Objects.equals(extremum.getMaximum(), extremum.getMinimum())) {
            extremum.setMinimum(extremum.getMaximum() / 2f);
            extremum.setMaximum(extremum.getMaximum() * 3f / 2f);
        }

        if (config.alwaysShowDottedLine) {
            if (lastClose > extremum.getMaximum()) {
                extremum.setMaximum(lastClose);
            } else if (lastClose < extremum.getMinimum()) {
                extremum.setMinimum(lastClose);
            }
        }
        extremum.calculatePeek();
    }

    /**
     * 重置当前颜色
     */
    private void resetCurrentColor() {
        if (fenShiDataList.isEmpty()) {
            return;
        }
        float lastPrice = getLastPrice();
        if (lastPrice > lastClose) {
            config.currentColor = config.upColor;
        } else if (lastPrice < lastClose) {
            config.currentColor = config.downColor;
        } else {
            config.currentColor = config.equalColor;
        }
    }
}
