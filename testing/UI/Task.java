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
public class Task extends RecursiveTreeObject<Task>{

    public SimpleStringProperty site;
    public SimpleStringProperty profile;
    public SimpleStringProperty item;
    public SimpleStringProperty status;

    public Task(String site, String profile, String item, String status) {
        this.site = new SimpleStringProperty(site);
        this.profile = new SimpleStringProperty(profile);
        this.item = new SimpleStringProperty(item);
        this.status = new SimpleStringProperty(status);
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
    
}
