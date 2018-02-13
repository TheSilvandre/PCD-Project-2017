import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Simple config reader.
 */
public class ConfigReader {

    private String configPath;
    private String serverName;
    private Integer serverPort;

    /**
     * Constructor
     * @param configPath Configuration file name with extension
     */
    public ConfigReader(String configPath) {
        this.configPath = configPath;
    }

    /**
     * Loads the values from the config file into memory.
     * Can close the program if the config file is not found or if there is an error reading the file.
     */
    public void loadValues(){
        try {
            FileReader fileReader = new FileReader(configPath);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            serverName = bufferedReader.readLine();
            serverPort = Integer.valueOf(bufferedReader.readLine());

            bufferedReader.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Could not find config file " + configPath + "!");
            System.exit(0);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file " + configPath + "!");
            System.exit(0);
        }
    }

    public String getServerName(){
        return serverName;
    }

    public Integer getServerPort(){
        return serverPort;
    }
}
