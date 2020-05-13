package ysn.com.stock.interceptor;

/**
 * @Author yangsanning
 * @ClassName FenShiUnitInterceptor
 * @Description 分时单位转换拦截器
 * @Date 2020/5/12
 */
public interface FenShiUnitInterceptor {

    /**
     * 拦截处理下表格当前成交量坐标
     *
     * @param currentVolume 当前成交量
     * @return 下表格当前成交量
     */
    String currentVolume(float currentVolume);

    /**
     * 拦截处理下表格最大值坐标
     *
     * @param maxVolume 成交量最大值
     * @return 下表格最大值坐标
     */
    String maxVolume(float maxVolume);

    /**
     * 拦截处理下表格中间值坐标
     *
     * @param centreVolume 成交量中间值
     * @return 下表格中间值坐标
     */
    String centreVolume(float centreVolume);

    /**
     * 拦截处理滑动点价格值
     *
     * @param slipPrice 滑动点价格
     * @return 滑动点价格值
     */
    String slipPrice(float slipPrice);

    /**
     * 拦截处理滑动点成交量值
     *
     * @param slipVolume 滑动点成交量
     * @return 滑动点成交量值
     */
    String slipVolume(float slipVolume);
}
