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
    @Multipart
    Call<TourItemDto> postTour();

    @PUT("tour/{id}")
    Call<TourItemDto> putTour(@Path("id") Long id, @Part TourItemDto tourItemDto, @Part Byte[] image);

    @DELETE("tour/{id}")
    Call<TourItemDto> deleteTour(@Path("id") Long id);

    @GET("tour/count")
    Call<Long> getTourCount();
}
