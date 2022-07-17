package com.mnt.fx.tool.proto.ui.view;


import com.mnt.base.thread.ThreadPoolManager;

import com.mnt.fx.tool.proto.cmd.CmdParseUtils;
import com.mnt.fx.tool.proto.conf.UserData;
import com.mnt.fx.tool.proto.ui.utils.ProtoVOParseUtils;
import com.mnt.fx.tool.proto.ui.vo.BaseCommandVO;
import com.mnt.fx.tool.proto.ui.vo.BaseProtoVO;
import com.mnt.gui.fx.base.BaseController;
import com.mnt.gui.fx.controls.dialog.DialogFactory;
import com.mnt.gui.fx.controls.file.FileChooserFacotry;
import com.mnt.gui.fx.view.anno.MainView;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 通讯协议工具类
 */
@MainView(appName = "通讯协议工具")
public class MainViewController extends BaseController {


    private HttpViewController httpViewController;

    private TcpViewController tcpViewController;


    @Override
    public void init() {
        super.init();
        initProtoList();
        addListener();
        initConsole();
        httpViewController = new HttpViewController();
        VBox.setVgrow(httpViewController, Priority.ALWAYS);
        tcpViewController = new TcpViewController();
        VBox.setVgrow(tcpViewController, Priority.ALWAYS);
    }

    /**
     * 添加监听
     */
    void addListener() {
        this.setOnKeyPressed((event) -> {

            KeyCode keyCode = event.getCode();
            if(event.isControlDown() && keyCode == KeyCode.S) {
                //save code
                generateCode();

            }
            if(keyCode == KeyCode.F5) {
                //update
                processUpdate(null);
                DialogFactory.getInstance().showSuccessMsg("刷新成功", "重新加载数据成功", ()->{});
            }
        });
    }



    /**
     * 加载最后一次选择的文件夹
     */
    private void loadLastDir() {
        String defaultPath = UserData.getUserConfig().getLastSelectedDir();
        if(null == defaultPath || "".equals(defaultPath)) {
            return;
        }

        File dir = new File(defaultPath);
        if(null != dir && dir.isDirectory()) {
            loadProto(defaultPath);
        }
    }



    @FXML
    void processMenuAbout(ActionEvent event) {

    }

    @FXML
    void processMenuOpenFile(ActionEvent event) {
        String defaultPath = UserData.getUserConfig().getLastSelectedDir();
        final File dir = FileChooserFacotry.chooserDirectorControl(getMainStage(), defaultPath);
        if(null == dir) {
            return;
        }

        UserData.getUserConfig().setLastSelectedDir(dir.getAbsolutePath());
        UserData.saveUserConfig();
        if(null != dir && dir.isDirectory()) {
            loadProto(dir.getAbsolutePath());
        }
    }

    @FXML
    void processMenuRequestSetting(ActionEvent event) {
        Stage innerStage = new Stage();
        innerStage.initModality(Modality.APPLICATION_MODAL);
        innerStage.initStyle(StageStyle.DECORATED);
        innerStage.setScene(new Scene(new RequestSettingController(innerStage)));
        innerStage.initOwner(stage);
        innerStage.showAndWait();
    }

    @FXML
    void processMenuSetting(ActionEvent event) {
        Stage innerStage = new Stage();
        innerStage.initModality(Modality.APPLICATION_MODAL);
        innerStage.initStyle(StageStyle.DECORATED);
        innerStage.setScene(new Scene(new SettingController(innerStage)));
        innerStage.initOwner(stage);
        innerStage.showAndWait();
    }

    @FXML
    void processMenuGenerateCode(ActionEvent event) {
        generateCode();
    }

    /**
     * 生成代码
     */
    void generateCode() {

        BaseProtoVO baseProtoVO = getSelectedProto();

        List<BaseCommandVO> baseCommandVOs = getCreateSelected();



        List<String> generateTypes = new ArrayList<>();

        if(cbJava.isSelected()) {
            generateTypes.add("java");
        }

        if(cbCs.isSelected()) {
            generateTypes.add("cs");
        }

        if(generateTypes.isEmpty()) {
            DialogFactory.getInstance().showFaildMsg("生成代码错误", "请选择生成代码类型", ()->{});
            return;
        }

        if(null == baseProtoVO) {
            DialogFactory.getInstance().showFaildMsg("生成代码错误", "请选择协议", ()->{});
            return;
        }

        if(null == baseCommandVOs || baseCommandVOs.isEmpty()) {
            DialogFactory.getInstance().showFaildMsg("生成代码错误", "请选择生成的命令", ()->{});
            return;
        }

        try {

            File xmlFile = new File(baseProtoVO.getFilePath());
            for(String generateType : generateTypes) {
                if("tcp".equals(baseProtoVO.getProtoType())) {
                    List<Integer> opCodes = new ArrayList<>();
                    for(BaseCommandVO baseCommandVO : baseCommandVOs) {
                        opCodes.add(Integer.parseInt(baseCommandVO.getCmdKey()));
                    }
                    CmdParseUtils.generateTCPProto(generateType, xmlFile, opCodes);

                } else if("http".equals(baseProtoVO.getProtoType())) {
                    List<String> actionNames = new ArrayList<>();
                    for(BaseCommandVO baseCommandVO : baseCommandVOs) {
                        actionNames.add(baseCommandVO.getCmdKey());
                    }
                    CmdParseUtils.generateHttpProto(generateType, xmlFile, actionNames);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            DialogFactory.getInstance().showConfirm("生成代码错误", e.getLocalizedMessage(), ()->{});
//            DialogFactory.getInstance().showFaildMsg("生成代码错误", "生成异常", ()->{});
            return;
        }



        DialogFactory.getInstance().showSuccessMsg("生成成功", "代码生成成功", ()->{});

    }


    /************************************************************************************************* 协议列表start *****************************************************************************************************/

    @FXML
    private Button btnTopDir;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnNext;

    @FXML
    private CheckBox cbSelect;

    @FXML
    private CheckBox cbJava;

    @FXML
    private CheckBox cbCs;


    @FXML
    private ListView<BaseProtoVO> listViewProtos;

    @FXML
    private ListView<BaseCommandVO> listViewCommad;

    @FXML
    private VBox vboxViewParams;

    private ObservableList<BaseProtoVO> itemProtos = FXCollections.observableArrayList();

    private ObservableList<BaseCommandVO> itemCommads = FXCollections.observableArrayList();

    /**
     * 上一步栈
     */
    private Stack<String> backStack = new Stack<>();

    /**
     * 下一步栈
     */
    private Stack<String> nextStack = new Stack<>();



    /**
     * 初始化协议列表
     */
    private void initProtoList() {
        checkCanBack();
        checkCanNext();

        listViewProtos.setItems(itemProtos);
        listViewCommad.setItems(itemCommads);

        listViewProtos.setCellFactory(new Callback<ListView<BaseProtoVO>, ListCell<BaseProtoVO>>() {
            @Override
            public ListCell<BaseProtoVO> call(ListView<BaseProtoVO> param) {
                return new ListCell<BaseProtoVO>() {
                    @Override
                    protected void updateItem(BaseProtoVO item, boolean empty) {
//                        super.updateItem(item, empty);
                        if(!empty) {
                            if(item.isDir()) {
                                HBox hbox = new HBox();
                                hbox.setAlignment(Pos.CENTER_LEFT);
                                Image imgDir = new Image(getClass().getResourceAsStream("res/fileDir.png"));
                                Label label = new Label(item.getRemark());
                                ImageView imgv = new ImageView(imgDir);
                                imgv.setFitWidth(20);
                                imgv.setFitHeight(20);
                                hbox.getChildren().add(imgv);
                                hbox.getChildren().add(label);
                                hbox.setSpacing(3);
                                setGraphic(hbox);
                            } else {
                                Label label = new Label(item.getRemark());
                                if("tcp".equals(item.getProtoType())) {
                                    label.setStyle("-fx-text-fill:#08d824");

                                } else if("http".equals(item.getProtoType())) {
                                    label.setStyle("-fx-text-fill:#ff5b67");
                                }
                                setGraphic(label);
                            }

                        } else {
                            setGraphic(null);
                        }
                        super.updateItem(item, empty);
                    }
                };
            }
        });

        listViewCommad.setCellFactory(new Callback<ListView<BaseCommandVO>, ListCell<BaseCommandVO>>() {
            @Override
            public ListCell<BaseCommandVO> call(ListView<BaseCommandVO> param) {
                return new ListCell<BaseCommandVO>(){
                    @Override
                    protected void updateItem(BaseCommandVO item, boolean empty) {

                        if(!empty) {
                            String text = item.getRemark();
                            boolean mulitChooseCommad = true;
                            if(mulitChooseCommad) {

                                HBox hbox ;
                                CheckBox checkBox;
                                Label lbl;

                                hbox = new HBox();
                                hbox.setAlignment(Pos.CENTER_LEFT);
                                checkBox = new CheckBox();
                                lbl = new Label();
                                hbox.getChildren().add(checkBox);
                                hbox.getChildren().add(lbl);
                                setGraphic(hbox);


                                lbl.setText(text);
                                item.chooseProperty().bindBidirectional(checkBox.selectedProperty());

                            } else {

                                Label label = new Label(text);
                                setGraphic(label);

                            }

                        } else {
                            setGraphic(null);
                        }

                        super.updateItem(item, empty);
                    }
                };
            }
        });

        listViewProtos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BaseProtoVO>() {
            @Override
            public void changed(ObservableValue<? extends BaseProtoVO> observable, BaseProtoVO oldValue, BaseProtoVO newValue) {
                itemCommads.clear();

                if(null != newValue && !newValue.isDir()) {
                    List<BaseCommandVO> BaseCommandVOs = ProtoVOParseUtils.parseProtoCmd(newValue);
                    itemCommads.addAll(BaseCommandVOs);
                }

            }
        });

        listViewProtos.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2) {
                    BaseProtoVO baseProtoVO = listViewProtos.getSelectionModel().getSelectedItem();
                    if(baseProtoVO.isDir()) {
                        nextStack.clear();
                        checkCanNext();
                        backStack.add(UserData.getUserConfig().getLastSelectedDir());
                        selDir(baseProtoVO.getFilePath());
                    }
                }
            }
        });

        listViewCommad.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BaseCommandVO>() {
            @Override
            public void changed(ObservableValue<? extends BaseCommandVO> observable, BaseCommandVO oldValue, BaseCommandVO newValue) {
                selectCommand(newValue);
            }
        });

        cbSelect.selectedProperty().addListener((observable, oldValue, newValue) -> {
            itemCommads.forEach((item -> item.setChoose(newValue)));
        });
    }



    /**
     * 判断是否可以上一步
     */
    private void checkCanBack() {
        if(backStack.empty()) {
            btnBack.setDisable(true);
        } else {
            btnBack.setDisable(false);
        }
    }

    /**
     * 判断是否可以下一步
     */
    private void checkCanNext() {
        if(nextStack.empty()) {
            btnNext.setDisable(true);
        } else {
            btnNext.setDisable(false);
        }
    }

    /**
     * 选择文件夹操作
     * @param dirPath
     */
    private void selDir(String dirPath) {

        UserData.getUserConfig().setLastSelectedDir(dirPath);
        UserData.saveUserConfig();
        loadProto(dirPath);
        checkCanBack();
    }

    /**
     * 加载协议
     * @param dirPath
     */
    private void loadProto(String dirPath) {
        itemProtos.clear();
        itemProtos.addAll(ProtoVOParseUtils.parseProtoDir(dirPath));

    }

    /**
     * 清除所有文件关联
     */
    private void clean() {
        if(!itemProtos.isEmpty()) {
            for (BaseProtoVO baseProtoVO : itemProtos) {
                baseProtoVO.clean();
            }
        }
        itemProtos.clear();
    }

    /**
     * 获取当前协议
     * @return
     */
    private BaseProtoVO getSelectedProto() {
        return listViewProtos.getSelectionModel().getSelectedItem();
    }

    /**
     * 获取所有选中
     * @return
     */
    private List<BaseCommandVO> getCreateSelected() {
        List<BaseCommandVO> result = new ArrayList<>();

        itemCommads.forEach((itemCommad) -> {
            if(itemCommad.isChoose()) {
                result.add(itemCommad);
            }
        });

        return Collections.unmodifiableList(result);
    }



    /**
     * 选择命令
     * @param baseCommand
     */
    private void selectCommand(BaseCommandVO baseCommand) {
        if(null == baseCommand) {
            return;
        }
        vboxViewParams.getChildren().clear();
        BaseProtoVO baseProtoVO = getSelectedProto();
        if("tcp".equals(baseProtoVO.getProtoType())) {
            tcpViewController.selectCommad(baseProtoVO, baseCommand);
            vboxViewParams.getChildren().add(tcpViewController);

        } else if("http".equals(baseProtoVO.getProtoType())){
            httpViewController.selectCommad(baseProtoVO, baseCommand);
            vboxViewParams.getChildren().add(httpViewController);
        }




    }

    /**
     * 获取选择的命令
     * @return
     */
    private BaseCommandVO getSelectedCommad() {
        return listViewCommad.getSelectionModel().getSelectedItem();
    }



    @FXML
    void processBack(ActionEvent event) {
        String dir = backStack.pop();
        nextStack.push(UserData.getUserConfig().getLastSelectedDir());
        selDir(dir);
        checkCanNext();
        checkCanBack();
    }

    @FXML
    void processNext(ActionEvent event) {
        String dir = nextStack.pop();
        backStack.push(UserData.getUserConfig().getLastSelectedDir());
        selDir(dir);
        checkCanBack();
        checkCanNext();
    }

    @FXML
    void processOpenDir(ActionEvent event) {
        try {
            Desktop.getDesktop().open(new File(UserData.getUserConfig().getLastSelectedDir()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void processTopDir(ActionEvent event) {
        backStack.clear();
        nextStack.clear();
        checkCanBack();
        checkCanNext();
        File currDir = new File(UserData.getUserConfig().getLastSelectedDir());
        selDir(currDir.getParent());
    }

    @FXML
    void processUpdate(ActionEvent event) {
        String dirPath = UserData.getUserConfig().getLastSelectedDir();
        final File dir = new File(dirPath);
        if(null != dir && dir.isDirectory()) {
            loadProto(dirPath);

        }
    }



    /************************************************************************************************* 协议列表end *****************************************************************************************************/


    /************************************************************************************************* 协议信息start ****************************************************************************************************/

    /**
     * 错误码代码生成
     * @param event
     */
    @FXML
    void processMenuErrorCode(ActionEvent event) {

    }

    /**
     * 协议导入模板代码
     * @param event
     */
    @FXML
    void processMenuProtoImport(ActionEvent event) {

    }


    /************************************************************************************************* 控制台start ****************************************************************************************************/


    @FXML
    private TextArea txtAreaConsole;

    private static Queue<String> logQueue = new ArrayBlockingQueue<String>(10000);

    /**
     * 初始化控制台
     */
    private void initConsole() {
        initLogQueue();
        loadLastDir();
    }

    /**
     * 初始化log队列
     */
    private void initLogQueue() {
        ThreadPoolManager.getInstance().scheduleAtFixedRate(()-> {
            String log = logQueue.poll();
            if(null != log) {
                Platform.runLater(()-> {
                    txtAreaConsole.appendText(log + "\n");
                });

            }

        }, 300, 50);
    }




    @FXML
    void processClean(ActionEvent event) {
        txtAreaConsole.clear();
    }

    @FXML
    void processCopy(ActionEvent event) {
        String context = txtAreaConsole.getText();

        if(!"".equals(context) && null != context) {
            setSysClipboardText(context);
        }
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


    /**
     * 添加log信息到控制台
     * @param log
     */
    public static void addConsloeLog(String log) {
        logQueue.add(log);
    }

    /************************************************************************************************* 控制台end ****************************************************************************************************/


}
