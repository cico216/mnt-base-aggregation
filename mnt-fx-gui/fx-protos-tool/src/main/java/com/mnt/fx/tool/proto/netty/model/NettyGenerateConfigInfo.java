package com.mnt.fx.tool.proto.netty.model;

/**
 * 代码生成配置信息
 * @author cc
 * @date 2018/8/17 13:33
 */
public class NettyGenerateConfigInfo {

    /**
     * 生成类型
     */
    private String type;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 项目名称
     */
    private String projectName;


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}
