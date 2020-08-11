/**
 * Controller for working with Supreme Model. All calls must be routed through his controller.
 * @author darod
 */

package controllers;
import Models.SupremeModel;
import org.json.JSONException;

public class SupremeController {
    private final SupremeModel model = new SupremeModel();
    
    public boolean sendSearchItemRequest(String _itemKeyword, String _itemType) throws InterruptedException {
        return this.model.searchForItem(_itemKeyword, _itemType);
    }
    
    public boolean sendAcessoryRequest() throws JSONException {
        return this.model.sizeAndStyleIDAccessory();
    }
    
    public boolean sendClothesRequest(String _color) throws JSONException {
        return this.model.sizeAndStyleIDClothes(_color);
    }
    
    public boolean sendAddToCartRequest() throws JSONException {
        return this.model.requestAddToCart();
    }
    
    public String getSupremeSessCookie() {
        return this.model.getSupremeSessCookie();
    }
}
