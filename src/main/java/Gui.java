import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;

/**
 *
 */
public class Gui extends Stage {

    /**
     *
     */
    public Gui() {
        super();
        setTitle("System Info");
        setScene(initialize());
    }

    /**
     *
     * @return
     */
    public Scene initialize() {
        VBox root = new VBox();
        // TODO move to css file
        root.setAlignment(Pos.TOP_CENTER);
        getOsNameAndVersion(root);
        getDisplaySessionName(root);
        return new Scene(root);
    }

    /**
     *
     */
    private void getOsNameAndVersion(VBox root) {
        String s;
        try {
            Label osLabel = new Label();
            Label versionLabel = new Label();
            BufferedReader br = new BufferedReader(new FileReader("/etc/os-release"));
            while ((s = br.readLine()) != null) {
                int startIndex;
                if(s.contains("NAME=")) {
                    startIndex = s.indexOf("=") + 1;
                    osLabel.setText("OS: " + s.substring(startIndex).replace("\"", ""));
                } else if(s.contains("VERSION=")) {
                    startIndex = s.indexOf("=") + 1;
                    versionLabel.setText("Version: " + s.substring(startIndex).replace("\"", ""));
                }
                if(!osLabel.getText().isEmpty() && !versionLabel.getText().isEmpty()){
                    break;
                }
            }
            root.getChildren().addAll(osLabel, versionLabel);
        } catch (Exception e) {

        }
    }

    /**
     *
     * @param root
     */
    private void getDisplaySessionName(VBox root) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", "echo ${XDG_SESSION_TYPE}");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            process.waitFor();
            String mode = reader.readLine();
            Label displayMode = new Label("Display Server: " + mode);
            root.getChildren().add(displayMode);
        } catch (Exception exception) {

        }
    }

    /**
     *
     */
    private void getTotalRam(VBox root) {

    }
}
