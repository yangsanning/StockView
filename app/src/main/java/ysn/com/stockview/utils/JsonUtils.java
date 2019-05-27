package ysn.com.stockview.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * @Author yangsanning
 * @ClassName JsonUtils
 * @Description 一句话概括作用
 * @Date 2019/5/4
 * @History 2019/5/4 author: description:
 */
public class JsonUtils {

    /**
     * 获取去最原始的数据信息
     *
     * @return json data
     */
    public static <T> T getData(Context context, String fileName, Class<T> classOfT) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(fileName);
            return new Gson().fromJson(convertStreamToString(inputStream), classOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * input 流转换为字符串
     */
    private static String convertStreamToString(InputStream inputStream) {
        String s = null;
        try {
            //格式转换
            Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
            if (scanner.hasNext()) {
                s = scanner.next();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}
