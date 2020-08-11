/**
 * Another layer of abstraction between the adapter and controller.
 * Used to interpret results from adapter, before handing appropriate information to controller.
 * @author darod
 */
package Models;

import Adapters.SupremeAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SupremeModel {

    private final SupremeAdapter adapter = new SupremeAdapter();
    private String itemID;
    private String styleID;
    private String sizeID;
    private String supremeSessCookie;
    private boolean result = false;

    public boolean searchForItem(String _itemKeyword, String _itemType) throws InterruptedException {
        String itemID = "NOT FOUND";
        int reconnectCount = 0;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        System.out.println("Getting stock and searching for item. Time: " + dtf.format(LocalDateTime.now()));
        /**
         * This loop will execute until the item is found. Iterates (1/sec) If
         * the item is never found, it will loop 600 times. (about 10 mins) The
         * idea is to keep sending requests to the endpoint until the page loads
         * with the item we want. Ideally we want to start this 30-60s before
         * drop time.
         */
        do {
            JSONArray jsonArr = this.adapter.getStock(_itemType);
            try {
                for (int i = 0; i < jsonArr.length(); i++) {
                    if (jsonArr.getJSONObject(i).getString("name").contains(_itemKeyword)) {
                        itemID = jsonArr.getJSONObject(i).get("id").toString();
                        System.out.println("Item found. Item ID: " + itemID + ". Time: " + dtf.format(LocalDateTime.now()));
                        this.itemID = itemID;
                        this.result = true;
                        break;
                    }
                }
            } catch (JSONException e) {

            }
            if (itemID.equals("NOT FOUND")) {
                reconnectCount++;
                System.out.println("Item not found. Attempting to reconnect. Reconnect count: " + reconnectCount + ". Time: " + dtf.format(LocalDateTime.now()));
                Thread.sleep((long) 1000);
            }
            if (reconnectCount > 600) {
                break;
            }

        } while (itemID.equals("NOT FOUND"));
        return result;
    }

    public boolean sizeAndStyleIDAccessory() throws JSONException {
        this.result = false;

        String styleID = "";
        String sizeID = "";
        JSONArray jsonArr = this.adapter.getItemInfo(this.itemID);

        //finds styleID and sizeID in json array
        this.styleID = jsonArr.getJSONObject(0).get("id").toString();
        JSONArray jsonArr2 = jsonArr.getJSONObject(0).getJSONArray("sizes");
        this.sizeID = jsonArr2.getJSONObject(0).get("id").toString();
        System.out.println("Style ID: " + this.styleID);
        System.out.println("Size ID: " + this.sizeID);
        
        return this.result;
    }

    public boolean sizeAndStyleIDClothes(String _color) throws JSONException {
        this.result = false;
        JSONArray jsonArr = this.adapter.getItemInfo(this.itemID);

        /**
         * Finds styleID and sizeID in json array Default size is medium given
         * by: sizeID = jsonArr2.getJSONObject(1).get("id").toString();
         */
        for (int i = 0; i < jsonArr.length(); i++) {
            if (jsonArr.getJSONObject(i).getString("name").equals(_color)) {
                this.styleID = jsonArr.getJSONObject(i).get("id").toString();
                JSONArray jsonArr2 = jsonArr.getJSONObject(i).getJSONArray("sizes");
                this.sizeID = jsonArr2.getJSONObject(1).get("id").toString();
                System.out.println("Style ID: " + this.styleID);
                System.out.println("Size ID: " + this.sizeID);
                this.result = true;
                return this.result;
            }
        }
        System.out.println("Could not find color, returning empty map.");
        return this.result;
    }

    public boolean requestAddToCart() throws JSONException {
        Map<String, String> result = this.adapter.addToCart(this.itemID, this.styleID, this.sizeID);
        JSONObject cartResponse = new JSONObject(result.get("response"));
        String success = cartResponse.getString("success");
        if(success.equals("false")) 
            return false;
        else if(success.equals("true")){
            this.supremeSessCookie = result.get("supremeSessCookie");
            return true;
        }  
        else {
            System.out.println("Unknown response in requestAddToCart method in model. Response: " + success);
            return false;
        }       
    }

    
    
    
    ////////////////////////////////GETTERS/SETTERS////////////////////////////////////////////////////////////
    public String getItemID() {
        return this.itemID;
    }
    public String getSizeID() {
        return this.sizeID;
    }
    public String getStyleID() {
        return this.styleID;
    }
    public String getSupremeSessCookie() {
        return this.supremeSessCookie;
    }
}
