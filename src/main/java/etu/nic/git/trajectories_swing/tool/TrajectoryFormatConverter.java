package etu.nic.git.trajectories_swing.tool;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import etu.nic.git.trajectories_swing.file.FileDataTool;
import etu.nic.git.trajectories_swing.file.TrajectoryFile;
import etu.nic.git.trajectories_swing.model.TrajectoryRow;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TrajectoryFormatConverter {
    public static TrajectoryFile createTrajectoryFileFromJson(String jsonTrajectory) {
        Gson gson = new Gson();

        Trajectory trajectory = gson.fromJson(jsonTrajectory, Trajectory.class);

        StringBuilder trajectoryFileText = new StringBuilder();
        for (TrajectoryPoint point : trajectory.getTrajectoryPoints()) {
            TrajectoryRow row = new TrajectoryRow(point);
            trajectoryFileText.append(row.toFileString());
            trajectoryFileText.append("\n");
        }
        return new TrajectoryFile(
                trajectory.getId(),
                trajectory.getFileName(),
                TrajectoryFile.TRAJECTORY_NAME_PREFIX + TrajectoryFile.getNextTrajectoryIndex(),
                trajectoryFileText.toString()
        );
    }

    public static List<TrajectoryFile> createTrajectoryFileListFromJson(String jsonListOfTrajectories) {
        Gson gson = new Gson();
        List<TrajectoryFile> trajectoryFiles = new ArrayList<>();

        Type listType = new TypeToken<List<Trajectory>>() {}.getType();
        List<Trajectory> trajectories = gson.fromJson(jsonListOfTrajectories, listType);

        for (Trajectory trajectory : trajectories) {
            StringBuilder trajectoryFileText = new StringBuilder();
            for (TrajectoryPoint point : trajectory.getTrajectoryPoints()) {
                TrajectoryRow row = new TrajectoryRow(point);
                trajectoryFileText.append(row.toFileString());
                trajectoryFileText.append("\n");
            }
            trajectoryFiles.add(new TrajectoryFile(
                    trajectory.getId(),
                    trajectory.getFileName(),
                    TrajectoryFile.TRAJECTORY_NAME_PREFIX + TrajectoryFile.getNextTrajectoryIndex(),
                    trajectoryFileText.toString()
            ));
            TrajectoryFile.incrementTrajectoryIndex();
        }

        return trajectoryFiles;
    }

    public static class Trajectory {
        private Long id;

        private String fileName;

        private List<TrajectoryPoint> trajectoryPoints = new ArrayList<>();

        public Trajectory() {
        }

        public Trajectory(TrajectoryFile trajectoryFile) {
            String path = validateAndReturnPath(trajectoryFile.getPath());
            List<TrajectoryRow> trajectoryRowList = FileDataTool.parseToTrajectoryRowList(trajectoryFile.getData());
            List<TrajectoryPoint> trajectoryPointList = new ArrayList<>();
            for (TrajectoryRow row : trajectoryRowList) {
                trajectoryPointList.add(new TrajectoryPoint(row));
            }

            this.id = trajectoryFile.getId();
            this.fileName = path;
            this.trajectoryPoints = trajectoryPointList;
        }

        public Trajectory(String fileName, List<TrajectoryPoint> trajectoryPoints) {
            this.fileName = fileName;
            this.trajectoryPoints = trajectoryPoints;
        }

        private String validateAndReturnPath(String path) {
            if (FileDataTool.isValidPath(path)) {
                Path pathToFile = Paths.get(path);

                String fileNameWithExt = pathToFile.getFileName().toString(); // Получаем "xxx.txt"

                // Убираем расширение
                int dotIndex = fileNameWithExt.lastIndexOf(".");
                return (dotIndex == -1) ? fileNameWithExt : fileNameWithExt.substring(0, dotIndex);
            } else {
                return path;
            }
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public List<TrajectoryPoint> getTrajectoryPoints() {
            return trajectoryPoints;
        }

        public void setTrajectoryPoints(List<TrajectoryPoint> trajectoryPoints) {
            this.trajectoryPoints.clear();
            this.trajectoryPoints.addAll(trajectoryPoints);
        }
    }

    public static class TrajectoryPoint {
        private double time;
        private double coordinateX;
        private double coordinateY;
        private double coordinateZ;
        private double velocityX;
        private double velocityY;
        private double velocityZ;

        public TrajectoryPoint() {
        }

        public TrajectoryPoint(double time, double CoordinateX, double CoordinateY, double CoordinateZ, double VelocityX, double VelocityY, double VelocityZ) {
            this.time = time;
            this.coordinateX = CoordinateX;
            this.coordinateY = CoordinateY;
            this.coordinateZ = CoordinateZ;
            this.velocityX = VelocityX;
            this.velocityY = VelocityY;
            this.velocityZ = VelocityZ;
        }

        public TrajectoryPoint(TrajectoryRow trajectoryRow) {
            this(
                    trajectoryRow.getTime(),
                    trajectoryRow.getCoordinateX(),
                    trajectoryRow.getCoordinateY(),
                    trajectoryRow.getCoordinateZ(),
                    trajectoryRow.getVelocityX(),
                    trajectoryRow.getVelocityY(),
                    trajectoryRow.getVelocityZ()
            );
        }

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
        }

        public double getCoordinateX() {
            return coordinateX;
        }

        public void setCoordinateX(double coordinateX) {
            this.coordinateX = coordinateX;
        }

        public double getCoordinateY() {
            return coordinateY;
        }

        public void setCoordinateY(double coordinateY) {
            this.coordinateY = coordinateY;
        }

        public double getCoordinateZ() {
            return coordinateZ;
        }

        public void setCoordinateZ(double coordinateZ) {
            this.coordinateZ = coordinateZ;
        }

        public double getVelocityX() {
            return velocityX;
        }

        public void setVelocityX(double velocityX) {
            this.velocityX = velocityX;
        }

        public double getVelocityY() {
            return velocityY;
        }

        public void setVelocityY(double velocityY) {
            this.velocityY = velocityY;
        }

        public double getVelocityZ() {
            return velocityZ;
        }

        public void setVelocityZ(double velocityZ) {
            this.velocityZ = velocityZ;
        }
    }
}
