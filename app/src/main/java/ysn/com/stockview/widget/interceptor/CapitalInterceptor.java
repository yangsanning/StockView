package ysn.com.stockview.widget.interceptor;

import ysn.com.stock.interceptor.CapitalUnitInterceptor;
import ysn.com.stock.utils.NumberUtils;

/**
 * @Author yangsanning
 * @ClassName CapitalInterceptor
 * @Description 一句话概括作用
 * @Date 2020/7/16
 */
public class CapitalInterceptor implements CapitalUnitInterceptor {

    @Override
    public String leftCoordinate(float value) {
        return NumberUtils.numberFormat(value, 2);
    }

    @Override
    public String rightCoordinate(float value) {
        return NumberUtils.numberFormat(value/1000, 3);
    }
}
