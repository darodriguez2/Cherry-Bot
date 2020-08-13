/**
 * AddTaskView that corresponds with the addTask UI.
 * @author darod
 */
package views;

import Utilities.TreeTableTask;
import Utilities.TaskThread;
import Utilities.ViewUtil;
import com.jfoenix.controls.JFXTreeTableView;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class AddTaskView extends ViewUtil implements Initializable {

    @FXML
    ComboBox selectSite;
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
    JFXTreeTableView<TreeTableTask> treeTableView;

    @FXML
    Label errorLabel;
    
    protected TaskView tv = new TaskView();
    protected Map<String, String> taskInfo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //ONLY SUPREME IS SUPPORTED...FOR NOW. 6/14/2020
        this.selectSite.getItems().add("Supreme");

        this.itemType.getItems().addAll("Clothing", "Accessory");
        this.size.getItems().addAll("small", "medium", "large");

        this.clothing.setVisible(false);
        this.accessory.setVisible(false);
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
            String uuid = UUID.randomUUID().toString();
            System.out.println("adding task UUID: " + uuid);
            TreeTableTask taskToAdd = new TreeTableTask(uuid, this.selectSite.getValue().toString(), this.selectProfile.getValue().toString(), this.itemKeyword.getText(), "Ready to start");
            TreeItem<TreeTableTask> addTask = new TreeItem<>(taskToAdd);
            this.tv.user.addTreeTableTask(addTask);
            this.treeTableView.getRoot().getChildren().add(addTask);
            this.taskInfo = new HashMap<>();
            this.taskInfo.put("site", this.selectSite.getValue().toString());
            this.taskInfo.put("profile", this.selectProfile.getValue().toString());
            this.taskInfo.put("itemType", this.itemType.getValue().toString());
            this.taskInfo.put("itemKeyword", this.itemKeyword.getText());
            if(this.itemType.getValue().equals("Clothing")) {
                this.taskInfo.put("size", this.size.getValue().toString());
                this.taskInfo.put("color", this.color.getText());
            }
            this.tv.user.addTask(uuid, new TaskThread(this.tv.user.getProfiles().get(this.selectProfile.getValue().toString()), taskInfo));
            closeWindow();
        } else {
            this.errorLabel.setText("Please fill all required fields");
        }
    }

    public boolean checkFields() {
        if (this.itemType.getValue() != null) {
            switch (this.itemType.getValue().toString()) {
                case "Clothing":
                    if (this.selectSite.getValue() == null || this.selectProfile.getValue() == null || this.itemKeyword.getText() == null
                            || this.color.getText().equals("") || this.size.getValue() == null) {
                        return false;
                    }
                    break;
                case "Accessory":
                    if (this.selectSite.getValue() == null || this.selectProfile.getValue() == null || this.itemKeyword.getText() == null) {
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

    public void setTreeTableView(JFXTreeTableView<TreeTableTask> _treeTableView) {
        this.treeTableView = _treeTableView;
    }
    
    public void setTaskView(TaskView _tv) {
        this.tv = _tv;
    }
    
    public void addProfilesTOComboBox(Set<String> _profileNames) {
        for(String profileName : _profileNames) {
            this.selectProfile.getItems().add(profileName);
        }
    }
}
