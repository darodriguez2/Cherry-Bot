/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Adapters.SupremeAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SupremeModel {

    private final SupremeAdapter adapter = new SupremeAdapter();
    private String itemID;
    private String styleID;
    private String sizeID;
    private boolean result = false;

    public boolean searchForItem(String _itemKeyword, String _itemType) throws InterruptedException {
        String itemID = "NOT FOUND";
        int reconnectCount = 0;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Searching for item at: " + dtf.format(now));
        /**
         * This loop will execute until the item is found. Iterates (1/sec) If
         * the item is never found, it will loop 600 times. (about 10 mins) The
         * idea is to keep sending requests to the endpoint until the page loads
         * with the item we want. Ideally we want to start this 30-60s before
         * drop time.
         */
        do {
            System.out.println("marker1");
            JSONArray jsonArr = this.adapter.getStock(_itemType);
            try {
                for (int i = 0; i < jsonArr.length(); i++) {
                    if (jsonArr.getJSONObject(i).getString("name").contains(_itemKeyword)) {
                        itemID = jsonArr.getJSONObject(i).get("id").toString();
                        System.out.println("Item found. Item ID: " + itemID);
                        this.itemID = itemID;
                        this.result = true;
                        break;
                    }
                }
            } catch (JSONException e) {

            }
            if (itemID.equals("NOT FOUND")) {
                reconnectCount++;
                System.out.println("Item not found. Reconnect count: " + reconnectCount);
                Thread.sleep((long) 1000);
            }
            if (reconnectCount > 600) {
                break;
            }

        } while (itemID.equals("NOT FOUND"));

        now = LocalDateTime.now();
        System.out.println("Item found at: " + dtf.format(now));

        return result;
    }

    public Map<String, String> sizeAndStyleIDAccessory(String _itemID) throws JSONException {
        Map<String, String> map = new HashMap<>();
        String styleID = "";
        String sizeID = "";
        JSONArray jsonArr = adapter.getItemInfo(_itemID);

        //finds styleID and sizeID in json array
        styleID = jsonArr.getJSONObject(0).get("id").toString();
        JSONArray jsonArr2 = jsonArr.getJSONObject(0).getJSONArray("sizes");
        sizeID = jsonArr2.getJSONObject(0).get("id").toString();
        System.out.println("Style ID: " + styleID);
        System.out.println("Size ID: " + sizeID);

        map.put("sizeID", sizeID);
        map.put("styleID", styleID);
        return map;
    }

    public boolean sizeAndStyleIDClothes(String _color) throws JSONException {
        this.result = false;
        Map<String, String> map = new HashMap<>();
//        String styleID = "";
//        String sizeID = "";
        JSONArray jsonArr = adapter.getItemInfo(this.itemID);

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
                map.put("sizeID", sizeID);
                map.put("styleID", styleID);
                return this.result;
            }
        }
        System.out.println("Could not find color, returning empty map.");
        return this.result;
    }

    public Map<String, String> requestAddToCart(String _itemID, String _styleID, String _sizeID) {
        Map<String, String> result = adapter.addToCart(_itemID, _styleID, _sizeID);
        return result;
    }

    public String getItemID() {
        return this.itemID;
    }
    public String getSizeID() {
        return this.sizeID;
    }
    public String getStyleID() {
        return this.styleID;
    }
}
