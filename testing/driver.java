
package testing;

import Models.SupremeModel;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
public class driver {
    public static void main(String[] args) throws InterruptedException, JSONException {
        SupremeModel model = new SupremeModel();
//        String itemID = model.searchForItem("Waves", "Accessories");
//        System.out.println(itemID);
//        Map<String, String> sizeStyleIDs = new HashMap<>();
//        sizeStyleIDs = model.sizeAndStyleIDAccessory(itemID);
//        System.out.println("Style ID: " + sizeStyleIDs.get("styleID"));
//        System.out.println("Size ID: " + sizeStyleIDs.get("sizeID"));
        
        System.out.println(model.searchForItem("Silk", "Shirts"));
        String itemID = model.getItemID();
        System.out.println(itemID);
        System.out.println(model.sizeAndStyleIDClothes("Peach"));
        System.out.println("Size ID: " + model.getSizeID());
        System.out.println("Style ID: " + model.getStyleID());

    }
}
