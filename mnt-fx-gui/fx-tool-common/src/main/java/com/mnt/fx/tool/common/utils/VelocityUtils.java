package com.mnt.fx.tool.common.utils;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * 模板引擎工具类
 *
 * @author cc
 * @Date 2017年8月8日下午4:32:49
 */
public class VelocityUtils {

	
	private static final class SingletonHolder
	{
		private static final VelocityUtils	INSTANCE	= new VelocityUtils();
	}

	public static VelocityUtils getInstance()
	{
		return SingletonHolder.INSTANCE;
	}

	private VelocityUtils()
	{
		try {
			Properties p = new Properties();
			p.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, System.getProperty("user.dir") + "/tmp/");
			Velocity.init(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 解析模板
	 * @param templateName
	 * @param toFilePath
	 * @param params
	 */
	public void parseTemplate(String templateName, String toFilePath, Map<String, Object> params) 
	{
		try {
			
			VelocityContext context = new VelocityContext();
			//设置参数  ℃ 、 m³，
			for (Entry<String, Object> paramKV : params.entrySet()) {
				context.put(paramKV.getKey(), paramKV.getValue());
			}
			
			FileWriter fw = null;
			try {
			//如果文件存在，则追加内容；如果文件不存在，则创建文件
				File f = new File(toFilePath);
				fw = new FileWriter(f, false);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Velocity.mergeTemplate(templateName, "UTF-8" , context, fw);
			//System.err.println("templateName : " + templateName + " , toFilePath : " + toFilePath);
			try {
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析模板
	 * @param templateName
	 * @param params
	 */
	public String parseTemplate(String templateName, Map<String, Object> params)
	{
		String result = "";
		StringWriter sw = new StringWriter();
		try {

			VelocityContext context = new VelocityContext();
			//设置参数  ℃ 、 m³，
			for (Entry<String, Object> paramKV : params.entrySet()) {
				context.put(paramKV.getKey(), paramKV.getValue());
			}



			Velocity.mergeTemplate(templateName, "UTF-8" , context, sw);
			//System.err.println("templateName : " + templateName + " , toFilePath : " + toFilePath);

			result = sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				sw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	
	public static void main(String[] args) {
		Map<String, Object> params = new HashMap<>();
		params.put("test", "姜彪");
		VelocityUtils.getInstance().parseTemplate("test.vm", "D:/test/test.java", params);
	}
	
}
