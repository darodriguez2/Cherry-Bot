/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import Utilities.ViewUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author darod
 */
public class AddProfileView extends ViewUtil implements Initializable {

    @FXML
    private TextField profileName;
    @FXML
    private TextField fullName;
    @FXML
    private TextField email;
    @FXML
    private TextField phone;
    @FXML
    private TextField street;
    @FXML
    private TextField zip;
    @FXML
    private TextField city;

    @FXML
    private ComboBox state;

    @FXML
    private TextField cardNumber;
    @FXML
    private TextField month;
    @FXML
    private TextField year;
    @FXML
    private TextField cvv;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    public ListView profileListView;
    
    @FXML
    private Label errorLabel;
    
    private TextField[] textFields;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            addStatesToComboBox();
        } catch (IOException ex) {
            Logger.getLogger(AddProfileView.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.textFields = new TextField[]{this.profileName, this.fullName, this.email, this.phone, this.street, this.zip, this.city, this.cardNumber, this.month, this.year, this.cvv};
    }

    public void cancelAddProfile() {
        this.closeWindow(this.mainAnchorPane);
    }
    
    public void addProfile() {
        String result = checkFields();
        if(!result.equals("PASS"))
            this.errorLabel.setText(result);
        else {
            this.profileListView.getItems().add(this.profileName.getText());
            this.closeWindow(this.mainAnchorPane);
        }
    }

    public void setListView(ListView _listView) {
        this.profileListView = _listView;
    }

    public String checkFields() {
        //check for any blank fields
        for (TextField textField : textFields) {
            if(textField.getText().equals("") || textField.getText().isEmpty()) {
                return "Cannot leave any fields blank";
            }
        }
        if(this.zip.getText().length() != 5)
            return "Invalid Zip";
        else if(this.month.getText().length() != 2 || this.year.getText().length() != 2)
            return "Month and year must be 2 digits";
        else if(this.cvv.getText().length() != 3)
            return "Invalid cvv, must be 3 digits";
        else if(this.cardNumber.getText().length() != 19) 
            return "Invalid card number";
        else if(!this.email.getText().contains("@"))
            return "Invalid email";
        else return "PASS";
    }   

    public void addStatesToComboBox() throws FileNotFoundException, IOException {
        File file = new File("C:\\Users\\darod\\Documents\\NetBeansProjects\\Cherry_V1\\src\\Artifacts\\fiftyStates.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null) {
            this.state.getItems().addAll(st);
        }
    }
}
