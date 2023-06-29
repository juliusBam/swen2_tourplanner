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

    public byte[] getReport(Long id) {
        try {
            Response<ResponseBody> response = api.getReport(id).execute();
            ResponseBody responseBody = response.body();
            return responseBody.bytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getSummaryReport() {
        try {
            Response<ResponseBody> response = api.getSummaryReport().execute();
            ResponseBody responseBody = response.body();
            return responseBody.bytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
