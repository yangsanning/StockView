package ysn.com.stock.bean;

/**
 * @Author yangsanning
 * @ClassName IFenShiData
 * @Description 分时点的数据需要实现的接口
 * @Date 2020/4/16
 */
public interface IFenShiData {

    /**
     * 时间
     */
    String getFenShiTime();

    /**
     * 价格
     */
    float getFenShiPrice();

    /**
     * 成交量
     */
    float getFenShiVolume();

    /**
     * 均价
     */
    float getFenShiAvgPrice();
}
