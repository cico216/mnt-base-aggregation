package com.mnt.fx.tool.proto.http.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 每条请求参数
 * @author jiangbiao
 * @date 2018/8/17 14:58
 */
public class HttpCommadReqParam {

    /**
     * 参数名
     */
    private String name;

    /**
     * 参数备注
     */
    private String remark;

    /**
     * 参数类型
     */
    private String type;

    /**
     * 参数长度
     */
    private Integer length;

    /**
     * 最大值
     */
    private String max;

    /**
     * 最小值
     */
    private String min;


    /**
     * 是否必传
     */
    private Boolean must;

    /**
     * 类型泛型参数
     */
    private String typeClass;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * date日期格式
     */
    private String format;

    /**
     * 校验通知
     */
    private String valMsg;

    /**
     * 校验注解
     */
    private String valid;

    /**
     * 校验代码
     */
    private String validCode;

    /**
     * 请求参数列表
     */
    private List<HttpCommadReqParam> childrens = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Boolean getMust() {
        return must;
    }

    public void setMust(Boolean must) {
        this.must = must;
    }

    public String getTypeClass() {
        return typeClass;
    }

    public void setTypeClass(String typeClass) {
        this.typeClass = typeClass;
    }

    public List<HttpCommadReqParam> getChildrens() {
        return childrens;
    }


    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getValMsg() {
        return valMsg;
    }

    public void setValMsg(String valMsg) {
        this.valMsg = valMsg;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getValidCode() {
        return validCode;
    }

    public void setValidCode(String validCode) {
        this.validCode = validCode;
    }
}
