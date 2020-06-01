package ysn.com.stock.utils;

import android.content.Context;
import android.support.annotation.ColorRes;

/**
 * @Author yangsanning
 * @ClassName ResUtils
 * @Description 一句话概括作用
 * @Date 2020/6/1
 */
public class ResUtils {

    public static int getColor(Context context, @ColorRes int colorRes) {
        return context.getResources().getColor(colorRes);
    }
}
