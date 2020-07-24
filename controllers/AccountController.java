/**
 * Controller for login and account creation views. 
 * @author darod
 */
package controllers;

import Adapters.DatabaseAdapter;


public class AccountController {
    private DatabaseAdapter dbAdapter = new DatabaseAdapter();
    
    public boolean checkUsernameRequest(String _username) {
        return this.dbAdapter.checkUsernameExists(_username);
    }
    
    public boolean createAccountRequest(String _username, String _password, String _uuid) {
        return this.dbAdapter.createAccount(_username, _password, _uuid);
    }
    
    public String validateLoginRequest(String _username, String _password) {
        return this.dbAdapter.validateLogin(_username, _password);
    }
}
