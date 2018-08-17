package cn.com.spbun.common;

import java.util.List;

/**
 * 模块工具包
 * 
 * @author NOLY DAKE
 *
 */
public class TmodelUtil {
    
    public static String convent2ClassName(String tableName) {

        String[] array = tableName.toLowerCase().split("_");

        StringBuffer result = new StringBuffer();

        result.append("T");

        boolean isFirst = true;

        for (String string : array) {

            if (string == null || string.trim().length() == 0) {
                continue;
            }

            if (isFirst) {
                result.append(string);
                isFirst = false;
            } else {
                result.append(string.substring(0, 1).toUpperCase());
                result.append(string.substring(1));
            }

        }

        return result.toString();
    }

    public static String column2ClazzForLower(String columnName) {

        String[] array = columnName.toLowerCase().split("_");

        StringBuffer result = new StringBuffer();
        int i = 0;
        for (String string : array) {

            if (string == null || string.trim().length() == 0) {
                continue;
            }
            if (i == 0) {
                result.append(string);

            } else {
                result.append(string.substring(0, 1).toUpperCase());
                result.append(string.substring(1));
            }
            i++;
        }

        return result.toString();
    }

    public static String column2ClazzForUpper(String columnName) {

        String[] array = columnName.toLowerCase().split("_");

        StringBuffer result = new StringBuffer();
        for (String string : array) {

            if (string == null || string.trim().length() == 0) {
                continue;
            }

            result.append(string.substring(0, 1).toUpperCase());
            result.append(string.substring(1));
        }

        return result.toString();
    }

    /**
     * 移除表中的前缀信息，如果前缀不在指定的列表中，那么直接返回该表
     * 
     * @param tableName
     *            表名称
     * @param tablePrefix
     *            前缀列表
     * @return 移除前缀后的表名
     */
    public static String removePrefix(String tableName, List<String> tablePrefix) {

        if (tablePrefix != null && tablePrefix.size() > 0) {
            for (String suffix : tablePrefix) {
                if (suffix == null || suffix == "") {
                    continue;
                }
                if (tableName.startsWith(suffix)) {
                    tableName = tableName.replaceFirst(suffix, "");
                    break;
                }
            }
        }

        return tableName;
    }

    public static String convent2InstanceName(String tableName) {

        String[] array = tableName.toLowerCase().split("_");

        StringBuffer result = new StringBuffer();

        result.append("t");

        boolean isFirst = true;

        for (String string : array) {

            if (string == null || string.trim().length() == 0) {
                continue;
            }

            if (isFirst) {
                result.append(string);
                isFirst = false;
            } else {
                result.append(string.substring(0, 1).toUpperCase());
                result.append(string.substring(1));
            }

        }

        return result.toString();
    }

   
}
