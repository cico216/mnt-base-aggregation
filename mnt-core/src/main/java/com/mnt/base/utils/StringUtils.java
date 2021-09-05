package com.mnt.base.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * String 对象工具类
 *
 * @author jiangbiao
 * @Date 2017年4月26日下午2:52:13
 */
public class StringUtils {

	public static final String EMPTY_STR = ""; //空String字符串
	
	/**
	 * 判断当前String对象是否为空  , "" 也为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str)
	{
		return (str == null || EMPTY_STR.equals(str));
	}
	
	
	/**
	 * 将当前String 对象 按照指定的分割符然后转换为集合 (仅支持基础类型)
	 * @param str
	 * @param splitTag
	 * @return
	 */
	public static List<Integer> parseToList(String str, String splitTag)
	{
		String [] strs = str.split(splitTag);
		
		List<Integer> result = new ArrayList<>(strs.length);
		for (String strItem : strs) {
			result.add(Integer.valueOf(strItem));
		}
		return result;
	}
	
	/**
	 * 将数字转换占位字符串  如  "000" , 1  转换为 "001"
	 * @param pattern "000"
	 * @param num 1
	 * @return
	 */
	public static String formatNumToString(String pattern, long num)
	{
		if(num < 0)
		{
			return String.valueOf(num);
		}
		String numStr = String.valueOf(num);
		
		if(numStr.length() > pattern.length())
		{
			throw new IllegalAccessError("输入的数字长度不能大于 pattern 长度");
		}
		
		return pattern.substring(numStr.length(), pattern.length()) + numStr;
	}

	/**
	 * @Author: xuebowen
	 * convert "Key2" to "key2"
	 * @Date: 11:12 2018/11/30
	 */
	public static String title(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	/**
     *  首字母大写
     * @param name
     * @return
     */
    public static String captureName(String name)
    {
        // name = name.substring(0, 1).toUpperCase() + name.substring(1);
        // return name;
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }
    
    public static boolean isEqual(String s1, String s2)
    {
        if (s1 == null)
        {
            s1 = "";
        }
        if (s2 == null)
        {
            s2 = "";
        }

        return (s1.equals(s2));
    }

	/**
	 * 中文转Unicode
	 */
	public static String gbEncoding(final String gbString) {   //gbString = "测试"
		char[] utfBytes = gbString.toCharArray();   //utfBytes = [测, 试]
		String unicodeBytes = "";
		for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
			String hexB = Integer.toHexString(utfBytes[byteIndex]);   //转换为16进制整型字符串
			if (hexB.length() <= 2) {
				hexB = "00" + hexB;
			}
			unicodeBytes = unicodeBytes + "\\u" + hexB;
		}
		return unicodeBytes;
	}

	/**
	 *Unicode转中文
	 */
	public static String decodeUnicode(final String dataStr) {
		int start = 0;
		int end = 0;
		final StringBuffer buffer = new StringBuffer();
		while (start > -1) {
			end = dataStr.indexOf("\\u", start + 2);
			String charStr = "";
			if (end == -1) {
				charStr = dataStr.substring(start + 2, dataStr.length());
			} else {
				charStr = dataStr.substring(start + 2, end);
			}
			char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
			buffer.append(new Character(letter).toString());
			start = end;
		}
		return buffer.toString();
	}

	private final static String ENCODE = "UTF-8";
	/**
	 * URL 解码
	 *
	 * @return String
	 */
	public static String url2String(String str) {
		String result = "";
		if (null == str) {
			return "";
		}
		try {
			result = java.net.URLDecoder.decode(str, ENCODE);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * URL 转码
	 *
	 * @return String
	 */
	public static String string2Url(String str) {
		String result = "";
		if (null == str) {
			return "";
		}
		try {
			result = java.net.URLEncoder.encode(str, ENCODE);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String phone2Secret(String phone){
		if(isEmpty(phone)){
			return "";
		}
		try {
			return phone.substring(0,3)+"****"+phone.substring(7);
		}catch (Exception e){
			return "";
		}
	}

	public static String certificateNo2Secret(String certificateNo){
		if(isEmpty(certificateNo)){
			return "";
		}
		try {
			return certificateNo.substring(0,certificateNo.length()-6)+"******";
		}catch (Exception e){
			return "";
		}
	}

	public static String secretHouseName(String houseName){
		if(isEmpty(houseName)){
			return "";
		}
		try {
			return houseName.substring(0,houseName.indexOf("-"))+"******";
		}catch (Exception e){
			return "";
		}
	}

	public static String secretPersonName(String personName){
		if(isEmpty(personName)){
			return "";
		}
		try {
			return personName.substring(0,personName.length()-1)+"*";
		}catch (Exception e){
			return "";
		}
	}


//	@SuppressWarnings("unchecked")
//	public static <T> T  parseToT(String str)
//	{
//		return (T)str;
//	}
	
	public static void main(String[] args) {
//		String test = "1,2,3,4";
//		
//		List<Integer> ts = parseToList(test, ",");
//		int s4 = ts.get(0) + 1;
//		System.err.println(s4);
		
		System.err.println(phone2Secret("13578964587"));
	}



}
