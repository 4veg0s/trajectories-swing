package etu.nic.git.trajectories_swing.tool;

import com.google.gson.Gson;
import etu.nic.git.trajectories_swing.file.TrajectoryFile;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpClient {
    private static final String BASE_SERVER_URL = "http://localhost:8888";
    private static final String BASE_API_URL = BASE_SERVER_URL + "/api";
    private static final String BASE_API_TRAJECTORIES_URL = BASE_API_URL + "/trajectories";

    static OkHttpClient client = new OkHttpClient();

    public static List<TrajectoryFile> getAllTrajectoryFiles() {
        // GET
        Request request = new Request.Builder()
                .url(BASE_API_TRAJECTORIES_URL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;

            String bodyStr = response.body().string();

            if (response.isSuccessful()) {
                List<TrajectoryFile> trajectoryFiles;
                if (bodyStr.isEmpty()) {
                    trajectoryFiles = new ArrayList<>();
                } else {
                    trajectoryFiles = TrajectoryFormatConverter.createTrajectoryFileListFromJson(bodyStr);
                }
                return trajectoryFiles;
            } else {
                throw new RuntimeException(response.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * POST - создать новую траекторию
     *
     * @param trajectoryFile данные траектории для создания
     * @return
     */
    public static boolean createTrajectoryFile(TrajectoryFile trajectoryFile) {
        Gson gson = new Gson();

        TrajectoryFormatConverter.Trajectory trajectory = new TrajectoryFormatConverter.Trajectory(trajectoryFile);
        String bodyJson = gson.toJson(trajectory);
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                bodyJson
        );

        Request request = new Request.Builder()
                .url(BASE_API_TRAJECTORIES_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;

            TrajectoryFormatConverter.Trajectory savedTrajectory = gson.fromJson(response.body().string(), TrajectoryFormatConverter.Trajectory.class);
            trajectoryFile.setId(savedTrajectory.getId());

            return response.isSuccessful();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * PUT - обновить существующую траекторию
     *
     * @param trajectoryFile данные траектории для обновления (предполагается наличие ID у файла)
     * @return
     */
    public static boolean updateTrajectoryFile(TrajectoryFile trajectoryFile) {
        Gson gson = new Gson();

        TrajectoryFormatConverter.Trajectory trajectory = new TrajectoryFormatConverter.Trajectory(trajectoryFile);
        String bodyJson = gson.toJson(trajectory);
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                bodyJson
        );

        Request request = new Request.Builder()
                .url(BASE_API_TRAJECTORIES_URL + "/" + trajectory.getId())
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;

            return response.isSuccessful();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DELETE - удалить существующую траекторию
     *
     * @param trajectoryFile данные траектории для удаления
     * @return успешность
     */
    public static boolean deleteTrajectoryFile(TrajectoryFile trajectoryFile) {
        TrajectoryFormatConverter.Trajectory trajectory = new TrajectoryFormatConverter.Trajectory(trajectoryFile);

        Request request = new Request.Builder()
                .url(BASE_API_TRAJECTORIES_URL + "/" + trajectory.getId())
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;

            return response.isSuccessful();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
