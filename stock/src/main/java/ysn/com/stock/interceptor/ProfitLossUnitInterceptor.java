package ysn.com.stock.interceptor;

/**
 * @Author yangsanning
 * @ClassName YCoordinateInterceptor
 * @Description 盈亏额/盈亏率单位转换拦截器
 * 不设置则默认百分比模式
 * @Date 2020/6/1
 */
public interface ProfitLossUnitInterceptor {

    /**
     * 表格左边坐标转换
     *
     * @param yCoordinate 坐标值
     * @return 转换的表格左边坐标
     */
    String yCoordinate(float yCoordinate);

    /**
     * 滑动区域显示的值转换
     *
     * @param slideValue 滑动值
     * @return 转换的后滑动区域显示值
     */
    String slideValue(float slideValue);
}
