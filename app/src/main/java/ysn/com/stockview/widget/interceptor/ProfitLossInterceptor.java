package ysn.com.stockview.widget.interceptor;

import ysn.com.stock.interceptor.ProfitLossUnitInterceptor;
import ysn.com.stock.utils.NumberUtils;

/**
 * @Author yangsanning
 * @ClassName ProfitLossInterceptor
 * @Description 一句话概括作用
 * @Date 2020/6/1
 */
public class ProfitLossInterceptor implements ProfitLossUnitInterceptor {

    @Override
    public String yCoordinate(float value) {
        return NumberUtils.numberFormat(value, 2) +"万";
    }
}
