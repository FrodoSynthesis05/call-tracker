/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Julian
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ConfigReader {

private String executablePath;
private String destinationFolder;
private String iconPackPath;
private String APIKey;
private String tesseract;

public ConfigReader(String filePath) {
    readConfigFile(filePath);
}

public String getExecutablePath() {
    return executablePath;
}

public String getDestinationFolder() {
    return destinationFolder;
}

public String getIconPackPath() {
    return iconPackPath;
}

public String getAPIKey() {
    return APIKey;
}

public String getTesseract() {
    return tesseract;
}

private void readConfigFile(String filePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        executablePath = reader.readLine().replace("\\", "//");
        destinationFolder = reader.readLine().replace("\\", "//");
        iconPackPath = reader.readLine().replace("\\", "//");
        APIKey = reader.readLine();
        tesseract = reader.readLine().replace("\\", "//");
    } catch (IOException e) {
        System.out.println("An error occurred while reading the config.txt file.");
    }
}

}
