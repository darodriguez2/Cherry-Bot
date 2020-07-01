/**
 * Implementation of interface methods
 */
package translators;

import Interfaces.SupremeInterface;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SupremeTranslator implements SupremeInterface {

    @Override
    public JSONArray getStock(String _itemType) {
        String mobileStockEndpoint = "https://www.supremenewyork.com/mobile_stock.json";
        JSONObject obj;
        JSONArray jsonArr = null;
        try {
            obj = new JSONObject(fetchGetResponse(mobileStockEndpoint).toString());
            jsonArr = obj.getJSONObject("products_and_categories").getJSONArray(_itemType);
        } catch (IOException | JSONException ex) {
            Logger.getLogger(SupremeTranslator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonArr;
    }

    @Override
    public JSONArray getItemInfo(String _itemID) {
        String itemInfoEndpoint = "https://www.supremenewyork.com/shop/" + _itemID + ".json";
        JSONObject obj;
        JSONArray jsonArr = null;
        try {
            obj = new JSONObject(fetchGetResponse(itemInfoEndpoint).toString());
            jsonArr = obj.getJSONArray("styles");
        } catch (IOException | JSONException ex) {
            Logger.getLogger(SupremeTranslator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonArr;
    }

    @Override
    public Map<String, String> addToCart(String _itemID, String _styleID, String _sizeID) {
        Map<String, String> arguements = new HashMap<>();
        arguements.put("s", _sizeID);
        arguements.put("st", _styleID);
        arguements.put("qty", "1");
        Map<String, String> response = null;
        try {
            response = fetchAddToCartResponse(arguements);
        } catch (IOException ex) {
            Logger.getLogger(SupremeTranslator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    private Map<String, String> fetchAddToCartResponse(Map<String, String> _arguements) throws MalformedURLException, IOException {
        Map<String, String> mapToReturn = new HashMap<>();
        URL url = new URL("https://www.supremenewyork.com/shop/" + _arguements.get("itemID") + "/add.json");

        /**
         * Note: the openConnection() method does not actually establish a network connection.
         * Java Doc for this method explains
         * Therefore we do not have to check for timeout/response codes here
         */
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        StringJoiner sj = new StringJoiner("&");
        for (Map.Entry<String, String> entry : _arguements.entrySet()) {
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

        boolean connected = false;
        int reconnectCount = 0;
        int responseCode;
        con.setConnectTimeout(5000);
        /**
         * Here we want to try to connect within 5 secs before submitting POST request.
         * Once we submit post request we check response code, if not 200 then repeat.
         * We need to ensure that our POST request returns a response code of 200 before
         * trying to read the response body from the input stream. 
         */
        do{
            try{
                con.connect();
            }catch(java.net.SocketTimeoutException e){
                reconnectCount++;
                System.out.println("Connection Timed Out, attemtping to reconnect. Count: " + reconnectCount);
                con.disconnect();
                continue;
            }
            
            con.getOutputStream().write(out);
            responseCode = con.getResponseCode();
            
            if(responseCode == 200) {
                System.out.println("Post request successful, response code: " + responseCode);
                connected = true;
            }
            else {
                reconnectCount++;
                System.out.println("Post request failed, response code: " + responseCode + ". Attempting to reconnect. Count: " + reconnectCount);
                con.disconnect();
            }
        }while(!connected);


        /**
         * To reach this point we must have have gotten response code 200.
         * Now we can read the data from the input stream and return it.
         */
        StringBuffer content = new StringBuffer();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }
        }

        //Also return cookie
        String supremeSessCookie = getSupremeSessCookie(con);
        mapToReturn.put("response", content.toString());
        mapToReturn.put("supremeSessCookie", supremeSessCookie);

        return mapToReturn;
    }

    private String getSupremeSessCookie(HttpURLConnection _con) {
        Map<String, List<String>> responseHeaders = _con.getHeaderFields();
        List<String> cookieList = responseHeaders.get("Set-Cookie");

        //can verify cookie values with for each loop (optional)
//        int i = 1;
//        for (String cookie : cookieList) {
//            System.out.println(i + ". " + cookie);
//            i++;
//        }
        //The supreme session cookie should be the 2nd one in the list.
        String supremeSessCookie = cookieList.get(1);

        return supremeSessCookie;
    }

    private StringBuffer fetchGetResponse(String _endPoint) throws MalformedURLException, IOException {
        URL url = new URL(_endPoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        String inputLine;
        StringBuffer content = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        con.disconnect();
        return content;
    }

}
