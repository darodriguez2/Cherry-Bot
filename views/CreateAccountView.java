/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import Utilities.ViewUtility;
import controllers.AccountController;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

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
    
    private AccountController acController = new AccountController();
    
    public void confirm(ActionEvent _event) throws IOException {
        if (checkUsername()) {
            if (checkPassword()) {
                UUID uuid = UUID.randomUUID();
                Boolean result = this.acController.createAccountRequest(this.username.getText(), this.password.getText(), uuid.toString());
                if(result) {
                  LoginView view = this.switchScenes(_event, "fxml/LoginFXML.fxml").getController();  
                  view.errorLabel.setText("Account creation successful");
                }
                else {
                    System.out.println("Error Creating Account");
                }
            } else {
                this.errorLabel.setText("Passwords do not match");
            }
        } else {
            this.errorLabel.setText("Username already taken");
        }
    }

    public boolean checkPassword() {
        if (!this.password.getText().equals(this.confirmPassword.getText()) || this.password.getText().isEmpty() || this.confirmPassword.getText().isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean checkUsername() {
        if(this.username.getText().isEmpty()) {
            return false;
        }
        else {
            return this.acController.checkUsernameRequest(this.username.getText());
        }      
    }

    public void back(ActionEvent _event) throws IOException {
        this.switchScenes(_event, "fxml/LoginFXML.fxml");
    }

    @FXML
    public void exit() {
        this.closeApplication(this.mainAnchorPane);
    }
}
