import java.util.List;
import java.util.ArrayList;

public class User implements Subject {

    private String username;
    private UserGroup group;
    private List<User> followers;
    private List<User> followings;
    private List<String> newsfeed;
    private List<Observer> observers;
    private long creationTime;
    private long lastUpdateTime;
    private static User lastUpdateUser;

    public User(String username) {
        this.username = username;
        this.followers = new ArrayList<>();
        this.followings = new ArrayList<>();
        this.newsfeed = new ArrayList<>();
        this.group = null;
        this.observers = new ArrayList<>();
        this.creationTime = System.currentTimeMillis();
        this.lastUpdateTime = this.creationTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addFollower(User follower) {
        if (follower != null && !follower.equals(this)) {
            followers.add(follower);
        }
    }

    public List<User> getFollowers() {
        return followers;
    }

    public List<User> getFollowings() {
        return followings;
    }

    public void addToFeed(String tweet) {
        newsfeed.add(tweet);
    }

    public List<String> getNewsfeed() {
        return newsfeed;
    }

    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup group) {
        // Check if the user is already in a group
        if (this.group == null) {
            this.group = group;
        }
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void accept(UserElementVisitor visitor) {
        visitor.visit(this);
    }

    public void addFollowing(User following) {
        if (following != null && !following.equals(this)) {
            followings.add(following);
        }
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void addToFeedAndNotify(String tweet) {
        addToFeed(tweet);
        notifyObservers(tweet);
    }

    private void updateFollowersLastUpdateTime() {
        for (User follower : followers) {
            follower.updateLastUpdateTime();
        }
    }

    public void updateLastUpdateTime() {
        this.lastUpdateTime = System.currentTimeMillis();
        lastUpdateUser = this;
    }
}
