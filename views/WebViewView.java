package views;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.json.JSONException;
import controllers.WebViewController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.stage.Stage;

public class WebViewView implements Initializable {
    @FXML
    private WebView webView;

    @FXML
    private AnchorPane mainAnchorPane;
    
    @FXML
    private FontAwesomeIconView itemFoundLoad;
    

    private WebEngine engine;
    private final WebViewController controller = new WebViewController();

    //Just a simple mobile user agent
    private final String USERAGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";
    private final String itemKeyword = "Small Box Tee";
    private final String itemType = "new";
    private final String itemColor = "White";
    private String supremeSessCookie = "";
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    void handleGoogleButtonAction() {
        engine.setUserAgent(this.USERAGENT);
        engine.setJavaScriptEnabled(true);

        engine.load("https://accounts.google.com/");
    }

    @FXML
    void handleYoutubeButtonAction() {
        engine.setUserAgent(this.USERAGENT);
        engine.setJavaScriptEnabled(true);

        engine.load("https://www.youtube.com/");
    }

    @FXML
    void handleSupremeButtonAction() throws MalformedURLException, IOException, JSONException, InterruptedException {
        this.controller.sendSearchItemRequest(this.itemKeyword, this.itemType);
        this.controller.sendClothesRequest(this.itemColor);
        boolean success = this.controller.sendAddToCartRequest();
        if (!success) {
            do {
                success = this.controller.sendAddToCartRequest();
            } while (!success);
        }
        this.supremeSessCookie = this.controller.getSupremeSessCookie();

        engine.setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
        engine.setJavaScriptEnabled(true);

        URI uri = URI.create("https://www.supremenewyork.com/checkout");
        Map<String, List<String>> headers = new LinkedHashMap<>();
        headers.put("Set-Cookie", Arrays.asList(this.supremeSessCookie));
        java.net.CookieHandler.getDefault().put(uri, headers);
        engine.load("https://www.supremenewyork.com/checkout");

        engine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                webView.getEngine().executeScript("javascript:document.getElementById('order_billing_name').value = 'Blue monster';"
                        + "document.getElementById('order_email').value = 'darodriguez56732@gmail.com'; "
                        + "document.getElementById('order_tel').value = '812-233-3333';"
                        + "document.getElementById('bo').value = 'test address';"
                        + "document.getElementById('order_billing_zip').value = '12345';"
                        + "document.getElementById('order_billing_city').value = 'Schectandy';"
                        + "document.getElementById('rnsnckrn').value = '1234 1234 4444 3333';"
                        + "document.getElementById('orcer').value = '006';"
                        + "document.getElementById('credit_card_month').value = '08';"
                        + "document.getElementById('credit_card_year').value = '2024';"
                        + "document.getElementById('order_terms').click();"
                        + "document.getElementsByName('commit')[0].click();");

            }
        });
    }

    @FXML
    public void closeApplication() {
        Stage stage = (Stage) this.mainAnchorPane.getScene().getWindow();
        stage.close();
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    @FXML
    public void switchToBillingInfo(ActionEvent _event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/UserCredentialsFXML.fxml"));
        Parent parentUsingFXML = loader.load();
        Scene sceneToSwitchTo = new Scene(parentUsingFXML);
        Stage referenceStage = (Stage) ((Node) _event.getSource()).getScene().getWindow();
        parentUsingFXML.setOnMousePressed(
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event
            ) {
                xOffset = referenceStage.getX() - event.getScreenX();
                yOffset = referenceStage.getY() - event.getScreenY();
            }
        }
        );

        parentUsingFXML.setOnMouseDragged(
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event
            ) {
                referenceStage.setX(event.getScreenX() + xOffset);
                referenceStage.setY(event.getScreenY() + yOffset);
            }
        }
        );

        referenceStage.setScene(sceneToSwitchTo);

        referenceStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.mainAnchorPane.setBackground(Background.EMPTY);
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        this.engine = this.webView.getEngine();
        engine.setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
        engine.setJavaScriptEnabled(true);

        engine.load("https://www.google.com/");
    }

}
