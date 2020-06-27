package ysn.com.stock.function;

/**
 * @Author yangsanning
 * @ClassName Call
 * @Description 一句话概括作用
 * @Date 2020/6/24
 */
public interface Call<T> {

    T call(T t);
}
