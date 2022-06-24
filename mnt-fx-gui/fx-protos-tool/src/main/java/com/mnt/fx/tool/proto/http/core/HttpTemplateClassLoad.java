package com.mnt.fx.tool.proto.http.core;

//import com.mnt.game.tool.http.generate.CsHttpClientProtoGenerate;
//import com.mnt.game.tool.http.generate.JavaHttpServerGenerate;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 代码生成脚本加载类
 *
 * @author jiangbiao
 * @Date 2018年5月4日下午3:03:50
 */
public class HttpTemplateClassLoad {

	/**
	 * 协议生成模板
	 */
	public static List<HttpProtoCodeGenerateTemplate> PROTO_CODE_GENERATE_TEMPLATE = new ArrayList<>();;
	private static final Map<String, HttpProtoCodeGenerateTemplate> httpProtoCodeGenerates = new HashMap<>(2);
	static {
//		httpProtoCodeGenerates.put("java", new JavaHttpServerGenerate());
//		httpProtoCodeGenerates.put("cs", new CsHttpClientProtoGenerate());
	}


	/**
	 * 初始化类加载
	 * @param classes
	 * @param classLoad
	 */
	public static void init(List<Class<?>> classes, URLClassLoader classLoad) {

		for (Class<?> c : classes) {
			if(HttpProtoCodeGenerateTemplate.class.equals(c.getSuperclass())) {

				try {
					PROTO_CODE_GENERATE_TEMPLATE.add((HttpProtoCodeGenerateTemplate)c.newInstance());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		}
//		System.err.println(PROTO_CODE_GENERATE_TEMPLATE);



	}
	public static HttpProtoCodeGenerateTemplate getByType(String type) {
		return httpProtoCodeGenerates.get(type);
	}

	public static boolean checkTypeSupport(String type) {
		return httpProtoCodeGenerates.containsKey(type);
	}

	/**
	 * 卸载class
	 */
	public static void unloadClass() {
		PROTO_CODE_GENERATE_TEMPLATE.clear();
	}

}
