package ysn.com.stock.interceptor;

/**
 * @Author yangsanning
 * @ClassName YCoordinateInterceptor
 * @Description 盈亏额/盈亏率单位转换拦截器
 * 不设置则默认百分比模式
 * @Date 2020/6/1
 */
public interface ProfitLossUnitInterceptor {

    String yCoordinate(float yCoordinate);

    String slidePrice(float slidePrice);
}
