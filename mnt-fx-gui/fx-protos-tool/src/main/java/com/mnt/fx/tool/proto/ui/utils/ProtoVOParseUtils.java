package com.mnt.fx.tool.proto.ui.utils;

import com.mnt.fx.tool.common.utils.XMLParseUtils;
import com.mnt.fx.tool.proto.ui.vo.*;
import com.mnt.gui.fx.controls.dialog.DialogFactory;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Node;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 协议展示解析工具
 *
 * @author cc
 * @date 2022/3/10
 */
public class ProtoVOParseUtils {


    /**
     * 解析文件协议
     * @param dirPath
     * @return
     */
    public static List<BaseProtoVO> parseProtoDir(String dirPath) {

        List<BaseProtoVO> result = new ArrayList<>();
        File file =  new File(dirPath);
        File[] xmlFiles = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".xml") || new File(dir.getPath() + "/" + name).isDirectory();
            }
        });

        for(File xmlFile : xmlFiles){
            try {
                BaseProtoVO baseProtoVO;
                if(xmlFile.isDirectory()) {
                    baseProtoVO = new BaseProtoVO();
                    baseProtoVO.setFilePath(xmlFile.getAbsolutePath());
                    baseProtoVO.setRemark(xmlFile.getName());
                    baseProtoVO.setDir(true);

                } else {
                    XMLParseUtils.XMLObject xmlObject = XMLParseUtils.parseXML(xmlFile);
                    baseProtoVO = new BaseProtoVO();
                    baseProtoVO.setXmlObject(xmlObject);
                    baseProtoVO.setFilePath(xmlFile.getAbsolutePath());
                    String remake ;

                    if(xmlFile.getName().startsWith("tcp")) {
                        baseProtoVO.setProtoType("tcp");
                        remake =  getProtoAttr(xmlObject,"remark") + "[" + getProtoAttr(xmlObject,"codeLimit") + "]";
                    } else {
                        baseProtoVO.setProtoType("http");
                        remake =  getProtoAttr(xmlObject,"remark") + "[" + getProtoAttr(xmlObject,"name") + "]";
                    }
                    baseProtoVO.setRemark(remake);
                }
                result.add(baseProtoVO);
            } catch (Exception e) {
                e.printStackTrace();
                DialogFactory.getInstance().showConfirm("文件加载失败" + file.getAbsolutePath(), e.getLocalizedMessage(), ()->{});
//                ConsoleLogUtils.log("文件加载失败 : " + file.getAbsolutePath() + " | " + e.getLocalizedMessage());
            }
        }
        return result;
    }

    /**
     * 解析cmd命令
     * @param baseProtoVO
     * @return
     */
    public static List<BaseCommandVO> parseProtoCmd(BaseProtoVO baseProtoVO) {
        XMLParseUtils.XMLObject xmlObject = baseProtoVO.getXmlObject();
        List<BaseCommandVO> result = new ArrayList<>();

        BaseCommandVO baseCommandVO;
        if("tcp".equals(baseProtoVO.getProtoType())) {
            List<Node> nodes = xmlObject.findEle("protos/cmd");
            for (Node node : nodes) {
                baseCommandVO = new BaseCommandVO();
                baseCommandVO.setCurrNode(node);
                baseCommandVO.setCmdKey(node.valueOf("@opCode"));
                String remark = "[" + node.valueOf("@opCode") + "]" + node.valueOf("@remark");
                baseCommandVO.setRemark(remark);
                result.add(baseCommandVO);
            }

        } else if("http".equals(baseProtoVO.getProtoType())){
            List<Node> nodes = xmlObject.findEle("protos/action");
            for (Node node : nodes) {
                baseCommandVO = new BaseCommandVO();
                baseCommandVO.setCurrNode(node);
                baseCommandVO.setCmdKey(node.valueOf("@path"));
                String remark =   "[" + node.valueOf("@method") + "]" + node.valueOf("@remark");
                baseCommandVO.setRemark(remark);
                result.add(baseCommandVO);
            }

        }

        return result;
    }


    /**
     * 解析详细命令
     * @return
     */
    public static List<CommadReqVO> parseCommadReqVOs(BaseCommandVO baseCommadVO) {
        Node requestNode = baseCommadVO.getCurrNode().selectSingleNode("request");
        List<CommadReqVO> result = new ArrayList<>();
        setCommadReqChildrenVO(requestNode, result);
        return result;
    }

    private static void setCommadReqChildrenVO(Node requestNode, List<CommadReqVO> result) {
        List<Node> paramNodes = requestNode.selectNodes("param");

        CommadReqVO commadReqVO;
        for (Node node : paramNodes) {
            commadReqVO = new CommadReqVO();
            commadReqVO.setName(node.valueOf("@name"));
            commadReqVO.setRemark(node.valueOf("@remark"));
            commadReqVO.setType(node.valueOf("@type"));
            commadReqVO.setValid(node.valueOf("@valid"));
            commadReqVO.setTypeClass(node.valueOf("@typeClass"));
            commadReqVO.setValMsg(node.valueOf("@valMsg"));
            String length = node.valueOf("@length");

            String limit = length;
            String min =  node.valueOf("@min");
            String max =  node.valueOf("@max");

            if(!StringUtils.isEmpty(min) || !StringUtils.isEmpty(max)) {
                limit = "[" + (StringUtils.isEmpty(min) ? "- ∞" : min) + "," + (StringUtils.isEmpty(max) ? "+ ∞" : max) + "]";
                //如果为string类型 则最小值为0
                if("string".equals(String.valueOf(commadReqVO.getType()).toLowerCase())) {
                    limit.replace("- ∞", "0");
                }

            }

            commadReqVO.setLimit(limit);
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
                ConsoleLogUtils.log( "[" + commadReqVO.getName() + "]length, max, min必须为数字");
                ConsoleLogUtils.log(e);
            }

            String must = node.valueOf("@must");
            commadReqVO.setMust(Boolean.valueOf(must));
            commadReqVO.setTest(node.valueOf("@test"));
            commadReqVO.setFormat(node.valueOf("@format"));
            result.add(commadReqVO);

            List<Node> innerParamNodes = requestNode.selectNodes("param");
            if(!innerParamNodes.isEmpty()) {
                setCommadReqChildrenVO(node, commadReqVO.getChildrens());
            }

        }

    }


    /**
     * 解析详细命令
     * @return
     */
    public static List<CommadRespVO> parseCommadRespVOs(BaseCommandVO baseCommadVO) {
        Node requestNode = baseCommadVO.getCurrNode().selectSingleNode("response");
        List<CommadRespVO> result = new ArrayList<>();


        boolean isDataStart = false;
        if(isDataStart) {
            List<Node> paramNodes = requestNode.selectNodes("param");
            //答复参数从data开始解析
            for (Node dataNode : paramNodes) {
                if("data".equals(dataNode.valueOf("@name"))) {
                    setCommadRespChildrenVO(dataNode, result);
                }
            }
        } else {
            setCommadRespChildrenVO(requestNode, result);
        }


        return result;
    }

    private static void setCommadRespChildrenVO(Node requestNode, List<CommadRespVO> result) {
        List<Node> paramNodes = requestNode.selectNodes("param");

        CommadRespVO commadRespVO;
        for (Node node : paramNodes) {
            commadRespVO = new CommadRespVO();
            commadRespVO.setName(node.valueOf("@name"));
            commadRespVO.setRemark(node.valueOf("@remark"));
            commadRespVO.setType(node.valueOf("@type"));
            commadRespVO.setTypeClass(node.valueOf("@typeClass"));
            commadRespVO.setTest(node.valueOf("@test"));
            commadRespVO.setFormat(node.valueOf("@format"));
            result.add(commadRespVO);

            List<Node> innerParamNodes = requestNode.selectNodes("param");
            if(!innerParamNodes.isEmpty()) {
                setCommadRespChildrenVO(node, commadRespVO.getChildrens());
            }

        }

    }

    /**
     * 解析详细命令
     * @return
     */
    public static List<CommandParamVO> parseCommandParamVO(BaseCommandVO BaseCommandVO) {
        Node requestNode = BaseCommandVO.getCurrNode();
        List<CommandParamVO> result = new ArrayList<>();
        setCommandChildrenCommandVO(requestNode, result);
        return result;
    }

    private static void setCommandChildrenCommandVO(Node requestNode, List<CommandParamVO> result) {
        List<Node> paramNodes = requestNode.selectNodes("param");

        CommandParamVO commandParamVO;
        for (Node node : paramNodes) {
            commandParamVO = new CommandParamVO();
            commandParamVO.setName(node.valueOf("@name"));
            commandParamVO.setRemark(node.valueOf("@remark"));
            commandParamVO.setType(node.valueOf("@type"));
            commandParamVO.setTypeClass(node.valueOf("@typeClass"));


            result.add(commandParamVO);

            List<Node> innerParamNodes = requestNode.selectNodes("param");
            if(!innerParamNodes.isEmpty()) {
                setCommandChildrenCommandVO(node, commandParamVO.getChildrens());
            }

        }

    }

    public static String getProtoPath(XMLParseUtils.XMLObject xmlObject) {

        return getProtoAttr(xmlObject,"path");

    }


    public static String getProtoAttr(XMLParseUtils.XMLObject xmlObject, String attrName) {

        return xmlObject.getRoot().attributeValue(attrName);

    }

}
