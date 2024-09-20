package etu.nic.git.trajectories_swing.file;

import etu.nic.git.trajectories_swing.model.TrajectoryRow;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, отвечающий за чтение из файла и запись в файл траекторной информации
 */
public class FileDataLoader {
    /**
     * Метод считывает данные из файла
     *
     * @param filePath путь до файла
     * @return данные из файла в виде строки
     */
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

    /**
     * Метод записывает данные в файл
     *
     * @param filePath путь до файла
     * @param data     данные, которые необходимо записать
     */
    public static void writeDataToFile(String filePath, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод парсит строку файловых данных и выдает список траекторной информации на ее основе
     *
     * @param fileData данные файла
     * @return список строк траектории
     */
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
