/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author darod
 */
public class ViewUtility {

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
        referenceStage.show();
        return loader;
    }
    
    public FXMLLoader switchToTaskScene(Event _event, String _fxml) throws IOException {
        FXMLLoader loader;
        Parent parent;
        Scene sceneToSwitchTo;
        Stage referenceStage;

        loader = new FXMLLoader(getClass().getClassLoader().getResource(_fxml));
        parent = loader.load();
        sceneToSwitchTo = new Scene(parent);
        sceneToSwitchTo.getStylesheets().add("CSS/mainPageCSS.css");;

        referenceStage = (Stage) ((Node) _event.getSource()).getScene().getWindow();
        referenceStage.setScene(sceneToSwitchTo);
        referenceStage.show();
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
}
