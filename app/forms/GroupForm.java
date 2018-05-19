package forms;

import models.users.Group;

import java.util.ArrayList;
import java.util.UUID;

public class GroupForm {
    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Group toGroup(int groupID, UUID ownerID) {
        Group group = new Group(groupID, groupName, new ArrayList<UUID>(), ownerID);
        return group;
    }
}
