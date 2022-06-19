package com.mnt.fx.tool.common.utils;


/**
 * 生成代码文件名工具类
 *
 * @author cc
 * @Date 2017年8月9日下午3:52:55
 */
public class GenerateNameUtils {

	
	/**
	 * 获取table对应的文件类名
	 * @param tableName
	 * @return
	 */
	public static String getClassFileName(String tableName) {
		String [] nameArrs = tableName.split("_");
		
		StringBuilder sbStr = new StringBuilder("");
		
		for (String str : nameArrs) {
			if(isAppend(str)) {
				sbStr.append(upperFristStr(str));
			}
		}
		
		return sbStr.toString();
	}
	
	/**
	 * 获取table对应的java命名
	 * @param tableName
	 * @return
	 */
	public static String getJavaName(String tableName) {
		String [] nameArrs = tableName.split("_");
		
		if(nameArrs.length < 2) {
			return tableName;
		}
		
		StringBuilder sbStr = new StringBuilder("");
		
		for (String str : nameArrs) {
			if(isAppend(str)) {
				if(sbStr.length() == 0) {
					sbStr.append(str);
				} else {
					sbStr.append(upperFristStr(str));
				}
			}
		}
		
		return sbStr.toString();
	}
	
	/**
	 * 大写首字母
	 * @param word
	 * @return
	 */
	private static String upperFristStr(String str) {
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
	 * 是否添加到名称中
	 * @param str
	 * @return
	 */
	private static boolean isAppend(String str) {
		if(null == str || str.isEmpty() || str.equals("t")) {
			return false;
		}
		
		return true;
	}
	
	
	public static void main(String[] args) {
		System.err.println(getClassFileName("user_info"));
		System.err.println(getJavaName("user_info"));
	}
	
}

