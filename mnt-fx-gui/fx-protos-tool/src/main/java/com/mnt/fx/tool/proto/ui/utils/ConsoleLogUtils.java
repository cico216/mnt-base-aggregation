package com.mnt.fx.tool.proto.ui.utils;

import com.mnt.fx.tool.proto.ui.view.MainViewController;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 控制台log工具
 */
public class ConsoleLogUtils {

    /**
     * 添加控制台log
     * @param log
     */
    public static void log(String log) {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();

        if(null != log) {
            boolean  isStart = false;
            for(StackTraceElement stack : stacks) {

                if(isStart) {
                    log = log + "  |---{" + stack.getClassName() + "." + stack.getMethodName() + " : " + stack.getLineNumber() + "}";
                    break;
                }
                if(stack.getClassName().equals(ConsoleLogUtils.class.getName())) {
                    isStart = true;
                }

            }

        }

        MainViewController.addConsloeLog(log);
    }

    /**
     * 添加控制台log
     * @param e
     */
    public static void log(Exception e) {
        String log = "";
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            log = "\r\n" + sw.toString() + "\r\n";
            sw.close();
            pw.close();
        } catch (Exception e2) {
           e2.printStackTrace();
        }



        MainViewController.addConsloeLog(log);
    }

}
