package ysn.com.stockview.bean;

import java.util.List;

/**
 * @Author yangsanning
 * @ClassName ProfitLoss
 * @Description 一句话概括作用
 * @Date 2020/6/1
 */
public class ProfitLoss {

    private List<String> date;
    private List<Float> amount;
    private List<Float> rate;

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }

    public List<Float> getAmount() {
        return amount;
    }

    public void setAmount(List<Float> amount) {
        this.amount = amount;
    }

    public List<Float> getRate() {
        return rate;
    }

    public void setRate(List<Float> rate) {
        this.rate = rate;
    }
}
