package com.mnt.fx.tool.proto.http.model;

/**
 * 代码生成配置信息
 * @author jiangbiao
 * @date 2018/8/17 13:33
 */
public class HttpGenerateConfigInfo {

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

    /**
     * 兼容spring cloud项目 api请求和回复参数
     */
    private String apiProjectName;


    /**
     * 模块名称
     */
    private String moduleName;


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

    public String getApiProjectName() {
        return apiProjectName;
    }

    public void setApiProjectName(String apiProjectName) {
        this.apiProjectName = apiProjectName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
