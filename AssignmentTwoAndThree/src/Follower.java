public class Follower implements Observer {
    private String username;
    private long lastUpdateTime;

    public Follower(String username) {
        this.username = username;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    @Override
    public void update(String message) {
        System.out.println(username + "received an update: " + message);
    }

    public String getUsername() {
        return username;
    }
}