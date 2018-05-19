package models.users;

import java.util.List;
import java.util.UUID;

public class Group {

    private final String groupName;
    private final UUID owner;
    private int idGroup = 0;
    private List<UUID> pupilIds;

    public Group(int idGroup, String groupName, List<UUID> pupilIds, UUID owner) {
        this.idGroup = idGroup;
        this.groupName = groupName;
        this.pupilIds = pupilIds;
        this.owner = owner;
    }

    public static Group create(String groupName, List<UUID> pupilIds, UUID owner) {
        return new Group(0, groupName, pupilIds, owner);
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public String getGroupName() {
        return groupName;
    }

    public List<UUID> getPupilIds() {
        return pupilIds;
    }

    public void setPupilIds(List<UUID> pupilIds) {
        this.pupilIds = pupilIds;
    }
}
