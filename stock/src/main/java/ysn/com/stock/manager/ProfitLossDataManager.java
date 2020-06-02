package ysn.com.stock.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author yangsanning
 * @ClassName ProfitLossDataManager
 * @Description {@link ysn.com.stock.view.ProfitLossView} 的数据管理
 * @Date 2020/6/1
 */
public class ProfitLossDataManager {

    public List<Float> priceList = new ArrayList<>();
    public List<Float> yCoordinateList = new ArrayList<>();

    /**
     * coordinateMax: 坐标最大值
     * coordinateMedian: 坐标中间值
     * coordinateMin: 坐标最小值
     * coordinatePeak: 坐标极差
     */
    public float coordinateMax, coordinateMedian, coordinateMin, coordinatePeak;

    public List<String> timesList = new ArrayList<>();

    public Float getPrice(int position) {
        return priceList.get(position);
    }

    public String getTime(int position) {
        return timesList.isEmpty() ? "" : timesList.get(position);
    }

    public String getFistTime() {
        return getTime(0);
    }

    public String getLastTime() {
        return getTime(timesList.size() - 1);
    }

    public boolean isNotEmpty() {
        return !priceList.isEmpty();
    }

    public int dataSize() {
        return priceList.size();
    }

    public void setData(List<Float> priceList, List<String> timesList) {
        this.priceList = priceList;
        this.timesList = timesList;

        float maxValue = Collections.max(priceList);
        float minValue = Collections.min(priceList);

        // 中间坐标
        coordinateMedian = (maxValue + minValue) / 2f;
        // 极差
        float peak = Math.abs(minValue - maxValue);
        // 梯度
        float t1 = peak / 3.33f;
        float t2 = 2 * t1;

        yCoordinateList.clear();
        yCoordinateList.add(coordinateMedian - t2);
        yCoordinateList.add(coordinateMedian - t1);
        yCoordinateList.add(coordinateMedian);
        yCoordinateList.add(coordinateMedian + t1);
        yCoordinateList.add(coordinateMedian + t2);

        coordinateMax = yCoordinateList.get(yCoordinateList.size() - 1);
        coordinateMin = yCoordinateList.get(0);
        coordinatePeak = coordinateMax - coordinateMin;
    }
}
