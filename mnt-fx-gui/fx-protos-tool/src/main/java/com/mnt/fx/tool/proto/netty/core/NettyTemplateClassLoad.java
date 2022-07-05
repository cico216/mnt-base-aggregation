package com.mnt.fx.tool.proto.netty.core;


import com.mnt.fx.tool.proto.netty.generate.CSTcpClientProtoGenerate;
import com.mnt.fx.tool.proto.netty.generate.JavaTcpServerProtoGenerate;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 代码生成脚本加载类
 *
 * @author cc
 * @Date 2018年5月4日下午3:03:50
 */
public class NettyTemplateClassLoad {

	/**
	 * 协议生成模板
	 */
	public static List<NettyProtoCodeGenerateTemplate> PROTO_CODE_GENERATE_TEMPLATE = new ArrayList<>() ;
	private static final Map<String, NettyProtoCodeGenerateTemplate> nettyProtoCodeGenerates = new HashMap<>(2);
	static {
		nettyProtoCodeGenerates.put("java", new JavaTcpServerProtoGenerate());
		nettyProtoCodeGenerates.put("cs", new CSTcpClientProtoGenerate());
	}

	/**
	 * 初始化类加载
	 * @param classes
	 * @param classLoad
	 */
	public static void init(List<Class<?>> classes, URLClassLoader classLoad) {
		for (Class<?> c : classes) {
			if(NettyProtoCodeGenerateTemplate.class.equals(c.getSuperclass())) {

				try {
					PROTO_CODE_GENERATE_TEMPLATE.add((NettyProtoCodeGenerateTemplate)c.newInstance());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		}
//		System.err.println(PROTO_CODE_GENERATE_TEMPLATE);



	}

	public static NettyProtoCodeGenerateTemplate getByType(String type) {
		return nettyProtoCodeGenerates.get(type);
	}

	public static boolean checkTypeSupport(String type) {
		return nettyProtoCodeGenerates.containsKey(type);
	}




	/**
	 * 卸载class
	 */
	public static void unloadClass() {
		PROTO_CODE_GENERATE_TEMPLATE.clear();
	}

}
