package cn.com.sby.common;

public class StringUtil {

    /**
     * 空字符串
     */
    public static final String EMPTY = "";

    public static String defaultIfEmpty(String value, String defaultValue) {

        if (isEmpty(value)) {
            return defaultValue;
        }

        return value;
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }
}
