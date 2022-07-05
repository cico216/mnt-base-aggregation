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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * vue uniapp 协议代码生成
 */
public class JavaTcpServerProtoGenerate extends NettyProtoCodeGenerateTemplate {


    @Override
    public String getType() {
        return "java.game.server";
    }

    @Override
    protected void generateImpl(NettyProtoModel nettyProtoModel) {
        String generatePath = getGeneratePath(nettyProtoModel);

        generatePath = generatePath + PathUtils.packageToPath(nettyProtoModel.getGenerateConfigInfo().getPackageName()) +  PathUtils.getSeparator();

        //创建路径
        checkAndCreateDir(generatePath);


        for (NettyCommandModel commandModel : nettyProtoModel.getCommands()) {
            //模板引擎替换的参数
            Map<String, Object> protosParams = new HashMap<>();

            String protosJavaFilePath;
            //模板名称
            String tmpName;
            //生成代码的文件名
            String className;
            //生成类的包名
            String packagePath;

            //判断是发送代码还是接收代码
            if("s".equals(commandModel.getSrc())) {
                packagePath = nettyProtoModel.getGenerateConfigInfo().getPackageName() + "." + nettyProtoModel.getModuleName() + ".sends" ;
                className = commandModel.getName() + "SendablePacket";
                String javaClassDir = generatePath + PathUtils.getSeparator() + nettyProtoModel.getModuleName() + PathUtils.getSeparator() + "sends" + PathUtils.getSeparator();
                checkAndCreateDir(javaClassDir);

                protosJavaFilePath =  javaClassDir + className + ".java";
                tmpName = getSendProtoTemplateName();
                parseSendParams(commandModel.getCommandParams(), commandModel.getInnerParams());

                String sendDecrParams = "";
                for (NettyCommandParam commandParam : commandModel.getCommandParams()) {
                    sendDecrParams += ", " + commandParam.getUnboxType() + " " + commandParam.getName();
                }
                //发送的参数声明
                protosParams.put("sendDecrParams", sendDecrParams);
                for (Map.Entry<String, String> codeTmpKey : nettyProtoModel.getCodeTmps().entrySet()) {
                    if(codeTmpKey.getKey().startsWith("javaS")) {
                        protosParams.put(codeTmpKey.getKey(), codeTmpKey.getValue());
                    }
                }
            } else {
                packagePath = nettyProtoModel.getGenerateConfigInfo().getPackageName() + "." +  nettyProtoModel.getModuleName() + ".receives" ;
                className = commandModel.getName() + "ReceivablePacket";
                String javaClassDir = generatePath + PathUtils.getSeparator() + nettyProtoModel.getModuleName()  + PathUtils.getSeparator() + "receives" + PathUtils.getSeparator();
                checkAndCreateDir(javaClassDir);
                protosJavaFilePath =  javaClassDir + className + ".java";

                tmpName = getReceiveProtoTemplateName();
                parseReceiveParams(commandModel.getCommandParams(), commandModel.getInnerParams());
                for (Map.Entry<String, String> codeTmpKey : nettyProtoModel.getCodeTmps().entrySet()) {
                    if(codeTmpKey.getKey().startsWith("javaR")) {
                        protosParams.put(codeTmpKey.getKey(), codeTmpKey.getValue());
                    }
                }

            }
//            //公共模块时
//            if(nettyProtoModel.getGenerateConfigInfo().getProjectName().contains("game-server/game-common")) {
//                //添加连接包
//                commandModel.getCommandImportClass().add(nettyProtoModel.getGenerateConfigInfo().getPackageName() + ".entitys.BaseGameClientConnection");
//
//                protosParams.put("connection", "BaseGameClientConnection");
//            } else {
//                //添加连接包
//                commandModel.getCommandImportClass().add(nettyProtoModel.getGenerateConfigInfo().getPackageName() + ".entitys.GameClientConnection");
//
//                protosParams.put("connection", "GameClientConnection");
//            }



            //获取保留代码
            String holdCode = getHoldCode(protosJavaFilePath);
            getImportClass(protosJavaFilePath, commandModel.getCommandImportClass(), nettyProtoModel.getCodeTmps().values());

            protosParams.put("user", nettyProtoModel.getUser());
            protosParams.put("date", nettyProtoModel.getDate());

            protosParams.put("packagePath", packagePath);
            protosParams.put("remark", commandModel.getRemark());
            protosParams.put("opCode", commandModel.getOpCode());

            protosParams.put("params", commandModel.getCommandParams());
            protosParams.put("className", className);
            protosParams.put("importPackages", commandModel.getCommandImportClass());
            protosParams.put("holdCode", holdCode);



            try{
                VelocityUtils.getInstance().parseTemplate(tmpName, protosJavaFilePath, protosParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
            codeTmp = "writeString(buffer, #{name});";
        } else if("Long".equals(typeName)) {
            codeTmp = "writeLong(buffer, #{name});";
        } else if("Integer".equals(typeName)) {
            codeTmp = "writeInt(buffer, #{name});";
        } else if("Boolean".equals(typeName)) {
            codeTmp = "writeBoolean(buffer, #{name});";
        } else if("Float".equals(typeName)) {
            codeTmp = "writeFloat(buffer, #{name});";
        } else if("Double".equals(typeName)) {
            codeTmp = "writeDouble(buffer, #{name});";
        } else if("Charset".equals(typeName)) {
            codeTmp = "writeChar(buffer, #{name});";
        } else if("Byte".equals(typeName)) {
            codeTmp = "writeByte(buffer, #{name});";
        } else if("Short".equals(typeName)) {
            codeTmp = "writeShort(buffer, #{name});";
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
                String code = TAB + TAB + TAB + getSendCodeTmp(commandParam.getType()).replace("#{name}", commandParam.getName());
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
        String result = TAB + TAB + TAB + "int #{name}Size = #{listName}.size();\n";
        result += TAB + TAB + TAB + "writeShort(buffer, #{name}Size);\n";
        result += TAB + TAB + TAB + "for(#{type} #{name}Inner : #{listName}) { \n".replace("#{type}", commandParam.getTypeClass());
        if(innerParams.isEmpty()) {
            String innerParamName = commandParam.getName() + "Inner";
            String code = getSendCodeTmp(commandParam.getTypeClass()).replace("#{name}", innerParamName);
            result += TAB  + TAB + TAB  + TAB + code + "\n";
        } else {

            for (NettyCommandParam innerNettyCommandParam : innerParams) {
                if(StringUtils.isBlank(innerNettyCommandParam.getTypeClass())) {
                    String innerParamName = commandParam.getName() + "Inner.";
                    if("Boolean".equals(innerNettyCommandParam.getType())) {
                        innerParamName += "is" ;
                    } else {
                        innerParamName += "get" ;
                    }
                    innerParamName += NameUtils.upperFristStr(innerNettyCommandParam.getName()) + "()";
                    String code = getSendCodeTmp(innerNettyCommandParam.getType()).replace("#{name}", innerParamName);
                    result += TAB + TAB + TAB + TAB + code + " //" + innerNettyCommandParam.getRemark() + "\n";
                } else {
                    String listParamInnerName = commandParam.getName() + "Inner.";
                    if("Boolean".equals(innerNettyCommandParam.getType())) {
                        listParamInnerName += "is" ;
                    } else {
                        listParamInnerName += "get" ;
                    }
                    listParamInnerName += NameUtils.upperFristStr(innerNettyCommandParam.getName()) + "()";
                    result += parseInnerSendParam(innerNettyCommandParam, innerNettyCommandParam.getChildrens(), listParamInnerName);
                }
            }
        }
        result += TAB + TAB + TAB + "}\n";
        return result.replace("#{name}", commandParam.getName()).replace("#{listName}", listParamName);
    }


    /**
     * 获取接收的代码
     * @param commandParam
     * @return
     */
    private String getReceiveCodeTmp(NettyCommandParam commandParam) {
        String typeName = commandParam.getType();
        String codeTmp = "";
        typeName = ParamTypeUtils.convertType(typeName);
        if("String".equals(typeName)) {
            codeTmp = "#{name} = readString();";
        } else if("Long".equals(typeName)) {
            codeTmp = "#{name} = readLong();";
        } else if("Integer".equals(typeName)) {
            codeTmp = "#{name} = readInt();";
        } else if("Boolean".equals(typeName)) {
            codeTmp = "#{name} = readBoolean();";
        } else if("Float".equals(typeName)) {
            codeTmp = "#{name} = readFloat();";
        } else if("Double".equals(typeName)) {
            codeTmp = "#{name} = readDouble();";
        } else if("Charset".equals(typeName)) {
            codeTmp = "#{name} = readChar();";
        } else if("Byte".equals(typeName)) {
            codeTmp = "#{name} = readByte();";
        } else if("Short".equals(typeName)) {
            codeTmp = "#{name} = readShort();";
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
            if(StringUtils.isBlank(commandParam.getTypeClass())) {
                String code = TAB + TAB + getReceiveCodeTmp(commandParam).replace("#{name}", commandParam.getName());
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
            codeTmp = "#{name}(readString());";
        } else if("Long".equals(typeName)) {
            codeTmp = "#{name}(readLong());";
        } else if("Integer".equals(typeName)) {
            codeTmp = "#{name}(readInt());";
        } else if("Boolean".equals(typeName)) {
            codeTmp = "#{name}(readBoolean());";
        } else if("Float".equals(typeName)) {
            codeTmp = "#{name}(readFloat());";
        } else if("Double".equals(typeName)) {
            codeTmp = "#{name}(readDouble());";
        } else if("Charset".equals(typeName)) {
            codeTmp = "#{name}(readChar());";
        } else if("Byte".equals(typeName)) {
            codeTmp = "#{name}(readByte());";
        } else if("Short".equals(typeName)) {
            codeTmp = "#{name}(readShort());";
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
        String result = TAB  + TAB + TAB + "int #{name}Size = readShort();\n";
        if(commandParam.getName().equals(listParamName)) {
            result += TAB  + TAB + TAB + "#{name} = new ArrayList<>(#{name}Size);\n";
        } else {
            result += TAB  + TAB + TAB + listParamName.replace(".get", ".set").replace(")", "") + "new ArrayList<>(#{name}Size));\n";
        }

        result += TAB  + TAB + TAB + "for(int #{forParamIndex} = 0; #{forParamIndex} < #{name}Size; #{forParamIndex}++) {\n";
        if(innerParams.isEmpty()) {
            String innerParamName = listParamName + ".add";
            String code = getInnerReceiveCodeTmp(commandParam.getTypeClass()).replace("#{name}", innerParamName);
            result += TAB + TAB + TAB + code + "\n";
        } else {
            result += TAB  + TAB + TAB + TAB + commandParam.getTypeClass() + " #{name}Inner = new " + commandParam.getTypeClass() +  "();\n";
            for (NettyCommandParam innerNettyCommandParam : innerParams) {
                if(StringUtils.isBlank(innerNettyCommandParam.getTypeClass())) {
                    String innerParamName = "#{name}Inner.set" +  NameUtils.upperFristStr(innerNettyCommandParam.getName());
                    String code = getInnerReceiveCodeTmp(innerNettyCommandParam.getType()).replace("#{name}", innerParamName);
                    result += TAB + TAB + TAB + TAB + code + " //" + innerNettyCommandParam.getRemark() + "\n";
                } else {
                    String listParamInnerName = commandParam.getName() + "Inner.get" +  NameUtils.upperFristStr(innerNettyCommandParam.getName()) + "()";
                    result += parseInnerReceiveParam(innerNettyCommandParam, innerNettyCommandParam.getChildrens(), listParamInnerName,forParamIndex + 1);
                }
            }
            result += TAB  + TAB + TAB + TAB + "#{listName}.add(#{name}Inner);\n";
        }

        result += TAB + TAB + TAB + "}\n";
        return result.replace("#{name}", commandParam.getName()).replace("#{listName}", listParamName).replace("#{forParamIndex}", forParamNames.get(forParamIndex));
    }



    @Override
    public String getGeneratePath(NettyProtoModel NettyProtoModel) {
        return UserData.getUserConfig().getServerProjectPath() + PathUtils.getSeparator() +
                NettyProtoModel.getGenerateConfigInfo().getProjectName() + PathUtils.getSeparator() + PathUtils.getSeparator() + "src"+ PathUtils.getSeparator() +
                "main" + PathUtils.getSeparator() + "java" +
                 PathUtils.getSeparator();
    }

    /**
     * 获取保留代码
     * @return 保留的代码块
     */
    public void getImportClass(String filePath, List<String> importPackages, Collection<String> codeTmpsValues) {
        File file = new File(filePath);

        if(file.exists()) {
            try {
                FileReader fr;

                fr = new FileReader(file);
                BufferedReader br=new BufferedReader(fr);
                String line ;
                while ((line = br.readLine()) != null) {

                    if(line.startsWith("import ") && line.endsWith(";")) {

                        if(!codeTmpsValues.contains(line)) {
                            String packageStr = line.trim().replace("import ", "").replace(";", "");
                            if(!importPackages.contains(packageStr)) {
                                importPackages.add(packageStr);
                            }
                        }


                    }


                }
                br.close();
                fr.close();



            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    /**
     * 基础模板路径
     * @return 基础模板前缀
     */
    private String baseTmpPath() {
        return "java/tcp/java.game.server.";
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
