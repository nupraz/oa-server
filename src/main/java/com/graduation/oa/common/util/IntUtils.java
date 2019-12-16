package com.graduation.oa.common.util;

import java.text.DecimalFormat;

public final class


IntUtils {
    public IntUtils() {
    }

    public static String format(int i, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format((long)i);
    }

    public static long chineseDigit(String chnNum) {
        return 0L;
    }
}
