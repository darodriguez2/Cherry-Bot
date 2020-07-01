/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import Utilities.ViewUtility;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author darod
 */
public class CreateAccountView extends ViewUtility {

    @FXML
    TextField username;

    @FXML
    TextField password;

    @FXML
    TextField confirmPassword;

    @FXML
    Label errorLabel;

    @FXML
    AnchorPane mainAnchorPane;

    public void confirm(ActionEvent _event) throws IOException {
        if (checkPassword()) {
            LoginView view = this.switchScenes(_event, "fxml/LoginFXML.fxml").getController();
            view.errorLabel.setText("Account creation successful");
        } else {
            this.errorLabel.setText("Passwords do not match");
        }
    }

    public boolean checkPassword() {
        if (!this.password.getText().equals(this.confirmPassword.getText())) {
            return false;
        }
        return true;
    }

    public boolean checkUsername() {
        //here we want to check if the username already exists in the database
        return true;
    }

    public void back(ActionEvent _event) throws IOException {
        this.switchScenes(_event, "fxml/LoginFXML.fxml");
    }

    @FXML
    public void exit() {
        this.closeApplication(this.mainAnchorPane);
    }
}
