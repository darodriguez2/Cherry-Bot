/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testing.UI;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author diego
 */
public class TreeTableTask extends RecursiveTreeObject<TreeTableTask>{

    public SimpleStringProperty site;
    public SimpleStringProperty profile;
    public SimpleStringProperty item;
    public SimpleStringProperty status;
    public String uuid;

    public TreeTableTask(String _uuid, String _site, String _profile, String _item, String _status) {
        this.uuid = _uuid;
        this.site = new SimpleStringProperty(_site);
        this.profile = new SimpleStringProperty(_profile);
        this.item = new SimpleStringProperty(_item);
        this.status = new SimpleStringProperty(_status);
    }

    public SimpleStringProperty getSite() {
        return this.site;
    }

    public SimpleStringProperty getProfile() {
        return this.profile;
    }

    public SimpleStringProperty getItem() {
        return this.item;
    }
    
    public SimpleStringProperty getStatus() {
        return this.status;
    }
    
    public String getUUID() {
        return this.uuid;
    }
    
}
