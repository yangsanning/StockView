package ysn.com.stockview.widget.interceptor;

import ysn.com.stock.interceptor.ProfitLossUnitInterceptor;
import ysn.com.stock.utils.NumberUtils;

/**
 * @Author yangsanning
 * @ClassName ProfitLossInterceptor
 * @Description 一句话概括作用
 * @Date 2020/6/1
 */
public class ProfitLossPercentInterceptor implements ProfitLossUnitInterceptor {

    @Override
    public String yCoordinate(float yCoordinate) {
        return NumberUtils.numberFormat(yCoordinate * 100, 2) + "%";
    }

    @Override
    public String slidePrice(float slidePrice) {
        return NumberUtils.numberFormat(slidePrice * 100, 2) + "%";
    }
}
