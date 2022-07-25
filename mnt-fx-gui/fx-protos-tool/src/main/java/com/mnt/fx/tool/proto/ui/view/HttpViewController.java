package com.mnt.fx.tool.proto.ui.view;

import com.alibaba.fastjson.JSONObject;
import com.mnt.base.thread.ThreadPoolManager;
import com.mnt.base.utils.JSONFormatUtils;
import com.mnt.fx.tool.proto.conf.UserData;
import com.mnt.fx.tool.proto.ui.utils.ConsoleLogUtils;
import com.mnt.fx.tool.proto.ui.utils.HttpRequestUtils;
import com.mnt.fx.tool.proto.ui.utils.ProtoVOParseUtils;
import com.mnt.fx.tool.proto.ui.vo.BaseCommandVO;
import com.mnt.fx.tool.proto.ui.vo.BaseProtoVO;
import com.mnt.fx.tool.proto.ui.vo.CommadReqVO;

import com.mnt.fx.tool.proto.ui.vo.CommadRespVO;
import com.mnt.gui.fx.base.BaseController;
import com.mnt.gui.fx.controls.dialog.DialogFactory;
import com.mnt.gui.fx.loader.FXMLLoaderUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http协议展示界面
 *
 * @author cc
 * @date 2022/3/10
 */
public class HttpViewController extends BaseController {



    /************************************************************************************************* 协议信息start ****************************************************************************************************/

    @FXML
    private TextField txtUrlPathFrist;

    @FXML
    private TextField txtUrlPathScoend;

    @FXML
    private CheckBox cbIsBody;

    @FXML
    private TreeTableView<CommadReqVO> treeTableRequest;

    @FXML
    private TreeTableColumn<CommadReqVO, String> trclumReqName;

    @FXML
    private TreeTableColumn<CommadReqVO, String> trclumReqRemark;

    @FXML
    private TreeTableColumn<CommadReqVO, String> trclumReqType;

    @FXML
    private TreeTableColumn<CommadReqVO, Integer> trclumReqLength;

    @FXML
    private TreeTableColumn<CommadReqVO, Boolean> trclumReqMust;

    @FXML
    private TreeTableColumn<CommadReqVO, String> trclumReqTest;

    /**
     * 返回参数相关
     */
    @FXML
    private TreeTableView<CommadRespVO> treeTableResponse;

    @FXML
    private TreeTableColumn<CommadRespVO, String> trclumRespName;

    @FXML
    private TreeTableColumn<CommadRespVO, String> trclumRespRemark;

    @FXML
    private TreeTableColumn<CommadRespVO, String> trclumRespType;

    @FXML
    private TreeTableColumn<CommadRespVO, String> trclumRespTypeClass;

    @FXML
    private TreeTableColumn<CommadRespVO, String> trclumRespTest;

    public HttpViewController() {
        FXMLLoaderUtil.load(this);
    }

    @Override
    public void init() {
        super.init();
        initReqTree();
    }


    /**
     * 初始化请求树
     */
    private void initReqTree() {
        //req
        treeTableRequest.getSelectionModel().setCellSelectionEnabled(true);
        trclumReqName.setCellValueFactory(new TreeItemPropertyValueFactory("name"));
        trclumReqRemark.setCellValueFactory(new TreeItemPropertyValueFactory("remark"));
        trclumReqType.setCellValueFactory(new TreeItemPropertyValueFactory("type"));
        trclumReqLength.setCellValueFactory(new TreeItemPropertyValueFactory("limit"));
        trclumReqMust.setCellValueFactory(new TreeItemPropertyValueFactory("must"));
        trclumReqTest.setCellValueFactory(new TreeItemPropertyValueFactory("test"));

        trclumReqTest.setCellFactory(new Callback<TreeTableColumn<CommadReqVO, String>, TreeTableCell<CommadReqVO, String>>() {
            @Override
            public TreeTableCell<CommadReqVO, String> call(TreeTableColumn<CommadReqVO, String> param) {
                return new TreeTableCell<CommadReqVO, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        if(!empty) {
                            if(null != item && getTreeTableRow().getTreeItem() != null) {

                                TextField textField = new TextField(item);

                                textField.textProperty().bindBidirectional(getTreeTableRow().getTreeItem().getValue().testProperty());
                                setGraphic(textField);

                                setGraphic(textField);
                                textField.requestFocus();
                                textField.end();
                                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                                    if(null != newValue) {
                                        rebuildReqText();
                                    }
                                });

                            }

                        } else {
                            setGraphic(null);
                        }
                        super.updateItem(item, empty);
                    }
                };
            }
        });

        treeTableRequest.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.isControlDown() && event.getCode() == KeyCode.C) {
                    ObservableList<TreeTablePosition<CommadReqVO,?>> selectCells = treeTableRequest.getSelectionModel().getSelectedCells();
                    if(null != selectCells && !selectCells.isEmpty()) {
                        TreeItem<CommadReqVO> item = selectCells.get(0).getTreeItem();
                        int selectIndex = selectCells.get(0).getColumn();
                        String selectVal = null;
                        if(selectIndex == 0) {
                            selectVal = item.getValue().getName();
                        } else if(selectIndex == 1) {
                            selectVal = item.getValue().getRemark();
                        } else if(selectIndex == 2) {
                            selectVal = item.getValue().getType();
                        } else if(selectIndex == 3) {
                            selectVal = String.valueOf(item.getValue().getLength());
                        } else if(selectIndex == 4) {
                            selectVal = String.valueOf(item.getValue().isMust());
                        } else if(selectIndex == 5) {
                            selectVal = item.getValue().getTest();
                        } else {

                        }

                        if(null != selectVal) {
                            setSysClipboardText(selectVal);
                        }


                    }


                }
            }
        });

        //resp
        treeTableResponse.getSelectionModel().setCellSelectionEnabled(true);
        trclumRespName.setCellValueFactory(new TreeItemPropertyValueFactory("name"));
        trclumRespRemark.setCellValueFactory(new TreeItemPropertyValueFactory("remark"));
        trclumRespType.setCellValueFactory(new TreeItemPropertyValueFactory("type"));
        trclumRespTest.setCellValueFactory(new TreeItemPropertyValueFactory("test"));
        trclumRespTypeClass.setCellValueFactory(new TreeItemPropertyValueFactory("typeClass"));

        treeTableResponse.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.isControlDown() && event.getCode() == KeyCode.C) {
                    ObservableList<TreeTablePosition<CommadRespVO,?>> selectCells = treeTableResponse.getSelectionModel().getSelectedCells();
                    if(null != selectCells && !selectCells.isEmpty()) {
                        TreeItem<CommadRespVO> item = selectCells.get(0).getTreeItem();
                        int selectIndex = selectCells.get(0).getColumn();
                        String selectVal = null;
                        if(selectIndex == 0) {
                            selectVal = item.getValue().getName();
                        } else if(selectIndex == 1) {
                            selectVal = item.getValue().getRemark();
                        } else if(selectIndex == 2) {
                            selectVal = item.getValue().getType();
                        } else if(selectIndex == 3) {
                            selectVal = item.getValue().getTest();
                        }  else {

                        }

                        if(null != selectVal) {
                            setSysClipboardText(selectVal);
                        }
                    }


                }
            }
        });

//        trclumReqTest.setCellFactory(new Callback<TreeTableColumn<CommadReqVO, String>, TreeTableCell<CommadReqVO, String>>() {
//            @Override
//            public TreeTableCell<CommadReqVO, String> call(TreeTableColumn<CommadReqVO, String> param) {
//                return new TreeTableCell<CommadReqVO, String>() {
//                    @Override
//                    public void updateItem(String item, boolean empty) {
//                        if(!empty) {
//
//                            TextField textField = new TextField(item);
//                            textField.textProperty().bindBidirectional(getTreeTableRow().getTreeItem().getValue().testProperty());
//                            setGraphic(textField);
//                        } else {
//                            setGraphic(null);
//                        }
//                        super.updateItem(item, empty);
//                    }
//                };
//            }
//        });
    }

    /**
     * 选择协议
     * @param baseCommad
     */
    public void selectCommad(BaseProtoVO baseProtoVO, BaseCommandVO baseCommad) {

        if(null == baseCommad) {
            return;
        }

        if(null != treeTableRequest.getRoot()) {
            //清除当前选项
            treeTableRequest.getRoot().getChildren().clear();
        }

        if(null != treeTableResponse.getRoot()) {
            //清除当前选项
            treeTableResponse.getRoot().getChildren().clear();
        }

        //请求一级路径
        String path = ProtoVOParseUtils.getProtoPath(baseProtoVO.getXmlObject());

        String secondPath = baseCommad.getCurrNode().valueOf("@path");

        txtUrlPathFrist.setText(path);
        txtUrlPathScoend.setText(secondPath);

//        //设置请求方法
//        String method = baseCommad.getMethod();
//        lblReqMethod.setText(method);

        String body = baseCommad.getCurrNode().valueOf("@body");
        //是否为body
        boolean isBody = Boolean.valueOf(body);
        cbIsBody.setSelected(isBody);


        //解析请求参数
        List<CommadReqVO> commadReqVOs = ProtoVOParseUtils.parseCommadReqVOs(baseCommad);

//        for (CommadReqVO commadReqVO : commadReqVOs) {
//
//
//            treeTableRequest.getRoot().getChildren().add();
//        }
        treeTableRequest.setRoot(new TreeItem<>());
        setReqChildren(treeTableRequest.getRoot(), commadReqVOs);

        //解析答复参数
        treeTableResponse.setRoot(new TreeItem<>());
        List<CommadRespVO> commadResp = ProtoVOParseUtils.parseCommadRespVOs(baseCommad);
        setRespChildren(treeTableResponse.getRoot(), commadResp);


        //设置测试界面

        copyRequestUrl = null;

        String testUrl = UserData.getUserConfig().getTestHttpUrl();

        if(null == testUrl) {
            testUrl = "";
        }

        //移除最后一个斜杠
        if(testUrl.endsWith("/")) {
            testUrl = testUrl.substring(0, testUrl.length() - 1);
        }

        copyRequestUrl = testUrl + "/" + path + "/" + secondPath;

        txtRequestUrl.setText(copyRequestUrl);

        rebuildReqText();
    }

    /**
     * 递归设置子节点
     * @param baseCommadItem
     * @param commadReqVOs
     */
    private void setReqChildren(TreeItem<CommadReqVO> baseCommadItem, List<CommadReqVO> commadReqVOs) {
        TreeItem<CommadReqVO> treeItem;
        for (CommadReqVO commadReqVO : commadReqVOs) {
            treeItem =  new TreeItem<>(commadReqVO);
            baseCommadItem.getChildren().add(treeItem);
            if(!commadReqVO.getChildrens().isEmpty()) {
                setReqChildren(treeItem, commadReqVO.getChildrens());
            }

        }
    }

    /**
     * 递归设置返回子节点
     * @param baseCommadItem
     * @param commadRespVOs
     */
    private void setRespChildren(TreeItem<CommadRespVO> baseCommadItem, List<CommadRespVO> commadRespVOs) {
        TreeItem<CommadRespVO> treeItem;
        for (CommadRespVO commadRespVO : commadRespVOs) {
            treeItem =  new TreeItem<>(commadRespVO);
            treeItem.setExpanded(true);
            baseCommadItem.getChildren().add(treeItem);
            if(!commadRespVO.getChildrens().isEmpty()) {
                setRespChildren(treeItem, commadRespVO.getChildrens());
            }

        }
    }


    /************************************************************************************************* 协议信息end *****************************************************************************************************/



    /************************************************************************************************* 测试协议start ****************************************************************************************************/

    @FXML
    private TextField txtRequestUrl;

    @FXML
    private TextField txtXCount;

    @FXML
    private TextArea txtAreaRequest;

    @FXML
    private TextArea txtAreaResonse;

    /**
     * 请求路径
     */
    private String copyRequestUrl;

    /**
     * 初始化协议测试
     */
    private void initProtoTest() {


    }



    @FXML
    void processCopyURL(ActionEvent event) {
        if(null == copyRequestUrl) {
            DialogFactory.getInstance().showFaildMsg("复制失败", "请选择请求的的命令", ()->{});
            return;
        }

        setSysClipboardText(copyRequestUrl);
        DialogFactory.getInstance().showSuccessMsg("复制成功", copyRequestUrl, ()->{});

    }

    /**
     * 请求测试
     * @param testUrl
     */
    private void requestTest(String testUrl) {

        //是否为body请求
        if(cbIsBody.isSelected()) {
            bodyTest(testUrl);
        } else {
            notEncryptTest(testUrl);
        }

    }


    @FXML
    void processTest(ActionEvent event) {
        String testUrl = txtRequestUrl.getText();

        if("" == testUrl || "".equals(testUrl)) {
            DialogFactory.getInstance().showFaildMsg("请求失败", "请选择请求的的命令", ()->{});
            return;
        }

        requestTest(testUrl);
    }

    @FXML
    void processTest100(ActionEvent event) {
        String testUrl = txtRequestUrl.getText();

        if("" == testUrl || "".equals(testUrl)) {
            DialogFactory.getInstance().showFaildMsg("请求失败", "请选择请求的的命令", ()->{});
            return;
        }

        for (int i = 0; i < 20 ; i ++) {
            for (int j = 0; j < 5 ; j ++) {
                ThreadPoolManager.getInstance().schedule(()->{

                    requestTest(testUrl);

                }, 500 * i);
            }

        }
    }

    @FXML
    void processTestX(ActionEvent event) {
        String testUrl = txtRequestUrl.getText();

        if("" == testUrl || "".equals(testUrl)) {
            DialogFactory.getInstance().showFaildMsg("请求失败", "请选择请求的的命令", ()->{});
            return;
        }

        int count;
        try {
            count = Integer.parseInt(txtXCount.getText());
        } catch (Exception e) {
            DialogFactory.getInstance().showFaildMsg("请求失败", "请选择输入数字", ()->{});
            return;
        }

        for (int j = 0; j < count ; j ++) {
            ThreadPoolManager.getInstance().schedule(()->{
                requestTest(testUrl);
            }, 500 * (j / 5));
        }

    }


    /**
     * 不加密请求
     * @param testUrl
     */
    private void notEncryptTest(String testUrl) {

        String requestParamUrl =  buildRequestUrl(testUrl);
        ConsoleLogUtils.log("请求URL : [" + requestParamUrl + "]");
        ThreadPoolManager.getInstance().execute(()-> {
            String requestResult = HttpRequestUtils.getHttpResult(requestParamUrl, "GET");

            if(null == requestResult || "".equals(requestResult)) {
                Platform.runLater(()-> {
                    txtAreaResonse.setText("请求异常或超时!");
                });
                //请求返回超时
                return;
            }

            String formatRequestResult = JSONFormatUtils.formatJson(requestResult);

            Platform.runLater(()-> {
                //设置返回数据
                txtAreaResonse.setText(formatRequestResult);
            });
        });


    }

    /**
     * 构建请求url
     * @param testUrl
     * @return
     */
    private String buildRequestUrl(String testUrl) {
        StringBuilder paramUrlSB = new StringBuilder();
        final SimpleBooleanProperty isFrist = new SimpleBooleanProperty(true);

        treeTableRequest.getRoot().getChildren().forEach(commadReqVOTreeItem ->{
            if(null != commadReqVOTreeItem.getValue().getTest()) {
                if(isFrist.get()) {
                    isFrist.set(false);
                } else {
                    paramUrlSB.append("&");
                }
                paramUrlSB.append(commadReqVOTreeItem.getValue().getName());
                paramUrlSB.append("=");
                paramUrlSB.append(commadReqVOTreeItem.getValue().getTest());
            }

//            paramsStr += paramEntry.getKey() + "=" + String.valueOf(paramEntry.getValue());

        });

        //http请求的地址
        String requestParamUrl;
        if(paramUrlSB.length() > 0) {
            requestParamUrl = testUrl + "?" + paramUrlSB.toString();
        } else {
            requestParamUrl = testUrl;
        }
        return requestParamUrl;
    }

    /**
     * body请求
     * @param testUrl
     */
    private void bodyTest(String testUrl) {

        String json = buildBodyRequestParam();
        ConsoleLogUtils.log("请求URL : [" + testUrl + json  + "]");
        ThreadPoolManager.getInstance().execute(()-> {
            String requestResult = HttpRequestUtils.getHttpBodyResult(testUrl, json);
            if(null == requestResult || "".equals(requestResult)) {
                //请求返回超时
                Platform.runLater(()-> {
                    txtAreaResonse.setText("请求异常或超时!");
                });
                return;
            }

            String formatRequestResult = JSONFormatUtils.formatJson(requestResult);
            //设置返回数据
            Platform.runLater(()-> {
                txtAreaResonse.setText(formatRequestResult);
            });


        });

    }

    /**
     * 构建body请求参数
     * @return
     */
    private String buildBodyRequestParam() {
        JSONObject jsonObject = new JSONObject();

        treeTableRequest.getRoot().getChildren().forEach(commadReqVOTreeItem ->{
            if(!commadReqVOTreeItem.getValue().getChildrens().isEmpty()) {
                final Map<String, Object> paramMap = new HashMap<>(commadReqVOTreeItem.getValue().getChildrens().size());

                buildInnerJson(commadReqVOTreeItem.getValue().getChildrens(), paramMap);
                List<Map<String, Object>> paramList = new ArrayList<>();
                paramList.add(paramMap);
                jsonObject.put(commadReqVOTreeItem.getValue().getName(), paramList);
            } else {
                if(null != commadReqVOTreeItem.getValue().getTest()) {
                    jsonObject.put(commadReqVOTreeItem.getValue().getName(), commadReqVOTreeItem.getValue().getTest());
                }
            }

        });
        String json = jsonObject.toJSONString();
        return json;
    }

    /**
     * 递归构建子类
     * @param commadReqVOs
     * @param paramMap
     */
    private void buildInnerJson(List<CommadReqVO> commadReqVOs, Map<String, Object> paramMap) {
        for (CommadReqVO commadReqVO : commadReqVOs) {
            if(!commadReqVO.getChildrens().isEmpty()) {
                Map<String, Object> innerParamMap = new HashMap<>(commadReqVO.getChildrens().size());
                List<Map<String, Object>> paramList = new ArrayList<>();
                paramList.add(innerParamMap);
                paramMap.put(commadReqVO.getName(), paramList);
                buildInnerJson(commadReqVO.getChildrens(), innerParamMap);
                continue;
            }
            if(null != commadReqVO.getTest()) {
                paramMap.put(commadReqVO.getName(), commadReqVO.getTest());

            }

        }
    }

    /**
     * 重新构建请求内容
     */
    private void rebuildReqText() {
        String testUrl = txtRequestUrl.getText();
        String requestUrl;
        if(cbIsBody.isSelected()) {
            String jsonParam = buildBodyRequestParam();
            if(StringUtils.isNotEmpty(jsonParam)) {
                jsonParam = JSONFormatUtils.formatJson(jsonParam);
            }
            requestUrl = testUrl + "\n" + jsonParam;
        } else {
            requestUrl = buildRequestUrl(testUrl);
        }
        txtAreaRequest.setText(requestUrl);
    }
    /**
     * 设置复制内容到剪切板
     * @param writeMe
     */
    private void setSysClipboardText(String writeMe) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(writeMe);
        clipboard.setContents(tText, null);
    }



    /************************************************************************************************* 测试协议end ****************************************************************************************************/


}
