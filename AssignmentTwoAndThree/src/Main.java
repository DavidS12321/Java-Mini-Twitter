public class Main {
    public static void main(String[] args) {
        UserManager userManager = UserManager.getInstance();
        AdminPanel panel = new AdminPanel(userManager);
        panel.launchPanel();
    }
}