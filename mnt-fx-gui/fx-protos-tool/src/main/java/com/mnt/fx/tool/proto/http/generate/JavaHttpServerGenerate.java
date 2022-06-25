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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * java 接收 controller 生成
 */
public class JavaHttpServerGenerate extends HttpProtoCodeGenerateTemplate {


    @Override
    public String getType() {
        return "java.server";
    }

    @Override
    protected void generateImpl(HttpProtoModel protoModel) {
        handleModel(protoModel);

        String generatePath = getGeneratePath(protoModel);

        //创建路径
        checkAndCreateDir(generatePath);
        checkAndCreateDir(generatePath + PathUtils.getSeparator() +
                PathUtils.packageToPath(protoModel.getGenerateConfigInfo().getPackageName())
                + PathUtils.getSeparator());

        //生成controller
        String controllerPath = generatePath + PathUtils.getSeparator() +
                PathUtils.packageToPath(protoModel.getGenerateConfigInfo().getPackageName())
                + PathUtils.getSeparator() + protoModel.getControllerName() + ".java";



        Map<String, Object> controllerParams = new HashMap<>();

        //获取保留代码
        String controllerHoldCode = getHoldCode(controllerPath);

        controllerParams.put("importPackages", protoModel.getImportPackages());
        controllerParams.put("package", protoModel.getGenerateConfigInfo().getPackageName());
        controllerParams.put("remark", protoModel.getRemark());
        controllerParams.put("user", protoModel.getUser());
        controllerParams.put("generateValid", protoModel.isGenerateValid());
        controllerParams.put("date", protoModel.getDate());
        controllerParams.put("requestMapper", protoModel.getRequestMapper());
        controllerParams.put("controllerName", protoModel.getControllerName());
        //只创建新增的controller
        controllerParams.put("actions", getHoldControllerCode(controllerPath, protoModel.getActions(), protoModel.getImportPackages()));
        controllerParams.put("holdCode", controllerHoldCode);

        VelocityUtils.getInstance().parseTemplate(getControllerTemplateName(), controllerPath, controllerParams);

        //兼容 cloud api的情况
        generatePath = getApiGeneratePath(protoModel);

        for (HttpActionModel actionModel : protoModel.getActions()) {
            try{
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

                String reqParamPath = generatePath + PathUtils.packageToPath(actionModel.getReqPackage())  + PathUtils.getSeparator() + actionModel.getReqClass() + ".java";
                String reqParaHoldCode = getHoldCode(reqParamPath);
                reqParams.put("holdCode", reqParaHoldCode);

                //创建路径
                checkAndCreateDir( generatePath + PathUtils.packageToPath(actionModel.getReqPackage()));

                VelocityUtils.getInstance().parseTemplate(getReqParamTemplateName(), reqParamPath, reqParams);





                //生成答复参数实体
                Map<String, Object> respParams = new HashMap<>();
                respParams.put("respParamName", actionModel.getRespClass());
                respParams.put("user", protoModel.getUser());
                respParams.put("date", protoModel.getDate());
                respParams.put("remark", actionModel.getRemark());
                respParams.put("respParams", getResponeDataParam(actionModel.getCommadRespParams()));
                respParams.put("package", actionModel.getRespPackage());
                respParams.put("importPackages", actionModel.getRespImprotClass());

                //创建内部类代码
                String innerRespClassStr = convertRespInnerClass(actionModel.getInnerRespParams());
                respParams.put("innerClassStr", innerRespClassStr);

                String respParamPath = generatePath + PathUtils.packageToPath(actionModel.getRespPackage()) + PathUtils.getSeparator() + actionModel.getRespClass() + ".java";

                String resqParaHoldCode = getHoldCode(respParamPath);
                respParams.put("holdCode", resqParaHoldCode);
                //创建路径
                checkAndCreateDir(generatePath + PathUtils.packageToPath(actionModel.getRespPackage()));

                VelocityUtils.getInstance().parseTemplate(getRespParamemplateName(), respParamPath, respParams);

            } catch (Exception e) {
                e.printStackTrace();
               // ConsoleLogUtils.log("[" + actionModel.getActionName() + "] - 参数类生成错误 : Exception ---  " + e);
            }

        }

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
            String requestParamClass = upperName + "RequestParam";
            actionModel.setReqClass(requestParamClass);
            //请求参数名
            String requestParamName = actionModel.getActionName() + "RequestParam";
            actionModel.setReqName(requestParamName);

            //答复参数类
            String responseParamClass = upperName + "ResponseParam";
            actionModel.setRespClass(responseParamClass);

            //答复参数名
            String responseParamName =  actionModel.getActionName() + "ResponseParam";
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
        String type = reqParam.getType();

        String typeClass = reqParam.getTypeClass();
        List<String> actionReqImportsClass = actionModel.getReqImprotClass();


        DefaultLoadClassEnums typeClassLoadClassEnums =  DefaultLoadClassEnums.getByName(typeClass);

        if(null != typeClassLoadClassEnums) {

            checkAndAdd(actionReqImportsClass, typeClassLoadClassEnums.getImportClass());
        }

        DefaultLoadClassEnums defaultLoadClassEnums =  DefaultLoadClassEnums.getByName(type);
        if(null != defaultLoadClassEnums) {

            checkAndAdd(actionReqImportsClass, defaultLoadClassEnums.getImportClass());
            //集合泛型处理
            if(defaultLoadClassEnums == DefaultLoadClassEnums.LIST) {

                if(null == typeClass) {
                    type = type + "<" + NameUtils.buildInnerClassName(reqParam.getName()) + ">";
                } else {
                    String needImportType = ParamTypeUtils.convertType(typeClass);
                    if(null == needImportType) {
                        checkAndAdd(actionReqImportsClass, typeClass);
                        int lastIndex = typeClass.lastIndexOf(".") + 1;
                        typeClass = typeClass.substring(lastIndex);
                    }
                    type = type + "<" + typeClass + ">";
                }


            } else if(defaultLoadClassEnums == DefaultLoadClassEnums.DATE) {
                checkAndAdd(actionReqImportsClass, "org.springframework.format.annotation.DateTimeFormat");
                if(actionModel.getBody()) {
                    checkAndAdd(actionReqImportsClass, "com.fasterxml.jackson.annotation.JsonFormat");
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

        String type = respParam.getType();

        String typeClass = respParam.getTypeClass();
        List<String> actionRespImportsClass = actionModel.getRespImprotClass();

        //泛型类是否需要加载
        DefaultLoadClassEnums typeClassLoadClassEnums =  DefaultLoadClassEnums.getByName(typeClass);

        if(null != typeClassLoadClassEnums) {

            checkAndAdd(actionRespImportsClass, typeClassLoadClassEnums.getImportClass());
        }

        DefaultLoadClassEnums defaultLoadClassEnums =  DefaultLoadClassEnums.getByName(type);

        if(null != defaultLoadClassEnums) {

            checkAndAdd(actionRespImportsClass, defaultLoadClassEnums.getImportClass());

            //集合泛型处理
            if(defaultLoadClassEnums == DefaultLoadClassEnums.LIST) {

                if(StringUtils.isEmpty(typeClass)) {
                    type = type + "<" + NameUtils.buildInnerClassName(respParam.getName()) + ">";
                } else {
                    String needImportType = ParamTypeUtils.convertType(typeClass);
                    if(null == needImportType) {
                        checkAndAdd(actionRespImportsClass, typeClass);
                        int lastIndex = typeClass.lastIndexOf(".") + 1;
                        typeClass = typeClass.substring(lastIndex);
                    }
                    type = type + "<" + typeClass + ">";
                }
            } else if(defaultLoadClassEnums == DefaultLoadClassEnums.DATE) {
                checkAndAdd(actionRespImportsClass, "com.fasterxml.jackson.annotation.JsonFormat");
            }
        }


        respParam.setType(type);

        if(!respParam.getChildrens().isEmpty()) {
            for (HttpCommadRespParam httpCommadRespParam : respParam.getChildrens()) {
                handleRespParam(actionModel, httpCommadRespParam);
            }
        }
    }

    /**
     * 获取是否生成内部data数据
     * @param commadRespParams
     * @return
     */
    private List<HttpCommadRespParam> getResponeDataParam(List<HttpCommadRespParam> commadRespParams) {
        boolean isGenerateData = false;

        if(isGenerateData) {
            return commadRespParams;
        } else {
            for (HttpCommadRespParam commadRespParam : commadRespParams) {
                if("data".equals(commadRespParam.getName())) {
                    return commadRespParam.getChildrens();
                }
            }
        }
        return commadRespParams;
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



    /**
     * 获取保留代码
     * @return 保留的代码块
     */
    public List<HttpActionModel> getHoldControllerCode(String filePath, List<HttpActionModel> actionModels, List<String> importPackages) {
        File file = new File(filePath);

        List<HttpActionModel> result = new ArrayList<>(actionModels);
        if(file.exists()) {
            try {

                List<HttpActionModel> removeActions = new ArrayList<>(actionModels.size());

                FileReader fr;

                fr = new FileReader(file);
                BufferedReader br=new BufferedReader(fr);
                String line ;
                while ((line = br.readLine()) != null) {

                    if(line.startsWith("import ") && line.endsWith(";")) {

                        if(!line.contains("import org.springframework.web.bind.annotation.RequestMapping;") &&
                                !line.contains("import org.springframework.web.bind.annotation.RestController;") &&
                                !line.contains("import com.mnt.tools.dep.BaseController;") &&
                                !line.contains("import com.mnt.tools.dep.AjaxResult;") &&
                                !line.contains("import org.springframework.web.bind.annotation.RequestBody;")
                                ) {
                            String packageStr = line.trim().replace("import ", "").replace(";", "");
                            if(!importPackages.contains(packageStr)) {
                                importPackages.add(packageStr);
                            }
                        }


                    }


                    if(line.contains("@RequestMapping")) {
                        for (HttpActionModel actionUrl : actionModels) {
                            if(line.contains("@RequestMapping(\"" + actionUrl.getRequestMapper() + "\")")) {
                                removeActions.add(actionUrl);
                            }
                            //request body 包引入
                            if(actionUrl.getBody()) {
                                if(!importPackages.contains("org.springframework.web.bind.annotation.RequestBody")) {
                                    importPackages.add("org.springframework.web.bind.annotation.RequestBody");
                                }
                            }
                        }

                    }

                }
                br.close();
                fr.close();

                result.removeAll(removeActions);

                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return actionModels;
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

    @Override
    public String getGeneratePath(HttpProtoModel protoModel) {
        return UserData.getUserConfig().getServerProjectPath() + PathUtils.getSeparator() +
                protoModel.getGenerateConfigInfo().getProjectName() + PathUtils.getSeparator() + "src"+ PathUtils.getSeparator() +
                "main" + PathUtils.getSeparator() + "java"
                + PathUtils.getSeparator();
    }

    private String getApiGeneratePath(HttpProtoModel protoModel) {
        if(StringUtils.isEmpty(protoModel.getGenerateConfigInfo().getApiProjectName())) {
            return getGeneratePath(protoModel);
        }

        return UserData.getUserConfig().getServerProjectPath() + PathUtils.getSeparator() +
                protoModel.getGenerateConfigInfo().getApiProjectName() + PathUtils.getSeparator() + "src"+ PathUtils.getSeparator() +
                "main" + PathUtils.getSeparator() + "java"
                + PathUtils.getSeparator();
    }

    /**
     * 基础模板路径
     * @return 基础模板前缀
     */
    private String baseTmpPath() {
        return "java/http/java.server.receive.";
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
