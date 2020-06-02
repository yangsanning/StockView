package ysn.com.stock.manager;

import android.support.annotation.NonNull;

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

    public List<Float> valueList = new ArrayList<>();
    public List<Float> yCoordinateList = new ArrayList<>();

    /**
     * coordinateMax: 坐标最大值
     * coordinateMedian: 坐标中间值
     * coordinateMin: 坐标最小值
     * coordinatePeak: 坐标极差
     */
    public float coordinateMax, coordinateMedian, coordinateMin, coordinatePeak;

    public List<String> timesList = new ArrayList<>();

    public Float getValue(int position) {
        return valueList.get(position);
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
        return !valueList.isEmpty();
    }

    public int dataSize() {
        return valueList.size();
    }

    public void setData(@NonNull List<Float> valueList,@NonNull List<String> timesList) {
        this.valueList = valueList;
        this.timesList = timesList;

        float maxValue = Collections.max(valueList);
        float minValue = Collections.min(valueList);

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
