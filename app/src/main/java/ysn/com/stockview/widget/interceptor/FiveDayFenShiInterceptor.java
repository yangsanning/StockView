package ysn.com.stockview.widget.interceptor;

import ysn.com.stock.interceptor.FenShiUnitInterceptor;
import ysn.com.stock.utils.NumberUtils;

/**
 * @Author yangsanning
 * @ClassName FiveDayFenShiInterceptor
 * @Description 一句话概括作用
 * @Date 2020/5/12
 */
public class FiveDayFenShiInterceptor implements FenShiUnitInterceptor {

    @Override
    public String currentVolume(float currentVolume) {
        return "量:" + NumberUtils.getVolume((int) currentVolume);
    }

    @Override
    public String maxVolume(float maxVolume) {
        return NumberUtils.getVolume((int) maxVolume);
    }

    @Override
    public String centreVolume(float centreVolume) {
        return NumberUtils.getVolume((int) centreVolume);
    }

    @Override
    public String slipPrice(float slipPrice) {
        return NumberUtils.decimalFormat(slipPrice);
    }

    @Override
    public String slideTime(String slideTime) {
        return slideTime.substring(8, 10) + ":" + slideTime.substring(10);
    }

    @Override
    public String slipVolume(float slipVolume) {
        return NumberUtils.decimalFormat(slipVolume);
    }
}
