package com.mnt.fx.tool.proto.ui.view;

import com.mnt.fx.tool.proto.conf.UserData;
import com.mnt.gui.fx.base.BaseController;
import com.mnt.gui.fx.loader.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiangbiao
 * @date 2018/9/13 9:41
 */
public class RequestSettingController extends BaseController {

    @FXML
    private VBox vbParams;


    private Stage currStage;


    public RequestSettingController(Stage stage) {
        this.currStage = stage;
        FXMLLoaderUtil.load(this);
    }


    @Override
    public void init() {
        super.init();

        //设置请求头
        Map<String, String> headers = UserData.getUserConfig().getHeaders();

        if(null == headers) {
            return;
        }

        for (Map.Entry<String, String> headerKV : headers.entrySet()) {

            //初始化参数
            HeaderParamController headerParamController = new HeaderParamController((delController)->{
                vbParams.getChildren().remove(delController);
            }, headerKV.getKey(), headerKV.getValue());

            //添加到列表显示
            vbParams.getChildren().add(headerParamController);

        }
        addListener();

    }

    /**
     * 添加监听
     */
    private void addListener() {
        this.setOnKeyPressed((event) -> {
            esc(event);
        });
        vbParams.setOnKeyPressed((event) -> {
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
    void processAddParam(ActionEvent event) {
        //初始化参数
        HeaderParamController headerParamController = new HeaderParamController((delController)->{
            vbParams.getChildren().remove(delController);
        }, "", "");
        vbParams.getChildren().add(headerParamController);
    }


    @FXML
    void processClose(ActionEvent event) {
        currStage.close();
    }

    @FXML
    void processConfirm(ActionEvent event) {


        Map<String, String> headers = new HashMap<>();
        //设置请求头
        vbParams.getChildren().forEach((node) -> {
            HeaderParamController headerParamController = (HeaderParamController)node;

            if("".equals(headerParamController.getName()) || "".equals(headerParamController.getValue())) {
                return;
            }
            headers.put(headerParamController.getName(), headerParamController.getValue());
        });

        UserData.getUserConfig().setHeaders(headers);

        UserData.saveUserConfig();

        currStage.close();
    }



}
