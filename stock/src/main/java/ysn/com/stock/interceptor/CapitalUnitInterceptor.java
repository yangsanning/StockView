package ysn.com.stock.interceptor;

/**
 * @Author yangsanning
 * @ClassName CapitalUnitInterceptor
 * @Description {@link ysn.com.stock.view.CapitalView} 单位拦截器，必须项
 * @Date 2020/7/16
 */
public interface CapitalUnitInterceptor {

    /**
     * 拦截转换所有左边坐标
     *
     * @param value 值
     */
    String leftCoordinate(float value);

    /**
     *  拦截转换所有右边坐标
     *
     * @param value 值
     */
    String rightCoordinate(float value);
}
