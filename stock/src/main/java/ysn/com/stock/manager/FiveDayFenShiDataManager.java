package ysn.com.stock.manager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ysn.com.stock.bean.IFenShi;
import ysn.com.stock.utils.NumberUtils;
import ysn.com.stock.utils.TimeUtils;

/**
 * @Author yangsanning
 * @ClassName FiveDayFenShiDataManager
 * @Description 五日分时数据管理器
 * @Date 2020/5/7
 */
public class FiveDayFenShiDataManager {

    private DecimalFormat decimalFormat;
    public FenShiDataManager dataManager1;
    public FenShiDataManager dataManager2;
    public FenShiDataManager dataManager3;
    public FenShiDataManager dataManager4;
    public FenShiDataManager dataManager5;
    private List<FenShiDataManager> dataManagerList = new ArrayList<>();

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
     * 最新一天
     */
    public Date date;

    public FiveDayFenShiDataManager(DecimalFormat decimalFormat) {
        dataManager1 = new FenShiDataManager(decimalFormat);
        dataManager2 = new FenShiDataManager(decimalFormat);
        dataManager3 = new FenShiDataManager(decimalFormat);
        dataManager4 = new FenShiDataManager(decimalFormat);
        dataManager5 = new FenShiDataManager(decimalFormat);
        this.decimalFormat = decimalFormat;
    }

    public <T extends IFenShi> void setData1(T fenShi) {
        dataManager1.setData(fenShi);
    }

    public <T extends IFenShi> void setData2(T fenShi) {
        dataManager2.setData(fenShi);
    }

    public <T extends IFenShi> void setData3(T fenShi) {
        dataManager3.setData(fenShi);
    }

    public <T extends IFenShi> void setData4(T fenShi) {
        dataManager4.setData(fenShi);
    }

    public <T extends IFenShi> void setData5(T fenShi) {
        if (fenShi != null) {
            dataManager5.setData(fenShi);

            date = TimeUtils.string2Date(fenShi.getFenShiData().get(0).getFenShiTime());
            lastClose = dataManager5.lastClose;

            initDataManagerList();
            initData();
        }
    }

    /**
     * 过滤掉没有数据的 dataManager
     */
    private void initDataManagerList() {
        dataManagerList.clear();
        if (!dataManager1.isPriceEmpty()) {
            dataManagerList.add(dataManager1);
        }
        if (!dataManager2.isPriceEmpty()) {
            dataManagerList.add(dataManager2);
        }
        if (!dataManager3.isPriceEmpty()) {
            dataManagerList.add(dataManager3);
        }
        if (!dataManager4.isPriceEmpty()) {
            dataManagerList.add(dataManager4);
        }
        if (!dataManager5.isPriceEmpty()) {
            dataManagerList.add(dataManager5);
        }
    }

    private void initData() {
        FenShiDataManager fenShiDataManager = dataManagerList.get(0);
        maxPrice = fenShiDataManager.maxPrice;
        minPrice = fenShiDataManager.minPrice;
        maxVolume = fenShiDataManager.maxVolume;

        for (int i = 1; i < dataManagerList.size(); i++) {
            FenShiDataManager dataManager = dataManagerList.get(i);
            maxPrice = Math.max(maxPrice, dataManager.maxPrice);
            minPrice = Math.min(minPrice, dataManager.minPrice);
            maxVolume = Math.max(maxVolume, dataManager.maxVolume);
        }

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

        maxVolumeString = NumberUtils.getVolume((int) maxVolume / 100);
        centreVolumeString = NumberUtils.getVolume((int) maxVolume / 200);
    }
}
