/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.util.Map;
import javafx.scene.web.WebEngine;
import views.WebViewerView;

/**
 *
 * @author darod
 */
public class TaskThread extends Thread {

    Map<String, Object> paymentProfile;
    Map<String, String> taskInfo;
    WebViewerView view;

    private WebEngine engine;
    private final String USERAGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";

    public TaskThread(Map<String, Object> _profile, Map<String, String> _info) {
        this.paymentProfile = _profile;
        this.taskInfo = _info;
    }

    @Override
    public void run() {
        //do stuff
    }

    public void setWebViewer(WebViewerView _view) {
        this.view = _view;
        this.engine = this.view.webView.getEngine();
        engine.setUserAgent(this.USERAGENT);
        engine.setJavaScriptEnabled(true);

        engine.load("https://www.google.com/");

    }

    public void googleLogin() {
        engine.setUserAgent(this.USERAGENT);
        engine.setJavaScriptEnabled(true);

        engine.load("https://accounts.google.com/");
    }

}
