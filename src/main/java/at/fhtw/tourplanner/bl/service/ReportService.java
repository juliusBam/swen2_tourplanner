package at.fhtw.tourplanner.bl.service;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.dal.repository.ReportRepository;
import javafx.stage.DirectoryChooser;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public void downloadSummaryReport(String absolutePath) {
        Call<ResponseBody> callReport = reportRepository.getSummaryReport();
        callReport.timeout().timeout(3, TimeUnit.MINUTES);
        callReport.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try (FileOutputStream stream = new FileOutputStream(absolutePath)) {
                    byte[] bytes = response.body().bytes();
                    stream.write(bytes);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                // TODO: handle this
                System.out.println("Summary report request timed out!");
            }
        });

    }

    public void downloadDetailReport(TourItem tourItem, String sessionId, String absolutePath) {
        Call<ResponseBody> callReport = reportRepository.getReport(tourItem.getId(), sessionId);
        callReport.timeout().timeout(3, TimeUnit.MINUTES);
        callReport.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try (FileOutputStream stream = new FileOutputStream(absolutePath)) {
                    byte[] bytes = response.body().bytes();
                    stream.write(bytes);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                // TODO: handle this
                System.out.println("Individual report request timed out!");
            }
        });
    }
}
