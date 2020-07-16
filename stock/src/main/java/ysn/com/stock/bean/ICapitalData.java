package ysn.com.stock.bean;

/**
 * @Author yangsanning
 * @ClassName ICapitalData
 * @Description 一句话概括作用
 * @Date 2020/7/15
 */
public interface ICapitalData {

    /**
     * 价格
     */
    float getPrice();

    /**
     * 总资金净流入
     */
    float getFinanceInFlow();

    /**
     * 主力净流入
     */
    float getMainInFlow();

    /**
     * 散户净流入
     */
    float getRetailInFlow();
}
