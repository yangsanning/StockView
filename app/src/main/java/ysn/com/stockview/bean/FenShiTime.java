package ysn.com.stockview.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @Author yangsanning
 * @ClassName FenShiTime
 * @Description 一句话概括作用
 * @Date 2018/8/15
 * @History 2018/8/15 author: description:
 */
public class FenShiTime {

    /**
     * date : 20180814
     * symbol : sh000001
     * code : sh000001
     * data : [{"dateTime":"201808140930","trade":2780.74,"volume":2693903},{"dateTime":"201808140931","trade":2782.15,"volume":1232986}]
     */

    private String date;
    private float settlement;
    private String symbol;
    @SerializedName(value = "code", alternate = {"stockCode", "bkcode", "zscode"})
    private String code;
    private List<DataBean> data;
    private int nodeNumber;
    private String endTime;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public float getSettlement() {
        return settlement;
    }

    public void setSettlement(float settlement) {
        this.settlement = settlement;
    }

    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public static class DataBean {
        /**
         * dateTime : 201808140930
         * trade : 2780.74
         * volume : 2693903
         */

        @SerializedName(value = "dateTime", alternate = {"realDateTime"})
        private String dateTime;
        private float trade;
        private float avgPrice;
        private float volume;

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
}
