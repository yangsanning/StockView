package ysn.com.stock.manager;

import android.support.annotation.IntRange;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ysn.com.stock.bean.IFenShi;
import ysn.com.stock.bean.IFenShiData;
import ysn.com.stock.interceptor.FenShiUnitInterceptor;
import ysn.com.stock.utils.NumberUtils;

/**
 * @Author yangsanning
 * @ClassName FenShiDataManager
 * @Description 分时数据管理
 * @Date 2020/5/7
 */
public class FenShiDataManager {

    private DecimalFormat decimalFormat;

    /**
     * 价格集合
     */
    public List<Float> priceList = new ArrayList<>();

    /**
     * 均价集合
     */
    public List<Float> avePriceList = new ArrayList<>();

    /**
     * 时间集合
     */
    public List<String> timeList = new ArrayList<>();

    /**
     * 昨收(中间坐标值)
     */
    public float lastClose = 0.0f;

    /**
     * 最大价格(左上角)
     */
    public float maxPrice = 0.0f;

    /**
     * 最小价格(左下角)
     */
    public float minPrice = 0.0f;

    /**
     * 百分比(右侧百分比坐标值)
     */
    public String percent = " 100%";

    /**
     * 当前交易量
     */
    public List<Float> volumeList = new ArrayList<>();

    /**
     * 最大交易量
     */
    public float maxVolume;

    /**
     * 最大交易量坐标值
     */
    public String maxVolumeString = "";

    /**
     * 交易量中间坐标
     */
    public String centreVolumeString = "";

    /**
     * 当前数据的时间
     */
    public long time;

    /**
     * 分时单位转换拦截器
     */
    private FenShiUnitInterceptor fenShiUnitInterceptor;

    public FenShiDataManager(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }

    public boolean isPriceEmpty() {
        return priceList.isEmpty();
    }

    public boolean isPriceNoEmpty() {
        return !isPriceEmpty();
    }

    public boolean isTimeEmpty() {
        return timeList.isEmpty();
    }

    public boolean isTimeNotEmpty() {
        return !isTimeEmpty();
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
     * 是否是最后一个价格
     */
    public boolean isLastPrice(int position) {
        return position == getLastPricePosition();
    }

    /**
     * 获取最后一个价格position
     */
    public int getLastPricePosition() {
        return priceSize() - 1;
    }

    /**
     * 根据position获取均价
     */
    public float getAvePrice(@IntRange(from = 0) int position) {
        return position < avePriceSize() ? avePriceList.get(position) : 0;
    }

    /**
     * 均价集合大小
     */
    public int avePriceSize() {
        return avePriceList.size();
    }

    /**
     * 根据position获取时间
     */
    public String getTime(@IntRange(from = 0) int position) {
        return position < timeSize() ? timeList.get(position) : "";
    }

    /**
     * 时间集合大小
     */
    public int timeSize() {
        return timeList.size();
    }

    /**
     * 获取最后一个时间点
     */
    public String getLastTime() {
        return getTime(getLastTimePosition());
    }

    /**
     * 获取最后一个时间position
     */
    public int getLastTimePosition() {
        return timeSize() - 1;
    }

    /**
     * 根据position获取成交量
     */
    public float getVolume(@IntRange(from = 0) int position) {
        return position < aveVolumeSize() ? volumeList.get(position) : 0;
    }

    /**
     * 成交量集合大小
     */
    public int aveVolumeSize() {
        return volumeList.size();
    }

    public <T extends IFenShi> void setData(T fenShi) {
        setData(fenShi, true);
    }

    /**
     * 设置数据
     */
    public <T extends IFenShi> void setData(T fenShi, boolean isInit) {
        if (fenShi != null) {
            priceList.clear();
            avePriceList.clear();
            timeList.clear();
            volumeList.clear();
            List<? extends IFenShiData> fenShiData = fenShi.getFenShiData();
            for (int i = 0; i < fenShiData.size(); i++) {
                addStockPrice(fenShiData.get(i).getFenShiPrice(), i);
                avePriceList.add(fenShiData.get(i).getFenShiAvgPrice());
                timeList.add(fenShiData.get(i).getFenShiTime().substring(8, 10) + ":" + fenShiData.get(i).getFenShiTime().substring(10));
                addVolume(fenShiData.get(i).getFenShiVolume(), i);
            }
            lastClose = fenShi.getFenShiLastClose();
            time = fenShi.getFenShiTime();
            if (isInit) {
                initData();
            }
        }
    }

    private void addStockPrice(float trade, int position) {
        priceList.add(trade);

        if (position == 0) {
            maxPrice = trade;
            minPrice = trade;
        }

        if (maxPrice < trade) {
            maxPrice = trade;
        } else if (minPrice > trade) {
            minPrice = trade;
        }
    }

    private void addVolume(float volume, int position) {
        volumeList.add(volume);
        if (position == 0) {
            maxVolume = volume;
        }
        if (maxVolume < volume) {
            maxVolume = volume;
        }
    }

    private void initData() {
        if (Math.abs(minPrice - lastClose) > Math.abs(maxPrice - lastClose)) {
            float temp = maxPrice;
            maxPrice = minPrice;
            minPrice = temp;
        }

        if (maxPrice > lastClose) {
            minPrice = lastClose * 2 - maxPrice;
        } else {
            minPrice = maxPrice;
            maxPrice = lastClose * 2 - maxPrice;
        }

        // 百分比坐标值
        percent = decimalFormat.format(((maxPrice - lastClose) / lastClose * 100)) + "%";

        maxVolumeString = fenShiUnitInterceptor == null ? NumberUtils.getVolume((int) maxVolume) : fenShiUnitInterceptor.maxVolume(maxVolume);
        centreVolumeString = fenShiUnitInterceptor == null ? NumberUtils.getVolume((int) maxVolume / 2) : fenShiUnitInterceptor.centreVolume(maxVolume / 2);
    }

    /**
     * 设置分时单位转换拦截器
     */
    public void setFenShiUnitInterceptor(FenShiUnitInterceptor fenShiUnitInterceptor) {
        this.fenShiUnitInterceptor = fenShiUnitInterceptor;
    }
}
