package com.zydl.wealthcalculator.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Sch.
 * Date: 2019/8/19
 * description:
 */
public class TimeUtils {
    /**
     * 获取当前日期
     * @return yyyy-MM-dd
     */
    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(System.currentTimeMillis());
        return date;
    }

    /**
     * 获取当前时间
     * @return HH:mm:ss
     */
    public static String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(System.currentTimeMillis());
        return time;
    }

    /**
     * 获取完整时间
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentLongDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(System.currentTimeMillis());
        return date;
    }

    /**
     * 根据格式返回当前时间
     * @param pattern 时间格式
     * @return
     */
    public static String getData(String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(System.currentTimeMillis());
        return date;
    }
    /**
     * 根据格式返回当前时间
     * @param time 时间戳
     * @return
     */
    public static String getData(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(time);
        return date;
    }
    /**
     * 根据格式比较两个时间
     * @param time1 第一个时间
     * @param time2 第二个时间
     * @param pattern 时间格式
     * @return 第一个时间小于等于第二个时间，返回true，否则返回false
     */
    public static boolean compareTime(String time1, String time2, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        long temp1 = 0;
        long temp2 = 0;
        try {
            temp1 = format.parse(time1).getTime();
            temp2 = format.parse(time2).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (temp1 <= temp2) {
            return true;
        } else {
            return false;
        }

    }
}
