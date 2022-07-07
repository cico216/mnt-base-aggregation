package com.mnt.fx.tool.proto.cmd;


import com.mnt.fx.tool.proto.conf.UserData;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 代码生成命令行入口
 *
 * @author cc
 * @date 2022/3/8
 */
public class CmdMain {

    private static List<String> supportProtos = Arrays.asList("tcp", "http");
    private static List<String> supportType = Arrays.asList("java", "cs");


    public static void main(String[] args) {
        if(args.length < 3) {
            System.err.println("param error");
            return;
        }
        UserData.init();

        String protoType = args[0];
        if(!supportProtos.contains(protoType)) {
            System.err.println("not support proto, only support " + supportProtos.toString() );
            return;
        }

        String type = args[1];
        if(!supportType.contains(type)) {
            System.err.println("not support type, only support " + supportType.toString() );
            return;
        }

        String filePath = args[2];
        if(!filePath.endsWith(".xml")) {
            System.err.println("not support file, only support xml ");
            return;
        }

        File xmlFIle = new File(filePath);
        if(!xmlFIle.isFile()) {
            System.err.println("please input xml file path ");
            return;
        }

        if(protoType.equals("tcp")) {
            List<Integer> opCodes = new ArrayList<>();
            if(args.length == 4){
                String paramNames = args[3];
                String[] strs = paramNames.replaceAll("，", ",").split(",");
                for (int i = 0; i < strs.length; i++) {
                    opCodes.add(Integer.parseInt(strs[i]));
                }
            }
            CmdParseUtils.generateTCPProto(type, xmlFIle, opCodes);
        } else {
            List<String> paramNames = new ArrayList<>();
            if(args.length == 4){
                String paramNamesStr = args[3];
                String[] strs = paramNamesStr.replaceAll("，", ",").split(",");
                for (int i = 0; i < strs.length; i++) {
                    paramNames.add(args[i]);
                }
            }
            CmdParseUtils.generateHttpProto(type, xmlFIle, paramNames);
        }

        System.out.println("generate code success");

    }

}
