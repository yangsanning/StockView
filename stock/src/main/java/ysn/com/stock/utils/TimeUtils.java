package ysn.com.stock.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @Author yangsanning
 * @ClassName TimeUtils
 * @Description 时间工具类
 * @Date 2020/5/7
 */
public class TimeUtils {

    private static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_DAY = "MMdd";

    public static long formatYyyyMmDd(String date) {
        try {
            return new SimpleDateFormat(FORMAT_YYYY_MM_DD, Locale.getDefault()).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String formatDay(long timestamp) {
        return new SimpleDateFormat(FORMAT_DAY, Locale.getDefault()).format(new Date(timestamp));
    }

    /**
     * 当前日期减少天数后的日期
     *
     * @param num 减少的天数
     */
    public static long reduceDay(long time, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.DAY_OF_MONTH, num);
        return calendar.getTimeInMillis();
    }
}
