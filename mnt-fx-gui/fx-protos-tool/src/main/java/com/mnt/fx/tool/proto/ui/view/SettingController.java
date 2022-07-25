package com.mnt.fx.tool.proto.ui.view;

import com.mnt.fx.tool.proto.conf.UserData;
import com.mnt.gui.fx.base.BaseController;
import com.mnt.gui.fx.controls.file.FileChooserFacotry;
import com.mnt.gui.fx.loader.FXMLLoaderUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.File;

/**
 * 代码生成设置界面
 * @author jiangbiao
 * @date 2018/9/13 9:41
 */
public class SettingController extends BaseController {

    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtServerProjectPath;

    @FXML
    private TextField txtClientProjectPath;

    private Stage currStage;

    private ObservableList<String> itemTypes = FXCollections.observableArrayList();


    public SettingController(Stage stage) {
        this.currStage = stage;
        FXMLLoaderUtil.load(this);
    }


    @Override
    public void init() {
        super.init();

        String userName = UserData.getUserConfig().getUser();
        txtUserName.setText(userName);

        String serverProjectPath = UserData.getUserConfig().getServerProjectPath();

        txtServerProjectPath.setText(serverProjectPath);

        String clientProjectPath = UserData.getUserConfig().getClientProjectPath();

        txtClientProjectPath.setText(clientProjectPath);




        addListener();

    }

    /**
     * 添加监听
     */
    private void addListener() {
        this.setOnKeyPressed((event) -> {
            esc(event);
        });

    }

    /**
     * 退出事件
     * @param event
     */
    private void esc(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if(keyCode == KeyCode.ESCAPE) {
            //quit
            currStage.close();
        }
    }


    @FXML
    void processClose(ActionEvent event) {
        currStage.close();
    }

    @FXML
    void processConfirm(ActionEvent event) {


        UserData.getUserConfig().setUser(txtUserName.getText());

        UserData.getUserConfig().setServerProjectPath(txtServerProjectPath.getText());
        UserData.getUserConfig().setClientProjectPath(txtClientProjectPath.getText());



        UserData.saveUserConfig();

        currStage.close();
    }


    @FXML
    void processSelectServerDir(ActionEvent event) {

        if(null != txtServerProjectPath.getText() && !"".equals(txtServerProjectPath.getText())) {
            final File dir = FileChooserFacotry.chooserDirectorControl(getMainStage(), txtServerProjectPath.getText());
            if(null != dir && dir.isDirectory()) {
                txtServerProjectPath.setText(dir.getAbsolutePath());
            }

        } else {
            final File dir = FileChooserFacotry.chooserDirectorControl(getMainStage());
            if(null != dir && dir.isDirectory()) {
                txtServerProjectPath.setText(dir.getAbsolutePath());
            }
        }

    }

    @FXML
    void processSelectClientDir(ActionEvent event) {

        if(null != txtClientProjectPath.getText() && !"".equals(txtClientProjectPath.getText())) {
            final File dir = FileChooserFacotry.chooserDirectorControl(getMainStage(), txtClientProjectPath.getText());
            if(null != dir && dir.isDirectory()) {
                txtClientProjectPath.setText(dir.getAbsolutePath());
            }

        } else {
            final File dir = FileChooserFacotry.chooserDirectorControl(getMainStage());
            if(null != dir && dir.isDirectory()) {
                txtClientProjectPath.setText(dir.getAbsolutePath());
            }
        }

    }


}
