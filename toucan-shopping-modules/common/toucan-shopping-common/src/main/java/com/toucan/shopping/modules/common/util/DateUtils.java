package com.toucan.shopping.modules.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtils {


    public static final DateFormat FORMATTER_YEAR = new SimpleDateFormat("yyyy");
    public static final DateFormat FORMATTER_MON = new SimpleDateFormat("yyyy-MM");
    public static final DateFormat FORMATTER_DD = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat FORMATTER_DD_CN = new SimpleDateFormat("yyyy年MM月dd日");
    public static final DateFormat FORMATTER_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat FORMATTER_TSS = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


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
     * 拿到当前时间
     * @return
     */
    public static Date currentDate()
    {
        return new Date();
    }

}
