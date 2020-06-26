/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import Utilities.ViewUtility;
import com.jfoenix.controls.JFXTreeTableView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import testing.UI.*;

public class AddTaskView extends ViewUtility implements Initializable {

    @FXML
    ComboBox selectStore;
    @FXML
    ComboBox selectProfile;
    @FXML
    ComboBox itemType;
    @FXML
    ComboBox size;

    @FXML
    TextField itemKeyword;
    @FXML
    TextField color;
    @FXML
    TextField info1;
    @FXML
    TextField info2;
    @FXML
    TextField info3;
    @FXML
    TextField info4;

    @FXML
    AnchorPane clothing;
    @FXML
    AnchorPane accessory;
    @FXML
    AnchorPane mainAnchorPane;

    @FXML
    JFXTreeTableView<Task> taskView;

    @FXML
    Label errorLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //ONLY SUPREME IS SUPPORTED...FOR NOW. 6/14/2020
        this.selectStore.getItems().add("Supreme");

        this.selectProfile.getItems().add(getProfiles());
        this.itemType.getItems().addAll("Clothing", "Accessory");
        this.size.getItems().addAll("small", "medium", "large");

        this.clothing.setVisible(false);
        this.accessory.setVisible(false);
    }

    /**
     * Method for getting profiles from AWS. For now, we are hard coding a
     * sample profile
     *
     * @return
     */
    public String getProfiles() {
        return "profile x";
    }

    public void itemTypeSelected() {
        if (this.itemType.getValue().equals("Clothing")) {
            this.clothing.setVisible(true);
            this.accessory.setVisible(false);
        } else if (this.itemType.getValue().equals("Accessory")) {
            this.clothing.setVisible(false);
            this.accessory.setVisible(true);
        }
    }

    public void addTask() {
        if (checkFields()) {
            Task taskToAdd = new Task(this.selectStore.getValue().toString(), this.selectProfile.getValue().toString(), this.itemKeyword.getText(), "Ready to start");
            TreeItem<Task> addTask = new TreeItem<>(taskToAdd);
            this.taskView.getRoot().getChildren().add(addTask);
            closeWindow();
        } else {
            this.errorLabel.setText("Please fill all required fields");
        }
    }

    public boolean checkFields() {
        if (this.itemType.getValue() != null) {
            switch (this.itemType.getValue().toString()) {
                case "Clothing":
                    if (this.selectStore.getValue() == null || this.selectProfile.getValue() == null || this.itemKeyword.getText() == null
                            || this.color.getText().equals("") || this.size.getValue() == null) {
                        return false;
                    }
                    break;
                case "Accessory":
                    if (this.selectStore.getValue() == null || this.selectProfile.getValue() == null || this.itemKeyword.getText() == null) {
                        return false;
                    }
                    break;
            }

        } else {
            return false;
        }

        return true;
    }

    public void cancelAddTask() {
        closeWindow();
    }

    public void closeWindow() {
        this.closeWindow(this.mainAnchorPane);
    }

    public void setTaskView(JFXTreeTableView<Task> _taskView) {
        this.taskView = _taskView;
    }
}
