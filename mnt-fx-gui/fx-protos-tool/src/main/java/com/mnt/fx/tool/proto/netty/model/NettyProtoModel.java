package com.mnt.fx.tool.proto.netty.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * proto model obj
 * @author cc
 */
@Data
public class NettyProtoModel {

    /**
     * 代码生成配置信息
     */
    private NettyGenerateConfigInfo generateConfigInfo;


    /**
     * 注释信息
     */
    private String remark;

    /**
     * 所属模块
     */
    private String moduleName;

    /**
     * 当前用户
     */
    private String user;

    /**
     * 创建日期
     */
    private String date;

    /**
     * 请求列表
     */
    private List<NettyCommandModel> commands;

    /**
     * 代码模板
     */
    private Map<String, String> codeTmps;


}
