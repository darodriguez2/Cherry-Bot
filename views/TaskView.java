/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import Utilities.TaskThread;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import Utilities.ViewUtil;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.paint.Paint;
import testing.UI.TreeTableTask;

/**
 *
 * @author diego
 */
public class TaskView extends ViewUtil implements Initializable  {

    @FXML
    private JFXTreeTableView<TreeTableTask> treeTableView;

    @FXML
    private ObservableList<TreeTableTask> task;

    @FXML
    private TreeItem<TreeTableTask> root;

    @FXML
    private AnchorPane mainAnchorPane;
    
    @FXML
    public JFXButton taskButton;
    
    @FXML
    public FontAwesomeIconView taskIcon;

    private JFXTreeTableColumn<TreeTableTask, String> site;
    private JFXTreeTableColumn<TreeTableTask, String> item;
    private JFXTreeTableColumn<TreeTableTask, String> profile;
    private JFXTreeTableColumn<TreeTableTask, String> status;

    private double xOffset = 0;
    private double yOffset = 0;
   

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createColumns();
        deselectRowEventListener();
        this.treeTableView.setPlaceholder(new Label("Add a task"));
        task = FXCollections.observableArrayList();
        task.add(new TreeTableTask("uuid", "Supreme", "Profile 1", "Relief Box Logo", "testing"));
        root = new RecursiveTreeItem<>(task, RecursiveTreeObject::getChildren);
        this.treeTableView.getColumns().setAll(this.site, this.profile, this.item, this.status);
        this.treeTableView.setRoot(root);
        this.treeTableView.setShowRoot(false);
        
        this.taskButton.setTextFill( Paint.valueOf("#ffa2a2"));
        this.taskIcon.setFill(Paint.valueOf("#ffa2a2"));
    }

    public void refreshTreeTableView() {
        for(TreeItem<TreeTableTask> treeTableTask : this.user.getTreeTableTasks()) {
            this.treeTableView.getRoot().getChildren().add(treeTableTask);
        }   
    }
    @FXML
    public void add(ActionEvent _event) throws IOException {
        FXMLLoader loader = this.openPopupWindow("fxml/AddTaskFXML2.fxml");
        AddTaskView view = loader.getController();
        view.setTreeTableView(this.treeTableView);
        view.setTaskView(this);
        view.addProfilesTOComboBox(this.user.getProfiles().keySet());
    }

    @FXML
    public void delete() {
        System.out.println("attempting to delete");
        TreeItem<TreeTableTask> taskToDelete = this.treeTableView.getSelectionModel().getSelectedItem();
        this.treeTableView.getRoot().getChildren().remove(taskToDelete);
    }
    
    @FXML
    public void deleteAll() {
        
    }
    
    @FXML
    public void startAll() {
        System.out.println(this.user.getTasks());
    }
    
    @FXML
    public void stopAll() throws IOException {
        this.openPopupWindow("fxml/WebViewerFXML.fxml");
    }

    @FXML
    public void closeApplication() {
        this.closeApplication(this.mainAnchorPane);

    }

    
    @FXML
    public void profileButton(ActionEvent _event) throws IOException {
        this.switchToProfileScene(_event, "fxml/ProfileFXML.fxml", this.user);
        
    }
    
    @FXML
    public void googleLogin() throws IOException {
      TreeTableTask selectedTask = this.treeTableView.getSelectionModel().getSelectedItem().getValue();
      System.out.println(selectedTask.getUUID());
      String taskThreadID = selectedTask.getUUID().toString();
      TaskThread thread = this.user.getTasks().get(taskThreadID);
      thread.setWebViewer(this.openPopupWindow("fxml/WebViewerFXML.fxml").getController());
      thread.googleLogin();   
    }
    
    @FXML
    public void watchYoutube() {
        
    }
    
    @FXML
    public void solveCaptcha() {
        
    }
    
    
    /////////////////////TABLE CLASSES//////////////////////////////

    @FXML
    public JFXTreeTableView getTreeTableView() { 
        return this.treeTableView;
    }

    public void createColumns() {
        this.site = new JFXTreeTableColumn<>("Site");
        site.setPrefWidth(treeTableView.getPrefWidth() / 4);
        site.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TreeTableTask, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TreeTableTask, String> param) {
                return param.getValue().getValue().site;
            }
        });

        this.profile = new JFXTreeTableColumn<>("Profile");
        profile.setPrefWidth(treeTableView.getPrefWidth() / 4);
        profile.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TreeTableTask, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TreeTableTask, String> param) {
                return param.getValue().getValue().profile;
            }
        });

        this.item = new JFXTreeTableColumn<>("Item");
        item.setPrefWidth(treeTableView.getPrefWidth() / 4);
        item.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TreeTableTask, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TreeTableTask, String> param) {
                return param.getValue().getValue().item;
            }
        });

        this.status = new JFXTreeTableColumn<>("Status");
        status.setPrefWidth(treeTableView.getPrefWidth() / 4);
        status.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TreeTableTask, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TreeTableTask, String> param) {
                return param.getValue().getValue().status;
            }
        });
    }

    public void deselectRowEventListener() {
        this.treeTableView.setRowFactory(new Callback<TreeTableView<TreeTableTask>, TreeTableRow<TreeTableTask>>() {
            @Override
            public TreeTableRow<TreeTableTask> call(TreeTableView<TreeTableTask> tableView2) {
                final TreeTableRow<TreeTableTask> row = new TreeTableRow<>();
                row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        final int index = row.getIndex();
                        if (index >= 0 && index < treeTableView.getRoot().getChildren().size() && treeTableView.getSelectionModel().isSelected(index)) {
                            treeTableView.getSelectionModel().clearSelection();
                            event.consume();
                        }
                    }
                });
                return row;
            }
        });
    }
}
