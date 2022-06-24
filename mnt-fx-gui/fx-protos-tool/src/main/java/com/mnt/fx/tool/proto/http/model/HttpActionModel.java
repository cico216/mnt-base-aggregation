package com.mnt.fx.tool.proto.http.model;

import java.util.List;
import java.util.Map;

/**
 * action名称
 * @author jiangbiao
 * @date 2018/8/17 12:08
 */
public class HttpActionModel {


    /**
     * 请求方法名
     */
    private String actionName;

    /**
     * 请求路径
     */
    private String requestMapper;

    /**
     * 请求方法类型
     */
    private String method;

    /**
     * 是否为requestbody请求
     */
    private Boolean isBody;

    /**
     * 请求参数所在包
     */
    private String reqPackage;

    /**
     * 请求参数类
     */
    private String reqClass;

    /**
     * 请求参数名称
     */
    private String reqName;

    /**
     * 答复参数类
     */
    private String respClass;

    /**
     * 答复参数名称
     */
    private String respName;

    /**
     * 答复参数包路径
     */
    private String respPackage;

    /**
     * 注释信息
     */
    private String remark;

    /**
     * 请求参数列表
     */
    private List<HttpCommadReqParam> commadReqParams;

    /**
     * 答复参数列表
     */
    private List<HttpCommadRespParam> commadRespParams;

    /**
     * 内部请求参数
     */
    private Map<HttpCommadReqParam, List<HttpCommadReqParam>> innerReqParams;

    /**
     * 内部返回参数
     */
    private Map<HttpCommadRespParam, List<HttpCommadRespParam>> innerRespParams;

    /**
     * 请求参数引入的包
     */
    private List<String> reqImprotClass;

    /**
     * 返回参数引入的包
     */
    private List<String> respImprotClass;

    /**
     * 测试的入参
     */
    private String testParams;

    /**
     * 测试备注
     */
    private String testRemark;

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getRequestMapper() {
        return requestMapper;
    }

    public void setRequestMapper(String requestMapper) {
        this.requestMapper = requestMapper;
    }

    public String getReqClass() {
        return reqClass;
    }

    public void setReqClass(String reqClass) {
        this.reqClass = reqClass;
    }

    public String getReqName() {
        return reqName;
    }

    public void setReqName(String reqName) {
        this.reqName = reqName;
    }

    public String getRespClass() {
        return respClass;
    }

    public void setRespClass(String respClass) {
        this.respClass = respClass;
    }

    public String getRespName() {
        return respName;
    }

    public void setRespName(String respName) {
        this.respName = respName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<HttpCommadReqParam> getCommadReqParams() {
        return commadReqParams;
    }

    public void setCommadReqParams(List<HttpCommadReqParam> commadReqParams) {
        this.commadReqParams = commadReqParams;
    }

    public List<HttpCommadRespParam> getCommadRespParams() {
        return commadRespParams;
    }

    public void setCommadRespParams(List<HttpCommadRespParam> commadRespParams) {
        this.commadRespParams = commadRespParams;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Boolean getBody() {
        return isBody;
    }

    public void setBody(Boolean body) {
        isBody = body;
    }

    public String getReqPackage() {
        return reqPackage;
    }

    public void setReqPackage(String reqPackage) {
        this.reqPackage = reqPackage;
    }

    public String getRespPackage() {
        return respPackage;
    }

    public void setRespPackage(String respPackage) {
        this.respPackage = respPackage;
    }

    public Map<HttpCommadReqParam, List<HttpCommadReqParam>> getInnerReqParams() {
        return innerReqParams;
    }

    public void setInnerReqParams(Map<HttpCommadReqParam, List<HttpCommadReqParam>> innerReqParams) {
        this.innerReqParams = innerReqParams;
    }

    public Map<HttpCommadRespParam, List<HttpCommadRespParam>> getInnerRespParams() {
        return innerRespParams;
    }

    public void setInnerRespParams(Map<HttpCommadRespParam, List<HttpCommadRespParam>> innerRespParams) {
        this.innerRespParams = innerRespParams;
    }

    public List<String> getReqImprotClass() {
        return reqImprotClass;
    }

    public void setReqImprotClass(List<String> reqImprotClass) {
        this.reqImprotClass = reqImprotClass;
    }

    public List<String> getRespImprotClass() {
        return respImprotClass;
    }

    public void setRespImprotClass(List<String> respImprotClass) {
        this.respImprotClass = respImprotClass;
    }

    public String getTestParams() {
        return testParams;
    }

    public void setTestParams(String testParams) {
        this.testParams = testParams;
    }

    public String getTestRemark() {
        return testRemark;
    }

    public void setTestRemark(String testRemark) {
        this.testRemark = testRemark;
    }
}
