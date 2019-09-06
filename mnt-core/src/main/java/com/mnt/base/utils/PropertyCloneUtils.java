package com.mnt.base.utils;



import com.mnt.base.anno.PropField;
import com.mnt.base.anno.PropNoClone;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 属性克隆工具类 用来复制 vo 和 dto 的属性
 *
 * @author jiangbiao
 * @Date 2017年4月18日下午2:44:04
 */
public class PropertyCloneUtils {

	
	
	/**
	 * 属性克隆
	 * @param fromObj
	 * @param toObj
	 */
	public static void clone(Object fromObj, Object toObj)
	{
		if(null == fromObj || null == toObj)
		{
			return;
		}
		
		Field[] toFields = toObj.getClass().getDeclaredFields();
		
		for (Field field : toFields) {
			
			if(checkFieldNeedClone(field))
			{
				//获取获得属性值得字段名称
				String fieldName = getCloneFieldName(field);
				//给字段设置属性
				setFieldValue(field, fieldName, fromObj, toObj);
			}
		}
		
		
	}
	

	/**
	 * 判断当前字段是否克隆属性
	 * @param field
	 * @return
	 */
	private static boolean checkFieldNeedClone(Field field)
	{
		if(field.isAnnotationPresent(PropNoClone.class))
		{
			return false;
		}
		return true;
	}
	
	/**
	 * 获取当前属性字段的名称
	 * @param field
	 * @return
	 */
	private static String getCloneFieldName(Field field)
	{
		if(field.isAnnotationPresent(PropField.class))
		{
			PropField propField = field.getAnnotation(PropField.class);
			
			String result = propField.value();
			
			if(null != result && !result.equals(""))
			{
				return result;
			}
			
		}
		
		return field.getName();
	}
	
	/**
	 * 给字段设置值
	 * @param tofield
	 * @param fieldName
	 * @param fromObj
	 * @param toObj
	 */
	private static void setFieldValue(Field tofield, String fieldName, Object fromObj, Object toObj)
	{
		try {
			Field fromField = fromObj.getClass().getDeclaredField(fieldName);
			if(fromField == null)
			{
				return;
			}
			
			Class<?> type = tofield.getType();
			fromField.setAccessible(true);
			tofield.setAccessible(true);
			
			if(Modifier.isFinal(tofield.getModifiers()))
			{
				return;
			}
			
			if(type.isAssignableFrom(String.class))
			{
					tofield.set(toObj, fromField.get(fromObj));
			}
			else if(type.isAssignableFrom(int.class))
			{
				tofield.setInt(toObj, fromField.getInt(fromObj));
			}
			else if(type.isAssignableFrom(boolean.class))
			{
				tofield.setBoolean(toObj, fromField.getBoolean(fromObj));
			}
			else if(type.isAssignableFrom(float.class))
			{
				tofield.setFloat(toObj, fromField.getFloat(fromObj));
			}
			else if(type.isAssignableFrom(long.class))
			{
				tofield.setLong(toObj, fromField.getLong(fromObj));
			}
			else if(type.isAssignableFrom(double.class))
			{
				tofield.setDouble(toObj, fromField.getDouble(fromObj));
			}
			else if(type.isAssignableFrom(char.class))
			{
				tofield.setChar(toObj, fromField.getChar(fromObj));
			}
			else if(type.isAssignableFrom(short.class))
			{
				tofield.setShort(toObj, fromField.getShort(fromObj));
			}
			else if(type.isAssignableFrom(byte.class))
			{
				tofield.setByte(toObj, fromField.getByte(fromObj));
			}
			else 
			{
				tofield.set(toObj, fromField.get(fromObj));
			}
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			if(e instanceof NoSuchFieldException)
			{
				
			}
			else
			{
				e.printStackTrace();
			}
		}
	}
	
}
