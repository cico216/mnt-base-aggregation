package com.mnt.fx.tool.proto.ui.vo;

import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * 每一条命令参数vo
 */
public class CommandParamVO {

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
     * 类型泛型参数
     */
    private SimpleStringProperty typeClass = new SimpleStringProperty("");


    private List<CommandParamVO> childrens = new ArrayList<>(10);

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



    public String getTypeClass() {
        return typeClass.get();
    }

    public SimpleStringProperty typeClassProperty() {
        return typeClass;
    }

    public void setTypeClass(String typeClass) {
        this.typeClass.set(typeClass);
    }

    public List<CommandParamVO> getChildrens() {
        return childrens;
    }


}
