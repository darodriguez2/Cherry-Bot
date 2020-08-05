package testing;

import Utilities.DynamoEncryptionUtil;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class driver {

    public static void main(String[] args) throws InterruptedException, JSONException, GeneralSecurityException {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDB dynamoDB = new DynamoDB(client);
        DynamoEncryptionUtil encrypt = new DynamoEncryptionUtil();
        Table table = dynamoDB.getTable("LoginCredentials");
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("Username", "test4");
        Item item = table.getItem(spec);
        Map<String, AttributeValue> encryptedInfo = new HashMap<>();

        encryptedInfo.put(
                "Username", new AttributeValue().withS(item.getString("Username")));
        encryptedInfo.put(
                "Password", new AttributeValue().withB(item.getByteBuffer("Password")));
        encryptedInfo.put(
                "UUID", new AttributeValue().withB(item.getByteBuffer("UUID")));
        encryptedInfo.put(
                "*amzn-ddb-map-desc*", new AttributeValue().withB(item.getByteBuffer("*amzn-ddb-map-desc*")));
        encryptedInfo.put(
                "*amzn-ddb-map-sig*", new AttributeValue().withB(item.getByteBuffer("*amzn-ddb-map-sig*")));

        System.out.println("Encrypted Items from table");
        for (final String attributeName : encryptedInfo.keySet()) {
            System.out.println(attributeName + ": " + encryptedInfo.get(attributeName));
        }

        Map<String, AttributeValue> decryptedInfo = encrypt.decryptItem("LoginCredentials", "Username", "0", item);
        JSONObject obj;
        System.out.println("Decrypted items using decrypt record method");
        for (final String attributeName : decryptedInfo.keySet()) {
            System.out.println(attributeName + ": " + decryptedInfo.get(attributeName));
            System.out.println(decryptedInfo.get(attributeName).toString());
            obj = new JSONObject(decryptedInfo.get(attributeName).toString());
            System.out.println(obj.get("S"));
        }
        
        

    }
}
