import java.util.ArrayList;
import java.util.List;

public class UserGroup {
    private String uniqueID;
    private List<User> users;
    private List<UserGroup> subGroups;
    private long creationTime;

    public UserGroup(String uniqueID) {
        this.uniqueID = uniqueID;
        this.users = new ArrayList<>();
        this.subGroups = new ArrayList<>();
        this.creationTime = System.currentTimeMillis();
    }

    public void addSubGroup(UserGroup group) {
        subGroups.add(group);
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<UserGroup> getSubGroups() {
        return subGroups;
    }

    public void setSubGroups(List<UserGroup> subGroups) {
        this.subGroups = subGroups;
    }

    public boolean contains(User user) {
        return users.contains(user);
    }

    public void accept(UserElementVisitor visitor) {
        visitor.visit(this);
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void addUser(User user) {
        // check if user is not already in the group
        if (!users.contains(user) && user.getGroup() == null) {
            users.add(user);
            user.setGroup(this);
        }
    }
}