package ysn.com.stock.bean;

import java.util.List;

/**
 * @Author yangsanning
 * @ClassName IFenShi
 * @Description 分时数据需要实现的接口
 * @Date 2020/4/16
 */
public interface IFenShi {

    /**
     * 数据总数(即表格需要绘制的点总数)
     */
    int getTotalCount();

    /**
     * 股票代码
     */
    String getFenShiCode();

    /**
     * 分时点集合
     */
    List<? extends IFenShiData> getFenShiData();

    /**
     * 昨日收盘价
     */
    float getFenShiLastClose();

    /**
     * 分时时间
     */
    long getFenShiTime();
}