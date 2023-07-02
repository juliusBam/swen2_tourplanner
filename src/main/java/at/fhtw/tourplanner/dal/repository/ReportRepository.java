package at.fhtw.tourplanner.dal.repository;

import at.fhtw.tourplanner.dal.api.TourPlannerAPI;
import javafx.scene.image.Image;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReportRepository {

    TourPlannerAPI api;

    public ReportRepository(TourPlannerAPI api) {
        this.api = api;
    }

    public Call<ResponseBody> getReport(Long id, String sessionId) {
        return api.getReport(id, sessionId);
    }

    public Call<ResponseBody> getSummaryReport() {
        return api.getSummaryReport();
    }
}
