package com.graduation.oa.common.util;

import com.graduation.oa.common.util.IntUtils;
import com.graduation.oa.common.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {
    private static Log logger = LogFactory.getLog(DateUtils.class);
    private static String PATTERN = "yyyyMMddHHmmss";

    public DateUtils() {
    }

    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000L;
    }

    public static int getWeek() {
        return getWeek(Calendar.getInstance(TimeZone.getTimeZone("GMT+8")));
    }

    public static int getWeek(Calendar cal) {
        int dayWeek = cal.get(7);
        --dayWeek;
        if (dayWeek == 0) {
            dayWeek = 7;
        }

        return dayWeek;
    }

    public static int getWeek(String date, String pattern) {
        if (date != null) {
            Date d = parseString(date, pattern);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            return getWeek(calendar);
        } else {
            return -1;
        }
    }

    public static String getTime() {
        return getTime(Calendar.getInstance(TimeZone.getTimeZone("GMT+8")));
    }

    public static String getTime(Calendar cal) {
        int hour = getHour(cal);
        int minute = getMinute(cal);
        int second = getSecond(cal);
        String curTime = IntUtils.format(hour, "00") + ":" + IntUtils.format(minute, "00") + ":" + IntUtils.format(second, "00");
        return curTime;
    }

    public static String getShortTime() {
        return getShortTime(Calendar.getInstance(TimeZone.getTimeZone("GMT+8")));
    }

    public static String getShortTime(Calendar cal) {
        int hour = getHour(cal);
        int minute = getMinute(cal);
        int second = getSecond(cal);
        String curTime = IntUtils.format(hour, "00") + IntUtils.format(minute, "00") + IntUtils.format(second, "00");
        return curTime;
    }

    public static String getDate() {
        return getDate(Calendar.getInstance(TimeZone.getTimeZone("GMT+8")));
    }

    public static String getYear() {
        int year = getYear(Calendar.getInstance(TimeZone.getTimeZone("GMT+8")));
        return IntUtils.format(year, "00");
    }

    public static String getMonth() {
        int month = getMonth(Calendar.getInstance(TimeZone.getTimeZone("GMT+8")));
        return IntUtils.format(month, "00");
    }

    public static String getDay() {
        int day = getDay(Calendar.getInstance(TimeZone.getTimeZone("GMT+8")));
        return IntUtils.format(day, "00");
    }

    public static String getCnDate() {
        String cnDate = getYear() + "年" + getMonth() + "月" + getDay() + "日";
        return cnDate;
    }

    public static String getDate(Calendar cal) {
        int year = getYear(cal);
        int month = getMonth(cal);
        int day = getDay(cal);
        return getDate(year, month, day);
    }

    public static String getDate(int year, int month, int day) {
        String curDate = IntUtils.format(year, "00") + "-" + IntUtils.format(month, "00") + "-" + IntUtils.format(day, "00");
        return curDate;
    }

    public static String getShortDate() {
        return getShortDate(Calendar.getInstance(TimeZone.getTimeZone("GMT+8")));
    }

    public static String getShortDate(Calendar cal) {
        int year = getYear(cal);
        int month = getMonth(cal);
        int day = getDay(cal);
        return getShortDate(year, month, day);
    }

    public static String getShortDate(int year, int month, int day) {
        String curDate = IntUtils.format(year, "00") + IntUtils.format(month, "00") + IntUtils.format(day, "00");
        return curDate;
    }

    public static int getDifYear(String date) {
        int year = Integer.parseInt(date.substring(0, 4));
        return Integer.valueOf(getYear()) - year;
    }

    public static int getYear(Calendar cal) {
        return cal.get(1);
    }

    public static int getMonth(Calendar cal) {
        return cal.get(2) + 1;
    }

    public static int getDay(Calendar cal) {
        return cal.get(5);
    }

    public static int getHour(Calendar cal) {
        return cal.get(11);
    }

    public static int getMinute(Calendar cal) {
        return cal.get(12);
    }

    public static int getSecond(Calendar cal) {
        return cal.get(13);
    }

    public static String toShortDate(String longDate) {
        return longDate.replaceAll("-", "");
    }

    public static String getDateTime() {
        return getDate() + " " + getTime();
    }

    public static String getDateTime(Calendar cal) {
        return getDate(cal) + " " + getTime(cal);
    }

    public static String getLastYear(String date) {
        return !StringUtils.isEmpty(date) && date.length() >= 4 ? Integer.valueOf(date.substring(0, 4)) - 1 + date.substring(4) : null;
    }

    public static String getLastMonth(String date) {
        return getLastMonth(date, 1);
    }

    public static String getLastMonth() {
        return getLastMonth(getDate().substring(0, 7));
    }

    public static String getLastMonth(String date, int m) {
        if (!StringUtils.isEmpty(date) && date.length() >= 7) {
            int year = Integer.valueOf(date.substring(0, 4));
            int month = Integer.valueOf(date.substring(5, 7));
            if (month - m <= 0) {
                year -= (m - month + 12) / 12;
                month = (month - m) % 12;
                month = month <= 0 ? 12 + month : month;
            } else {
                month -= m;
            }

            return year + "-" + StringUtils.addLeftZero(month, 2) + date.substring(7);
        } else {
            return null;
        }
    }

    public static String getNextMonth(String date) {
        return getNextMonth(date, 1);
    }

    public static String getNextMonth() {
        return getNextMonth(getDate().substring(0, 7));
    }

    public static String getNextMonth(int m) {
        return getNextMonth(getDate().substring(0, 7), m);
    }

    public static String getNextMonth(String date, int m) {
        if (!StringUtils.isEmpty(date) && date.length() >= 7) {
            int year = Integer.valueOf(date.substring(0, 4));
            int month = Integer.valueOf(date.substring(5, 7));
            if (month + m > 12) {
                year += (month + m) / 12;
                month = (month + m) % 12;
            } else {
                month += m;
            }

            return year + "-" + StringUtils.addLeftZero(month, 2) + date.substring(7);
        } else {
            return null;
        }
    }

    public static boolean checkDate(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);

        try {
            return df.parse(date) != null;
        } catch (ParseException var3) {
            return false;
        }
    }

    public static Date stringToDate(String strDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);

        try {
            return df.parse(strDate);
        } catch (ParseException var3) {
            return null;
        }
    }

    public static int dateToCalendar(Date date, Calendar cal) {
        cal.setTimeInMillis(date.getTime());
        return 0;
    }

    public static int stringToCalendar(String strDate, Calendar cal) {
        Date date = stringToDate(strDate);
        if (date == null) {
            return -1;
        } else {
            return dateToCalendar(date, cal) != 0 ? -1 : 0;
        }
    }

    public static Calendar longToCalendar(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return cal;
    }

    public static int dateDiff(String strDate1, String strDate2) {
        Date date1 = stringToDate(strDate1);
        Date date2 = stringToDate(strDate2);
        return date1 != null && date2 != null ? (int)((date2.getTime() - date1.getTime()) / 86400000L) : 0;
    }

    public static String addDate(String strDate, int days) {
        Date date = stringToDate(strDate);
        if (date == null) {
            return null;
        } else {
            long diff = (long)days * 24L * 3600L * 1000L;
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(date.getTime() + diff);
            return getDate(cal);
        }
    }

    public static String getTaskTime() {
        return getDateTime().substring(0, 16) + " " + getWeek();
    }

    public static int compareTaskTime(String time1, String time2) {
        for(int i = 0; i < time1.length(); ++i) {
            String str = time1.substring(i, i + 1);
            if (!str.equals("*")) {
                int ret = str.compareTo(time2.substring(i, i + 1));
                if (ret != 0) {
                    return ret;
                }
            }
        }

        return 0;
    }

    public static String getCnDate(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isEmpty(str)) {
            return null;
        } else {
            try {
                Date date = simpleDateFormat.parse(str);
                simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                return simpleDateFormat.format(date);
            } catch (ParseException var3) {
                logger.error(var3);
                return str;
            }
        }
    }

    public static String getCnDate(String str, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        if (StringUtils.isEmpty(str)) {
            return null;
        } else {
            try {
                Date date = simpleDateFormat.parse(str);
                simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                return simpleDateFormat.format(date);
            } catch (ParseException var4) {
                logger.error(var4);
                return str;
            }
        }
    }

    public static String getYear(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isEmpty(str)) {
            return null;
        } else {
            try {
                Date date = simpleDateFormat.parse(str);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                return IntUtils.format(getYear(calendar), "00");
            } catch (ParseException var4) {
                logger.error(var4);
                return str;
            }
        }
    }

    public static String getMonth(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isEmpty(str)) {
            return null;
        } else {
            try {
                Date date = simpleDateFormat.parse(str);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                return IntUtils.format(getMonth(calendar), "00");
            } catch (ParseException var4) {
                logger.error(var4);
                return str;
            }
        }
    }

    public static String getDay(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isEmpty(str)) {
            return null;
        } else {
            try {
                Date date = simpleDateFormat.parse(str);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                return IntUtils.format(getDay(calendar), "00");
            } catch (ParseException var4) {
                logger.error(var4);
                return str;
            }
        }
    }

    public static Date getDateDate() {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).getTime();
    }

    public static String getLastMonthFirstDay(String date) {
        return !StringUtils.isEmpty(date) && date.length() >= 7 ? getLastMonth(date.substring(0, 7)) + "-01" : null;
    }

    public static String getLastMonthFirstDay() {
        return getLastMonthFirstDay(getDate());
    }

    public static Date parseString(String date) {
        return parseString(date, PATTERN);
    }

    public static Date parseString(String date, String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = PATTERN;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date back = null;

        try {
            back = sdf.parse(date);
            return back;
        } catch (ParseException var5) {
            throw new RuntimeException(var5);
        }
    }

    public static String formatDate(Date date) {
        return formatDate(date, PATTERN);
    }

    public static String formatDate(Date date, String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            pattern = PATTERN;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String back = sdf.format(date);
        return back;
    }

    public static List<String> getDates(String startDate, String endDate, String pattern) {
        if (startDate != null && endDate != null && pattern != null) {
            List<String> dates = new ArrayList();
            Date start = parseString(startDate, pattern);
            Date end = parseString(endDate, pattern);
            if (start != null && end != null) {
                dates.add(startDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(start);

                while(calendar.getTime().before(end)) {
                    calendar.add(5, 1);
                    dates.add(formatDate(calendar.getTime(), pattern));
                }

                return dates;
            }
        }

        return null;
    }

    public static Double hourDiff(String t1, String t2, String pattern) {
        Date d1 = parseString(t1, pattern);
        Date d2 = parseString(t2, pattern);
        return d1 != null && d2 != null ? (double)(d2.getTime() - d1.getTime()) / 3600000.0D : null;
    }
}
