package at.fhtw.tourplanner.bl.service;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.dal.repository.ReportRepository;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.FileOutputStream;
import java.io.IOException;

public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public void downloadSummaryReport(String absolutePath) {
        Call<ResponseBody> callReport = reportRepository.getSummaryReport();
        handleDownload(absolutePath, callReport);

    }

    public void downloadDetailReport(TourItem tourItem, String sessionId, String absolutePath) {
        Call<ResponseBody> callReport = reportRepository.getReport(tourItem.getId(), sessionId);
        handleDownload(absolutePath, callReport);
    }

    private void handleDownload(String absolutePath, Call<ResponseBody> callReport) {
        callReport.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try (FileOutputStream stream = new FileOutputStream(absolutePath)) {
                    byte[] bytes = response.body().bytes();
                    stream.write(bytes);
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "The requested file has been downloaded.");
                        alert.setHeaderText("Download complete");
                        alert.showAndWait();
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Message: " + throwable.getMessage());
                    alert.setHeaderText("Download failed");
                    alert.showAndWait();
                });
            }
        });
    }
}
