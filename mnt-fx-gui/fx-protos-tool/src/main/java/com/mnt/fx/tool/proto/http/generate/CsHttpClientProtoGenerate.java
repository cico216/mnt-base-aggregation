package com.mnt.fx.tool.proto.http.generate;

import com.mnt.fx.tool.common.enums.DefaultLoadClassEnums;
import com.mnt.fx.tool.common.utils.NameUtils;
import com.mnt.fx.tool.common.utils.ParamTypeUtils;
import com.mnt.fx.tool.common.utils.PathUtils;
import com.mnt.fx.tool.common.utils.VelocityUtils;
import com.mnt.fx.tool.proto.conf.UserData;
import com.mnt.fx.tool.proto.http.core.HttpProtoCodeGenerateTemplate;
import com.mnt.fx.tool.proto.http.model.HttpActionModel;
import com.mnt.fx.tool.proto.http.model.HttpCommadReqParam;
import com.mnt.fx.tool.proto.http.model.HttpCommadRespParam;
import com.mnt.fx.tool.proto.http.model.HttpProtoModel;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * cs client 协议代码生成
 */
public class CsHttpClientProtoGenerate extends HttpProtoCodeGenerateTemplate {


    @Override
    public String getType() {
        return "cs.client";
    }

    @Override
    protected void generateImpl(HttpProtoModel protoModel) {
        String generatePath = getGeneratePath(protoModel);

        //创建路径
        checkAndCreateDir(generatePath);

        handleModel(protoModel);

        String protosLuaFilePath =  generatePath + PathUtils.getSeparator() + protoModel.getControllerName() + ".cs";

        Map<String, Object> protosParams = new HashMap<>();

        protosParams.put("controllerName", protoModel.getControllerName());
        protosParams.put("remark", protoModel.getRemark());
        protosParams.put("user", protoModel.getUser());
        protosParams.put("date", protoModel.getDate());
        protosParams.put("moduleName", protoModel.getGenerateConfigInfo().getPackageName());
        protosParams.put("actions", protoModel.getActions());

        protosParams.put("protoName", protoModel.getRequestMapper());


        //vo参数代码生成
        String paramClassStr = buildParamClassStr(protoModel);
        protosParams.put("paramClassStr", paramClassStr);




        try{
            VelocityUtils.getInstance().parseTemplate(getControllerTemplateName(), protosLuaFilePath, protosParams);
        } catch (Exception e) {
            e.printStackTrace();
           // ConsoleLogUtils.log("[" + protoModel.getControllerName() + "] - 协议文件生成错误 : Exception ---  " + e);
        }
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

    /**
     * 根据当前语言特性处理生成模型
     * @param protoModel
     */
    private void handleModel(HttpProtoModel protoModel) {

        List<String> importPackages = protoModel.getImportPackages();

        String actionPackage = protoModel.getGenerateConfigInfo().getPackageName();
        //当前控制层所在controller
        String controllerName = protoModel.getControllerName() + "Controller";
        protoModel.setControllerName(controllerName);

        for (HttpActionModel actionModel : protoModel.getActions()) {

            String upperName = NameUtils.upperFristStr(actionModel.getActionName());
            //请求参数类
            String requestParamClass = upperName + "ReqParam";
            actionModel.setReqClass(requestParamClass);
            //请求参数名
            String requestParamName = actionModel.getActionName() + "ReqParam";
            actionModel.setReqName(requestParamName);

            //答复参数类
            String responseParamClass = upperName + "RespParam";
            actionModel.setRespClass(responseParamClass);

            //答复参数名
            String responseParamName =  actionModel.getActionName() + "RespParam";
            actionModel.setRespName(responseParamName);

            //参数所在包
            String reqParamPackage = actionPackage + ".req";
            String respParamPackage = actionPackage + ".resp";

            //引入class
            importPackages.add(reqParamPackage + "." + requestParamClass);
            importPackages.add(respParamPackage + "." + responseParamClass);

            actionModel.setReqPackage(reqParamPackage);
            actionModel.setRespPackage(respParamPackage);

            for (HttpCommadReqParam httpCommadReqParam : actionModel.getCommadReqParams()) {
                handleReqParam(actionModel, httpCommadReqParam);
            }

            for (HttpCommadRespParam httpCommadRespParam : actionModel.getCommadRespParams()) {
                handleRespParam(actionModel, httpCommadRespParam);
            }

        }
    }

    /**
     * 处理请求参数
     * @param actionModel
     * @param reqParam
     */
    private void handleReqParam(HttpActionModel actionModel, HttpCommadReqParam reqParam) {
        String type = convertUnboxType(reqParam.getType());

        String typeClass = reqParam.getTypeClass();


        DefaultLoadClassEnums defaultLoadClassEnums =  DefaultLoadClassEnums.getByName(type);
        if(null != defaultLoadClassEnums) {


            //集合泛型处理
            if(defaultLoadClassEnums == DefaultLoadClassEnums.LIST) {

                if(null == typeClass) {
                    type = type + "<" + NameUtils.buildInnerClassName(reqParam.getName()) + ">";
                } else {
                    String needImportType = ParamTypeUtils.convertType(typeClass);
                    if(null == needImportType) {

                        int lastIndex = typeClass.lastIndexOf(".") + 1;
                        typeClass = typeClass.substring(lastIndex);
                    }
                    type = type + "<" + typeClass + ">";
                }


            }
        }


        reqParam.setType(type);

        if(!reqParam.getChildrens().isEmpty()) {
            for (HttpCommadReqParam httpCommadReqParam : reqParam.getChildrens()) {
                handleReqParam(actionModel, httpCommadReqParam);
            }
        }
    }

    /**
     * 处理返回参数
     * @param actionModel
     * @param respParam
     */
    private void handleRespParam(HttpActionModel actionModel, HttpCommadRespParam respParam) {

        String type = convertUnboxType(respParam.getType());

        String typeClass = respParam.getTypeClass();

        DefaultLoadClassEnums defaultLoadClassEnums =  DefaultLoadClassEnums.getByName(type);

        if(null != defaultLoadClassEnums) {


            //集合泛型处理
            if(defaultLoadClassEnums == DefaultLoadClassEnums.LIST) {

                if(StringUtils.isEmpty(typeClass)) {
                    type = type + "<" + NameUtils.buildInnerClassName(respParam.getName()) + ">";
                } else {
                    String needImportType = ParamTypeUtils.convertType(typeClass);
                    if(null == needImportType) {

                        int lastIndex = typeClass.lastIndexOf(".") + 1;
                        typeClass = typeClass.substring(lastIndex);
                    }
                    type = type + "<" + typeClass + ">";
                }
            }
        }


        respParam.setType(type);

        if(!respParam.getChildrens().isEmpty()) {
            for (HttpCommadRespParam httpCommadRespParam : respParam.getChildrens()) {
                handleRespParam(actionModel, httpCommadRespParam);
            }
        }
    }


    public String buildParamClassStr(HttpProtoModel protoModel) {
        String result = "";
        for (HttpActionModel actionModel : protoModel.getActions()) {
            try {
                Map<String, Object> reqParams = new HashMap<>();
                //生成请求参数实体
                reqParams.put("reqParamName", actionModel.getReqClass());
                reqParams.put("user", protoModel.getUser());
                reqParams.put("date", protoModel.getDate());
                reqParams.put("remark", actionModel.getRemark());
                reqParams.put("reqParams", actionModel.getCommadReqParams());
                reqParams.put("package", actionModel.getReqPackage());
                reqParams.put("importPackages", actionModel.getReqImprotClass());
                reqParams.put("body", actionModel.getBody());

                //创建内部类代码
                String innerReqClassStr = convertReqInnerClass(actionModel.getInnerReqParams());
                reqParams.put("innerClassStr", innerReqClassStr);

                result += VelocityUtils.getInstance().parseTemplate(getReqParamTemplateName(), reqParams);


                //生成答复参数实体
                Map<String, Object> respParams = new HashMap<>();
                respParams.put("respParamName", actionModel.getRespClass());
                respParams.put("user", protoModel.getUser());
                respParams.put("date", protoModel.getDate());
                respParams.put("remark", actionModel.getRemark());
                respParams.put("respParams", actionModel.getCommadRespParams());
                respParams.put("package", actionModel.getRespPackage());
                respParams.put("importPackages", actionModel.getRespImprotClass());

                //创建内部类代码
                String innerRespClassStr = convertRespInnerClass(actionModel.getInnerRespParams());
                respParams.put("innerClassStr", innerRespClassStr);



                result +=  VelocityUtils.getInstance().parseTemplate(getRespParamemplateName(), respParams);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 转换内部类请求参数
     * @param innerReqParamsMap
     * @return
     */
    private String convertReqInnerClass(Map<HttpCommadReqParam, List<HttpCommadReqParam>> innerReqParamsMap) {

        StringBuilder resultSB = new StringBuilder();

        //请求参数内部类
        for (Map.Entry<HttpCommadReqParam, List<HttpCommadReqParam>> innerReqParamEntry : innerReqParamsMap.entrySet()) {
            Map<String, Object> innerReqParams = new HashMap<>();

            //传入类型的则不生成代码
            boolean isGenerate = StringUtils.isEmpty(innerReqParamEntry.getKey().getTypeClass());
            if(!isGenerate) {
                resultSB.append("\n");
                continue;
            }
            String innerClassName = NameUtils.buildInnerClassName(innerReqParamEntry.getKey().getName());

            //生成请求参数实体
            innerReqParams.put("reqParamName", innerClassName);
            innerReqParams.put("remark", innerReqParamEntry.getKey().getRemark());
            innerReqParams.put("reqParams", innerReqParamEntry.getValue());

            String classCode = VelocityUtils.getInstance().parseTemplate(getInnerReqParamTemplateName(), innerReqParams);

            resultSB.append(classCode);
            resultSB.append("\n");
            resultSB.append("\n");
        }

        return resultSB.toString();
    }

    /**
     * 转换内部类答复参数
     * @param innerRespParamsMap
     * @return 生成代码
     */
    private String convertRespInnerClass(Map<HttpCommadRespParam, List<HttpCommadRespParam>> innerRespParamsMap) {

        StringBuilder resultSB = new StringBuilder();

        //请求参数内部类
        for (Map.Entry<HttpCommadRespParam, List<HttpCommadRespParam>> innerRespParamEntry : innerRespParamsMap.entrySet()) {
            Map<String, Object> innerRespParams = new HashMap<>();
            //传入类型的则不生成代码
            boolean isGenerate = StringUtils.isEmpty(innerRespParamEntry.getKey().getTypeClass());
            if(!isGenerate) {
                resultSB.append("\n");
                continue;
            }
            String innerClassName = NameUtils.buildInnerClassName(innerRespParamEntry.getKey().getName());
            //生成请求参数实体
            innerRespParams.put("respParamName", innerClassName);
            innerRespParams.put("remark", innerRespParamEntry.getKey().getRemark());
            innerRespParams.put("respParams", innerRespParamEntry.getValue());

//            System.err.println("classCode = " + innerRespParams);
            String classCode = VelocityUtils.getInstance().parseTemplate(getInnerRespParamemplateName(), innerRespParams);

//            System.err.println("classCode = " + classCode);
            resultSB.append(classCode);
            resultSB.append("\n");
            resultSB.append("\n");

        }
        return resultSB.toString();

    }

    @Override
    public String getGeneratePath(HttpProtoModel protoModel) {
        return UserData.getUserConfig().getClientProjectPath() + PathUtils.getSeparator() +
                protoModel.getGenerateConfigInfo().getProjectName() + PathUtils.getSeparator() +
                protoModel.getGenerateConfigInfo().getPackageName() + PathUtils.getSeparator();
    }


    /**
     * 基础模板路径
     * @return 基础模板前缀
     */
    private String baseTmpPath() {
        return "cs/http//cs.client.send.";
    }

    /**
     * 获取controller模板路径
     * @return 模板文件名
     */
    private String getControllerTemplateName() {
        return baseTmpPath() + "controller.vm";
    }

    /**
     * 获取请求参数模板路径
     * @return 模板文件名
     */
    private String getReqParamTemplateName() {
        return baseTmpPath() + "reqparam.vm";
    }

    /**
     * 获取答复参数模板路径
     * @return 模板文件名
     */
    private String getRespParamemplateName() {
        return baseTmpPath() + "respparam.vm";
    }

    /**
     * 获取请求嵌套参数模板路径
     * @return 模板文件名
     */
    private String getInnerReqParamTemplateName() {
        return baseTmpPath() + "innerreqparam.vm";
    }

    /**
     * 获取答复嵌套参数模板路径
     * @return 模板文件名
     */
    private String getInnerRespParamemplateName() {
        return baseTmpPath() + "innerrespparam.vm";
    }
}
