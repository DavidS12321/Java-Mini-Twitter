public class FollowerVisitor implements UserElementVisitor {
    private User currentUser;

    public FollowerVisitor(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void visit(User user) {
        if (!currentUser.equals(user)) {
            currentUser.addFollowing(user);
        }
    }

    @Override
    public void visit(UserGroup userGroup) {
    }
}