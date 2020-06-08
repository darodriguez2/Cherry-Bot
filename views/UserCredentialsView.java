/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author diego
 */
public class UserCredentialsView implements Initializable{
    
    @FXML
    private AnchorPane mainAnchorPane;
    
    @FXML
    private JFXComboBox month;
    
    @FXML
    private JFXComboBox year;
    
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    @FXML
    public void closeApplication() {
        Stage stage = (Stage) this.mainAnchorPane.getScene().getWindow();
        stage.close();
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }
    
    @FXML
    public void switchToMainScene(ActionEvent _event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/MainPageFXML.fxml"));
        Parent parentUsingFXML = loader.load();
        Scene sceneToSwitchTo = new Scene(parentUsingFXML);
        Stage referenceStage = (Stage) ((Node) _event.getSource()).getScene().getWindow();
        parentUsingFXML.setOnMousePressed(
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event
            ) {
                xOffset = referenceStage.getX() - event.getScreenX();
                yOffset = referenceStage.getY() - event.getScreenY();
            }
        }
        );

        parentUsingFXML.setOnMouseDragged(
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event
            ) {
                referenceStage.setX(event.getScreenX() + xOffset);
                referenceStage.setY(event.getScreenY() + yOffset);
            }
        }
        );

        referenceStage.setScene(sceneToSwitchTo);

        referenceStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       this.month.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
       this.year.getItems().addAll("2020", "2021", "2022", "2023", "2024", "2025", "2026", "2028");
               
    }
}
