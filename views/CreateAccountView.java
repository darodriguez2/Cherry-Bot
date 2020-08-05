/**
 * View that corresponds with the create account UI.
 * CONTROLLER: AccountController
 *
 * @author darod
 */
package views;

import Utilities.ViewUtil;
import controllers.AccountController;
import java.io.IOException;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author darod
 */
public class CreateAccountView extends ViewUtil {

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
                if (result) {
                    LoginView view = this.switchScenes(_event, "fxml/LoginFXML.fxml").getController();
                    view.errorLabel.setText("Account creation successful");
                } else {
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
        if (this.username.getText().isEmpty()) {
            return false;
        } else {
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
