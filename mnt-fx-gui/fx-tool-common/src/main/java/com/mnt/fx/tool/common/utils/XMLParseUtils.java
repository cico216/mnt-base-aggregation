package com.mnt.fx.tool.common.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * XML解析工具
 * @author cico
 */
public class XMLParseUtils {

    /**
     * 解析xml文件
     * @param file
     */
    public static XMLObject parseXML(File file) {
        return new XMLObject(file);
    }


    public static XMLObject parseXML(String filePath) {
       return XMLParseUtils.parseXML(new File(filePath));
    }


    /**
     * 解析xml文件
     * @param object
     * @return
     */
    private static void parse(XMLObject object) {
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(object.getFile());

            object.setDocument(document);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析指定路径的节点
     * @param object
     * @param nodePath
     * @return
     */
    private static List<Node> parse(XMLObject object, String nodePath) {
        Document doc = object.getDocument();
        if(null == doc) {
            return Collections.emptyList();
        }
        List<Node> nodes = doc.selectNodes(nodePath);

        return nodes;
    }


    public static class XMLObject {

        private final File file;
        private Document document;

        private XMLObject(File file) {
            this.file = file;
            parse(this);
        }

        private void setDocument(Document document) {
            this.document = document;
        }

        private File getFile() {
            return file;
        }

        private Document getDocument() {
            return document;
        }

        public List<Node> findEle(String nodePath) {
            return parse(this, nodePath);
        }

        public Element getRoot() {
            return document.getRootElement();
        }

        public void reload() {
            close();
            parse(this);
        }

        public void close() {
            if(null != document) {
                document.clone();
            }
        }

    }


    public static void main(String[] args) {
        XMLObject xmlObject = XMLParseUtils.parseXML(new File(System.getProperty("user.dir") + "/protos/test.xml"));
        List<Node> nodes = xmlObject.findEle("protos/action");
        for (Node node : nodes) {
            System.err.println(node);
        }
    }
}
