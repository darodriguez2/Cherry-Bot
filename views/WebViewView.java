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

public class WebViewView implements Initializable {

    @FXML
    private Button startButton;

    @FXML
    private Button supremeButton;

    @FXML
    private WebView webView;

    WebEngine engine;
    
    //Just a simple mobile user agent
    final private String USERAGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";
    final private String MOBILE_STOCK = "https://www.supremenewyork.com/mobile_stock.json";
    
    
    @FXML
    void handleStartButtonAction(ActionEvent event) {
        engine.setUserAgent(this.USERAGENT);
        engine.setJavaScriptEnabled(true);

        engine.load("https://www.google.com/");
    }

    @FXML
    void handleSupremeButtonAction(ActionEvent event) throws MalformedURLException, IOException, JSONException, InterruptedException {
        URL url99 = new URL(this.MOBILE_STOCK);
        HttpURLConnection con99 = (HttpURLConnection) url99.openConnection();

        //parse data from stringBuffer object using JSONObject
        boolean itemFound = false;
        String inputLine99;
        StringBuffer content99 = new StringBuffer();
        BufferedReader in99;
        JSONObject obj;
        JSONArray jsonArr;
        String itemID;
        String itemName = "Waves";
        int reconnectCount = 0;
        do {
            con99.setRequestMethod("GET");
            con99.connect();
            reconnectCount++;
            in99 = new BufferedReader(new InputStreamReader(con99.getInputStream()));

            while ((inputLine99 = in99.readLine()) != null) {
                content99.append(inputLine99);
            }

            obj = new JSONObject(content99.toString());

            jsonArr = obj.getJSONObject("products_and_categories").getJSONArray("Accessories");
            itemID = "";
            try {
                for (int i = 0; i < jsonArr.length(); i++) {
                    if (jsonArr.getJSONObject(i).getString("name").contains(itemName)) {
                        itemID = jsonArr.getJSONObject(i).get("id").toString();
                        System.out.println("Item found. Item ID: " + itemID);
                        itemFound = true;
                        break;
                    }
                }
            } catch (JSONException e) {

            }
            System.out.println("Product not found, reconnect count: " + reconnectCount);
            con99.disconnect();
            Thread.sleep((long) 1000);
            con99 = (HttpURLConnection) url99.openConnection();
        } while (!itemFound);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Item found at: " + dtf.format(now));

        url99 = new URL("https://www.supremenewyork.com/shop/" + itemID + ".json");
        con99 = (HttpURLConnection) url99.openConnection();

        in99 = new BufferedReader(new InputStreamReader(con99.getInputStream()));
        String inputLine98;
        StringBuffer content98 = new StringBuffer();
        while ((inputLine98 = in99.readLine()) != null) {
            content98.append(inputLine98);
        }

        obj = new JSONObject(content98.toString());
        jsonArr = obj.getJSONArray("styles");
        String styleID = "";
        String sizeID = "";
        //////////////////////////////////////////FOR CLOTHES/////////////////////////////////////////////////////////////////
//        for (int i = 0; i < jsonArr.length(); i++) {
//            if (jsonArr.getJSONObject(i).getString("name").equals("Black")) {
//                styleID = jsonArr.getJSONObject(i).get("id").toString();
//                JSONArray jsonArr2 = jsonArr.getJSONObject(i).getJSONArray("sizes");
//                sizeID = jsonArr2.getJSONObject(1).get("id").toString();
//                System.out.println("Style ID: " + styleID);
//                System.out.println("Size ID: " + sizeID);
//                break;
//            }
//        }
        ////////////////////////////////////////FOR ACCESSORIES/////////////////////////////////////////////////////////////////////
        styleID = jsonArr.getJSONObject(0).get("id").toString();
        JSONArray jsonArr2 = jsonArr.getJSONObject(0).getJSONArray("sizes");
        sizeID = jsonArr2.getJSONObject(0).get("id").toString();
        System.out.println("Style ID: " + styleID);
        System.out.println("Size ID: " + sizeID);

        /////////////////////////////////////////////////////////////////ITEM IDS FOUND////////////////////////////////////////////////////////////////////////////////////
        URL url = new URL("https://www.supremenewyork.com/shop/" + itemID + "/add.json");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);

        Map<String, String> arguments = new HashMap<>();
        arguments.put("s", sizeID);
        arguments.put("st", styleID);
        arguments.put("qty", "1");

        StringJoiner sj = new StringJoiner("&");
        for (Map.Entry<String, String> entry : arguments.entrySet()) {
            sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                    + URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        con.setFixedLengthStreamingMode(length);
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("accept-language", "en-US,en;q=0.9");
        con.setRequestProperty("x-requested-with", "XMLHttpRequest");
        con.setRequestProperty("content-type", "application/x-www-form-urlencoded");
        con.setRequestProperty("origin", "https://www.supremenewyork.com");
        con.setRequestProperty("referer", "https://www.supremenewyork.com/mobile/");
        con.setRequestProperty("Pragma", "no-cahce");
        con.setRequestProperty("Cache-Control", "no-cache");
        con.setRequestProperty("TE", "Trailers");

        con.connect();
        try (OutputStream os = con.getOutputStream()) {
            os.write(out);
        }

        StringBuilder content;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String line;
            content = new StringBuilder();
            while ((line = br.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }
        }

        System.out.println(content.toString());

        Map<String, List<String>> responseHeaders = con.getHeaderFields();
        List<String> listCookieHeaders = responseHeaders.get("Set-Cookie");
        System.out.println(listCookieHeaders.size());
        System.out.println(responseHeaders.size());
        int i = 1;
        for (String cookie : listCookieHeaders) {
            System.out.println(i + ". " + cookie);
            i++;
        }
        String cookie = listCookieHeaders.get(0);
        String cookie2 = listCookieHeaders.get(1);
        String cookie3 = listCookieHeaders.get(2);
        String cookie4 = listCookieHeaders.get(3);

        /**
         * IMPORTANT!! This part is verifying the item is in the cart. Because
         * we are opening a new connection, we need to set the cookie from the
         * previous connection, in order to maintain the same session. The
         * cookie is called "_supreme_sess" it is the second cookie in the list
         * of cookie headers.
         *
         */
        URL url2 = new URL("https://www.supremenewyork.com/shop/cart.json");
        con = (HttpURLConnection) url2.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Cookie", cookie2);
        con.connect();

        StringBuilder content2;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String line;
            content2 = new StringBuilder();
            while ((line = br.readLine()) != null) {
                content2.append(line);
                content2.append(System.lineSeparator());
            }
        }

        System.out.println(content2.toString());
        con.disconnect();

        engine.setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
        engine.setJavaScriptEnabled(true);

        URI uri = URI.create("https://www.supremenewyork.com/checkout");
        Map<String, List<String>> headers = new LinkedHashMap<String, List<String>>();
        headers.put("Set-Cookie", Arrays.asList(cookie2));
        java.net.CookieHandler.getDefault().put(uri, headers);
        engine.load("https://www.supremenewyork.com/checkout");

        engine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {

                HTMLDocument doc = (HTMLDocument) engine.getDocument();

                Element body = doc.getBody();
                NodeList formTags = body.getElementsByTagName("FORM");
                Element test = (Element) formTags.item(0);
                NodeList divTagsinBody = test.getElementsByTagName("DIV");

                NodeList formList = formTags.item(0).getChildNodes();

                Element firstNameField = (Element) divTagsinBody.item(7);
                //System.out.println(firstNameField.getAttributes().item(0).getNodeName());
                //firstNameField.setAttribute("order_billing_name", "Bob BObbbb");
                webView.getEngine().executeScript("javascript:document.getElementById('order_billing_name').value = 'Diego Rodriguez';"
                        + "document.getElementById('order_email').value = 'darodriguez56735@gmail.com'; "
                        + "document.getElementById('order_tel').value = '919-753-7496';"
                        + "document.getElementById('bo').value = '116 Otter Crest Way';"
                        + "document.getElementById('order_billing_zip').value = '27549';"
                        + "document.getElementById('order_billing_city').value = 'Holly Springs';"
                        + "document.getElementById('rnsnckrn').value = '5491 1299 8876 7721';"
                        + "document.getElementById('orcer').value = '006';");

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
