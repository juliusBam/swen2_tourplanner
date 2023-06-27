package at.fhtw.tourplanner.dal.api;

import at.fhtw.tourplanner.dal.api.response.RouteResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface MapQuestAPI {

    @GET("directions/v2/route")
    Call<RouteResponse> getRoute(@Query("key") String key, @Query("from") String from, @Query("to") String to, @Query("routeType") String transportType);

    @GET("staticmap/v5/map")
    Call<ResponseBody> getImage(@QueryMap Map<String, String> constantValues, @Query("locations") String markerLocations, @Query("boundingBox") String boundingBox);
}
