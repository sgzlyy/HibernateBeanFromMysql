package cn.com.spbun.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static String currentTime() {

        return new SimpleDateFormat(FORMAT_YYYY_MM_DD_HH_MM_SS).format(new Date());
    }
}
