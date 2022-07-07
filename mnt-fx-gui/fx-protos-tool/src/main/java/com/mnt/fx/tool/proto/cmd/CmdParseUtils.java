package com.mnt.fx.tool.proto.cmd;


import com.mnt.fx.tool.common.utils.XMLParseUtils;
import com.mnt.fx.tool.proto.http.core.HttpProtoCodeGenerateTemplate;
import com.mnt.fx.tool.proto.http.core.HttpTemplateClassLoad;
import com.mnt.fx.tool.proto.http.model.HttpActionModel;
import com.mnt.fx.tool.proto.http.model.HttpGenerateConfigInfo;
import com.mnt.fx.tool.proto.http.model.HttpProtoModel;
import com.mnt.fx.tool.proto.http.utils.HttpProtoParseUtils;
import com.mnt.fx.tool.proto.netty.core.NettyProtoCodeGenerateTemplate;
import com.mnt.fx.tool.proto.netty.core.NettyTemplateClassLoad;
import com.mnt.fx.tool.proto.netty.model.NettyCommandModel;
import com.mnt.fx.tool.proto.netty.model.NettyGenerateConfigInfo;
import com.mnt.fx.tool.proto.netty.model.NettyProtoModel;
import com.mnt.fx.tool.proto.netty.utils.NettyProtoParseUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 命令行解析工具
 *
 * @author cc
 * @date 2022/3/8
 */
public class CmdParseUtils {

    /**
     * 生成tcp 协议
     * @param type 代码类型  java 或者 cs
     * @param xmlFile xml文件
     * @param opCodes 需要生成的命令号
     */
    public static void generateTCPProto(String type, File xmlFile, List<Integer> opCodes) {

        XMLParseUtils.XMLObject xmlObject = XMLParseUtils.parseXML(xmlFile);
        NettyProtoModel nettyProtoModel =  NettyProtoParseUtils.getProto(xmlObject);
        List<NettyCommandModel> nettyCommandModels =  NettyProtoParseUtils.getCommads(type, xmlObject, opCodes);
        Map<String, NettyGenerateConfigInfo> generateConfigs =  NettyProtoParseUtils.getGenerateConfigs(xmlObject);
        nettyProtoModel.setCommands(nettyCommandModels);

        Map<String, String> codeTmps =  NettyProtoParseUtils.getCodeTmps(xmlObject);
        nettyProtoModel.setCodeTmps(codeTmps);
        //获取模板
        NettyProtoCodeGenerateTemplate generateTemplate = NettyTemplateClassLoad.getByType(type);
        //设置配置属性
        nettyProtoModel.setGenerateConfigInfo(generateConfigs.get(generateTemplate.getType()));
        //生成代码
        generateTemplate.generate(nettyProtoModel);

    }

    /**
     * 生成netty 协议
     * @param type 代码类型  java 或者 cs
     * @param xmlFile xml文件
     * @param actionNames 需要生成的请求名称
     */
    public static void generateHttpProto(String type, File xmlFile, List<String> actionNames) {
        XMLParseUtils.XMLObject xmlObject = XMLParseUtils.parseXML(xmlFile);
        HttpProtoModel httpProtoModel = HttpProtoParseUtils.getProto(xmlObject);
        List<HttpActionModel> actionModels = HttpProtoParseUtils.getCommads(type, xmlObject, actionNames);
        Map<String, HttpGenerateConfigInfo> generateConfigs =  HttpProtoParseUtils.getGenerateConfigs(xmlObject);
        httpProtoModel.setActions(actionModels);
        //获取模板
        HttpProtoCodeGenerateTemplate generateTemplate = HttpTemplateClassLoad.getByType(type);
        //设置配置属性
        httpProtoModel.setGenerateConfigInfo(generateConfigs.get(generateTemplate.getType()));
        //生成代码
        generateTemplate.generate(httpProtoModel);

    }



}
