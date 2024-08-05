package etu.nic.git.trajectories_swing.tools;

import etu.nic.git.trajectories_swing.model.TrajectoryRow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileDataLoader {
    public static String loadDataFromFile(String fileName) {
        List<String> rawFileLines = new ArrayList<>();
        StringBuilder fileText = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
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

    public static String[][] plainDataToArray(String data) {
        return null;
    }
}
