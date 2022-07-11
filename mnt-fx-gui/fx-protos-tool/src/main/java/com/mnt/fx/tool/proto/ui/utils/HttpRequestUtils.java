package com.mnt.fx.tool.proto.ui.utils;

import com.mnt.fx.tool.proto.conf.UserData;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

/**
 * http请求工具类
 * @author jiangbiao
 * @date 2018/9/12 16:55
 */
public class HttpRequestUtils {



    /**
     * 请求指定url
     * @param url
     * @param requestMethod 请求类型
     * @return
     */
    public static String getHttpResult(String url, String requestMethod) {
        String result = "";
        url = handlerURL(url);
        Map<String , String> headers = UserData.getUserConfig().getHeaders();

        HttpRequestBase httpRequest;
        if("POST".equals(requestMethod)) {
            httpRequest = new HttpPost();
        } else {
            httpRequest = new HttpGet(url);
        }

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(4000)
                .setConnectTimeout(4000)
                .setSocketTimeout(4000).build();
        httpRequest.setConfig(requestConfig);

        //设置请求头
        headers.forEach((key, value) -> {
            httpRequest.setHeader(key, value);
        });

        try {
            HttpResponse httpResponse = HttpClients.createDefault().execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(httpResponse.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }

        return result;
    }

    /**
     * 加密请求
     * @param url
     * @param jsonString
     * @return
     */
    public static String getHttpRasResult(String url, String jsonString) {
        String result = "";

        Map<String , String> headers = UserData.getUserConfig().getHeaders();

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(4000)
                .setConnectTimeout(4000)
                .setSocketTimeout(4000).build();
        httpPost.setConfig(requestConfig);

        httpPost.addHeader("Content-Type", "application/json");
        CloseableHttpResponse response = null;

        //设置请求头
        headers.forEach((key, value) -> {
            httpPost.setHeader(key, value);
        });
        try {
            httpPost.setEntity(new StringEntity(jsonString));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        finally {
            try {
                httpClient.close();
                if(null != response) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * body请求
     * @param url
     * @param jsonString
     * @return
     */
    public static String getHttpBodyResult(String url, String jsonString) {
        String result = "";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(4000)
                .setConnectTimeout(4000)
                .setSocketTimeout(4000).build();


        httpPost.addHeader("Content-Type", "application/json");

        httpPost.setConfig(requestConfig);

        CloseableHttpResponse response = null;
        Map<String , String> headers = UserData.getUserConfig().getHeaders();

        headers.forEach((key, value) -> {
            httpPost.setHeader(key, value);
        });

        try {
            httpPost.setEntity(new StringEntity(jsonString, "UTF-8"));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage().toString();
        }
        finally {
            try {
                httpClient.close();
                if(null != response) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


    private static String handlerURL(String url) {
        url = url.replaceAll(" ", "%20");
        url = url.replace("^", "%5e");
        return url;
    }

}
