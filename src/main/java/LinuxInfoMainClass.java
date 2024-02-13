import javafx.application.Platform;

public class LinuxInfoMainClass {
    public static void main(String[] args) {
        Platform.startup(() -> {
            Gui gui = new Gui();
            gui.initialize();
            gui.requestFocus();
            gui.show();
        });
    }
}
