package forms.JsonHelpers;

import models.users.Group;

public class GroupJson {
    private String groupName;

    public static GroupJson fromGroup(Group group) {
        GroupJson groupJson = new GroupJson();
        groupJson.setGroupName(group.getGroupName());
        return groupJson;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
