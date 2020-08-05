/**
 * AWS DynamoDB translator for communicating with DynamoDB. Translates information coming to and from database.
 * This is the ONLY java class allowed to communicate with DynamoDB.
 * All request MUST BE routed through adapter. Only the adapter communicates directly with this translator.
 *
 * @author Diego Rodriguez
 */
package translators;

import Interfaces.DatabaseInterface;
import Utilities.DynamoEncryptionUtil;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class DynamoTranslator implements DatabaseInterface {

    private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
    private final DynamoDB dynamoDB = new DynamoDB(client);
    private final DynamoEncryptionUtil encrypt = new DynamoEncryptionUtil();

    @Override
    public boolean checkUsernameExists(String _username) {
        Table table = dynamoDB.getTable("LoginCredentials");

        GetItemSpec spec = new GetItemSpec().withPrimaryKey("Username", _username);
        try {
            System.out.println("Attempting to read the item...");
            Item outcome = table.getItem(spec);
            System.out.println("GetItem succeeded: " + outcome);
            if (outcome == null) {
                System.out.println("Username does not exist");
                return true;
            } else {
                System.out.println("Username exists");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Unable to read item");
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean createAccount(String _username, String _password, String _uuid) {
        Map<String, AttributeValue> attributes = new HashMap<>();
        attributes.put("Username", new AttributeValue().withS(_username));
        attributes.put("Password", new AttributeValue().withS(_password));
        attributes.put("UUID", new AttributeValue().withS(_uuid));

        try {
            Map<String, AttributeValue> encryptedData = this.encrypt.encryptRecord("LoginCredentials", "Username", "0", attributes);
            client.putItem("LoginCredentials", encryptedData);;
            System.out.println("Encrypted recorded added");
            return true;
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(DynamoTranslator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Method used for validating login in login view Returns 1 for incorrect
     * username, returns 2 for incorrect password, returns uuid if the login
     * credentials are valid. Returns 3 if there is an error with database
     * connection (Try/Catch block catches an error).
     *
     * @param _username
     * @param _password
     * @return
     */
    @Override
    public String validateLogin(String _username, String _password) {
        Table table = dynamoDB.getTable("LoginCredentials");
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("Username", _username);
        try {
            System.out.println("Attempting to read the item from table...");
            Item tableItem = table.getItem(spec);
            System.out.println("Encrypted item from table: " + tableItem);
            if (tableItem == null) {
                System.out.println("Incorrect username");
                return "1";
            } else {
                Map<String, AttributeValue> decryptedItem = this.encrypt.decryptItem("LoginCredentials", "Username", "0", tableItem);
                JSONObject passwordObj = new JSONObject(decryptedItem.get("Password").toString());
                String decryptedPassword = passwordObj.getString("S").toString();
                if (!decryptedPassword.equals(_password)) {
                    System.out.println("Incorrect password/Password in database: " + decryptedPassword + " matches password given: " + _password);
                    return "2";
                } else {
                    System.out.println("Password in database: " + decryptedPassword + " matches password given: " + _password);
                    JSONObject uuidObj = new JSONObject(decryptedItem.get("UUID").toString());
                    String decryptedUUID = uuidObj.getString("S").toString();
                    System.out.println("Returning uuid: " + decryptedUUID);
                    return decryptedUUID;
                }
            }
        } catch (Exception e) {
            System.err.println("Error is thrown (try catch block in validateLogin)");
            System.err.println(e.getMessage());
            return "3";
        }
    }

    @Override
    public Map<String, Map<String, Object>> loadProfiles(String _uuid) {
        Table table = dynamoDB.getTable("UserProfiles");

        //Create a query spec to query for all items with the specified uuid.
        QuerySpec querySpec = new QuerySpec().withHashKey("uuid", _uuid);

        //store query result in a collection of items that we can iterate through.
        ItemCollection<QueryOutcome> items = table.query(querySpec);
        Iterator<Item> iterator = items.iterator();
        Item item;
        Map<String, Map<String, Object>> profiles = new HashMap<>();

        /**
         * Each item in the item collection corresponds to a profile in the
         * table. So we store each profile in a Map, with the key =
         * profile name and value = profile map
         */
        while (iterator.hasNext()) {
            item = iterator.next();
            System.out.println(item);
            profiles.put(item.asMap().get("profileName").toString(), item.asMap());
        }
        return profiles;
    }

    @Override
    public boolean addProfile(Map<String, String> _profileInfo) {
        System.out.println("Translator has recieved.....");
        for (String field : _profileInfo.keySet()) {
            System.out.println(field + ": " + _profileInfo.get(field));
        }

        Map<String, AttributeValue> attributes = new HashMap<>();
        attributes.put("uuid", new AttributeValue().withS("1"));
        attributes.put("profileName", new AttributeValue().withS(_profileInfo.get("profileName")));
        attributes.put("zip", new AttributeValue().withS(_profileInfo.get("zip")));
        attributes.put("cvv", new AttributeValue().withS(_profileInfo.get("cvv")));
        attributes.put("month", new AttributeValue().withS(_profileInfo.get("month")));
        attributes.put("phone", new AttributeValue().withS(_profileInfo.get("phone")));
        attributes.put("city", new AttributeValue().withS(_profileInfo.get("city")));
        attributes.put("year", new AttributeValue().withS(_profileInfo.get("year")));
        attributes.put("street", new AttributeValue().withS(_profileInfo.get("street")));
        attributes.put("fullName", new AttributeValue().withS(_profileInfo.get("fullName")));
        attributes.put("email", new AttributeValue().withS(_profileInfo.get("email")));
        attributes.put("cardNumber", new AttributeValue().withS(_profileInfo.get("cardNumber")));
        attributes.put("state", new AttributeValue().withS(_profileInfo.get("state")));

        //Map<String, AttributeValue> encryptedData = this.encrypt.encryptRecord("LoginCredentials", "Username", "0", attributes);
        client.putItem("UserProfiles", attributes);
        return true;
    }
}
