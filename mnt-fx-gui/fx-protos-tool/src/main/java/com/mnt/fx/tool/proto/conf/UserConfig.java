package com.mnt.fx.tool.proto.conf;

import lombok.Data;

import java.util.Map;

/**
 * 用户属性配置
 */
@Data
public class UserConfig {

    /**
     * 最后一次选择的文件夹
     */
    private String lastSelectedDir;

    /**
     * 客户端项目路径
     */
    private String clientProjectPath;

    /**
     * 服务端项目路径
     */
    private String serverProjectPath;

    /**
     * 测试http地址
     */
    private String testHttpUrl;

    /**
     * 当前用户
     */
    private String user;

    /**
     * 请求头参数
     */
    private Map<String , String> headers;



}
