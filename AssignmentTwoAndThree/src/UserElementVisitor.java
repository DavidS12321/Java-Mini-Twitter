public interface UserElementVisitor {
    void visit(User user);
    void visit(UserGroup userGroup);
}