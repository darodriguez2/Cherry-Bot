/**
 * LoginView that corresponds with the login UI.
 * CONTROLLER: AccountController
 *
 * @author darod
 */
package views;

import Utilities.User;
import Utilities.ViewUtil;
import controllers.AccountController;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class LoginView extends ViewUtil {

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

    protected String uuid;

    public boolean checkLogin() {
        if (this.username.getText().isEmpty() || this.password.getText().isEmpty()) {
            return false;
        } else {
            String result = this.acController.validateLoginRequest(this.username.getText(), this.password.getText());

            if (result.equals("1")) {
                System.out.println("Incorrect username (login view");
                return false;
            } else if (result.equals("2")) {
                System.out.println("Incorrect password (login view)");
                return false;
            } else if (result.equals("3")) {
                System.out.println("Database error (login view)");
            } else {
                this.uuid = result;
                return true;
            }
        }

        return false;
    }

    @FXML
    public void confirm(ActionEvent _event) throws IOException {
        if (checkLogin()) {
            this.user = new User(this.uuid, this.acController.loadProfileRequest("1"));
            System.out.println(this.user.getUuid());
            for (String field : this.user.getProfiles().keySet()) {
                System.out.println(field + ": " + this.user.getProfiles().get(field));
            }
            this.switchToTaskScene(_event, this.user);

        } else {
            this.errorLabel.setText("Incorrect username/password");
        }
    }

    public void loadProfiles(String _uuid) {
        this.acController.loadProfileRequest(_uuid);
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
