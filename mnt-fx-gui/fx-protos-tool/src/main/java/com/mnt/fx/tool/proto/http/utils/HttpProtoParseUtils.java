package com.mnt.fx.tool.proto.http.utils;


import com.mnt.fx.tool.common.utils.NameUtils;
import com.mnt.fx.tool.common.utils.ParamTypeUtils;
import com.mnt.fx.tool.common.utils.XMLParseUtils;
import com.mnt.fx.tool.proto.conf.UserData;
import com.mnt.fx.tool.proto.http.model.*;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 协议vo转换工具类
 */
public class HttpProtoParseUtils {


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
    public static String getProtoPath(XMLParseUtils.XMLObject xmlObject) {

        return getProtoAttr(xmlObject,"path");

    }

    /**
     * 获取协议类名
     * @param xmlObject
     * @return
     */
    public static String getProtoName(XMLParseUtils.XMLObject xmlObject) {

        return getProtoAttr(xmlObject,"name");

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
    public static HttpProtoModel getProto(XMLParseUtils.XMLObject xmlObject) {
        HttpProtoModel result = new HttpProtoModel();
        result.setRemark(getProtoRemark(xmlObject));
        String name = getProtoName(xmlObject);
        result.setRequestMapper(getProtoPath(xmlObject));
        result.setControllerName(name);
        result.setImportPackages(new ArrayList<>());
        result.setUser(UserData.getUserConfig().getUser());
        result.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        return result;
    }

    /**
     * 获取协议命令列表
     * @param generateType 生成代码类型  java  cs
     * @param xmlObject
     * @param actionNames  需要生成的命令
     * @return
     */
    public static List<HttpActionModel> getCommads(String generateType, XMLParseUtils.XMLObject xmlObject, List<String> actionNames) {
        List<Node> nodes = xmlObject.findEle("protos/action");
        List<HttpActionModel> result = new ArrayList<>(nodes.size());
        HttpActionModel vo;
        for (Node node : nodes) {
            String path = node.valueOf("@path");
            if(!actionNames.isEmpty()) {
                if(!actionNames.contains(path)) {
                    continue;
                }
            }
            vo = new HttpActionModel();
            vo.setActionName(path);
            vo.setRequestMapper(path);
            vo.setRemark(node.valueOf("@remark"));
            vo.setMethod(node.valueOf("@method").toUpperCase());
            String body = node.valueOf("@body");
            vo.setBody(Boolean.valueOf(body));

            List<String> actionReqImportsClass = new ArrayList<>();
            vo.setReqImprotClass(actionReqImportsClass);
            Map<HttpCommadReqParam, List<HttpCommadReqParam>> innerReqParams = new HashMap<>();
            vo.setInnerReqParams(innerReqParams);

            List<HttpCommadReqParam>  httpCommadReqParams = parseHttpCommadReqParams(generateType, node, vo); //解析请求参数
            vo.setCommadReqParams(httpCommadReqParams);

            //返回参数导入的包
            List<String> actionRespImportsClass = new ArrayList<>();
            vo.setRespImprotClass(actionRespImportsClass);
            Map<HttpCommadRespParam, List<HttpCommadRespParam>> innerRespParams = new HashMap<>();
            vo.setInnerRespParams(innerRespParams);

            List<HttpCommadRespParam>  httpCommadRespParams =  parseHttpCommadRespParams(generateType, node, vo); //解析返回参数
            vo.setCommadRespParams(httpCommadRespParams);


            //vo.setCurrNode(node);
            result.add(vo);
        }

        return result;
    }

    /**
     * 获取协议命令列表
     * @param xmlObject
     * @return
     */
    public static Map<String, HttpGenerateConfigInfo> getGenerateConfigs(XMLParseUtils.XMLObject xmlObject) {
        List<Node> nodes = xmlObject.findEle("protos/generates/generate");
        Map<String, HttpGenerateConfigInfo> result = new HashMap<>(nodes.size());
        HttpGenerateConfigInfo generateConfigInfo;
        for (Node node : nodes) {
            generateConfigInfo = new HttpGenerateConfigInfo();
            generateConfigInfo.setPackageName(node.valueOf("@actionPackage"));
            generateConfigInfo.setApiProjectName(node.valueOf("@apiProjectName"));
            generateConfigInfo.setProjectName(node.valueOf("@projectName"));
            generateConfigInfo.setType(node.valueOf("@type"));
            generateConfigInfo.setModuleName(node.valueOf("@moduleName"));
            result.put(generateConfigInfo.getType(), generateConfigInfo);
        }

        return result;
    }

    /**
     * 解析详细命令
     * @return
     */
    public static List<HttpCommadReqParam> parseHttpCommadReqParams(String generateType, Node currNode, HttpActionModel actionModel) {
        Node requestNode = currNode.selectSingleNode("request");
        List<HttpCommadReqParam> result = new ArrayList<>();
        setCommadReqChildrenCommadVO(generateType, requestNode, actionModel, result);
        return result;
    }

    private static void setCommadReqChildrenCommadVO(String generateType, Node requestNode, HttpActionModel actionModel, List<HttpCommadReqParam> result) {
        List<Node> paramNodes = requestNode.selectNodes("param");

        HttpCommadReqParam commadReqVO;
        for (Node node : paramNodes) {
            commadReqVO = new HttpCommadReqParam();
            commadReqVO.setName(node.valueOf("@name"));
            commadReqVO.setRemark(node.valueOf("@remark"));
            commadReqVO.setType(node.valueOf("@type"));
            commadReqVO.setValid(node.valueOf("@valid"));
            commadReqVO.setTypeClass(node.valueOf("@typeClass"));
            commadReqVO.setValMsg(node.valueOf("@valMsg"));
            String length = node.valueOf("@length");

            String min =  node.valueOf("@min");
            String max =  node.valueOf("@max");

            try {
                if(!StringUtils.isEmpty(length)) {
                    commadReqVO.setLength(Integer.parseInt(length));
                }
                if(!StringUtils.isEmpty(min)) {
                    commadReqVO.setMin(min);
                }
                if(!StringUtils.isEmpty(max)) {
                    commadReqVO.setMax(max);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("[" + commadReqVO.getName() + "]length, max, min必须为数字");
//                ConsoleLogUtils.log( "[" + commadReqVO.getName() + "]length, max, min必须为数字");
//                ConsoleLogUtils.log(e);
            }

            commadReqVO.setValidCode("");

            String must = node.valueOf("@must");
            commadReqVO.setMust(Boolean.valueOf(must));
            commadReqVO.setFormat(node.valueOf("@format"));

            String type = ParamTypeUtils.convertType(commadReqVO.getType());

            String typeClass = ParamTypeUtils.convertType(commadReqVO.getTypeClass());
            commadReqVO.setType(type);

            if(null != typeClass) {
                commadReqVO.setTypeClass(typeClass);
            }

            commadReqVO.setMethodName(NameUtils.upperFristStr(commadReqVO.getName()));

            result.add(commadReqVO);

            List<Node> innerParamNodes = node.selectNodes("param");
            if(!innerParamNodes.isEmpty()) {
                actionModel.getInnerReqParams().put(commadReqVO, commadReqVO.getChildrens());
                setCommadReqChildrenCommadVO(generateType, node, actionModel, commadReqVO.getChildrens());
            }

        }

    }


    /**
     * 解析详细命令
     * @return
     */
    public static List<HttpCommadRespParam> parseHttpCommadRespParams(String generateType, Node currNode,  HttpActionModel actionModel) {
        Node requestNode = currNode.selectSingleNode("response");
        List<HttpCommadRespParam> result = new ArrayList<>();


        boolean isDataStart = true;
        if(isDataStart) {
            List<Node> paramNodes = requestNode.selectNodes("param");
            //答复参数从data开始解析
            for (Node dataNode : paramNodes) {
                if("data".equals(dataNode.valueOf("@name"))) {
                    setCommadRespChildrenCommadVO(generateType, dataNode, actionModel, result);
                }
            }
        } else {
            setCommadRespChildrenCommadVO(generateType, requestNode,  actionModel, result);
        }


        return result;
    }

    private static void setCommadRespChildrenCommadVO(String generateType, Node requestNode, HttpActionModel actionModel, List<HttpCommadRespParam> result) {
        List<Node> paramNodes = requestNode.selectNodes("param");

        HttpCommadRespParam commadRespVO;
        for (Node node : paramNodes) {
            commadRespVO = new HttpCommadRespParam();
            commadRespVO.setName(node.valueOf("@name"));
            commadRespVO.setRemark(node.valueOf("@remark"));
            commadRespVO.setType(node.valueOf("@type"));
            commadRespVO.setTypeClass(node.valueOf("@typeClass"));
            commadRespVO.setFormat(node.valueOf("@format"));

            String type = ParamTypeUtils.convertType(commadRespVO.getType());
            commadRespVO.setType(type);
            String typeClass = ParamTypeUtils.convertType(commadRespVO.getTypeClass());

            if(null != typeClass) {
                commadRespVO.setTypeClass(typeClass);
            }
            commadRespVO.setMethodName(NameUtils.upperFristStr(commadRespVO.getName()));
            result.add(commadRespVO);

            List<Node> innerParamNodes = node.selectNodes("param");
            if(!innerParamNodes.isEmpty()) {
                List<HttpCommadRespParam> respChildrens = commadRespVO.getChildrens();
                actionModel.getInnerRespParams().put(commadRespVO, respChildrens);
                setCommadRespChildrenCommadVO(generateType, node, actionModel, respChildrens);
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
        XMLParseUtils.XMLObject xmlObject = XMLParseUtils.parseXML(new File(System.getProperty("user.dir") + "/protos/test.xml"));
        System.err.println(getProtoName(xmlObject));
    }

}
