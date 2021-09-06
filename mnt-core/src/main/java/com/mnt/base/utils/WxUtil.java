package com.mnt.base.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.util.*;

/**
 * Created by yanjun on 2017/7/12.
 */
public class WxUtil {

    private static Logger log = LoggerFactory.getLogger(WxUtil.class);

    public static String MD5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        byte[] var4 = array;
        int var5 = array.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            byte item = var4[var6];
            sb.append(Integer.toHexString(item & 255 | 256).substring(1, 3));
        }

        return sb.toString().toUpperCase();
    }

    //排序字段并转json
    public static String generateSignJson(Map<String, String> data) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();

        Map<String, Object> jsonObject = new HashMap<>();
        for (String k : keyArray) {
//            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
            jsonObject.put(k, data.get(k));
        }
        return  JSON.toJSONString(jsonObject);
    }
    //生成签名
    public static String generateSignature(final Map<String, String> data, String key) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        sb.append("key=").append(key);
        System.out.println("======" + sb);
        return MD5(sb.toString()).toUpperCase();
    }

    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();

            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == 1) {
                    Element element = (Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }

            try {
                stream.close();
            } catch (Exception var10) {
            }

            return data;
        } catch (Exception var11) {
            log.error("Invalid XML, can not convert to map. Error message: {}. XML content: {}", var11.getMessage(), strXML);
            throw var11;
        }
    }

    public static String mapToXml(Map<String, String> data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("xml");
        document.appendChild(root);
        Iterator var5 = data.keySet().iterator();

        while (var5.hasNext()) {
            String key = (String) var5.next();
            String value = data.get(key);
            if (value == null) {
                value = "";
            }

            value = value.trim();
            Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty("encoding", "UTF-8");
        transformer.setOutputProperty("indent", "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString();

        try {
            writer.close();
        } catch (Exception var12) {
            log.error("Invalid map, can not convert to XML . Error message: {}. XML content: {}", var12.getMessage(), data);
            throw var12;
        }

        return output;
    }

    public static String generateNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    public static String generateRandomStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
    }

    //    public static String generateSHA1(final Map<String, String> data, String key) {
//        try {
//            Set<String> keySet = data.keySet();
//            String[] keyArray = keySet.toArray(new String[keySet.size()]);
//            Arrays.sort(keyArray);
//            StringBuilder sb = new StringBuilder();
//            for (String k : keyArray) {
//                if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
//                    sb.append(k).append("=").append(data.get(k).trim()).append("&");
//            }
//            sb.append("key=").append(key);
//            System.out.println("======" + sb);
//            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
//            crypt.reset();
//            crypt.update(sb.toString().getBytes());
//            return WechatSingUtils.byteToHex(crypt.digest());
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
