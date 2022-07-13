package com.mnt.fx.tool.proto.ui.init;

import com.mnt.fx.tool.proto.conf.UserData;
import com.mnt.fx.tool.proto.netty.core.NettyTemplateClassLoad;
import com.mnt.gui.fx.init.InitContext;
import lombok.extern.slf4j.Slf4j;

import java.net.URLClassLoader;
import java.util.List;

/**
 * 启动初始化
 *
 * @author cc
 * @Date 2017年8月8日下午3:05:04
 */
@Slf4j
public class StartInit extends InitContext {

	
	@Override
	public void afterInitView(URLClassLoader classLoad) {




	}

	@Override
	public void init(List<Class<?>> classes, URLClassLoader classLoad) {
		UserData.init();
//		try {
//			NettyTemplateClassLoad.init(classes, classLoad);
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error("启动失败", e);
//		}

		//初始化用户数据
//		UserData.init();
		
	}

	@Override
	public void shutdown() {
		try {
			NettyTemplateClassLoad.unloadClass();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
