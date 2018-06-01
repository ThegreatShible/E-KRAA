package forms.JsonHelpers;

import models.users.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupListJson {
    private List<GroupJson> groups;

    public static GroupListJson fromGroupList(List<Group> groupList) {
        GroupListJson groupListJson = new GroupListJson();
        List<GroupJson> groupJsonList = new ArrayList<>();
        for (Group group : groupList) {
            groupJsonList.add(GroupJson.fromGroup(group));
        }
        groupListJson.setGroups(groupJsonList);
        return groupListJson;
    }

    public List<GroupJson> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupJson> groups) {
        this.groups = groups;
    }

}
