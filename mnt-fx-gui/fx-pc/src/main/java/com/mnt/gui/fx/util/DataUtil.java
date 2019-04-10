package com.mnt.gui.fx.util;

import com.mnt.gui.fx.init.InitFactory;

/**
 * 
 * <p>
 * 公共数据
 * </p>
 * @author    mnt.cico
 * @version  2016年5月15日 上午12:37:55 mnt.cico .
 * @since   FX8.0
 */
public interface DataUtil
{
	//class url path
	String BIN_PATH = "file:/" + System.getProperty("user.dir") + "/bin";
	
	// app jar path
	String APP_PATH = "file:/" + System.getProperty("user.dir") + "/app";

	//target
	String TARGET_PATH = "file:/" + InitFactory.class.getClassLoader().getResource("").getPath();
}
