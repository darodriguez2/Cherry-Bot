package views;

import Utilities.User;
import Utilities.ViewUtil;
import com.jfoenix.controls.JFXButton;
import com.sun.prism.paint.Color;
import controllers.ProfileController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;

/**
 *
 * @author darod
 */
public class ProfileView extends ViewUtil implements Initializable {

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
    private TextField state;

    @FXML
    private TextField cardNumber;
    @FXML
    private TextField month;
    @FXML
    private TextField year;
    @FXML
    private TextField cvv;

    @FXML
    public ListView<String> profileListView = new ListView<>();

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    public JFXButton profileButton;
    @FXML
    private JFXButton confirmEdit;
    @FXML
    private JFXButton cancelEdit;

    @FXML
    public FontAwesomeIconView profileIcon;

    @FXML
    private Label errorLabel;

    //For performing operations on all textfields by looping through array.
    private TextField[] textFields;

    private ProfileController pc = new ProfileController();

    @FXML
    public void closeApplication() {
        this.closeApplication(this.mainAnchorPane);
    }

    @FXML
    public void taskButton(ActionEvent _event) throws IOException {
        this.switchToTaskScene(_event, this.user);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.profileButton.setTextFill(Paint.valueOf("#ffa2a2"));
        this.profileIcon.setFill(Paint.valueOf("#ffa2a2"));
        textFields = new TextField[]{this.profileName, this.fullName, this.email, this.phone, this.street, this.zip, this.city, this.state, this.cardNumber, this.month, this.year, this.cvv};
        readOnlyTextFields();

        Label placeholder = new Label();
        placeholder.getStyleClass().add("placeholder");
        placeholder.setText("Add a profile");
        this.profileListView.setPlaceholder(placeholder);

    }

    @FXML
    public void addProfile(ActionEvent _event) throws IOException {
        System.out.println("Opening Popup Window");
        //Important: we pass this instance of the profile view by passing "this" as a parameter.
        AddProfileView view = this.openAddProfileWindow(_event, "fxml/AddProfileFXML.fxml", this).getController();
        view.setListView(this.profileListView);
        view.user = this.user;
    }

    public void reloadProfiles() {
        System.out.println("reloading profiles");
        this.user = new User(this.user.getUuid(), this.pc.loadProfileRequest(this.user.getUuid()));
        this.profileListView.getItems().clear();
        for (String field : this.user.getProfiles().keySet()) {
            this.profileListView.getItems().add(field);
        }
    }

    /**
     * This method is for setting all text fields to read only. For some reason
     * I can only change the background color of the text fields through a css,
     * so that's why the style class method is used.
     */
    @FXML
    public void readOnlyTextFields() {
        for (TextField textField : textFields) {
            textField.getStyleClass().removeAll("editableFields");
            textField.getStyleClass().add("uneditableFields");
            textField.setStyle("-fx-text-inner-color: white");
            textField.editableProperty().set(false);
        }
        this.confirmEdit.setDisable(true);
        this.confirmEdit.setVisible(false);
        this.cancelEdit.setDisable(true);
        this.cancelEdit.setVisible(false);
    }

    /**
     * This method is for setting all text fields to an editable state. For some
     * reason I can only change the background color of the text fields through
     * a css, so that's why the style class method is used.
     */
    @FXML
    public void editProfile() {
        for (TextField textField : textFields) {
            textField.getStyleClass().removeAll("uneditableFields");
            textField.getStyleClass().add("editableFields");
            textField.setStyle("-fx-text-inner-color: black");
            textField.editableProperty().set(true);
        }
        this.confirmEdit.setDisable(false);
        this.confirmEdit.setVisible(true);
        this.cancelEdit.setDisable(false);
        this.cancelEdit.setVisible(true);
    }

    @FXML
    public void confirmEdit() {
        readOnlyTextFields();
        if (!checkFields().equals("PASS")) {
            this.errorLabel.setText(checkFields());
        } else {
            String selectedProfile = this.profileListView.getSelectionModel().getSelectedItem();
            Map<String, String> profileInfo = new HashMap<>();
            for (TextField textField : textFields) {
                profileInfo.put(textField.getId(), textField.getText());
            }
            profileInfo.put("uuid", this.user.getUuid());
            this.pc.deleteProfileRequest(this.user.getUuid(), selectedProfile);
            this.pc.addProfileRequest(profileInfo);
            reloadProfiles();
            this.errorLabel.setText("Profile Successfully Updated");
        }
    }

    @FXML
    public void cancelEdit() {
        readOnlyTextFields();
        profileSelected();
        reloadProfiles();
    }

    @FXML
    public void deleteProfile() {
        String selectedItem = this.profileListView.getSelectionModel().getSelectedItem();
        this.pc.deleteProfileRequest(this.user.getUuid(), selectedItem);
        reloadProfiles();
        profileSelected();
    }

    @FXML
    public void profileSelected() {
        String selectedProfile = this.profileListView.getSelectionModel().getSelectedItem();
        if (selectedProfile == null) {
            for (TextField textField : textFields) {
                textField.setText("");
            }
        } else {
            Map<String, Object> profileInfo = this.user.getProfiles().get(selectedProfile);
            for (TextField textField : textFields) {
                textField.setText(profileInfo.get(textField.getId()).toString());
            }
        }
        this.errorLabel.setText("");
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

}
