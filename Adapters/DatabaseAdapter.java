/**
 * Adapter for handling all information going to and from the translator. 
 * @author darod
 */
package Adapters;

import Interfaces.DatabaseInterface;
import translators.DynamoTranslator;


public class DatabaseAdapter implements DatabaseInterface{
    private static final DatabaseInterface TRANSLATOR = new DynamoTranslator();
    
    @Override
    public boolean checkUsernameExists(String _username) {
        return DatabaseAdapter.TRANSLATOR.checkUsernameExists(_username);
    }

    @Override
    public boolean createAccount(String _username, String _password, String _uuid) {
        return DatabaseAdapter.TRANSLATOR.createAccount(_username, _password, _uuid);
    }

    @Override
    public String validateLogin(String _username, String _password) {
        return DatabaseAdapter.TRANSLATOR.validateLogin(_username, _password);
    }
    
}
