package views;

import Utilities.ViewUtility;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;


/**
 *
 * @author darod
 */
public class ProfileView extends ViewUtility implements Initializable{
    
    @FXML private Label profileName;
    @FXML private Label fullName;
    @FXML private Label email;
    @FXML private Label phone;
    @FXML private Label street;
    @FXML private Label zip;
    @FXML private Label city;
    @FXML private Label state;
    
    @FXML private Label cardNumber;
    @FXML private Label month;
    @FXML private Label year;
    @FXML private Label cvv;
    
    @FXML private ListView<String> profileListView;
    
    @FXML private AnchorPane mainAnchorPane;
    
    @FXML public JFXButton profileButton;
    
    @FXML public FontAwesomeIconView profileIcon;
    
    @FXML
    public void closeApplication() {
        this.closeApplication(this.mainAnchorPane);
    }
    
    @FXML
    public void taskButton(ActionEvent _event) throws IOException {
        this.switchToTaskScene(_event, "fxml/MainPageV2.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.profileButton.setTextFill(Paint.valueOf("#ffa2a2"));
        this.profileIcon.setFill(Paint.valueOf("#ffa2a2"));
    }
    
    @FXML
    public void addProfile() {
        this.profileListView = new ListView();
        this.profileListView.getItems().add("test");
    }
    
    
    
}
