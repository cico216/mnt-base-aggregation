package com.mnt.fx.tool.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * json 配置文件工具类
 */
public class JSONConfigUtils {

    /**
     * 保存数据到指定文件
     * @param t
     * @param configPath
     * @param <T>
     */
    public static <T> void saveData(T t, String configPath) {
        if(null == t) {
            return;
        }
        if(StringUtils.isEmpty(configPath)) {
            return;
        }
        writeJsonToFile(t, configPath);

    }

    /**
     * 保存集合数据到指定文件
     * @param list
     * @param configPath
     * @param <T>
     */
    public static <T> void saveDatas(List<T> list, String configPath) {
        if(null == list || list.isEmpty()) {
            return;
        }
        if(StringUtils.isEmpty(configPath)) {
            return;
        }

        writeJsonToFile(list, configPath);

    }

    /**
     * 写入json信息到文件
     * @param obj
     * @param configPath
     */
    private static void writeJsonToFile(Object obj, String configPath) {
        try {
            FileOutputStream ops = new FileOutputStream(configPath);
            JSON.writeJSONString(ops , obj, SerializerFeature.PrettyFormat);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 加载指定路径的数据为对象
     * @param clazz
     * @param configPath
     * @param <T>
     * @return
     */
    public static <T> T loadData(Class<T> clazz, String configPath) {

        String jsonText = readFileToString(configPath);
        return JSON.parseObject(jsonText, clazz);
    }

    /**
     * 加载指定路径的数据为对象集合
     * @param clazz
     * @param configPath
     * @param <T>
     * @return
     */
    public static <T> List<T> loadDatas(Class<T> clazz, String configPath) {

        String jsonText = readFileToString(configPath);

        return JSON.parseArray(jsonText, clazz);
    }

    /**
     * 读取文件为字符串
     * @param configPath
     * @return
     */
    private static String readFileToString(String configPath) {
        File file = new File(configPath);
        if(!file.exists()){
            return null;
        }
        FileInputStream inputStream = null;
        String result = null;
        try {
            inputStream = new FileInputStream(file);
            int length = inputStream.available();
            byte bytes[] = new byte[length];
            inputStream.read(bytes);
            result = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if( null != inputStream) {
              try {
                  inputStream.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
            }
        }
        return result;
    }

}
