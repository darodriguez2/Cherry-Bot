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
    
    public void checkLogin() {
        //Grab username and password, then check them against aws to see if an account exists. 
        //This should return a UUID
    }
    
    @FXML
    public void confirm(ActionEvent _event) throws IOException {
        checkLogin();
        this.switchToTaskScene(_event);
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
