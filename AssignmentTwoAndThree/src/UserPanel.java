import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class UserPanel {

    private JPanel panel;
    private UserManager userManager;
    private String username;
    private DefaultListModel<String> newsfeedListModel;
    private DefaultListModel<String> followersListModel;
    private List<Follower> followers;
    private JFrame frame;

    public UserPanel(UserManager userManager, String username) {
        this.userManager = userManager;
        this.username = username;
        this.newsfeedListModel = new DefaultListModel<>();
        this.followersListModel = new DefaultListModel<>();
        this.followers = new ArrayList<>();
    }

    public JPanel createUserPanel() {
        JPanel userPanel = new JPanel(new BorderLayout());

        // Follow panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createTitledBorder(username));

        JLabel userIDLabel = new JLabel("User ID:");
        topPanel.add(userIDLabel);

        JTextField followTextField = new JTextField(10);
        topPanel.add(followTextField);

        JButton followButton = new JButton("Follow");
        followButton.setBackground(Color.CYAN);
        followButton.addActionListener(e -> {
            String userToFollow = followTextField.getText();
            User userToFollowObject = userManager.getUserByUsername(userToFollow);
            if (userToFollowObject != null) {
                Follower follower = new Follower(userToFollowObject.getUsername());
                userToFollowObject.registerObserver(follower);
                followers.add(follower);
                followersListModel.addElement(follower.getUsername());

                User currentUser = userManager.getUserByUsername(username);
                if (currentUser != null) {
                    currentUser.addFollower(userToFollowObject);
                }
            }
        });
        topPanel.add(followButton);

        userPanel.add(topPanel, BorderLayout.NORTH);

        // Following panel
        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setBorder(BorderFactory.createTitledBorder("Following"));

        JList<String> followersList = new JList<>(followersListModel);
        JScrollPane followersScrollPane = new JScrollPane(followersList);
        middlePanel.add(followersScrollPane, BorderLayout.CENTER);

        userPanel.add(middlePanel, BorderLayout.CENTER);

        // Post and newsfeed panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Post Message"));

        JPanel postMessagePanel = new JPanel(new BorderLayout());

        JTextField messageTextField = new JTextField();
        postMessagePanel.add(messageTextField, BorderLayout.CENTER);

        JButton postButton = new JButton("Post");
        postButton.setBackground(Color.CYAN);
        postButton.addActionListener(e -> {
            String message = messageTextField.getText();
            userManager.postMessage(username, message);
            newsfeedListModel.addElement(message);
        });
        postMessagePanel.add(postButton, BorderLayout.EAST);

        bottomPanel.add(postMessagePanel, BorderLayout.NORTH);

        JPanel newsfeedPanel = new JPanel(new BorderLayout());
        JLabel newsfeedLabel = new JLabel("Newsfeed:");
        newsfeedPanel.add(newsfeedLabel, BorderLayout.NORTH);

        JList<String> newsfeedList = new JList<>(newsfeedListModel);
        JScrollPane newsfeedScrollPane = new JScrollPane(newsfeedList);
        newsfeedPanel.add(newsfeedScrollPane, BorderLayout.CENTER);

        bottomPanel.add(newsfeedPanel, BorderLayout.CENTER);

        userPanel.add(bottomPanel, BorderLayout.SOUTH);

        return new JPanel();
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public String getUsername() {
        return username;
    }

    public void createAndShowPanel() {
        JPanel panel = createUserPanel();

        frame = new JFrame("User Panel - " + username);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 400);
        frame.add(panel);
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }

    public List<Follower> getFollowers() {
        return followers;
    }
}