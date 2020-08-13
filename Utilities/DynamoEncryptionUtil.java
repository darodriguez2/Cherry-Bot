/**
 * Model for encrypting data to be put into dynamo db
 * and decrypting data from dynamo db
 * @author darod
 */
package Utilities;

import com.amazonaws.services.dynamodbv2.datamodeling.encryption.DynamoDBEncryptor;
import com.amazonaws.services.dynamodbv2.datamodeling.encryption.EncryptionContext;
import com.amazonaws.services.dynamodbv2.datamodeling.encryption.EncryptionFlags;
import com.amazonaws.services.dynamodbv2.datamodeling.encryption.providers.DirectKmsMaterialProvider;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DynamoEncryptionUtil {
    
    
    
    
    //maybe customer master key amazon resource name (cmkArn) shouldn't be hardcoded in program. 
    //Put in txt file later? (artifact?)
    private final String cmkArn;
    private final String region = "us-east-2";
    private final AWSKMS kms = AWSKMSClientBuilder.standard().withRegion(this.region).build();
    private final DirectKmsMaterialProvider cmp;
    private final DynamoDBEncryptor encryptor;
    
    public DynamoEncryptionUtil() throws IOException {
        System.out.println(getARN());
        this.cmkArn = getARN();
        this.cmp = new DirectKmsMaterialProvider(kms, cmkArn);
        this.encryptor  = DynamoDBEncryptor.getInstance(cmp);
    }
    /**
     * Method for encrypting a map of attributes to be put into table. If there
     * is no sort key(range key) in a table, pass string "0" as parameter.
     *
     * @param _tableName
     * @param _primaryKey
     * @param _sortKey
     * @param _record
     * @return
     * @throws java.security.GeneralSecurityException
     */
    public Map<String, AttributeValue> encryptRecord(String _tableName, String _primaryKey, String _sortKey, Map<String, AttributeValue> _record) throws GeneralSecurityException {
        EncryptionContext encryptionContext;
        //Build Encryption Context with or without a sortKey depending on the value that is passed. 
        if (_sortKey.equals("0")) {
            encryptionContext = new EncryptionContext.Builder().withTableName(_tableName).withHashKeyName(_primaryKey).build();
        } else {
            encryptionContext = new EncryptionContext.Builder().withTableName(_tableName).withHashKeyName(_primaryKey).withRangeKeyName(_sortKey).build();
        }

        /**
         * Create attribute actions using EnumSets for encryption flags and a
         * map to store the key value pairs, where the keys are the attribute
         * names and the values are the attribute actions given by the
         * encryption flags.
         */
        final EnumSet<EncryptionFlags> signOnly = EnumSet.of(EncryptionFlags.SIGN);
        final EnumSet<EncryptionFlags> encryptAndSign = EnumSet.of(EncryptionFlags.ENCRYPT, EncryptionFlags.SIGN);
        final Map<String, Set<EncryptionFlags>> actions = new HashMap<>();

        /**
         * Cycle through all attributes in the record parameter, assigning the
         * appropriate encryption flag for each and storing the key/value pair
         * in the map.
         */
        for (final String attributeName : _record.keySet()) {
            if (attributeName.equals(_primaryKey) || attributeName.equals(_sortKey)) {
                actions.put(attributeName, signOnly);
            } else {
                actions.put(attributeName, encryptAndSign);
            }
        }

        //Create and return the encrypted record. 
        final Map<String, AttributeValue> encryptedRecord = this.encryptor.encryptRecord(_record, actions, encryptionContext);
        return encryptedRecord;
    }
    
    
    /**
     * Decrypts item that is passed and returns a map of decrypted attributes
     * @param _tableName
     * @param _primaryKey
     * @param _sortKey
     * @param _item
     * @return
     * @throws GeneralSecurityException 
     */
    public Map<String, AttributeValue> decryptItem(String _tableName, String _primaryKey, String _sortKey, Item _item) throws GeneralSecurityException {
        EncryptionContext encryptionContext;
        //Build Encryption Context with or without a sortKey depending on the value that is passed. 
        if (_sortKey.equals("0")) {
            encryptionContext = new EncryptionContext.Builder().withTableName(_tableName).withHashKeyName(_primaryKey).build();
        } else {
            encryptionContext = new EncryptionContext.Builder().withTableName(_tableName).withHashKeyName(_primaryKey).withRangeKeyName(_sortKey).build();
        }
        
        /**
         * Convert encrypted item into map of String and Attribute Values.
         * Note that primary key and sort key are always sign only and therefore can be retrieved from item with getString,
         * while all other attribute values are encrypted (stored as bytes) and must be retrieved with getByteBuffer.
         */
        Map<String, AttributeValue> encryptedInfo = new HashMap<>();
        
        for (final String attributeName : _item.asMap().keySet()) {
            if (attributeName.equals(_primaryKey) || attributeName.equals(_sortKey)) {
                encryptedInfo.put(attributeName,  new AttributeValue().withS(_item.getString(attributeName)));
            }
            else {
                encryptedInfo.put(attributeName,  new AttributeValue().withB(_item.getByteBuffer(attributeName)));
            }
        }
        
        
        final EnumSet<EncryptionFlags> signOnly = EnumSet.of(EncryptionFlags.SIGN);
        final EnumSet<EncryptionFlags> encryptAndSign = EnumSet.of(EncryptionFlags.ENCRYPT, EncryptionFlags.SIGN);
        final Map<String, Set<EncryptionFlags>> actions = new HashMap<>();
        
        /**
         * Cycle through all attributes in encryptedInfo, assigning the
         * appropriate encryption flag for each and storing the key/value pair
         * in the map.
         */
        for (final String attributeName : encryptedInfo.keySet()) {
            if (attributeName.equals(_primaryKey) || attributeName.equals(_sortKey)) {
                actions.put(attributeName, signOnly);
            } else if (attributeName.equals("*amzn-ddb-map-desc*") || attributeName.equals("*amzn-ddb-map-sig*")) {
                //we want to ignore these 2 attributes
                continue;
            } else {
                //we can assume all other attributes are encrypted and signed
                actions.put(attributeName, encryptAndSign);
            }
        }       
        
        //Create and return the decrypted item as map. 
        final Map<String, AttributeValue> decryptedRecord = this.encryptor.decryptRecord(encryptedInfo, actions, encryptionContext);
        return decryptedRecord;
    }
    
    public String getARN() throws FileNotFoundException, IOException {
        String filePath = System.getProperty("user.dir") + "\\src\\Artifacts\\cmkArn.txt\\";
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st = br.readLine();
        System.out.println(st);
        return st;

    }
}
