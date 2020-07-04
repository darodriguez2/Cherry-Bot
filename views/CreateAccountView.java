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

    private DynamoDbClientBuilder builder2 = DynamoDbClient.builder();
    private DynamoDbClient client = builder2.build();
    
    public void confirm(ActionEvent _event) throws IOException {
        if (checkUsername()) {
            if (checkPassword()) {
                UUID uuid = UUID.randomUUID();
                Map<String, AttributeValue> itemAttributes = new HashMap<>();
                itemAttributes.put("Username", AttributeValue.builder().s(this.username.getText()).build());
                itemAttributes.put("Password", AttributeValue.builder().s(this.password.getText()).build());
                itemAttributes.put("UUID", AttributeValue.builder().s(uuid.toString()).build());
                PutItemRequest request = PutItemRequest.builder().tableName("LoginCredentials").item(itemAttributes).build();
                try {
                    client.putItem(request);
                    System.out.println( " was successfully updated");

                } catch (ResourceNotFoundException e) {
                    System.err.format("Error: The table \"%s\" can't be found.\n", "LoginCredentials");
                    System.err.println("Be sure that it exists and that you've typed its name correctly!");
                    System.exit(1);
                } catch (DynamoDbException e) {
                    System.err.println(e.getMessage());
                    System.exit(1);
                }
                LoginView view = this.switchScenes(_event, "fxml/LoginFXML.fxml").getController();
                view.errorLabel.setText("Account creation successful");
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
        Map<String, AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put("Username", AttributeValue.builder().s(this.username.getText()).build());
        GetItemRequest request = GetItemRequest.builder().key(keyToGet).tableName("LoginCredentials").build();
        try {
            Map<String, AttributeValue> returnedItem = this.client.getItem(request).item();

            if (returnedItem.isEmpty()) {
                System.out.println("Username does not exist");
                return true;
            } else {
                System.out.println("Username exists");
                return false;
            }
        } catch (DynamoDbException e) {
            System.out.println("EXCEPTION AT USERNAME");
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Unable to submit request.");
        return false;
    }

    public void back(ActionEvent _event) throws IOException {
        this.switchScenes(_event, "fxml/LoginFXML.fxml");
    }

    @FXML
    public void exit() {
        this.closeApplication(this.mainAnchorPane);
    }
}
