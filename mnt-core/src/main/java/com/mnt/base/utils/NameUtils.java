package com.mnt.base.utils;

/**
 * 名称工具utils
 */
public class NameUtils {

    /**
     * 大写首字母
     * @param str
     * @return
     */
    public static String upperFristStr(String str) {
        if(null == str || "t".equals(str) || str.isEmpty()) {
            return "";
        }

        if(str.length() < 2) {
            return str;
        }

        String fristStr = str.substring(0, 1);
        String otherStr = str.substring(1, str.length());

        return fristStr.toUpperCase() + otherStr;
    }

    /**
     * 小首字母
     * @param str
     * @return
     */
    public static String lowerFristStr(String str) {
        if(null == str || "t".equals(str) || str.isEmpty()) {
            return "";
        }

        if(str.length() < 2) {
            return str;
        }

        String fristStr = str.substring(0, 1);
        String otherStr = str.substring(1, str.length());

        return fristStr.toLowerCase() + otherStr;
    }


    /**
     * 构建内部类类名
     * @param name
     * @return
     */
    public static String buildInnerClassName(String name) {

        return  NameUtils.upperFristStr(name) + "Bean";
    }

}
