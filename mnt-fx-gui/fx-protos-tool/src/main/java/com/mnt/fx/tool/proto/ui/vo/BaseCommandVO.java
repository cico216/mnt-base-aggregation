package com.mnt.fx.tool.proto.ui.vo;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.dom4j.Node;

/**
 * 基础命令vo
 */
public class BaseCommandVO {

    /**
     * 是否选择
     */
    private SimpleBooleanProperty choose = new SimpleBooleanProperty(false);

    /**
     * 命令备注
     */
    private SimpleStringProperty remark = new SimpleStringProperty("");

    /**
     * 生成代码的命令key
     */
    private String cmdKey;

    /**
     * 当前节点
     */
    private Node currNode;


    public boolean isChoose() {
        return choose.get();
    }

    public SimpleBooleanProperty chooseProperty() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose.set(choose);
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

    public Node getCurrNode() {
        return currNode;
    }

    public void setCurrNode(Node currNode) {
        this.currNode = currNode;
    }

    public String getCmdKey() {
        return cmdKey;
    }

    public void setCmdKey(String cmdKey) {
        this.cmdKey = cmdKey;
    }
}
