package com.mnt.gui.fx.init;

import com.mnt.gui.fx.util.DataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * init factory
 * @date 2019.04.40
 * @author cico
 */
public class InitFactory {
    private static final Logger log = LoggerFactory.getLogger(InitFactory.class);

    /**
     * get scan jar and class path
     * @return
     */
    public static URL[] getLoadClassOrJarUrl() {
        URL urlBin = null;
        URL urlApp = null;
        URL urlTarget = null;
        try {
            urlBin = new URL(DataUtil.BIN_PATH);
            urlApp = new URL(DataUtil.APP_PATH);
            urlTarget = new URL(DataUtil.TARGET_PATH);
        } catch (MalformedURLException e) {
            log.error("url path is error [" + DataUtil.BIN_PATH + "] ["+ DataUtil.APP_PATH + "] ["+ DataUtil.TARGET_PATH + "]", e);
        }
        if(!urlTarget.toString().contains("target/classes")) {
            return new URL[]{urlApp, urlBin};
        }

        return new URL[]{urlApp, urlBin, urlTarget};
    }
}
