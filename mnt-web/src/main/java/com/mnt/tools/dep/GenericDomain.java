package com.mnt.tools.dep;


import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * dto基类
 *
 * @author jiangbiao
 * @Date 2017年4月18日上午11:37:21
 */
@SuppressWarnings("serial")
public class GenericDomain<PK> implements Serializable {
	
	protected PK id; //id
	protected String userCreate;//创建用户
	protected String userModified;//修改用户
    @JSONField(format = "yyyy-MM-dd")
    protected Date gmtCreate;//创建日期
    @JSONField(format = "yyyy-MM-dd")
    protected Date gmtModified;//修改日期
	protected String remark;//备注
	protected boolean delFlag;//删除标记
	

	public PK getId() {
		return id;
	}

	public void setId(PK id) {
		this.id = id;
	}

   public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate == null ? null : userCreate.trim();
    }

    public String getUserModified() {
        return userModified;
    }

    public void setUserModified(String userModified) {
        this.userModified = userModified == null ? null : userModified.trim();
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	public boolean isDelFlag() {
		return delFlag;
	}

	public void setDelFlag(boolean delFlag) {
		this.delFlag = delFlag;
	}
	
}
