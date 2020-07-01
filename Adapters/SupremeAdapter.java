/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Adapters;

import Interfaces.SupremeInterface;
import java.util.Map;
import org.json.JSONArray;
import translators.SupremeTranslator;

/**
 *
 * @author darod
 */
public class SupremeAdapter implements SupremeInterface {
    private static final SupremeInterface TRANSLATOR = new SupremeTranslator();
    
    @Override
    public JSONArray getStock(String _itemType) {
        return SupremeAdapter.TRANSLATOR.getStock(_itemType);
    }

    @Override
    public JSONArray getItemInfo(String _itemID) {
        return SupremeAdapter.TRANSLATOR.getItemInfo(_itemID);
    }

    @Override
    public Map<String, String> addToCart(String itemID, String styleID, String sizeID) {
        return TRANSLATOR.addToCart(itemID, styleID, sizeID);
    }
    
}
