package etu.nic.git.trajectories_swing.file_handling;

import etu.nic.git.trajectories_swing.model.TrajectoryRow;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileDataLoader {
    public static String readDataFromFile(String filePath) {
        List<String> rawFileLines = new ArrayList<>();
        StringBuilder fileText = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while (reader.ready()) {
                rawFileLines.add(reader.readLine());
            }
            for (String line : rawFileLines) {
                fileText.append(line);
                fileText.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileText.toString();
    }
    public static void writeDataToFile(String filePath, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<TrajectoryRow> parseToTrajectoryRowList(String fileData) {
        String[] lines = fileData.split("\n");
        List<TrajectoryRow> trajectoryRowList = new ArrayList<>();
        for (String line : lines) {
            if (TrajectoryRow.isValidTrajectoryString(line)) {
                trajectoryRowList.add(TrajectoryRow.buildTrajectoryRowFromString(line));
            }
        }
        return trajectoryRowList;
    }
}
