package ysn.com.stock.bean;

/**
 * @Author yangsanning
 * @ClassName FenShiData
 * @Description 一句话概括作用
 * @Date 2019/5/4
 * @History 2019/5/4 author: description:
 */
public class FenShiData {

    /**
     * dateTime : 201808140930
     * trade : 2780.74
     * volume : 2693903
     */

    private String dateTime;
    private float trade;
    private float avgPrice;
    private float volume;

    public FenShiData() {
    }

    public FenShiData(String dateTime, float trade, float avgPrice, float volume) {
        this.dateTime = dateTime;
        this.trade = trade;
        this.avgPrice = avgPrice;
        this.volume = volume;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public float getTrade() {
        return trade;
    }

    public void setTrade(float trade) {
        this.trade = trade;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(float avgPrice) {
        this.avgPrice = avgPrice;
    }
}
