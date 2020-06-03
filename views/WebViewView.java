package views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringJoiner;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import controllers.WebViewController;

public class WebViewView implements Initializable {

    @FXML
    private Button startButton;

    @FXML
    private Button supremeButton;

    @FXML
    private WebView webView;

    private WebEngine engine;
    private final WebViewController controller = new WebViewController();
    
    //Just a simple mobile user agent
    private final String USERAGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";
    private final String itemKeyword = "Silk";
    private final String itemType = "Shirts";
    private final String itemColor = "Peach";
    private String supremeSessCookie = "";
            
    
    
    @FXML
    void handleStartButtonAction(ActionEvent event) {
        engine.setUserAgent(this.USERAGENT);
        engine.setJavaScriptEnabled(true);

        engine.load("https://www.google.com/");
    }

    @FXML
    void handleSupremeButtonAction(ActionEvent event) throws MalformedURLException, IOException, JSONException, InterruptedException {
       
        this.controller.sendSearchItemRequest(this.itemKeyword, this.itemType);
        this.controller.sendClothesRequest(this.itemColor);
        boolean success = this.controller.sendAddToCartRequest();
        if(!success) {
            do {
                success = this.controller.sendAddToCartRequest();
            }while(!success);    
        }
        this.supremeSessCookie = this.controller.getSupremeSessCookie();

        engine.setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
        engine.setJavaScriptEnabled(true);
        
        URI uri = URI.create("https://www.supremenewyork.com/checkout");
        Map<String, List<String>> headers = new LinkedHashMap<String, List<String>>();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        this.engine = this.webView.getEngine();
        engine.setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
        engine.setJavaScriptEnabled(true);

        engine.load("https://www.google.com/");
    }

}
