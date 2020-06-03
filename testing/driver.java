
package testing;

import Models.SupremeModel;
import controllers.WebViewController;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
public class driver {
    public static void main(String[] args) throws InterruptedException, JSONException {
        SupremeModel model = new SupremeModel();
        WebViewController controller = new WebViewController();
        
//        System.out.println(model.searchForItem("Silk", "Shirts"));
//        String itemID = model.getItemID();
//        System.out.println(itemID);
//        System.out.println(model.sizeAndStyleIDClothes("Peach"));
//        System.out.println("Size ID: " + model.getSizeID());
//        System.out.println("Style ID: " + model.getStyleID());
//        System.out.println("Cart response: " + model.requestAddToCart());

        System.out.println(controller.sendSearchItemRequest("Silk", "Shirts"));
        System.out.println(controller.sendClothesRequest("Peach"));
        System.out.println(controller.sendAddToCartRequest());
        System.out.println(controller.getSupremeSessCookie());
    }
}
