/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import Utilities.ViewUtility;
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
    
    private DynamoDbClientBuilder builder2 = DynamoDbClient.builder();
    private DynamoDbClient client = builder2.build();
    
    public boolean checkLogin() {
        if (this.username.getText().isEmpty()) {
            return false;
        } else {
            Map<String, AttributeValue> keyToGet = new HashMap<>();
            keyToGet.put("Username", AttributeValue.builder().s(this.username.getText()).build());
            GetItemRequest request = GetItemRequest.builder().key(keyToGet).tableName("LoginCredentials").build();
            try {
                Map<String, AttributeValue> returnedItem = this.client.getItem(request).item();

                if (returnedItem.isEmpty()) {
                    System.out.println("Incorrect Username");
                    return false;
                } else {
                    String username = returnedItem.get("Username").s();
                    String accountPassword = returnedItem.get("Password").s();
                    System.out.println(accountPassword);
                    if (!this.password.getText().equals(accountPassword)) {
                        System.out.println("Incorrect Password");
                        System.out.println("Password entered: " + this.password.getText() + "does not match password in db: " + accountPassword);
                        return false;
                    }
                    String uuid = returnedItem.get("UUID").s();
                    System.out.println(uuid);
                    return true;
                }
            } catch (DynamoDbException e) {
                System.out.println("EXCEPTION AT USERNAME");
                System.err.println(e.getMessage());
                System.exit(1);
            }
            System.out.println("Unable to submit request.");
            return false;
        }
    }
    
    @FXML
    public void confirm(ActionEvent _event) throws IOException {
        if (checkLogin()) {
            this.switchToTaskScene(_event);
        } else {
            this.errorLabel.setText("Incorrect Login");
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
