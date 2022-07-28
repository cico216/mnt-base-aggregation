package com.mnt.fx.tool.proto.ui.view;

import com.mnt.fx.tool.proto.ui.utils.ProtoVOParseUtils;
import com.mnt.fx.tool.proto.ui.vo.BaseCommandVO;
import com.mnt.fx.tool.proto.ui.vo.BaseProtoVO;
import com.mnt.fx.tool.proto.ui.vo.CommandParamVO;
import com.mnt.gui.fx.base.BaseController;
import com.mnt.gui.fx.loader.FXMLLoaderUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;

import java.util.List;

/**
 * tcp协议展示界面
 *
 * @author cc
 * @date 2022/3/10
 */
public class TcpViewController extends BaseController {


    @FXML
    private TextField txtCommandCode;

    @FXML
    private CheckBox cbIsSend;

    @FXML
    private CheckBox cbIsReceive;

    @FXML
    private TreeTableView<CommandParamVO> treeTableRequest;

    @FXML
    private TreeTableColumn<CommandParamVO, String> trclumName;

    @FXML
    private TreeTableColumn<CommandParamVO, String> trclumRemark;

    @FXML
    private TreeTableColumn<CommandParamVO, String> trclumType;

    @FXML
    private TreeTableColumn<CommandParamVO, String> trclumTypeClass;


    public TcpViewController() {
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
        trclumName.setCellValueFactory(new TreeItemPropertyValueFactory("name"));
        trclumRemark.setCellValueFactory(new TreeItemPropertyValueFactory("remark"));
        trclumType.setCellValueFactory(new TreeItemPropertyValueFactory("type"));
        trclumTypeClass.setCellValueFactory(new TreeItemPropertyValueFactory("typeClass"));


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

        txtCommandCode.setText(baseCommad.getCurrNode().valueOf("@opCode"));

        //是否为客户端发送
        boolean isClient = "c".equals(baseCommad.getCurrNode().valueOf("@src"));

        cbIsSend.setSelected(isClient);
        cbIsReceive.setSelected(!isClient);



        //解析请求参数
        List<CommandParamVO> commandParamVOs = ProtoVOParseUtils.parseCommandParamVO(baseCommad);


        treeTableRequest.setRoot(new TreeItem<>());
        setParamChildren(treeTableRequest.getRoot(), commandParamVOs);



    }

    /**
     * 递归设置子节点
     * @param baseCommadItem
     * @param commandParamVOs
     */
    private void setParamChildren(TreeItem<CommandParamVO> baseCommadItem, List<CommandParamVO> commandParamVOs) {
        TreeItem<CommandParamVO> treeItem;
        for (CommandParamVO commandParamVO : commandParamVOs) {
            treeItem =  new TreeItem<>(commandParamVO);
            baseCommadItem.getChildren().add(treeItem);
            if(!commandParamVO.getChildrens().isEmpty()) {
                setParamChildren(treeItem, commandParamVO.getChildrens());
            }

        }
    }



    /************************************************************************************************* 协议信息end *****************************************************************************************************/



}
