import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

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
        List<String> osInfo = getOsNameAndVersion();
        osInfo.add(getDisplaySessionName());
        osInfo.add(getTotalRam());
        for (String info : osInfo) {
            root.getChildren().add(new Label(info));
        }
        return new Scene(root);
    }

    /**
     *
     */
    private List<String> getOsNameAndVersion() {
        List<String> osInfo = new LinkedList<>();
        try {
            String s;
            BufferedReader br = new BufferedReader(new FileReader("/etc/os-release"));
            while ((s = br.readLine()) != null) {
                int startIndex;
                if(s.contains("NAME=")) {
                    startIndex = s.indexOf("=") + 1;
                    osInfo.add("OS: " + s.substring(startIndex).replace("\"", ""));
                } else if(s.contains("VERSION=")) {
                    startIndex = s.indexOf("=") + 1;
                    osInfo.add("Version: " + s.substring(startIndex).replace("\"", ""));
                }
                if(osInfo.size() == 2) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Unable to get os info");
        }
        return osInfo;
    }

    /**
     *
     */
    private String getDisplaySessionName() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", "echo ${XDG_SESSION_TYPE}");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            process.waitFor();
            String mode = reader.readLine();
            return "Display Server: " + mode;
        } catch (Exception exception) {
            System.out.println("Unable to get display server");
            return "";
        }
    }

    /**
     *
     */
    private String getTotalRam() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("/proc/meminfo"));
            String totalRam = br.readLine();
            // Strip to just digits
            String strippedString = totalRam.replaceAll("[^\\d.]", "");
            System.out.println(strippedString);
            long kb = Long.parseLong(strippedString);
            long gb = kb / 1000000;
            return "Total Ram: " + gb + " GB";
            // Number is in kb so convert to gb
        } catch (Exception e) {
            System.out.println("Unable to get ram size");
            return "";
        }
    }
}
