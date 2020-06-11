/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testing.UI;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
/**
 *
 * @author diego
 */
public class DriverView implements Initializable{

    @FXML
    private JFXTreeTableView <Task> taskView;
    
    @FXML
    private ObservableList<Task> task;
    
    @FXML
    private TreeItem<Task> root;
    
    @FXML
    private AnchorPane mainAnchorPane;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        
        JFXTreeTableColumn<Task, String> site = new JFXTreeTableColumn<>("Site");
        
        site.setPrefWidth(150);
        site.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Task, String> param) {
                return param.getValue().getValue().site;
            }
        });
        
        JFXTreeTableColumn<Task, String> profile = new JFXTreeTableColumn<>("Profile");
        site.setPrefWidth(150);
        profile.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Task, String> param) {
                return param.getValue().getValue().profile;
            }
        });
        
        JFXTreeTableColumn<Task, String> item = new JFXTreeTableColumn<>("Item");
        site.setPrefWidth(350);
        item.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Task, String> param) {
                return param.getValue().getValue().item;
            }
        });
        
        task = FXCollections.observableArrayList();
        task.add(new Task("Supreme", "Profile 1", "Relief Box Logo"));
        root = new RecursiveTreeItem<Task>(task, RecursiveTreeObject::getChildren);
        taskView.getColumns().setAll(site, profile, item);
        taskView.setRoot(root);
        taskView.setShowRoot(false);
        
    }
    
    @FXML
    public void addTasks(ActionEvent e) {
        System.out.println("adding");
        TreeItem<Task> addTask = new TreeItem<>(new Task("Supreme", "Profile 2", "Waves Bowl"));
        taskView.getRoot().getChildren().add(addTask);
    }
    
    @FXML
    public void closeApplication() {
        Stage stage = (Stage) this.mainAnchorPane.getScene().getWindow();
        stage.close();
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }
}


