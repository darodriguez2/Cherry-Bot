/**
 * AddProfileView that corresponds with the addTask UI.
 * CONTROLLER: ProfileController
 * @author darod
 */
package views;

import Utilities.ViewUtil;
import com.jfoenix.controls.JFXButton;
import controllers.ProfileController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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

    @FXML
    public JFXButton addButton;

    private TextField[] textFields;

    private ProfileController pc = new ProfileController();
    private ProfileView pv = new ProfileView();

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

    public void addProfile() throws IOException {
        String checkFieldsResult = checkFields();
        if (!checkFieldsResult.equals("PASS")) {
            this.errorLabel.setText(checkFieldsResult);
        } else {
            Map<String, String> profileInfo = new HashMap<>();
            for (TextField textField : textFields) {
                profileInfo.put(textField.getId(), textField.getText());
            }
            profileInfo.put(this.state.getId(), this.state.getValue().toString());
            profileInfo.put("uuid", this.user.getUuid());
            Boolean addProfileResult = this.pc.addProfileRequest(profileInfo);
            if (addProfileResult) {
                this.profileListView.getItems().add(this.profileName.getText());
                this.pv.reloadProfiles();
                this.closeWindow(this.mainAnchorPane);
            }
            else
                this.errorLabel.setText("Profile name already exists, please use a different one.");
        }
    }

    public void setListView(ListView _listView) {
        this.profileListView = _listView;
    }

    public void setProfileView(ProfileView _view) {
        this.pv = _view;
    }

    public String checkFields() {
        //check for any blank fields
        for (TextField textField : textFields) {
            if (textField.getText().equals("") || textField.getText().isEmpty()) {
                return "Cannot leave any fields blank";
            }
        }
        if (this.zip.getText().length() != 5) {
            return "Invalid Zip";
        } else if (this.month.getText().length() != 2 || this.year.getText().length() != 2) {
            return "Month and year must be 2 digits";
        } else if (this.cvv.getText().length() != 3) {
            return "Invalid cvv, must be 3 digits";
        } else if (this.cardNumber.getText().length() != 16) {
            return "Invalid card number, please make sure there are no spaces"; //NOTE: issue with JSONObject class if a string has spaces!!! will throw error!
        } else if (!this.email.getText().contains("@")) {
            return "Invalid email";
        } else {
            return "PASS";
        }
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
