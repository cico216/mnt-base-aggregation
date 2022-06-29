package com.mnt.fx.tool.proto.netty.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 每条请求参数
 * @author cc
 * @date 2018/8/17 14:58
 */
@Data
public class NettyCommandParam {

    /**
     * 参数名
     */
    private String name;

    /**
     * cs名称
     */
    private String csName;

    /**
     * 参数备注
     */
    private String remark;

    /**
     * 参数类型
     */
    private String type;

    /**
     * cs参数类型
     */
    private String csType;

    /**
     * 参数拆箱类型
     */
    private String unboxType;

    /**
     * 类型泛型参数
     */
    private String typeClass;

    /**
     * 参数转换的代码
     */
    private String code;


    /**
     * 请求参数列表
     */
    private List<NettyCommandParam> childrens = new ArrayList<>();

}
