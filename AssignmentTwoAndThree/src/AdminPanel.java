import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.List;
import java.util.ArrayList;

public class AdminPanel {

    private UserManager userManager;
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode rootNode;
    private Set<String> userSet;
    private DefaultListModel<String> newsfeedListModel;
    private DefaultListModel<String> followersListModel;
    private List<Follower> followers = new ArrayList<>();

    public AdminPanel(UserManager userManager) {
        this.userManager = userManager;
        this.userSet = new HashSet<>();
        this.newsfeedListModel = new DefaultListModel<>();
        this.followersListModel = new DefaultListModel<>();
    }

    public void launchPanel() {
        JFrame frame = new JFrame("Mini Twitter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // initialize tree model and root node
        rootNode = new DefaultMutableTreeNode("Root Folder");
        treeModel = new DefaultTreeModel(rootNode);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        JButton openUserPanelButton = new JButton("Open User Panel");

        // Tree view panel
        JTree tree = new JTree(treeModel);
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode.isLeaf()) {
                // Enable the "Open User Panel" button when a user is selected
                openUserPanelButton.setEnabled(true);
            } else {
                // Disable the "Open User Panel" button for non-user selections
                openUserPanelButton.setEnabled(false);
            }
        });

        JScrollPane treeScrollPane = new JScrollPane(tree);
        treeScrollPane.setBorder(BorderFactory.createTitledBorder("Tree View"));
        treeScrollPane.setBackground(Color.GRAY);
        mainPanel.add(treeScrollPane, BorderLayout.WEST);

        // user control panel
        JPanel userControlPanel = new JPanel(new GridBagLayout());
        userControlPanel.setBorder(BorderFactory.createTitledBorder("User Control"));
        GridBagConstraints gbc = new GridBagConstraints();

        userControlPanel.setBackground(Color.GRAY);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTH; // Align label to the top
        userControlPanel.setForeground(Color.WHITE);

        // top buttons
        JPanel topButtonPanel = new JPanel(new GridBagLayout());

        JLabel userIDLabel = new JLabel("UserID");
        userIDLabel.setBackground(Color.CYAN);
        addComponentToPanel(topButtonPanel, userIDLabel, 0, 0, 1.0);

        JTextField userIDTextField = new JTextField();
        addComponentToPanel(topButtonPanel, userIDTextField, 0, 1, 1.0);

        JButton addUserButton = new JButton("AddUser");
        addUserButton.setBackground(Color.CYAN);
        addUserButton.addActionListener(e -> addUserToTree(userIDTextField.getText()));
        addButtonToPanel(topButtonPanel, addUserButton, 1, 1, 1.0);

        JLabel groupIDLabel = new JLabel("GroupID");
        groupIDLabel.setBackground(Color.CYAN);
        addComponentToPanel(topButtonPanel, groupIDLabel, 0, 2, 1.0);

        JTextField groupIDTextField = new JTextField();
        addComponentToPanel(topButtonPanel, groupIDTextField, 0, 3, 1.0);

        JButton addGroupButton = new JButton("AddGroup");
        addGroupButton.setBackground(Color.CYAN);
        addGroupButton.addActionListener(e -> addUserToGroup(userIDTextField.getText(), groupIDTextField.getText()));
        addButtonToPanel(topButtonPanel, addGroupButton, 1, 3, 1.0);

        openUserPanelButton.setBackground(Color.CYAN);
        openUserPanelButton.setEnabled(false); // Initially disable the button
        openUserPanelButton.addActionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode.isLeaf()) {
                launchUserPanel(selectedNode.toString());
            }
        });

        addButtonToPanel(topButtonPanel, openUserPanelButton, 0, 4, 2.0);

        // bottom buttons
        JPanel bottomButtonPanel = new JPanel(new GridBagLayout());

        JButton showLastUpdateUserButton = new JButton("Last Updated User");
        showLastUpdateUserButton.setBackground(Color.CYAN);
        showLastUpdateUserButton.addActionListener(e -> showLastUpdateUser());
        addButtonToPanel(bottomButtonPanel, showLastUpdateUserButton, 1, 2, 2.0);

        JButton validateIDsButton = new JButton("Validate IDs");
        validateIDsButton.setBackground(Color.CYAN);
        validateIDsButton.addActionListener(e -> showValidationDialog());
        addButtonToPanel(bottomButtonPanel, validateIDsButton, 0, 2, 2.0);

        JButton showGroupTotalButton = new JButton("Show Group Total");
        showGroupTotalButton.setBackground(Color.CYAN);
        showGroupTotalButton.addActionListener(e -> launchTotalPanel("Group Total", userManager.getGroupTotal()));
        addButtonToPanel(bottomButtonPanel, showGroupTotalButton, 0, 0, 2.0);

        JButton showUserTotalButton = new JButton("Show User Total");
        showUserTotalButton.setBackground(Color.CYAN);
        showUserTotalButton.addActionListener(e -> launchTotalPanel("User Total", userManager.getUserTotal()));
        addButtonToPanel(bottomButtonPanel, showUserTotalButton, 1, 0, 2.0);

        JButton showTotalMessagesButton = new JButton("Show Total Messages");
        showTotalMessagesButton.setBackground(Color.CYAN);
        showTotalMessagesButton.addActionListener(e -> launchTotalPanel("Total Messages", userManager.getTotalMessages()));
        addButtonToPanel(bottomButtonPanel, showTotalMessagesButton, 0, 1, 2.0);

        JButton showPositiveTotalButton = new JButton("Show Positive Total");
        showPositiveTotalButton.setBackground(Color.CYAN);
        addButtonToPanel(bottomButtonPanel, showPositiveTotalButton, 1, 1, 2.0);

        showPositiveTotalButton.addActionListener(e -> {
            int totalMessages = userManager.getTotalMessages();
            int positiveMessages = userManager.getPositiveMessages();

            if (totalMessages > 0) {
                int percentage = ((int) positiveMessages / totalMessages) * 100;
                launchTotalPanel("Positive Messages Percentage", percentage);
            }
        });


        // add panels to user control panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 10, 0); // Set top and bottom insets to create a gap
        userControlPanel.add(topButtonPanel, gbc);

        GridBagConstraints gbcBottom = new GridBagConstraints();
        gbcBottom.gridx = 0;
        gbcBottom.gridy = 2;
        gbcBottom.weighty = 1.0; // Allow the bottom panel to expand vertically
        userControlPanel.add(bottomButtonPanel, gbcBottom);

        mainPanel.add(userControlPanel, BorderLayout.CENTER);
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void showLastUpdateUser() {
        // Assuming userManager has a method to get the user with the last update time
        User lastUpdateUser = userManager.getUserWithLastUpdateTime();

        if (lastUpdateUser != null) {
            JOptionPane.showMessageDialog(null, "Last updated user: " + lastUpdateUser.getUsername());
        } else {
            JOptionPane.showMessageDialog(null, "No users found with last update time.");
        }
    }

    private void showValidationDialog() {
        boolean isValid = validateIDs();

        String message;
        if (isValid) {
            message = "All IDs are valid.";
        } else {
            message = "Invalid IDs detected. Please ensure all IDs are unique and do not contain spaces.";
        }

        JOptionPane.showMessageDialog(null, message, "ID Validation Result", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean validateIDs() {
        Set<String> allIDs = new HashSet<>();

        // Check user IDs
        for (String userID : userSet) {
            if (!isValidID(userID) || !allIDs.add(userID)) {
                return false;
            }
        }

        // Check group IDs
        for (UserGroup group : userManager.getAllGroups()) {
            String groupID = group.getUniqueID();
            if (!isValidID(groupID) || !allIDs.add(groupID)) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidID(String id) {
        return id != null && !id.trim().isEmpty() && !id.contains(" ");
    }

    private void addUserToTree(String username) {
        // check if user already exists
        if (userSet.contains(username)) {
            System.out.println("User with ID " + username + " already exists.");
            return;
        }

        User user = userManager.createUser(username);

        // check if user has a group
        if (user.getGroup() != null) {
            DefaultMutableTreeNode groupNode = findGroupNode(user.getGroup().getUniqueID());
            if (groupNode != null) {
                DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(username);
                groupNode.add(userNode);
                treeModel.reload(groupNode);
            }
        } else {
            // if no group, add to root
            DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(username);
            rootNode.add(userNode);
            treeModel.reload(rootNode);
        }

        userSet.add(username);
    }

    private void addUserToGroup(String username, String groupName) {
        // check if username is empty
        if (username.isEmpty()) {
            // if username is empty create group w/o user
            UserGroup group = userManager.getUserGroup(groupName);
            if (group != null) {
                System.out.println("Group with ID " + groupName + " already exists.");
            } else {
                group = userManager.createUserGroup(groupName);
                DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(groupName);
                rootNode.add(groupNode);
                treeModel.reload();
            }
        } else {
            // if username is not empty create user and add to group

            // check if the user exists
            if (userSet.contains(username)) {
                System.out.println("User with ID " + username + " already exists.");
                return;
            }

            User user = userManager.createUser(username);

            // check if group exists
            UserGroup group = userManager.getUserGroup(groupName);
            if (group == null) {
                // if group does not exist create group
                group = userManager.createUserGroup(groupName);
                DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(groupName);
                rootNode.add(groupNode);
                treeModel.reload();
            }


            group.addUser(user);
            userSet.add(username);

            // updates tree
            DefaultMutableTreeNode groupNode = findGroupNode(group.getUniqueID());
            if (groupNode != null) {
                DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(username);
                groupNode.add(userNode);
                treeModel.reload();
            }
        }
    }


    private void launchTotalPanel(String labelText, int total) {
        JFrame totalFrame = new JFrame("Total Panel");
        totalFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        totalFrame.setSize(300, 150);

        JPanel totalPanel = new JPanel(new FlowLayout());
        totalPanel.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText + ": " + total);
        totalPanel.add(label);

        totalFrame.add(totalPanel);
        totalFrame.setVisible(true);
    }

    private JPanel createUserPanel(String username) {
        JPanel userPanel = new JPanel(new BorderLayout());

        User user = userManager.getUserByUsername(username);
        long creationTime = user.getCreationTime();
        long lastUpdateTime = user.getLastUpdateTime();

        // Follow panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createTitledBorder(username));
        topPanel.setBorder(BorderFactory.createTitledBorder(username + " (Created on: " + creationTime + ", Last Updated: " + lastUpdateTime + ")"));

        JLabel userIDLabel = new JLabel("User ID: " + creationTime + " (Last Updated: " + lastUpdateTime + ")");
        topPanel.add(userIDLabel);

        JTextField followTextField = new JTextField(10);
        topPanel.add(followTextField);

        JButton followButton = new JButton("Follow");
        followButton.setBackground(Color.CYAN);
        followButton.addActionListener(e -> {
            String userToFollow = followTextField.getText();
            User userToFollowObject = userManager.getUserByUsername(userToFollow);
            User currentUser = userManager.getUserByUsername(username);

            if (userToFollowObject != null && currentUser != null) {
                Follower follower = new Follower(userToFollowObject.getUsername());
                userToFollowObject.registerObserver(follower);
                currentUser.addFollower(userToFollowObject);

                followers.add(follower);
                followersListModel.addElement(follower.getUsername());
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

        return userPanel;
    }

    private void launchUserPanel(String username) {
        JFrame userFrame = new JFrame("User Panel");
        userFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        userFrame.setSize(400, 400);

        JPanel userPanel = createUserPanel(username);
        userFrame.add(userPanel);
        userFrame.setVisible(true);

        // after the user panel is created, create a FollowerVisitor
        User currentUser = userManager.getUserByUsername(username);
        FollowerVisitor followerVisitor = new FollowerVisitor(currentUser);

        // iterate followers and visit each user
        for (Follower follower : followers) {
            User userToFollow = userManager.getUserByUsername(follower.getUsername());
            if (userToFollow != null) {
                userToFollow.accept(followerVisitor);
            }
        }
    }

    private DefaultMutableTreeNode findGroupNode(String uniqueID) {

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            if (node.getUserObject().equals(uniqueID)) {
                return node;
            }
        }
        return null;
    }

    private void addButtonToPanel(JPanel panel, JButton button, int x, int y, double weightx) {
        GridBagConstraints gbc = new GridBagConstraints();
        button.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = weightx;
        panel.add(button, gbc);
    }

    private void addComponentToPanel(JPanel panel, JComponent component, int x, int y, double weightx) {
        GridBagConstraints gbc = new GridBagConstraints();
        component.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = weightx;
        panel.add(component, gbc);
    }
}