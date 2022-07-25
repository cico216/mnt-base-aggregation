package com.mnt.fx.tool.proto.ui.view;

import com.mnt.fx.tool.proto.ui.callback.ParamDeleteAction;
import com.mnt.gui.fx.base.BaseController;
import com.mnt.gui.fx.loader.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * 请求头参数页面
 * @author jiangbiao
 * @date 2018/9/13 15:12
 */
public class HeaderParamController extends BaseController {

    @FXML
    private TextField txtParamName;

    @FXML
    private TextField txtParamValue;

    private ParamDeleteAction paramDeleteAction;

    public HeaderParamController(ParamDeleteAction paramDeleteAction, String name, String value) {

        FXMLLoaderUtil.load(this);

        this.paramDeleteAction = paramDeleteAction;
        txtParamName.setText(name);
        txtParamValue.setText(value);

    }




    @Override
    public void init() {
        super.init();
    }



    @FXML
    void processDelete(ActionEvent event) {
        paramDeleteAction.delete(this);
    }


    /**
     * 获取参数名
     * @return
     */
    public String getName() {
        return txtParamName.getText();
    }

    /**
     * 获取参数值
     * @return
     */
    public String getValue() {
        return txtParamValue.getText();
    }


}
