package views;

import Utilities.ViewUtil;
import com.jfoenix.controls.JFXButton;
import com.sun.prism.paint.Color;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
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
    private ListView<String> profileListView = new ListView<>();

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

    //For performing operations on all textfields by looping through array.
    private TextField[] textFields;

    @FXML
    public void closeApplication() {
        this.closeApplication(this.mainAnchorPane);
    }

    @FXML
    public void taskButton(ActionEvent _event) throws IOException {
        this.switchToTaskScene(_event);
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
        System.out.println("Adding Profile");
        this.profileListView.getItems().add("test");
        
        AddProfileView view = this.openPopupWindow(_event, "fxml/AddProfileFXML.fxml").getController();
        view.setListView(this.profileListView);
    }

    
    /**
     * This method is for setting all text fields to read only.
     * For some reason I can only change the background color of 
     * the text fields through a css, so that's why the style class
     * method is used.
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
     * This method is for setting all text fields to an editable state.
     * For some reason I can only change the background color of 
     * the text fields through a css, so that's why the style class
     * method is used.
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
        //Send data to AWS and update list view here
    }
    
    @FXML 
    public void cancelEdit() {
        readOnlyTextFields();
        //Set text on textfields back to their original values
    }
    
    @FXML
    public void deleteProfile() {
        String selectedItem = this.profileListView.getSelectionModel().getSelectedItem();
        this.profileListView.getItems().remove(selectedItem);
        this.profileListView.getSelectionModel().clearSelection();
    }
    

}
