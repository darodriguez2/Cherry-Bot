/**
 * This is our main thread class for performing multi threading.
 * Communicates with the supreme controller to buy products.
 *
 * @author darod
 */
package Utilities;

import controllers.SupremeController;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONException;
import views.WebViewerView;

public class TaskThread extends Thread {
    
    private Map<String, Object> paymentProfile;
    private Map<String, String> taskInfo;
    private WebViewerView view;
    private boolean isOpen = false;
    
    private SupremeController controller = new SupremeController();
    private String supremeSessCookie = "";
    
    private WebEngine engine;
    private TreeItem<TreeTableTask> treeItem;
    private final String USERAGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";
    
    public TaskThread(Map<String, Object> _profile, Map<String, String> _info) {
        this.paymentProfile = _profile;
        this.taskInfo = _info;
        this.view = new WebViewerView();
    }
    
    @Override
    public void run() {
        String itemKeyword = this.taskInfo.get("itemKeyword");
        String itemType = this.taskInfo.get("itemType");

        //These 2 for loops are just for testing purposes.
        for (String paymentID : this.paymentProfile.keySet()) {
            System.out.println(this.getId() + " = " + paymentID + ": " + this.paymentProfile.get(paymentID));
        }
        
        for (String taskInfo : this.taskInfo.keySet()) {
            System.out.println(this.getId() + " = " + taskInfo + ": " + this.taskInfo.get(taskInfo));
        }

        /**
         * NOTE: This block of code is what we want the thread to do when run.
         * This is what actually performs the process of buying items on Supreme
         * website. The Supreme Website is currently OFFLINE until the start of
         * the next season, so this block of code is commented out.
         */
//        try {
//
//            this.controller.sendSearchItemRequest(itemKeyword, itemType);
//            if (itemType.equals("Clothing")) {
//                String itemColor = this.taskInfo.get("Color");
//                this.controller.sendClothesRequest(itemColor);
//            }
//
//            boolean success = this.controller.sendAddToCartRequest();
//            if (!success) {
//                do {
//                    success = this.controller.sendAddToCartRequest();
//                } while (!success);
//            }
//            this.supremeSessCookie = this.controller.getSupremeSessCookie();
//
//            engine.setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
//            engine.setJavaScriptEnabled(true);
//
//            URI uri = URI.create("https://www.supremenewyork.com/checkout");
//            Map<String, List<String>> headers = new LinkedHashMap<>();
//            headers.put("Set-Cookie", Arrays.asList(this.supremeSessCookie));
//            java.net.CookieHandler.getDefault().put(uri, headers);
//            engine.load("https://www.supremenewyork.com/checkout");
//
//            String name = this.paymentProfile.get("fullName").toString();
//            String email = this.paymentProfile.get("email").toString();
//            String phone = this.paymentProfile.get("phone").toString();
//            String street = this.paymentProfile.get("street").toString();
//            String zip = this.paymentProfile.get("zip").toString();
//            String city = this.paymentProfile.get("city").toString();
//            String cardNumber = this.paymentProfile.get("cardNumber").toString();
//            String cvv = this.paymentProfile.get("cvv").toString();
//            String month = this.paymentProfile.get("month").toString();
//            String year = this.paymentProfile.get("year").toString(); //NOTE: year is a string of length 2. Supreme site might require 4, may need to append to 20xx.
//
//            engine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
//                if (newState == Worker.State.SUCCEEDED) {
//                    this.engine.executeScript("javascript:document.getElementById('order_billing_name').value = '" + name + "';"
//                            + "document.getElementById('order_email').value = '" + email + "'; "
//                            + "document.getElementById('order_tel').value = '" + phone + "';"
//                            + "document.getElementById('bo').value = '" + street + "';"
//                            + "document.getElementById('order_billing_zip').value = '" + zip + "';"
//                            + "document.getElementById('order_billing_city').value = '" + city + "';"
//                            + "document.getElementById('rnsnckrn').value = '" + cardNumber + "';"
//                            + "document.getElementById('orcer').value = '" + cvv + "';"
//                            + "document.getElementById('credit_card_month').value = '" + month + "';"
//                            + "document.getElementById('credit_card_year').value = '" + year + "';");
//
//                }
//            });
//        } catch (InterruptedException | JSONException | IOException ex) {
//            Logger.getLogger(TaskThread.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
    
    public void googleLogin(TreeItem<TreeTableTask> _treeItem) throws IOException {
        this.treeItem = _treeItem;
        if (!this.isOpen) {
            openWebView();        
            setUpEngine();
            this.isOpen = true;
        }
        
        engine.load("https://accounts.google.com/");
    }
    
    @FXML
    public void watchYoutube(TreeItem<TreeTableTask> _treeItem) throws IOException {
        this.treeItem = _treeItem;
        if (!this.isOpen) {
            openWebView();   
            setUpEngine();
            this.isOpen = true;
        }
        engine.load("https://www.youtube.com/");
    }
    
    public Map<String, Object> getPaymentProfile() {
        return this.paymentProfile;
    }
    
    public Map<String, String> getTaskInfo() {
        return this.taskInfo;
    }
    
    public void setUpEngine() {
        this.engine = this.view.webView.getEngine();
        this.engine.setUserAgent(this.USERAGENT);
        this.engine.setJavaScriptEnabled(true);
    }
    
    public void webViewClosed() {
        this.isOpen = false;
        this.treeItem.getValue().getStatus().set("Ready to start");
    }
    
    public void setTreeTableItem(TreeItem<TreeTableTask> _treeItem) {
        this.treeItem = _treeItem;
    }

    //////////////////////WEBVIEW METHODS//////////////////////////////////
    /**
     * This looks sloppy but its necessary. These are methods from the view util
     * that are needed to open the web view scene and to make it moveable.
     * Because this class already extends Thread, it cannot also extend viewUtil
     * so there was no choice but to just copy and paste them here.
     */
    public void openWebView() throws IOException {
        this.view.setTaskThread(this);
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/WebViewerFXML.fxml"));
        loader.setController(this.view);
        Parent parent = loader.load();
        
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        
        makeStageMovable(parent, stage);
        
        stage.setScene(new Scene(parent));
        stage.show();
    }
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    public void makeStageMovable(Parent _parent, Stage _stage) {
        _parent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = _stage.getX() - event.getScreenX();
                yOffset = _stage.getY() - event.getScreenY();
            }
        });
        
        _parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                _stage.setX(event.getScreenX() + xOffset);
                _stage.setY(event.getScreenY() + yOffset);
            }
        });
    }
    
}
