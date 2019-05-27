package ysn.com.stockview.utils;

import java.math.BigDecimal;

/**
 * @Author yangsanning
 * @ClassName NumberUtils
 * @Description 一句话概括作用
 * @Date 2019/5/23
 * @History 2019/5/23 author: description:
 */
public class NumberUtils {

    /**
     * @param value 数值
     * @return 返回保留2位小数且四舍五入的数值
     */
    public static Float getNumberDecimalTwo(float value) {
        return getNumberDecimal(value, 2);
    }

    /**
     * @param value 数值
     * @return 返回保留3位小数且四舍五入的数值
     */
    public static Float getNumberDecimalThree(float value) {
        return getNumberDecimal(value, 3);
    }

    /**
     * @param value 数值
     * @param scale 位数
     * @return 返回保留 scale 位小数且四舍五入的数值
     */
    public static Float getNumberDecimal(float value, int scale) {
        BigDecimal bd = new BigDecimal(value);
        // BigDecimal.ROUND_HALF_UP 四舍五入
        return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
