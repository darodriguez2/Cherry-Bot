/**
 * Controller for the profile and addprofile views.
 * @author darod
 */
package controllers;

import Adapters.DatabaseAdapter;
import java.util.Map;


public class ProfileController {
    
    private final DatabaseAdapter db = new DatabaseAdapter();
    
    public boolean addProfileRequest(Map<String, String> _profileInfo) {
        return this.db.addProfile(_profileInfo);
    }
    
    public Map<String, Map<String, Object>> loadProfileRequest(String _uuid) {
        return this.db.loadProfiles(_uuid);
    }
    
    public boolean deleteProfileRequest(String _uuid, String _profileName) {
        return this.db.deleteProfile(_uuid, _profileName);
    }
}
