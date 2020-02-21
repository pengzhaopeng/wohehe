package com.messoft.gaoqin.wanyiyuan.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Administrator on 2018/3/12 0012.
 */

public class TimeUtils {

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @SuppressLint("SimpleDateFormat")
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @SuppressLint("SimpleDateFormat")
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy年MM月dd日");
        }
    };

    /**
     * 取得当前时间戳（精确到秒）
     *
     * @return
     */
    public static String getTimeStamp() {
        long time = System.currentTimeMillis();
        String t = String.valueOf(time / 1000);
        return t;
    }

    /***
     * 两个日期相差多少秒
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getTimeDelta(Date date1, Date date2) {
        long timeDelta = (date1.getTime() - date2.getTime()) / 1000;//单位是秒
        return timeDelta > 0 ? (int) timeDelta : (int) Math.abs(timeDelta);
    }

    /**
     * 时间转换为时间戳
     * 注意如果是毫秒就要除1000
     */
    public static long date2TimeStamp(String date_str) {
        if (!StringUtils.isNoEmpty(date_str)) { //为空就取当前时间
            date_str = getCurrentTime();
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(date_str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime() / 1000;//这里变成秒
//        res = String.valueOf(ts);
        return ts;
    }

    /**
     * 拿到当前时间 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * 拿到当前时间 yyyy-MM-dd
     *
     * @return
     */
    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 字符串转date
     * format 不传默认为yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static Date strToDate(String strTime, String format) {
        try {
            if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            return sdf.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取一个当前时间+随机4位数的验证码
     *
     * @return
     */
    public static String getRandomTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd", Locale.getDefault());
        String currentTime = sdf.format(date);
        int random = (int) ((Math.random() * 9 + 1) * 1000);
        return currentTime + random;
    }

    /**
     * 拿到当前时间 2018年01月
     *
     * @return
     */
    public static String getCurrentYYMMStr() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月", Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * 拿到当前时间
     * format yyyy年MM月等
     *
     * @return
     */
    public static String getCurrentYYMMStr(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(date);
    }

    /*获取星期几*/
    public static String getWeek() {
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "";
        }
    }

    /**
     * 日期转星期
     *
     * @param datetime
     * @return
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }


    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null") || seconds.equals("0")) {
            return "";
        }
        if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));

    }

    /**
     * 字符串格式化想要的格式 like: (2018-11-22 13:59:55) -- (11-22)
     */
    public static String timeFormat(String timeStr, String format) {

        try {
            if (!StringUtils.isNoEmpty(timeStr)) return "";
            Date date = toDate(timeStr);
            if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * HH:mm:ss
     * 毫秒转成 mm:ss的形式
     */
    public static String msToMMSS(long ms) {
        if (ms <= -1) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return formatter.format(ms);
    }

    /**
     * 根据当前日期获得所在周的日期区间（周一和周日日期）
     *
     * @param date
     * @return
     */
    public static String getTimeInterval(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
//        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        String imptimeBegin = sdf.format(cal.getTime());
        // System.out.println("所在周星期一的日期：" + imptimeBegin);
        cal.add(Calendar.DATE, 6);
        String imptimeEnd = sdf.format(cal.getTime());
        // System.out.println("所在周星期日的日期：" + imptimeEnd);
        return imptimeBegin + "," + imptimeEnd;
    }

    public static List<Date> findDates(Date dBegin, Date dEnd) {
        List lDate = new ArrayList();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }

    /**
     * 获取当年
     *
     * @return
     */
    public static int getPersentYear() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.YEAR);
        return month;
    }

    /**
     * 获取当前月
     *
     * @return
     */
    public static int getPersentMonth() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        return month;
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static int getPersentDay() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.DAY_OF_MONTH);
        return month;
    }

    /**
     * 比较两个时间差了几个月  2012-02  2013-02
     */
    public static int diffMouth(String mouth1, String mouth2) {
        if (!StringUtils.isNoEmpty(mouth1) || !StringUtils.isNoEmpty(mouth2)) {
            return -1;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar bef = Calendar.getInstance();
        Calendar aft = Calendar.getInstance();
        try {
            bef.setTime(sdf.parse(mouth1));
            aft.setTime(sdf.parse(mouth2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
        int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;
        return Math.abs(month + result);
    }

    /**
     * 获取上一个月的年和月
     */
    public static int[] getLastYearAndMonth(int year, int persentMonth) {
        int[] str = new int[2];
        if (persentMonth == 1) {
            //年
            str[0] = getPersentYear() - 1;
            //月
            str[1] = 12;
            return str;
        }
        str[0] = year;
        str[1] = persentMonth - 1;
        return str;
    }

    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String friendly_time(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }

    /**
     * 计算某个时间距离现在时间的差值
     * 返回格式化的字符串
     *
     * @param
     * @return
     */
    public static String longTimeToDay(String oldTime) {

        //转换成date类型
        try {
            Date start = dateFormater.get().parse(oldTime);
            Date end = new Date();
            //获取毫秒数
            Long startLong = start.getTime();
            Long endLong = end.getTime();
            //计算时间差,单位毫秒
            Long ms = endLong - startLong;

            return longTimeToDay1(ms);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算某个时间距离现在时间的差值
     * 返回毫秒数
     * now - oldTime
     *
     * @param
     * @return
     */
    public static Long longTimeToDay1(String oldTime) {

        //转换成date类型
        try {
            Date start = dateFormater.get().parse(oldTime);
            Date end = new Date();
            //获取毫秒数
            Long startLong = start.getTime();
            Long endLong = end.getTime();
            //计算时间差,单位毫秒

            return endLong - startLong;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 计算两个时间字符串的差值
     *
     * @param
     * @return
     */
    public static String strTimeDiff(String startTime, String endTime) {
        if (!StringUtils.isNoEmpty(startTime) || !StringUtils.isNoEmpty(startTime)) {
            return "";
        }
        //转换成date类型
        try {
            Date start = dateFormater.get().parse(startTime);
            Date end = dateFormater.get().parse(endTime);
            //获取毫秒数
            Long startLong = start.getTime();
            Long endLong = end.getTime();
            //计算时间差,单位毫秒
            Long ms = endLong - startLong;

            return longTimeToDay1(ms);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算两个时间字符串的差值
     *
     * @param
     * @return
     */
    public static long strTimeDiff1(String startTime, String endTime) {
        if (!StringUtils.isNoEmpty(startTime) || !StringUtils.isNoEmpty(startTime)) {
            return 0L;
        }
        //转换成date类型
        try {
            Date start = dateFormater.get().parse(startTime);
            Date end = dateFormater.get().parse(endTime);
            //获取毫秒数
            Long startLong = start.getTime();
            Long endLong = end.getTime();
            //计算时间差,单位毫秒
            return endLong - startLong;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    @NonNull
    public static String longTimeToDay1(Long ms) {
        if (ms == 0) {
            return "0天0小时0分";
        }
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
//        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "天");
        }
        if (hour > 0) {
            sb.append(hour + "小时");
        }
        if (minute > 0) {
            sb.append(minute + "分");
        }
        if (second > 0) {
            sb.append(second + "秒");
        }
//        if (milliSecond > 0) {
//            sb.append(milliSecond + "毫秒");
//        }
        return sb.toString();
    }

}
