package at.fhtw.tourplanner.bl.service;

import at.fhtw.tourplanner.dal.api.MapQuestAPI;
import at.fhtw.tourplanner.dal.api.response.RouteResponse;
import javafx.scene.image.Image;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MapQuestService {
    MapQuestAPI api;

    String key = "SECRET";

    Map<String, String> constantsMap = new HashMap<>();

    public MapQuestService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.mapquestapi.com/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        api = retrofit.create(MapQuestAPI.class);
        constantsMap.put("key", key);
        constantsMap.put("unit", "k");
        constantsMap.put("size", "600,400@2x");
    }

    public RouteResponse searchRoute(String from, String to, String transportType) {
        try {
            Call<RouteResponse> call = api.getRoute(key, from, to, transportType);
            Response<RouteResponse> response = call.execute();
            return response.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Image fetchRouteImage(String from, String to, String boundingBox) {
        try {
            String markerLocations = from + "|marker-start" + "||" + to + "|marker-end";

            Call<ResponseBody> call = api.getImage(constantsMap, markerLocations, boundingBox);
            Response<ResponseBody> response = call.execute();
            ResponseBody responseBody = response.body();
            byte[] bytes = responseBody.bytes();
            InputStream inputStream = new ByteArrayInputStream(bytes);
            return new Image(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Call<ResponseBody> fetchRouteImageAsync(String from, String to, String boundingBox) {

            String markerLocations = from + "|marker-start" + "||" + to + "|marker-end";

            return api.getImage(constantsMap, markerLocations, boundingBox);
    }
}
