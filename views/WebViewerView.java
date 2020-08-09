/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import Utilities.ViewUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author darod
 */
public class WebViewerView extends ViewUtil implements Initializable {

    @FXML
    public WebView webView;
    
    @FXML
    private AnchorPane mainAnchorPane;

    private WebEngine engine;
    private final String USERAGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        this.engine = this.webView.getEngine();
        engine.setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
        engine.setJavaScriptEnabled(true);

        engine.load("https://www.google.com/");
    }
    
    @FXML
    public void handleGoogleButtonAction() {
        engine.setUserAgent(this.USERAGENT);
        engine.setJavaScriptEnabled(true);

        engine.load("https://accounts.google.com/");
    }

    @FXML
    public void handleYoutubeButtonAction() {
        engine.setUserAgent(this.USERAGENT);
        engine.setJavaScriptEnabled(true);

        engine.load("https://www.youtube.com/");
    }
    
    public void closeWindow() {
        this.closeWindow(this.mainAnchorPane);
    }


   

}
