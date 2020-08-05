/**
 * Interface that defines how database translators should be implemented within the scope of this application.
 * @author darod
 */
package Interfaces;

import java.util.Map;


public interface DatabaseInterface {
    
    public boolean checkUsernameExists(String _username);
    
    public boolean createAccount(String _username, String _password, String _uuid);
    
    public String validateLogin(String _username, String _password);
    
    public Map<String, Map<String, Object>> loadProfiles(String _uuid);
    
    public boolean addProfile(Map<String, String> _profileInfo);
    
}
