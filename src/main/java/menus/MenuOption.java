package menus;

public class MenuOption {
    private final String title;
    private final Runnable action;

    public MenuOption(String title, Runnable action) {
        this.title = title;
        this.action = action;
    }

    public String getTitle() {
        return title;
    }

    public void execute() {
        action.run();
    }
}
