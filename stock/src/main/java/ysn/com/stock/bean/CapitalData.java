package ysn.com.stock.bean;

/**
 * @Author yangsanning
 * @ClassName CapitalData
 * @Description 一句话概括作用
 * @Date 2019/5/23
 * @History 2019/5/23 author: description:
 */
public class CapitalData {

    private float price;
    private float financeInFlow;
    private float mainInFlow;
    private float retailInFlow;

    public CapitalData() {
    }

    public CapitalData(float price, float financeInFlow, float mainInFlow, float retailInFlow) {
        this.price = price;
        this.financeInFlow = financeInFlow;
        this.mainInFlow = mainInFlow;
        this.retailInFlow = retailInFlow;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getFinanceInFlow() {
        return financeInFlow;
    }

    public void setFinanceInFlow(float financeInFlow) {
        this.financeInFlow = financeInFlow;
    }

    public float getMainInFlow() {
        return mainInFlow;
    }

    public void setMainInFlow(float mainInFlow) {
        this.mainInFlow = mainInFlow;
    }

    public float getRetailInFlow() {
        return retailInFlow;
    }

    public void setRetailInFlow(float retailInFlow) {
        this.retailInFlow = retailInFlow;
    }
}
