/**
 * General utility class extended by all views.
 * The purpose is to reduce redundant code by putting commonly used methods within this class.
 * @author darod
 */
package Utilities;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import views.AddProfileView;
import views.ProfileView;
import views.TaskView;
import views.WebViewerView;

public class ViewUtil {

    private double xOffset = 0;
    private double yOffset = 0;

    public User user;

    public FXMLLoader switchScenes(Event _event, String _fxml) throws IOException {
        FXMLLoader loader;
        Parent parent;
        Scene sceneToSwitchTo;
        Stage referenceStage;

        loader = new FXMLLoader(getClass().getClassLoader().getResource(_fxml));
        parent = loader.load();
        sceneToSwitchTo = new Scene(parent);

        referenceStage = (Stage) ((Node) _event.getSource()).getScene().getWindow();
        referenceStage.setScene(sceneToSwitchTo);

        makeStageMovable(parent, referenceStage);

        referenceStage.show();
        return loader;
    }

    public FXMLLoader switchToProfileScene(Event _event, String _fxml, User _user) throws IOException {
        FXMLLoader loader;
        Parent parent;
        Scene sceneToSwitchTo;
        Stage referenceStage;

        loader = new FXMLLoader(getClass().getClassLoader().getResource(_fxml));
        parent = loader.load();
        ProfileView view = loader.getController();
        view.user = _user;
        System.out.println("Adding Profiles to profile list view");
        for (String field : this.user.getProfiles().keySet()) {
            view.profileListView.getItems().add(field);
        }
        sceneToSwitchTo = new Scene(parent);
        sceneToSwitchTo.getStylesheets().add("CSS/ProfileCSS.css");

        referenceStage = (Stage) ((Node) _event.getSource()).getScene().getWindow();
        referenceStage.setScene(sceneToSwitchTo);

        makeStageMovable(parent, referenceStage);

        referenceStage.show();
        return loader;
    }

    public FXMLLoader switchToTaskScene(Event _event, User _user) throws IOException {
        String fxml = "fxml/TaskFXML.fxml";
        FXMLLoader loader;
        Parent parent;
        Scene sceneToSwitchTo;
        Stage referenceStage;

        loader = new FXMLLoader(getClass().getClassLoader().getResource(fxml));
        parent = loader.load();
        TaskView view = loader.getController();
        view.user = _user;
        view.refreshTreeTableView();
        System.out.println("uuid transferred to task view: " + view.user.getUuid());
        sceneToSwitchTo = new Scene(parent);
        sceneToSwitchTo.getStylesheets().add("CSS/mainPageCSS.css");;

        referenceStage = (Stage) ((Node) _event.getSource()).getScene().getWindow();
        referenceStage.setScene(sceneToSwitchTo);

        makeStageMovable(parent, referenceStage);

        referenceStage.show();
        return loader;
    }

    public FXMLLoader openPopupWindow(String _fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(_fxml));
        Parent parent = loader.load();

        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);

        makeStageMovable(parent, stage);

        stage.setScene(new Scene(parent));
        stage.show();

        return loader;
    }
    
    public FXMLLoader openWebView(String _fxml, WebViewerView _view) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(_fxml));
        loader.setController(_view);
        Parent parent = loader.load();

        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);

        makeStageMovable(parent, stage);

        stage.setScene(new Scene(parent));
        stage.show();

        return loader;
    }
    
    public FXMLLoader openAddProfileWindow(Event _event, String _fxml, ProfileView _view) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(_fxml));
        Parent parent = loader.load();

        AddProfileView view = loader.getController();
        view.setProfileView(_view);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);

        makeStageMovable(parent, stage);

        stage.setScene(new Scene(parent));
        stage.show();

        return loader;
    }
    

    @FXML
    public void closeApplication(AnchorPane _mainAnchorPane) {
        Stage stage = (Stage) _mainAnchorPane.getScene().getWindow();
        stage.close();
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void closeWindow(AnchorPane _mainAnchorPane) {
        Stage stage = (Stage) _mainAnchorPane.getScene().getWindow();
        stage.close();
    }

    public void makeStageMovable(Parent _parent, Stage _stage) {
        _parent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = _stage.getX() - event.getScreenX();
                yOffset = _stage.getY() - event.getScreenY();
            }
        });

        _parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                _stage.setX(event.getScreenX() + xOffset);
                _stage.setY(event.getScreenY() + yOffset);
            }
        });
    }
}
