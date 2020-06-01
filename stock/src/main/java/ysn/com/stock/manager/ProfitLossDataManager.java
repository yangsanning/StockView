package ysn.com.stock.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author yangsanning
 * @ClassName ProfitLossDataManager
 * @Description 一句话概括作用
 * @Date 2020/6/1
 */
public class ProfitLossDataManager {

    public static final String[] DEFAULT_Y_COORDINATES = new String[]{"-100.00%", "-50.00%", "0.00%", "50.00%", "100.00%"};

    public List<Float> priceList = new ArrayList<>();
    public List<Float> yCoordinateList = new ArrayList<>();
    public float maxValue;
    public float minValue;
    public float peak;

    public List<String> timesList = new ArrayList<>();

    public Float getPrice(int position) {
        return priceList.get(position);
    }

    public String getFistTime() {
        return timesList.isEmpty() ? "" : timesList.get(0);
    }

    public String getLastTime() {
        return timesList.isEmpty() ? "" : timesList.get(timesList.size() - 1);
    }

    public void setData(List<Float> priceList, List<String> timesList) {
        this.priceList = priceList;
        this.timesList = timesList;

        maxValue = Collections.max(priceList);
        minValue = Collections.min(priceList);

        // 中间坐标
        float median = (maxValue + minValue) / 2f;
        // 极差
        peak = (maxValue - minValue);
        // 梯度
        float t1 = peak / 3.33f;
        float t2 = peak / 6.66f;

        yCoordinateList.clear();
        yCoordinateList.add(median - t2);
        yCoordinateList.add(median - t1);
        yCoordinateList.add(median);
        yCoordinateList.add(median + t1);
        yCoordinateList.add(median + t2);
    }
}
