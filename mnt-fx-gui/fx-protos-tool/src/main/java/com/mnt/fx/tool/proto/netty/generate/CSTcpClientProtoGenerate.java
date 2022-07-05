package com.mnt.fx.tool.proto.netty.generate;


import com.mnt.fx.tool.common.utils.NameUtils;
import com.mnt.fx.tool.common.utils.ParamTypeUtils;
import com.mnt.fx.tool.common.utils.PathUtils;
import com.mnt.fx.tool.common.utils.VelocityUtils;
import com.mnt.fx.tool.proto.conf.UserData;
import com.mnt.fx.tool.proto.netty.core.NettyProtoCodeGenerateTemplate;
import com.mnt.fx.tool.proto.netty.model.NettyCommandModel;
import com.mnt.fx.tool.proto.netty.model.NettyCommandParam;
import com.mnt.fx.tool.proto.netty.model.NettyProtoModel;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * vue uniapp 协议代码生成
 */
public class CSTcpClientProtoGenerate extends NettyProtoCodeGenerateTemplate {


    @Override
    public String getType() {
        return "cs.game.client";
    }

    @Override
    protected void generateImpl(NettyProtoModel nettyProtoModel) {
        String generatePath = getGeneratePath(nettyProtoModel);

        //创建路径
        checkAndCreateDir(generatePath);

        for (NettyCommandModel commandModel : nettyProtoModel.getCommands()) {

            convertCsParams(commandModel.getCommandParams()); //转换java拆箱类型为cs拆箱类型


            Map<String, Object> protosParams = new HashMap<>();

            /**
             * cs代码文件
             */
            String protosCsFilePath;
            //模板名称
            String tmpName;
            //生成代码的文件名
            String className = "";
            //判断是发送代码还是接收代码
            if("c".equals(commandModel.getSrc())) {
                tmpName = getSendProtoTemplateName();
                className = commandModel.getName() + "SendPacket";
                String csClassDir = generatePath + PathUtils.getSeparator() + NameUtils.upperFristStr(nettyProtoModel.getModuleName()) + PathUtils.getSeparator() + "Sends" + PathUtils.getSeparator();
                checkAndCreateDir(csClassDir);
                protosCsFilePath = csClassDir + PathUtils.getSeparator() + className + ".cs";
                parseSendParams(commandModel.getCommandParams(), commandModel.getInnerParams());

                String sendDecrParams = "";
                boolean start = true;
                for (NettyCommandParam commandParam : commandModel.getCommandParams()) {
                    if(start) {
                        start = false;
                        sendDecrParams +=  handlerUnbox(commandParam.getUnboxType()) + " " + commandParam.getName();

                    } else {
                        sendDecrParams += ", " + handlerUnbox(commandParam.getUnboxType()) + " " + commandParam.getName();
                    }

                }
                //发送的参数声明
                protosParams.put("sendDecrParams", sendDecrParams);

                for (Map.Entry<String, String> codeTmpKey : nettyProtoModel.getCodeTmps().entrySet()) {
                    if(codeTmpKey.getKey().startsWith("csS")) {
                        protosParams.put(codeTmpKey.getKey(), codeTmpKey.getValue());
                    }
                }
            } else {
                tmpName = getReceiveProtoTemplateName();

                className = commandModel.getName() + "ReceivePacket";
                String csClassDir = generatePath + PathUtils.getSeparator() + NameUtils.upperFristStr(nettyProtoModel.getModuleName()) + PathUtils.getSeparator() + "Receives" + PathUtils.getSeparator();
                checkAndCreateDir(csClassDir);
                protosCsFilePath = csClassDir + PathUtils.getSeparator() + className + ".cs";
                parseReceiveParams(commandModel.getCommandParams(), commandModel.getInnerParams());

                for (Map.Entry<String, String> codeTmpKey : nettyProtoModel.getCodeTmps().entrySet()) {
                    if(codeTmpKey.getKey().startsWith("csR")) {
                        protosParams.put(codeTmpKey.getKey(), codeTmpKey.getValue());
                    }
                }
            }


            //获取保留代码
            String holdCode = getHoldCode(protosCsFilePath);



            protosParams.put("user", nettyProtoModel.getUser());
            protosParams.put("date", nettyProtoModel.getDate());

            protosParams.put("remark", commandModel.getRemark());
            protosParams.put("opCode", commandModel.getOpCode());

            protosParams.put("params", commandModel.getCommandParams());
            protosParams.put("className", className);
            protosParams.put("importPackages", handlerImportClass(commandModel.getCommandImportClass()));
            protosParams.put("holdCode", holdCode);

            try{
                VelocityUtils.getInstance().parseTemplate(tmpName, protosCsFilePath, protosParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 处理导入的包 C# Using 只需要命名空间
     * @param importClass
     * @return
     */
    private List<String> handlerImportClass(List<String> importClass) {
        List<String> result = new ArrayList<>();
        for (String importC : importClass) {
            int lastIndex = importC.lastIndexOf(".");
            String usingPath = importC.substring(0, lastIndex);
            if(!result.contains(usingPath)) {
                result.add(usingPath);
            }


        }

        return result;
    }

    /**
     * 拆箱类型转换
     * @param unboxType
     * @return
     */
    private String handlerUnbox(String unboxType) {
        if("boolean".equals(unboxType)) {
            return "bool";
        }

        return unboxType;
    }

    /**
     * 获取发送的代码
     * @param typeName
     * @return
     */
    private String getSendCodeTmp(String typeName) {
        typeName = ParamTypeUtils.convertType(typeName);
        String codeTmp = "";
        if("String".equals(typeName)) {
            codeTmp = "WriteString(#{name});";
        } else if("Long".equals(typeName)) {
            codeTmp = "WriteLong(#{name});";
        } else if("Integer".equals(typeName)) {
            codeTmp = "WriteInt(#{name});";
        } else if("Boolean".equals(typeName)) {
            codeTmp = "WriteBool(#{name});";
        } else if("Float".equals(typeName)) {
            codeTmp = "WriteFloat(#{name});";
        } else if("Double".equals(typeName)) {
            codeTmp = "WriteDouble(#{name});";
        } else if("Charset".equals(typeName)) {
            codeTmp = "WriteChar(#{name});";
        } else if("Byte".equals(typeName)) {
            codeTmp = "WriteByte(#{name});";
        } else if("Short".equals(typeName)) {
            codeTmp = "WriteShort(#{name});";
        }

        return codeTmp;
    }



    /**
     *  解析发送参数代码
     * @param commandParams
     * @param innerParamsMap
     */
    private void parseSendParams(List<NettyCommandParam> commandParams, Map<NettyCommandParam, List<NettyCommandParam>> innerParamsMap) {
        for (NettyCommandParam commandParam : commandParams) {
            if(StringUtils.isBlank(commandParam.getTypeClass())) {
                String code =  getSendCodeTmp(commandParam.getType()).replace("#{name}", commandParam.getName());
                commandParam.setCode(code);
            } else {
                commandParam.setCode(parseInnerSendParam(commandParam, commandParam.getChildrens(), commandParam.getName()));
            }
        }
    }


    /**
     * 解析内部list参数发送
     * @param commandParam
     * @param innerParams
     * @return
     */
    private String parseInnerSendParam(NettyCommandParam commandParam, List<NettyCommandParam> innerParams, String listParamName) {
        String result = TAB  + TAB  + "int #{name}Size = #{listName}.Count\n";
        result += TAB + TAB + TAB + TAB + "WriteShort(buffer, #{name}Size);\n";
        result += TAB + TAB + TAB + TAB + "foreach(#{type} #{name}Inner in #{listName}) { \n".replace("#{type}", commandParam.getTypeClass());

        //为基础类型时
        if(innerParams.isEmpty()) {
            String innerParamName = commandParam.getName() + "Inner";
            String code = getSendCodeTmp(commandParam.getTypeClass()).replace("#{name}", innerParamName);
            result += TAB  + TAB + TAB  + TAB + TAB + code + "\n";
        } else {
            for (NettyCommandParam innerCommandParam : innerParams) {
                if(StringUtils.isBlank(innerCommandParam.getTypeClass())) {
                    String innerParamName = commandParam.getName() + "Inner.";
                    innerParamName += NameUtils.upperFristStr(innerCommandParam.getName());
                    String code = getSendCodeTmp(innerCommandParam.getType()).replace("#{name}", innerParamName);
                    result += TAB + TAB + TAB + TAB + TAB + code + " //" + innerCommandParam.getRemark() + "\n";
                } else {
                    String listParamInnerName = commandParam.getName() + "Inner." +  NameUtils.upperFristStr(innerCommandParam.getName());

                    result += parseInnerSendParam(innerCommandParam, innerCommandParam.getChildrens(), listParamInnerName);
                }
            }
        }


        result += TAB  + TAB + TAB + TAB + "}\n";
        return result.replace("#{name}", commandParam.getName()).replace("#{listName}", listParamName);
    }


    /**
     * 获取接收的代码
     * @param typeName
     * @return
     */
    private String getReceiveCodeTmp(String typeName) {
        typeName = ParamTypeUtils.convertType(typeName);
        String codeTmp = "";
        if("String".equals(typeName)) {
            codeTmp = "#{name} = ReadString();";
        } else if("Long".equals(typeName)) {
            codeTmp = "#{name} = ReadLong();";
        } else if("Integer".equals(typeName)) {
            codeTmp = "#{name} = ReadInt();";
        } else if("Boolean".equals(typeName)) {
            codeTmp = "#{name} = ReadBool();";
        } else if("Float".equals(typeName)) {
            codeTmp = "#{name} = ReadFloat();";
        } else if("Double".equals(typeName)) {
            codeTmp = "#{name} = ReadDouble();";
        } else if("Charset".equals(typeName)) {
            codeTmp = "#{name} = ReadChar();";
        } else if("Byte".equals(typeName)) {
            codeTmp = "#{name} = ReadByte();";
        } else if("Short".equals(typeName)) {
            codeTmp = "#{name} = ReadShort();";
        }

        return codeTmp;
    }

    /**
     *  解析发送参数代码
     * @param commandParams
     * @param innerParams
     */
    private void parseReceiveParams(List<NettyCommandParam> commandParams, Map<NettyCommandParam, List<NettyCommandParam>> innerParams) {
        for (NettyCommandParam commandParam : commandParams) {
            if(commandParam.getChildrens().isEmpty() && StringUtils.isBlank(commandParam.getTypeClass())) {
                String code = getReceiveCodeTmp(commandParam.getType()).replace("#{name}", commandParam.getName());
                commandParam.setCode(code);
            } else {
                commandParam.setCode(parseInnerReceiveParam(commandParam, commandParam.getChildrens(), commandParam.getName(), 0));
            }
        }
    }


    /**
     * 获取内部接收的代码
     * @param typeName
     * @return
     */
    private String getInnerReceiveCodeTmp(String typeName) {
        typeName = ParamTypeUtils.convertType(typeName);
        String codeTmp = "";
        if("String".equals(typeName)) {
            codeTmp = "#{name} = ReadString();";
        } else if("Long".equals(typeName)) {
            codeTmp = "#{name} = ReadLong();";
        } else if("Integer".equals(typeName)) {
            codeTmp = "#{name} = ReadInt();";
        } else if("Boolean".equals(typeName)) {
            codeTmp = "#{name} = ReadBoolean();";
        } else if("Float".equals(typeName)) {
            codeTmp = "#{name} = ReadFloat();";
        } else if("Double".equals(typeName)) {
            codeTmp = "#{name} = ReadDouble();";
        } else if("Charset".equals(typeName)) {
            codeTmp = "#{name} = ReadChar();";
        } else if("Byte".equals(typeName)) {
            codeTmp = "#{name} = ReadByte();";
        } else if("Short".equals(typeName)) {
            codeTmp = "#{name} = ReadShort();";
        }

        return codeTmp;
    }
    /**
     * 解析内部list参数发送
     * @param commandParam
     * @param innerParams
     * @return
     */
    private String parseInnerReceiveParam(NettyCommandParam commandParam, List<NettyCommandParam> innerParams, String listParamName, int forParamIndex) {
        String result =  TAB  + TAB + TAB + TAB + "int #{name}Size = ReadShort();\n";
        result += TAB  + TAB + TAB + TAB + "#{listName} = new List<"+ commandParam.getTypeClass() +">(#{name}Size);\n";
        result += TAB  + TAB + TAB + TAB + "for(int #{forParamIndex} = 0; #{forParamIndex} < #{name}Size; #{forParamIndex}++) {\n";

        if(innerParams.isEmpty()) {
            String innerParamName = listParamName + ".Add";
            String code = getInnerReceiveCodeTmp(commandParam.getTypeClass()).replace("#{name}", innerParamName);
            result += TAB + TAB + TAB + code + "\n";
        } else {
            result += TAB+ TAB  + TAB + TAB + TAB + commandParam.getTypeClass() + " #{name}Inner = new " + commandParam.getTypeClass() +  "();\n";
            for (NettyCommandParam innerCommandParam : innerParams) {
                if(StringUtils.isBlank(innerCommandParam.getTypeClass())) {
                    String innerParamName = "#{name}Inner." +  NameUtils.upperFristStr(innerCommandParam.getName());
                    String code = getInnerReceiveCodeTmp(innerCommandParam.getType()).replace("#{name}", innerParamName);
                    result += TAB + TAB + TAB + TAB + TAB + code + " //" + innerCommandParam.getRemark() + "\n";
                } else {
                    String listParamInnerName = commandParam.getName() + "Inner.";
                    listParamInnerName += NameUtils.upperFristStr(innerCommandParam.getName());
                    result += parseInnerReceiveParam(innerCommandParam, innerCommandParam.getChildrens(), listParamInnerName, forParamIndex + 1);
                }
            }
            result += TAB  + TAB + TAB + TAB + TAB + "#{listName}.Add(#{name}Inner);\n";
        }

        result += TAB + TAB + TAB + TAB + "}\n";
        return result.replace("#{name}", commandParam.getName()).replace("#{listName}", listParamName).replace("#{forParamIndex}", forParamNames.get(forParamIndex));
    }

    /**
     * 获取拆箱类型
     * @param typeName 类型名称
     * @return 拆箱类型
     */
    public static String convertUnboxType(String typeName) {
        if("String".equals(typeName)) {
            return "string";
        } else if("Long".equals(typeName)) {
            return "long";
        } else if("Integer".equals(typeName)) {
            return "int";
        } else if("Boolean".equals(typeName)) {
            return "bool";
        } else if("Float".equals(typeName)) {
            return "float";
        } else if("Double".equals(typeName)) {
            return "double";
        }  else if("Date".equals(typeName)) {
            return "Date";
        }  else if("List".equals(typeName)) {
            return "List";
        }
        return typeName;
    }

    private void convertCsParams(List<NettyCommandParam> commandParams) {

        for (NettyCommandParam commandParam : commandParams) {
            commandParam.setUnboxType(convertUnboxType(commandParam.getUnboxType()));
            if(!commandParam.getChildrens().isEmpty()) {
                convertCsParams(commandParam.getChildrens());
            }
        }

    }




    @Override
    public String getGeneratePath(NettyProtoModel protoModel) {
        return UserData.getUserConfig().getClientProjectPath() + PathUtils.getSeparator() +
                protoModel.getGenerateConfigInfo().getProjectName() + PathUtils.getSeparator();
    }


    /**
     * 基础模板路径
     * @return 基础模板前缀
     */
    private String baseTmpPath() {
        return "cs/tcp/cs.game.client.";
    }

    /**
     * 获取发送协议模板路径
     * @return 模板文件名
     */
    private String getSendProtoTemplateName() {
        return baseTmpPath() + "send.proto.vm";
    }

    /**
     * 获取接收协议模板路径
     * @return 模板文件名
     */
    private String getReceiveProtoTemplateName() {
        return baseTmpPath() + "receive.proto.vm";
    }
}
