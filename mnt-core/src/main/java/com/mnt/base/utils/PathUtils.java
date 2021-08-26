package com.mnt.base.utils;

import java.io.File;

/**
 * 路径工具类
 * @author jiangbiao
 * @date 2018/9/3 11:10
 */
public class PathUtils {

    /**
     * 包名转换为相对应的路径
     * @param packageName
     * @return
     */
    public static String packageToPath(String packageName) {
        if(null == packageName || "".equals(packageName)) {
            return "";
        }

        String separtor =  getSeparator();

        if(separtor.equals("\\")) {
            separtor = "\\\\";
        }

        return packageName.replaceAll("\\.", separtor);
    }


    /**
     * 获取系统路径符号
     * @return
     */
    public static String getSeparator() {

        return File.separator;

    }

}
