package com.mnt.fx.tool.proto.conf;


import com.mnt.fx.tool.common.utils.JSONConfigUtils;

import java.util.Collections;

/**
 * 用户数据
 */
public class UserData {


    /**
     * 用户基础配置表
     */
    private static UserConfig userConfig;

    /**
     * 初始化用户数据
     */
    public static void init() {
        userConfig = JSONConfigUtils.loadData(UserConfig.class, getBasePath() + CONFIG_PATH);
        if(null == userConfig) {
            userConfig = new UserConfig();
            userConfig.setUser("");
            userConfig.setHeaders(Collections.emptyMap());
            userConfig.setServerProjectPath("");
            userConfig.setClientProjectPath("");
            userConfig.setLastSelectedDir("");

        }
    }

    /**
     * 配置文件地址
     */
    public static final String CONFIG_PATH = "conf/config.json";


    /**
     * 获取用户目录
     * @return
     */
    public static String getUserPath() {
        return System.getProperty("user.dir");
    }

    /**
     * 获取基础路径
     * @return
     */
    private static String getBasePath() {
        return getUserPath() + "/";
    }


    /**
     * 获取用户配置数据
     * @return
     */
    public static UserConfig getUserConfig() {

        return userConfig;
    }

    /**
     * 保存用户数据
     */
    public static void saveUserConfig() {
        JSONConfigUtils.saveData(userConfig, getBasePath() + CONFIG_PATH);


    }

    public static void main(String[] args) {
        UserData.init();
        saveUserConfig();
    }

}
