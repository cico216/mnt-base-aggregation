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
        String mvnTargetPath = null;

        try {
            urlBin = new URL(DataUtil.BIN_PATH);
            urlApp = new URL(DataUtil.APP_PATH);

            //target

            if(InitFactory.class.getClassLoader() != null && InitFactory.class.getClassLoader().getResource("") != null) {
                mvnTargetPath =  "file:/" +  InitFactory.class.getClassLoader().getResource("").getPath();
            }

            if(mvnTargetPath != null) {
                urlTarget = new URL(mvnTargetPath);
            }

        } catch (MalformedURLException e) {
            log.error("url path is error [" + DataUtil.BIN_PATH + "] ["+ DataUtil.APP_PATH + "] ["+ mvnTargetPath + "]", e);
        }
        if(null != urlTarget && !urlTarget.toString().contains("target/classes")) {
            return new URL[]{urlApp, urlBin, urlTarget};
        }

        return new URL[]{urlApp, urlBin};
    }
}
