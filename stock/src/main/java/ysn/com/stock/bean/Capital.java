package ysn.com.stock.bean;

import java.util.ArrayList;

/**
 * @Author yangsanning
 * @ClassName Capital
 * @Description 资金类（包含最大最小值）
 * @Date 2019/5/23
 * @History 2019/5/23 author: description:
 */
public class Capital {

    private ArrayList<CapitalData> data;

    /**
     * 最大净流入
     */
    private float maxInFlow = Integer.MIN_VALUE;

    /**
     * 最小净流入
     */
    private float mixInFlow = Integer.MAX_VALUE;

    /**
     * 最大价格
     */
    private float maxPrice = Integer.MIN_VALUE;

    /**
     * 最小价格
     */
    private float mixPrice = Integer.MAX_VALUE;

    public Capital() {
    }

    public Capital(float maxInFlow, float mixInFlow, ArrayList<CapitalData> data) {
        this.maxInFlow = maxInFlow;
        this.mixInFlow = mixInFlow;
        this.data = data;
    }

    public ArrayList<CapitalData> getData() {
        return data;
    }

    public void setData(ArrayList<CapitalData> data) {
        this.data = data;
    }

    public float getMaxInFlow() {
        return maxInFlow;
    }

    public void setMaxInFlow(float maxInFlow) {
        this.maxInFlow = maxInFlow;
    }

    public float getMixInFlow() {
        return mixInFlow;
    }

    public void setMixInFlow(float mixInFlow) {
        this.mixInFlow = mixInFlow;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(float maxPrice) {
        this.maxPrice = maxPrice;
    }

    public float getMixPrice() {
        return mixPrice;
    }

    public void setMixPrice(float mixPrice) {
        this.mixPrice = mixPrice;
    }

    public void checkInFlowExtremum(float financeInFlow, float mainInFlow, float retailInFlow) {
        if (financeInFlow > mainInFlow) {
            if (financeInFlow > retailInFlow) {
                checkMaxInFlow(financeInFlow);
                checkMinInFlow(Math.min(mainInFlow, retailInFlow));
            } else {
                checkMaxInFlow(retailInFlow);
                checkMinInFlow(mainInFlow);
            }
        } else if (mainInFlow > retailInFlow) {
            checkMaxInFlow(mainInFlow);
            checkMinInFlow(Math.min(financeInFlow, retailInFlow));
        } else {
            checkMaxInFlow(retailInFlow);
            checkMinInFlow(financeInFlow);
        }
    }

    private void checkMaxInFlow(float value) {
        this.maxInFlow = Math.max(this.maxInFlow, value);
    }

    private void checkMinInFlow(float value) {
        this.mixInFlow = Math.min(this.mixInFlow, value);
    }

    public Capital findPriceExtremum(float price) {
        this.maxPrice = Math.max(this.maxPrice, price);
        this.mixPrice = Math.min(this.mixPrice, price);
        return this;
    }
}
