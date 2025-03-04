/**
 * WebViewerView that corresponds with the WebViewer UI.
 * NOTE: An instance of this class is created whenever a new task thread is created.
 * The purpose of this class is to allow the task thread instance to control the
 * web view through this class.
 *
 * @author darod
 */
package views;

import Utilities.TaskThread;
import Utilities.ViewUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WebViewerView extends ViewUtil implements Initializable {

    @FXML
    public WebView webView;

    @FXML
    private AnchorPane mainAnchorPane;

    private WebEngine engine;
    private final String USERAGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";
    private TaskThread taskThread;

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
        this.taskThread.webViewClosed();
        this.closeWindow(this.mainAnchorPane);
    }
    
    public void setTaskThread(TaskThread _taskThread) {
        this.taskThread = _taskThread;
    }

}
