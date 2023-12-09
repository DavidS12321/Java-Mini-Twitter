import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;

public class UserManager {
    private static UserManager instance;
    private Map<String, UserGroup> userGroups;
    private Map<String, User> users;
    private Map<String, User> usersByUsername;

    private UserManager() {
        userGroups = new HashMap<>();
        users = new HashMap<>();
        usersByUsername = new HashMap<>();
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public UserGroup createUserGroup(String groupName) {
        UserGroup group = new UserGroup(groupName);
        userGroups.put(groupName, group);
        return group;
    }

    public User getUserByUsername(String username) {
        return usersByUsername.get(username);
    }

    public UserGroup getUserGroup(String groupName) {
        return userGroups.get(groupName);
    }

    public User createUser(String username) {
        User user = new User(username);
        usersByUsername.put(username, user);
        users.put(username, user);
        UserGroup rootGroup = getUserGroup("Root");
        if (rootGroup != null) {
            rootGroup.addUser(user);
        }
        return user;
    }

    public User getUserWithLastUpdateTime() {
        User lastUpdateUser = null;
        long maxLastUpdateTime = Long.MIN_VALUE;

        for (User user : users.values()) {
            if (user.getLastUpdateTime() > maxLastUpdateTime) {
                maxLastUpdateTime = user.getLastUpdateTime();
                lastUpdateUser = user;
            }
        }

        return lastUpdateUser;
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public int getGroupTotal() {
        return userGroups.size();
    }

    public int getUserTotal() {
        return users.size();
    }

    public int getTotalMessages() {
        int totalMessages = 0;
        for (User user : users.values()) {
            totalMessages += user.getNewsfeed().size();
        }
        return totalMessages;
    }

    public void postMessage(String username, String message) {
        User user = users.get(username);
        if (user != null) {
            user.addToFeed(message);
        } else {
            System.out.println("User not found: " + username);
        }
    }

    public Iterable<UserGroup> getAllGroups() {
        return userGroups.values();
    }

    public int getPositiveMessages() {
        int positiveCount = 0;

        for (User user : users.values()) {
            for (String message : user.getNewsfeed()) {
                if (isPositiveMessage(message)) {
                    positiveCount++;
                }
            }
        }
        return positiveCount;
    }

    private boolean isPositiveMessage(String message) {
        // Define a list of positive words
        List<String> positiveWords = Arrays.asList("good", "nice", "happy", "awesome", "excellent");

        // Check if the message contains any positive words
        for (String positiveWord : positiveWords) {
            if (message.toLowerCase().contains(positiveWord)) {
                return true;
            }
        }
        return false;
    }
}