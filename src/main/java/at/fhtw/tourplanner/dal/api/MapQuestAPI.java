package at.fhtw.tourplanner.dal.api;

import at.fhtw.tourplanner.dal.api.response.RouteResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapQuestAPI {

    @GET("directions/v2/route/")
    Call<RouteResponse> getRoute(@Query("key") String key, @Query("from") String from, @Query("to") String to);


}
