package at.fhtw.tourplanner.dal.api;

import at.fhtw.tourplanner.dal.dto.TourItemDto;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface TourPlannerAPI {

    @GET("tour")
    Call<List<TourItemDto>> getTours();

    @GET("tour/{id}")
    Call<TourItemDto> getTour(@Path("id") Long id);

    @POST("tour")
    Call<TourItemDto> postTour(@Body TourItemDto tourItemDto);

    @PUT("tour")
    Call<TourItemDto> putTour(@Body TourItemDto tourItemDto);

    @DELETE("tour/{id}")
    Call<Void> deleteTour(@Path("id") Long id);

    @GET("tour/count")
    Call<Long> getTourCount();
}
