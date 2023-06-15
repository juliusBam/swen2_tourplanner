package at.fhtw.tourplanner.bl.service;

import at.fhtw.tourplanner.dal.api.MapQuestAPI;
import at.fhtw.tourplanner.dal.api.response.RouteResponse;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

public class MapQuestService {
    MapQuestAPI api;

    String key = "SECRET";

    public MapQuestService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.mapquestapi.com/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        api = retrofit.create(MapQuestAPI.class);
    }

    public RouteResponse searchRoute(String from, String to) {
        try {
            Call<RouteResponse> call = api.getRoute(key, from, to);
            Response<RouteResponse> response = call.execute();
            return response.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
