package Utilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.scene.control.TreeItem;
import testing.UI.TreeTableTask;

public class User {

    public String uuid;
    public Map<String, Map<String, Object>> profiles;
    public Map<String, TaskThread> tasks = new HashMap<>();
    public Set<TreeItem<TreeTableTask>> treeTableTasks = new HashSet<>();

    public User(String _uuid, Map<String, Map<String, Object>> _profiles) {
        this.uuid = _uuid;
        this.profiles = _profiles;
    }

    public void addTask(String _taskID, TaskThread task) {
        this.tasks.put(_taskID, task);
    }

    public void addTreeTableTask(TreeItem<TreeTableTask> _treeTableTask) {
        this.treeTableTasks.add(_treeTableTask);
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

    public Map<String, TaskThread> getTasks() {
        return this.tasks;
    }

    public Set<TreeItem<TreeTableTask>> getTreeTableTasks() {
        return this.treeTableTasks;

    }

}
