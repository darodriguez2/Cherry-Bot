/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testing.UI;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableRow;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import views.AddTaskView;
import Utilities.ViewUtility;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.paint.Paint;

/**
 *
 * @author diego
 */
public class DriverView extends ViewUtility implements Initializable  {

    @FXML
    private JFXTreeTableView<Task> taskView;

    @FXML
    private ObservableList<Task> task;

    @FXML
    private TreeItem<Task> root;

    @FXML
    private AnchorPane mainAnchorPane;
    
    @FXML
    public JFXButton taskButton;
    
    @FXML
    public FontAwesomeIconView taskIcon;

    private JFXTreeTableColumn<Task, String> site;
    private JFXTreeTableColumn<Task, String> item;
    private JFXTreeTableColumn<Task, String> profile;
    private JFXTreeTableColumn<Task, String> status;

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createColumns();
        deselectRowEventListener();
        this.taskView.setPlaceholder(new Label("Add a task"));
        task = FXCollections.observableArrayList();
        task.add(new Task("Supreme", "Profile 1", "Relief Box Logo", "testing"));
        root = new RecursiveTreeItem<>(task, RecursiveTreeObject::getChildren);
        this.taskView.getColumns().setAll(this.site, this.profile, this.item, this.status);
        this.taskView.setRoot(root);
        this.taskView.setShowRoot(false);
        
        this.taskButton.setTextFill( Paint.valueOf("#ffa2a2"));
        this.taskIcon.setFill(Paint.valueOf("#ffa2a2"));

    }

    @FXML
    public void addButton(ActionEvent _event) throws IOException {
        FXMLLoader loader = this.openPopupWindow(_event, "fxml/AddTaskFXML2.fxml");
        AddTaskView view = loader.getController();
        view.setTaskView(this.taskView);
    }

    @FXML
    public void deleteButton() {
        System.out.println("attempting to delete");
        TreeItem<Task> taskToDelete = this.taskView.getSelectionModel().getSelectedItem();
        this.taskView.getRoot().getChildren().remove(taskToDelete);
    }

    @FXML
    public void closeApplication() {
        this.closeApplication(this.mainAnchorPane);

    }

    @FXML
    public void startButtonAction() {

    }

    @FXML
    public void stopButtonAction() {

    }
    
    @FXML
    public void profileButton(ActionEvent _event) throws IOException {
        this.switchScenes(_event, "fxml/ProfileFXML.fxml");
    }
    
    
    /////////////////////TABLE CLASSES//////////////////////////////

    @FXML
    public JFXTreeTableView getTreeTableView() {
        return this.taskView;
    }

    public void createColumns() {
        this.site = new JFXTreeTableColumn<>("Site");
        site.setPrefWidth(taskView.getPrefWidth() / 4);
        site.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Task, String> param) {
                return param.getValue().getValue().site;
            }
        });

        this.profile = new JFXTreeTableColumn<>("Profile");
        profile.setPrefWidth(taskView.getPrefWidth() / 4);
        profile.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Task, String> param) {
                return param.getValue().getValue().profile;
            }
        });

        this.item = new JFXTreeTableColumn<>("Item");
        item.setPrefWidth(taskView.getPrefWidth() / 4);
        item.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Task, String> param) {
                return param.getValue().getValue().item;
            }
        });

        this.status = new JFXTreeTableColumn<>("Status");
        status.setPrefWidth(taskView.getPrefWidth() / 4);
        status.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Task, String> param) {
                return param.getValue().getValue().status;
            }
        });
    }

    public void deselectRowEventListener() {
        this.taskView.setRowFactory(new Callback<TreeTableView<Task>, TreeTableRow<Task>>() {
            @Override
            public TreeTableRow<Task> call(TreeTableView<Task> tableView2) {
                final TreeTableRow<Task> row = new TreeTableRow<>();
                row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        final int index = row.getIndex();
                        if (index >= 0 && index < taskView.getRoot().getChildren().size() && taskView.getSelectionModel().isSelected(index)) {
                            taskView.getSelectionModel().clearSelection();
                            event.consume();
                        }
                    }
                });
                return row;
            }
        });
    }
}
