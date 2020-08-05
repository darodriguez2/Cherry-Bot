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
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
}
