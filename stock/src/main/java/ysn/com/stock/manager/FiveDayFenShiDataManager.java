package ysn.com.stock.manager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
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
    public LinkedHashMap<Integer, FenShiDataManager> dataManagerMap = new LinkedHashMap<>();
    public List<FenShiDataManager> dataManagerList = new ArrayList<>();

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

    public FiveDayFenShiDataManager(int count, DecimalFormat decimalFormat) {
        for (int i = 0; i < count; i++) {
            dataManagerMap.put(i, new FenShiDataManager(decimalFormat));
        }
        this.decimalFormat = decimalFormat;
    }

    public <T extends IFenShi> void setData(int position, T fenShi) {
        if (fenShi == null) {
            return;
        }
        FenShiDataManager dataManager = dataManagerMap.get(position);
        if (dataManagerMap.size() - 1 == position) {
            dataManager.setData(fenShi);

            date = TimeUtils.string2Date(fenShi.getFenShiData().get(0).getFenShiTime());
            lastClose = dataManager.lastClose;
            initDataManagerList();
            initData();
        } else {
            dataManager.setData(fenShi);
        }
    }

    /**
     * 过滤掉没有数据的 dataManager
     */
    private void initDataManagerList() {
        dataManagerList.clear();
        for (FenShiDataManager dataManager : dataManagerMap.values()) {
            if (!dataManager.isPriceEmpty()) {
                dataManagerList.add(dataManager);
            }
        }
    }

    private void initData() {
        FenShiDataManager dataManager = dataManagerList.get(0);
        maxPrice = dataManager.maxPrice;
        minPrice = dataManager.minPrice;
        maxVolume = dataManager.maxVolume;

        for (int i = 1; i < dataManagerList.size(); i++) {
            dataManager = dataManagerList.get(i);
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
