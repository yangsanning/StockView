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

    private static final String FORMAT_YYYY_MM_DD = "yyyyMMdd";
    public static final String FORMAT_DAY = "MM-dd";

    public static Date string2Date(String date) {
        try {
            return new SimpleDateFormat(FORMAT_YYYY_MM_DD, Locale.getDefault()).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDay(long timestamp) {
        return new SimpleDateFormat(FORMAT_DAY, Locale.getDefault()).format(new Date(timestamp));
    }

    /**
     * @param reduce date1 的前x天的天数
     */
    public static Date reduceDay(Date date, int reduce) {
        if (date == null) {
            return date;
        }
        return new Date(reduceDay(date.getTime(), reduce));
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
