package com.mnt.gui.fx.concurrent;

/**
 * fx thread factory
 */
public class FXThreadFactory {

    /**
     * run task and callback fx thread
     * @param serviceTask task
     */
    public static void runTask(ServiceTask serviceTask) {
        serviceTask.start();
    }

}
