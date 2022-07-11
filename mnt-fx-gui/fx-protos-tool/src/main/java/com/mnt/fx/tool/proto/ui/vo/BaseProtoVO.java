package com.mnt.fx.tool.proto.ui.vo;

import com.mnt.fx.tool.common.utils.XMLParseUtils;
import javafx.beans.property.SimpleStringProperty;

/**
 * 基础协议列表vo
 */
public class BaseProtoVO {

    /**
     * 显示名称
     */
    private SimpleStringProperty remark = new SimpleStringProperty("");

    /**
     * 是否为文件夹
     */
    private boolean isDir;

    /**
     * 协议类型 tcp or http
     */
    private String protoType;

    private String filePath;

    private XMLParseUtils.XMLObject xmlObject;

    public String getRemark() {
        return remark.get();
    }

    public SimpleStringProperty remarkProperty() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark.set(remark);
    }

    public XMLParseUtils.XMLObject getXmlObject() {
        return xmlObject;
    }

    public void setXmlObject(XMLParseUtils.XMLObject xmlObject) {
        this.xmlObject = xmlObject;
    }

    public String getProtoType() {
        return protoType;
    }

    public void setProtoType(String protoType) {
        this.protoType = protoType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }


    /**
     * 关闭文件流
     */
    public void clean() {
        if(null != xmlObject) {
            xmlObject.close();
        }
    }
}
