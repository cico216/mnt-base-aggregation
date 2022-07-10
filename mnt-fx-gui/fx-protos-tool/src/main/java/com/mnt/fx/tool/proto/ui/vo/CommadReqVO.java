package com.mnt.fx.tool.proto.ui.vo;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * 每一条请求vo
 */
public class CommadReqVO {

    /**
     * 参数名
     */
    private SimpleStringProperty name = new SimpleStringProperty("");

    /**
     * 参数备注
     */
    private SimpleStringProperty remark = new SimpleStringProperty("");

    /**
     * 参数类型
     */
    private SimpleStringProperty type = new SimpleStringProperty("");

    /**
     * 参数长度
     */
    private SimpleIntegerProperty length = new SimpleIntegerProperty(0);

    /**
     * 最小长度
     */
    private SimpleStringProperty min = new SimpleStringProperty("");

    /**
     * 最大长度
     */
    private SimpleStringProperty max = new SimpleStringProperty("");

    /**
     * 参数类型
     */
    private SimpleStringProperty limit = new SimpleStringProperty("");

    /**
     * 是否必传
     */
    private SimpleBooleanProperty must = new SimpleBooleanProperty(false);

    /**
     * 测试值
     */
    private SimpleStringProperty test = new SimpleStringProperty("");

    /**
     * 类型泛型参数
     */
    private SimpleStringProperty typeClass = new SimpleStringProperty("");

    /**
     * date格式化
     */
    private SimpleStringProperty format = new SimpleStringProperty("");

    /**
     * 验证通知
     */
    private SimpleStringProperty valMsg = new SimpleStringProperty("");

    /**
     * 校验注解
     */
    private SimpleStringProperty valid = new SimpleStringProperty("");

    private List<CommadReqVO> childrens = new ArrayList<>(10);

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getRemark() {
        return remark.get();
    }

    public SimpleStringProperty remarkProperty() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark.set(remark);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public int getLength() {
        return length.get();
    }

    public SimpleIntegerProperty lengthProperty() {
        return length;
    }

    public void setLength(int length) {
        this.length.set(length);
    }

    public boolean isMust() {
        return must.get();
    }

    public SimpleBooleanProperty mustProperty() {
        return must;
    }

    public void setMust(boolean must) {
        this.must.set(must);
    }

    public String getTest() {
        return test.get();
    }

    public SimpleStringProperty testProperty() {
        return test;
    }

    public void setTest(String test) {
        this.test.set(test);
    }

    public String getTypeClass() {
        return typeClass.get();
    }

    public SimpleStringProperty typeClassProperty() {
        return typeClass;
    }

    public void setTypeClass(String typeClass) {
        this.typeClass.set(typeClass);
    }

    public List<CommadReqVO> getChildrens() {
        return childrens;
    }

    public String getFormat() {
        return format.get();
    }

    public SimpleStringProperty formatProperty() {
        return format;
    }

    public void setFormat(String format) {
        this.format.set(format);
    }

    public String getMin() {
        return min.get();
    }

    public SimpleStringProperty minProperty() {
        return min;
    }

    public void setMin(String min) {
        this.min.set(min);
    }

    public String getMax() {
        return max.get();
    }

    public SimpleStringProperty maxProperty() {
        return max;
    }

    public void setMax(String max) {
        this.max.set(max);
    }

    public String getLimit() {
        return limit.get();
    }

    public SimpleStringProperty limitProperty() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit.set(limit);
    }

    public String getValMsg() {
        return valMsg.get();
    }

    public SimpleStringProperty valMsgProperty() {
        return valMsg;
    }

    public void setValMsg(String valMsg) {
        this.valMsg.set(valMsg);
    }

    public String getValid() {
        return valid.get();
    }

    public SimpleStringProperty validProperty() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid.set(valid);
    }
}
