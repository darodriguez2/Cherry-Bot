/**
 * Interface for defining what methods should be implemented when connecting to Supreme endpoints.
 */
package Interfaces;

import java.util.Map;
import org.json.JSONArray;


public interface SupremeInterface {
    
    public JSONArray getStock(String _itemType);
    
    public JSONArray getItemInfo(String _itemID);
    
    public Map<String, String> addToCart(String _itemID, String _styleID, String _sizeID);
    
    
    
    
}
