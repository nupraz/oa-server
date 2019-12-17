package com.graduation.oa.common.util;

import com.graduation.oa.common.support.SnowFlake;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class StringUtils {
    private static Log logger = LogFactory.getLog(StringUtils.class);
    static SnowFlake snowFlake = new SnowFlake(0L, 0L);

    public StringUtils() {
    }

    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    public static String convertNull(String s) {
        return s == null ? "" : s;
    }

    public static String serial() {
        return Calendar.getInstance().getTimeInMillis() + String.valueOf((int)(Math.random() * 1000.0D));
    }

    public static String guid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static int length(String value) {
        if (value == null) {
            return 0;
        } else {
            int valueLength = 0;
            String regex = "[Α-￥]";

            for(int i = 0; i < value.length(); ++i) {
                String temp = value.substring(i, i + 1);
                if (temp.matches(regex)) {
                    valueLength += 2;
                } else {
                    ++valueLength;
                }
            }

            return valueLength;
        }
    }

    public static String fillLeft(String str, int fillLen) {
        return fill("left", str, fillLen, "0");
    }

    public static String fillLeft(int str, int fillLen) {
        return fill("left", Integer.toString(str), fillLen, "0");
    }

    public static String fillLeft(String str, int fillLen, String fillChar) {
        return fill("left", str, fillLen, fillChar);
    }

    public static String fillRight(String str, int fillLen, String fillChar) {
        return fill("right", str, fillLen, fillChar);
    }

    public static String fill(String fillType, String str, int fillLen, String fillChar) {
        try {
            int strLen = str.getBytes("gbk").length;
            if (strLen >= fillLen) {
                return str;
            }

            for(int i = 0; i < fillLen - strLen; ++i) {
                if (fillType.equals("left")) {
                    str = fillChar + str;
                } else {
                    if (!fillType.equals("right")) {
                        return str;
                    }

                    str = str + fillChar;
                }
            }
        } catch (UnsupportedEncodingException var6) {
            logger.error(str + " getBytes error:" + var6);
        }

        return str;
    }

    public static String format(String inStr, int strLength) {
        String outStr = inStr.trim();
        if (outStr.equals("")) {
            outStr = "0.00";
        }

        if (strLength <= 0) {
            return outStr;
        } else {
            if (outStr.lastIndexOf(".") == -1) {
                outStr = outStr + ".";
            }

            int len = outStr.lastIndexOf(".") + strLength;
            int i = outStr.length();
            if (i > len) {
                return outStr.substring(0, len + 1);
            } else {
                while(i <= len) {
                    outStr = outStr + "0";
                    i = outStr.length();
                }

                return outStr;
            }
        }
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static boolean isDouble(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static boolean isChineseDigit(String str) {
        return "零一二三四五六七八九十百千万亿".contains(str);
    }

    public static String trimLeftZero(String str) {
        if (str != null && !str.equals("")) {
            if (str.equals("0")) {
                return "";
            } else {
                for(int i = 0; i < str.length(); ++i) {
                    if (str.charAt(i) != '0') {
                        str = str.substring(i);
                        break;
                    }
                }

                return str;
            }
        } else {
            return str;
        }
    }

    public static long leftDigit(String str) {
        String digitStr = "";

        int i;
        for(i = 0; i < str.length() && isInteger(str.substring(i, i + 1)); ++i) {
            digitStr = digitStr + str.substring(i, i + 1);
        }

        if (!org.springframework.util.StringUtils.isEmpty(digitStr)) {
            try {
                return Long.valueOf(digitStr);
            } catch (NumberFormatException var4) {
                return 0L;
            }
        } else {
            for(i = 0; i < str.length() && isChineseDigit(str.substring(i, i + 1)); ++i) {
                digitStr = digitStr + str.substring(i, i + 1);
            }

            return IntUtils.chineseDigit(digitStr);
        }
    }

    public static String addOneStr(String str) {
        if (str == null) {
            str = "01";
        }

        int numstr;
        if (trimLeftZero(str).length() == 1) {
            numstr = Integer.parseInt(trimLeftZero(str)) + 1;
            if (numstr == 10) {
                str = numstr + "";
            } else {
                str = "0" + numstr;
            }
        } else {
            numstr = Integer.parseInt(str) + 1;
            str = numstr + "";
        }

        return str;
    }

    public static String addOne(String str) {
        if (org.springframework.util.StringUtils.isEmpty(str)) {
            str = "0";
        }

        return String.valueOf(Integer.parseInt(str) + 1);
    }

    public static String addLeftZero(String inStr, int strLength) {
        String str;
        for(str = inStr.trim(); str.length() < strLength; str = "0" + str) {
        }

        return str;
    }

    public static String addLeftZero(int inInt, int strLength) {
        String inStr = String.valueOf(inInt);
        return addLeftZero(inStr, strLength);
    }

    public static String trimPoint(String inStr) {
        String outStr = inStr.trim();
        if (outStr.equals("")) {
            outStr = "0.00";
        }

        if (outStr.lastIndexOf(".") == -1) {
            outStr = outStr + ".";
        }

        int len = outStr.lastIndexOf(".");
        return outStr.substring(0, len);
    }

    public static String replaceLikeEl(String source, Map<String, String> refMap) {
        if (source != null && refMap != null) {
            int index = source.indexOf("${");
            if (index < 0) {
                return source;
            } else {
                int lastIndex = source.indexOf("}");
                if (lastIndex >= 0 && lastIndex >= index) {
                    String key = source.substring(index + 2, lastIndex);
                    String value = (String)refMap.get(key);
                    if (value == null) {
                        value = "";
                    }

                    return source.substring(0, index) + value + source.substring(lastIndex + 1);
                } else {
                    return source;
                }
            }
        } else {
            return source;
        }
    }

    public static String format(double inDouble, int strLength) {
        String outStr = String.valueOf(inDouble);
        return format(outStr, strLength);
    }

    public static String format(float inFloat, int strLength) {
        String outStr = String.valueOf(inFloat);
        return format(outStr, strLength);
    }

    public static String format(int inInt, int strLength) {
        String outStr = String.valueOf(inInt);
        return format(outStr, strLength);
    }

    public static String format(long inLong, int strLength) {
        String outStr = String.valueOf(inLong);
        return format(outStr, strLength);
    }

    public static String getId() {
        Random RANDOM = new Random();
        int nextInt = RANDOM.nextInt();
        nextInt = nextInt == -2147483648 ? 2147483647 : Math.abs(nextInt);
        return String.valueOf(nextInt);
    }

    public static String getSerial() {
        return String.valueOf(System.currentTimeMillis()).substring(0, 13) + String.valueOf(Math.random()).substring(2, 5);
    }

    public static String getSerial(int len) {
        return len == 10 ? String.valueOf(System.currentTimeMillis()).substring(3, 12) + String.valueOf(Math.random()).substring(4, 5) : String.valueOf(System.currentTimeMillis()).substring(0, 13) + String.valueOf(Math.random()).substring(2, 5);
    }

    public static String random(int len) {
        String r = String.valueOf(Math.random());
        return r.length() >= len + 2 ? r.substring(2, len + 2) : fillLeft(r.substring(2), len);
    }

    public static int getInt(String str) {
        return org.springframework.util.StringUtils.isEmpty(str) ? 0 : Integer.valueOf(str);
    }

    public static boolean isEmail(String str) {
        if (org.springframework.util.StringUtils.isEmpty(str)) {
            return false;
        } else {
            String regular = "\\w+@(\\w+\\.){1,3}\\w+";
            return Pattern.matches(regular, str);
        }
    }

    public static boolean isPhoneNo(String str, String separator) {
        if (separator == null || separator.equals("")) {
            separator = ",";
        }

        String[] phoneNoArray = str.split(separator);
        String[] var3 = phoneNoArray;
        int var4 = phoneNoArray.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String phoneNo = var3[var5];
            phoneNo = phoneNo.trim();
            if (!isMobile(phoneNo) && !isTelephone(phoneNo)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isMobile(String str) {
        if (org.springframework.util.StringUtils.isEmpty(str)) {
            return false;
        } else {
            String regular = "1[3,4,5,8]{1}\\d{9}";
            return Pattern.matches(regular, str.trim());
        }
    }

    public static boolean isTelephone(String str) {
        if (org.springframework.util.StringUtils.isEmpty(str)) {
            return false;
        } else {
            String wholeRegular = "[0]{1}[0-9]{2,3}-[0-9]{7,8}";
            String simpleRegular = "[0-9]{7,8}";
            return Pattern.matches(wholeRegular, str.trim()) || Pattern.matches(simpleRegular, str.trim());
        }
    }

    public static String joint(String[] arr) {
        return joint(arr, ",");
    }

    public static String joint(String[] arr, String seprate) {
        StringBuffer sb = new StringBuffer();
        if (arr != null) {
            String[] var3 = arr;
            int var4 = arr.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String s = var3[var5];
                if (sb.length() > 0) {
                    sb.append(seprate);
                }

                sb.append(s);
            }

            return sb.toString();
        } else {
            return null;
        }
    }

    public static String shortUrl() {
        return snowFlake.toOtherNumberSystem(snowFlake.nextId(), 62);
    }
}