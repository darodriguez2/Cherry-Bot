
package Utilities;

import java.util.Map;


public class User {
    
    private String uuid;
    private Map<String, Map<String, Object>> profiles;
    
    public User(String _uuid, Map<String, Map<String, Object>> _profiles) {
        this.uuid = _uuid;
        this.profiles = _profiles;
    }

    ////////////////////SETTERS//////////////////////////
    public void setUuid(String _uuid) {
        this.uuid = _uuid;
    }

    public void setProfiles(Map<String, Map<String, Object>> _profiles) {
        this.profiles = _profiles;
    }
    
    
////////////////////GETTERS//////////////////////////
    public String getUuid() {
        return this.uuid;
    }

    public Map<String, Map<String, Object>> getProfiles() {
        return this.profiles;
    }
    

}
