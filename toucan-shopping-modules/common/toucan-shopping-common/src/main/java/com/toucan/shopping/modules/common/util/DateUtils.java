package com.toucan.shopping.modules.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtils {


    public static final ThreadLocal<DateFormat> FORMATTER_YEAR =  new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy");
        }
    };
    public static final ThreadLocal<DateFormat> FORMATTER_MON = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return  new SimpleDateFormat("yyyy-MM");
        }
    };
    public static final ThreadLocal<DateFormat> FORMATTER_DD = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    public static final ThreadLocal<DateFormat> FORMATTER_DD_CN = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy年MM月dd日");
        }
    };
    public static final ThreadLocal<DateFormat> FORMATTER_SS = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    public static final ThreadLocal<DateFormat> FORMATTER_MM = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }
    };

    public static final ThreadLocal<DateFormat> FORMATTER_TSS = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        }
    };

    public static final ThreadLocal<DateFormat> FORMATTER_HH = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("HH");
        }
    };

    /**
     * 提前几秒钟方法
     *
     * @param date   基准时间
     * @param second 需提前秒数
     * @return
     */
    public static Date advanceSecond(Date date, long second) {
        long resultDate = date.getTime() - second * 1000;
        return new Date(resultDate);
    }


    /**
     * 提前几天的方法
     *
     * @param date   基准时间
     * @param day 需提前天数
     * @return
     */
    public static Date advanceDay(Date date, int day) {
        long resultDate = date.getTime() - ((day*24*60*60) * 1000);
        return new Date(resultDate);
    }

    /**
     * 增加指定秒
     * @param date   基准时间
     * @return
     */
    public static Date addSeconds(Date date, long seconds) {
        long resultDate = date.getTime() + (seconds * 1000);
        return new Date(resultDate);
    }

    /**
     * 增加指定毫秒
     * @param date   基准时间
     * @return
     */
    public static Date addMillisecond(Date date, long millisecond) {
        long resultDate = date.getTime() + millisecond;
        return new Date(resultDate);
    }
    /**
     * 根据格式化对象 解析时间
     * @param date
     * @param dateFormat
     * @return
     */
    public static Date parse(String date,DateFormat dateFormat) throws ParseException {
        return dateFormat.parse(date);
    }


    /**
     * 日期转字符串
     * @param date
     * @param dateFormat
     * @return
     */
    public static String format(Date date,DateFormat dateFormat) throws ParseException {
        return dateFormat.format(date);
    }

    /**
     * 返回 毫秒值 = date1-date2
     * @param date1
     * @param date2
     * @return
     */
    public static long subtract(Date date1,Date date2)
    {
        return date1.getTime()-date2.getTime();
    }

    /**
     * 拿到当前时间
     * @return
     */
    public static Date currentDate()
    {
        return new Date();
    }




}
