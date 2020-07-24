/**
 * AWS DynamoDB translator for communicating with DynamoDB. Translates information coming to and from database.
 * This is the ONLY java class allowed to communicate with DynamoDB. 
 * All request MUST BE routed through adapter. Only the adapter communicates directly with this translator. 
 * @author Diego Rodriguez
 */

package translators;

import Interfaces.DatabaseInterface;
import java.util.HashMap;
import java.util.Map;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;


public class DynamoTranslator implements DatabaseInterface {
    
    private DynamoDbClientBuilder builder = DynamoDbClient.builder();
    private DynamoDbClient client = builder.build();
    
    
    @Override
    public boolean checkUsernameExists(String _username) {
        Map<String, AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put("Username", AttributeValue.builder().s(_username).build());
        GetItemRequest request = GetItemRequest.builder().key(keyToGet).tableName("LoginCredentials").build();
        try {
            Map<String, AttributeValue> returnedItem = this.client.getItem(request).item();

            if (returnedItem.isEmpty()) {
                System.out.println("Username does not exist");
                return true;
            } else {
                System.out.println("Username exists");
                return false;
            }
        } catch (DynamoDbException e) {
            System.out.println("EXCEPTION AT USERNAME");
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Unable to submit request.");
        return false;
    }
    
    @Override
    public boolean createAccount(String _username, String _password, String _uuid) {
        Map<String, AttributeValue> itemAttributes = new HashMap<>();
                itemAttributes.put("Username", AttributeValue.builder().s(_username).build());
                itemAttributes.put("Password", AttributeValue.builder().s(_password).build());
                itemAttributes.put("UUID", AttributeValue.builder().s(_uuid).build());
                PutItemRequest request = PutItemRequest.builder().tableName("LoginCredentials").item(itemAttributes).build();
                try {
                    client.putItem(request);
                    System.out.println( "LoginCredentials was successfully updated");

                } catch (ResourceNotFoundException e) {
                    System.err.format("Error: The table \"%s\" can't be found.\n", "LoginCredentials");
                    System.err.println("Be sure that it exists and that you've typed its name correctly!");
                    System.exit(1);
                    return false;
                } catch (DynamoDbException e) {
                    System.err.println(e.getMessage());
                    System.exit(1);
                    return false;
                }
                return true;
    }
    
    /**
     * Method used for validating login in login view
     * Returns 1 for incorrect username, returns 2 for incorrect password, returns uuid if the login credentials are valid.
     * Returns 3 if there is an error with database connection (Try/Catch block catches an error).
     * @param _username
     * @param _password
     * @return 
     */
    @Override
    public String validateLogin(String _username, String _password) {
        String uuid;
        String passwordInDatabase;
        
        Map<String, AttributeValue> keyToGet = new HashMap<>();
            keyToGet.put("Username", AttributeValue.builder().s(_username).build());
            GetItemRequest request = GetItemRequest.builder().key(keyToGet).tableName("LoginCredentials").build();
            try {
                Map<String, AttributeValue> returnedItem = this.client.getItem(request).item();

                if (returnedItem.isEmpty()) {
                    System.out.println("Incorrect Username (translator)");
                    return "1";
                } else {
                    //code for getting username, might use later 7/24
                    //String username = returnedItem.get("Username").s();
                    passwordInDatabase = returnedItem.get("Password").s();
                    System.out.println(passwordInDatabase);
                    if (!_password.equals(passwordInDatabase)) {
                        System.out.println("Incorrect Password");
                        System.out.println("Password entered: " + _password + "does not match password in db: " + passwordInDatabase);
                        return "2";
                    }
                    uuid = returnedItem.get("UUID").s();
                    System.out.println(uuid);
                    return uuid;
                }
            } catch (DynamoDbException e) {
                System.out.println("EXCEPTION AT USERNAME");
                System.err.println(e.getMessage());
                System.exit(1);
            }
            System.out.println("Unable to submit request.");
            return "3";
    }
}
