package com.mnt.fx.tool.proto.netty.utils;



import com.mnt.fx.tool.common.enums.DefaultLoadClassEnums;
import com.mnt.fx.tool.common.utils.NameUtils;
import com.mnt.fx.tool.common.utils.ParamTypeUtils;
import com.mnt.fx.tool.common.utils.XMLParseUtils;

import com.mnt.fx.tool.proto.conf.UserData;
import com.mnt.fx.tool.proto.netty.model.NettyCommandModel;
import com.mnt.fx.tool.proto.netty.model.NettyCommandParam;
import com.mnt.fx.tool.proto.netty.model.NettyGenerateConfigInfo;
import com.mnt.fx.tool.proto.netty.model.NettyProtoModel;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 协议vo转换工具类
 */
public class NettyProtoParseUtils {


    /**
     * 获取测试地址
     * @param xmlObject
     * @return
     */
    public static String getProtoAttr(XMLParseUtils.XMLObject xmlObject, String attrName) {

        return xmlObject.getRoot().attributeValue(attrName);

    }

    /**
     * 获取协议文件名
     * @param xmlObject
     * @return
     */
    public static String getProtoRemark(XMLParseUtils.XMLObject xmlObject) {

       return getProtoAttr(xmlObject,"remark");

    }


    /**
     * 获取总路径
     * @param xmlObject
     * @return
     */
    public static String getProtoModuleName(XMLParseUtils.XMLObject xmlObject) {

        return getProtoAttr(xmlObject,"moduleName");

    }

    /**
     * 获取协议类名
     * @param xmlObject
     * @return
     */
    public static String getProtoCodeLimit(XMLParseUtils.XMLObject xmlObject) {

        return getProtoAttr(xmlObject,"codeLimit");

    }

    /**
     * 获取测试路径
     * @param xmlObject
     * @return
     */
    public static String getProtoTestUrl(XMLParseUtils.XMLObject xmlObject) {

        return getProtoAttr(xmlObject,"testUrl");

    }

    /**
     * 获取测试地址
     * @param xmlObject
     * @return
     */
    public static String getProtoTestAddr(XMLParseUtils.XMLObject xmlObject) {

        return getProtoAttr(xmlObject,"testAddr");

    }

//    /**
//     * 获取请求方法
//     * @param xmlObject
//     * @return
//     */
//    public static String getProtoReqMethod(XMLParseUtils.XMLObject xmlObject) {
//        String result = getProtoAttr(xmlObject,"method");
//        if(null != result) {
//            return result.toLowerCase();
//        }
//        return result;
//    }
//
//    /**
//     * 获取是否为body请求
//     * @param xmlObject
//     * @return
//     */
//    public static boolean getProtoIsBody(XMLParseUtils.XMLObject xmlObject) {
//
//        return "true".equals(getProtoAttr(xmlObject,"body"));
//
//    }

    /**
     * 获取基础协议名称
     * @param xmlObject
     * @return
     */
    public static NettyProtoModel getProto(XMLParseUtils.XMLObject xmlObject) {
        NettyProtoModel result = new NettyProtoModel();
        result.setUser(UserData.getUserConfig().getUser());
        result.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        result.setRemark(getProtoRemark(xmlObject));
        result.setModuleName(getProtoModuleName(xmlObject));
        return result;
    }

    /**
     * 获取协议命令列表
     * @param xmlObject
     * @return
     */
    public static List<NettyCommandModel> getCommads(String generateType, XMLParseUtils.XMLObject xmlObject, List<Integer> opCodes) {
        List<Node> nodes = xmlObject.findEle("protos/cmd");

        List<NettyCommandModel> result = new ArrayList<>(nodes.size());
        NettyCommandModel vo;

        for (Node node : nodes) {
            int opCode = Integer.parseInt(node.valueOf("@opCode"));
            if(!opCodes.isEmpty()) { //指定了命令号时 则只生成这些命令
                if(!opCodes.contains(opCode)) {
                    continue;
                }
            }

            vo = new NettyCommandModel();
            vo.setOpCode(opCode);
            vo.setName(node.valueOf("@name"));
            vo.setRemark(node.valueOf("@remark"));
            //引入class
            List<String> commandImportClass = new ArrayList<>();
            vo.setCommandImportClass(commandImportClass);

            vo.setSrc(node.valueOf("@src"));
//            vo.setCurrNode(node);
            parseCommandParamVOsToCommadVO(generateType, vo, node, commandImportClass);
            result.add(vo);
        }


        return result;
    }

    /**
     * 获取协议代码模板
     * @param xmlObject
     * @return
     */
    public static Map<String, String> getCodeTmps(XMLParseUtils.XMLObject xmlObject) {
        List<Node> nodes = xmlObject.findEle("protos/codetmps/tmp");
        Map<String, String> result = new HashMap<>(nodes.size());
        for (Node node : nodes) {
            result.put(node.valueOf("@tmpKey"), node.valueOf("@tmpValue"));
        }

        return result;
    }

    /**
     * 获取协议命令列表
     * @param xmlObject
     * @return
     */
    public static Map<String, NettyGenerateConfigInfo> getGenerateConfigs(XMLParseUtils.XMLObject xmlObject) {
        List<Node> nodes = xmlObject.findEle("protos/generates/generate");
        Map<String, NettyGenerateConfigInfo> result = new HashMap<>(nodes.size());
        NettyGenerateConfigInfo generateConfigInfo;
        for (Node node : nodes) {
            generateConfigInfo = new NettyGenerateConfigInfo();
            generateConfigInfo.setPackageName(node.valueOf("@cmdPackage"));
            generateConfigInfo.setProjectName(node.valueOf("@projectName"));
            generateConfigInfo.setType(node.valueOf("@type"));
            result.put(generateConfigInfo.getType(), generateConfigInfo);
        }

        return result;
    }

    /**
     * 解析详细命令
     * @return
     */
    public static List<NettyCommandParam> parseCommandParamVOsToCommadVO(String generateType, NettyCommandModel baseCommandVO, Node requestNode, List<String> commandImportClass) {
        //Node requestNode = baseCommandVO.getCurrNode();
        List<NettyCommandParam> result = new ArrayList<>();
        setCommandChildrenCommandVO(generateType, requestNode, result, commandImportClass);
        baseCommandVO.setCommandParams(result);
        return result;
    }

    private static void setCommandChildrenCommandVO(String generateType, Node requestNode, List<NettyCommandParam> result, List<String> commandImportClass) {
        List<Node> paramNodes = requestNode.selectNodes("param");

        NettyCommandParam commandParamVO;
        for (Node node : paramNodes) {
            commandParamVO = new NettyCommandParam();
            commandParamVO.setName(node.valueOf("@name"));
            commandParamVO.setCsName(NameUtils.upperFristStr(commandParamVO.getName()));
            commandParamVO.setRemark(node.valueOf("@remark"));
            String type = ParamTypeUtils.convertType(node.valueOf("@type"));

            String typeClass = node.valueOf("@typeClass");
            commandParamVO.setTypeClass(typeClass);
            commandParamVO.setType(type);
            commandParamVO.setUnboxType(ParamTypeUtils.getUnboxType(type));

            DefaultLoadClassEnums defaultLoadClassEnums =  DefaultLoadClassEnums.getByName(type);
            if(null != defaultLoadClassEnums) {

                String [] typeClassArray = new String[2];
                if(typeClass.contains("|")) {
                    String [] splitArray =   typeClass.split("\\|");
                    typeClassArray[0] = splitArray[0];
                    typeClassArray[1] = splitArray[1];
                } else {
                    typeClassArray[0] = typeClass;
                    typeClassArray[1] = typeClass;
                }

                if("java".equals(generateType)){
                    typeClass = typeClassArray[0];
                    checkAndAdd(commandImportClass, defaultLoadClassEnums.getImportClass());
                    if(defaultLoadClassEnums == DefaultLoadClassEnums.LIST) {
                        checkAndAdd(commandImportClass, "java.util.ArrayList");
                    }
                } else { //为cs时
                    typeClass = typeClassArray[1];

                }
                if(defaultLoadClassEnums == DefaultLoadClassEnums.LIST) {
                    //java的类型导入
                    if(!StringUtils.isEmpty(typeClass)) {
                        String needImportType = ParamTypeUtils.convertType(typeClass);
                        if(null == needImportType) {
                            checkAndAdd(commandImportClass, typeClass);
//                        type = type + "<" + NameUtils.buildInnerClassName(commadReqVO.getName()) + ">";
                            int lastIndex = typeClass.lastIndexOf(".") + 1;
                            typeClass = typeClass.substring(lastIndex);
                        }
                        type = type + "<" + typeClass + ">";
                        commandParamVO.setUnboxType(type);
                        commandParamVO.setTypeClass(typeClass);

                    }
                }


            }



            result.add(commandParamVO);

            List<Node> innerParamNodes = requestNode.selectNodes("param");
            if(!innerParamNodes.isEmpty()) {
                setCommandChildrenCommandVO(generateType, node, commandParamVO.getChildrens(), commandImportClass);
            }

        }

    }

    /**
     * 判断和添加类名 不允许重复
     * @param classList
     * @param classPackage
     */
    private static void checkAndAdd(List<String> classList, String classPackage) {

        if(!classList.contains(classPackage)) {
            classList.add(classPackage);
        }

    }




    public static void main(String[] args) {
        XMLParseUtils.XMLObject xmlObject = XMLParseUtils.parseXML(new File(System.getProperty("user.dir") + "/protocal-tools/protos/tcp_base.xml"));
        System.err.println(getProtoCodeLimit(xmlObject));
    }

}
