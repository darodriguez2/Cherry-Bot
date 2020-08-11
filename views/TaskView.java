/**
 * Task view for controlling task scene and facilitating all actions on that scene.
 *
 * @author diego
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
import java.util.Iterator;
import javafx.scene.paint.Paint;
import Utilities.TreeTableTask;

public class TaskView extends ViewUtil implements Initializable {

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

    private boolean tasksRunning;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createColumns();
        deselectRowEventListener();
        this.treeTableView.setPlaceholder(new Label("Add a task"));
        task = FXCollections.observableArrayList();
        //task.add(new TreeTableTask("uuid", "Supreme", "Profile 1", "Relief Box Logo", "testing")); //Dummy task
        root = new RecursiveTreeItem<>(task, RecursiveTreeObject::getChildren);
        this.treeTableView.getColumns().setAll(this.site, this.profile, this.item, this.status);
        this.treeTableView.setRoot(root);
        this.treeTableView.setShowRoot(false);

        this.taskButton.setTextFill(Paint.valueOf("#ffa2a2"));
        this.taskIcon.setFill(Paint.valueOf("#ffa2a2"));
    }

    //This methods need to be called whenever a new task is added
    public void refreshTreeTableView() {
        for (TreeItem<TreeTableTask> treeTableTask : this.user.getTreeTableTasks()) {
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

    //When a task is deleted from the tree table view it must also be removed from the user object's task property
    @FXML
    public void delete() {
        System.out.println("attempting to delete");
        TreeItem<TreeTableTask> taskToDelete = this.treeTableView.getSelectionModel().getSelectedItem();
        this.treeTableView.getRoot().getChildren().remove(taskToDelete);
        this.user.getTasks().remove(taskToDelete.getValue().getUUID());
    }

    /**
     * Iterates through all tasks on the tree table view, removing them one by
     * one from the user object. Then clears the tree.
     */
    @FXML
    public void deleteAll() {
        Iterator<TreeItem<TreeTableTask>> iterator = this.treeTableView.getRoot().getChildren().iterator();
        String taskID;
        while (iterator.hasNext()) {
            taskID = iterator.next().getValue().getUUID();
            this.user.getTasks().remove(taskID);
        }
        this.treeTableView.getRoot().getChildren().clear();
    }

    /**
     * Iterates through all tasks from the tree table view, gets the uuid
     * property of each task, then starts the task thread object within the user
     * object with that corresponding uuid.
     */
    @FXML
    public void startAll() {
        Iterator<TreeItem<TreeTableTask>> iterator = this.treeTableView.getRoot().getChildren().iterator();
        String taskID;
        while (iterator.hasNext()) {
            TreeItem<TreeTableTask> treeItem = iterator.next();
            treeItem.getValue().getStatus().set("Task Started");
            taskID = treeItem.getValue().getUUID();
            System.out.println("getting task with id: " + taskID);
            this.user.getTasks().get(taskID).start();
            this.tasksRunning = true;
        }
    }

    //Interrupts all task threads causing them to stop.
    @FXML
    public void stopAll() throws IOException {
        if (this.tasksRunning) {
            Iterator<TreeItem<TreeTableTask>> iterator = this.treeTableView.getRoot().getChildren().iterator();
            String taskID;
            while (iterator.hasNext()) {
                TreeItem<TreeTableTask> treeItem = iterator.next();
                treeItem.getValue().getStatus().set("Task Ended");
                taskID = treeItem.getValue().getUUID();
                System.out.println("Interrupting task with id: " + taskID);
                this.user.getTasks().get(taskID).interrupt();

                /**
                 * IMPORTANT: Because we are using stopping the threads with
                 * interrupt, the threads cannot be restarted again, so we are
                 * removing them from the user object and replacing them with a
                 * new thread with the exact same paymentProfile and taskInfo.
                 * This allows them to be started again.
                 */
                TaskThread oldTaskThread = this.user.getTasks().get(taskID);
                this.user.getTasks().remove(taskID);
                TaskThread newTaskThread = new TaskThread(oldTaskThread.getPaymentProfile(), oldTaskThread.getTaskInfo());
                this.user.getTasks().put(taskID, newTaskThread);
            }
            this.tasksRunning = false;
        }

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
        TreeItem<TreeTableTask> treeItem = this.treeTableView.getSelectionModel().getSelectedItem();
        TreeTableTask selectedTask = treeItem.getValue();
        selectedTask.getStatus().set("Logging into Gmail");
        String taskThreadID = selectedTask.getUUID();
        
        TaskThread thread = this.user.getTasks().get(taskThreadID);
        thread.googleLogin(treeItem);
    }

    @FXML
    public void watchYoutube() throws IOException {
        TreeItem<TreeTableTask> treeItem = this.treeTableView.getSelectionModel().getSelectedItem();
        TreeTableTask selectedTask = treeItem.getValue();
        selectedTask.getStatus().set("Watching Youtube");
        String taskThreadID = selectedTask.getUUID();
        
        TaskThread thread = this.user.getTasks().get(taskThreadID);
        thread.watchYoutube(treeItem);
    }

    @FXML
    public void solveCaptcha() {
        //will implement when supreme webstore
    }

    /////////////////////TABLE METHODS//////////////////////////////
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
