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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;

/**
 *
 * @author darod
 */
public class LoginView extends ViewUtility {
    
    @FXML
    private TextField username;
    
    @FXML
    private TextField password;
    
    @FXML
    protected Label errorLabel;
    
    @FXML
    private Label forgotPassword;
    
    @FXML
    private AnchorPane mainAnchorPane;
       
    private AccountController acController = new AccountController();
    
    public boolean checkLogin() {
        if (this.username.getText().isEmpty() || this.password.getText().isEmpty()) {
            return false;
        } else {
            String result = this.acController.validateLoginRequest(this.username.getText(), this.password.getText());
            
            if(result.equals("1")) {
                System.out.println("Incorrect username (login view");
                return false;
            }
            else if(result.equals("2")) {
                System.out.println("Incorrect password (login view)");
                return false;
            }
            else if(result.equals("3")) {
                System.out.println("Database error (login view)");
            }
            else{
                return true;
            }
        }
        
        return false;
    }
    
    @FXML
    public void confirm(ActionEvent _event) throws IOException {
        if (checkLogin()) {
            this.switchToTaskScene(_event);
        } else {
            this.errorLabel.setText("Incorrect username/password");
        }
    }
    
    public void loadProfiles() {
        //grab all user profiles from aws and store in...Map?
        //idea is to load them at this point so they don't have to be loaded again
        //unless the user adds a new profile. 
        //only want to exectue if checkLogin returns a valid UUID
    }
    
    @FXML
    public void createAccount(ActionEvent _event) throws IOException {
        this.switchScenes(_event, "fxml/CreateAccountFXML.fxml");
    }
    
    @FXML
    public void exit() {
        this.closeApplication(this.mainAnchorPane);
    }
    
    
}
