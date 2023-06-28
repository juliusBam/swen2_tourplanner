package at.fhtw.tourplanner.dal.api;

import at.fhtw.tourplanner.dal.dto.TourItemDto;
import at.fhtw.tourplanner.dal.dto.TourLogDto;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.concurrent.Executor;

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

    @POST("tourlog")
    Call<TourItemDto> createTourLog(@Body TourLogDto tourLogDto);

    @PUT("tourlog")
    Call<TourItemDto> updateTourLog(@Body TourLogDto tourLogDto);

    @DELETE("tourlog/{id}")
    Call<TourItemDto> deleteTourLog(@Path("id") Long tourLogId);

    @GET("tourlog/{id}")
    Call<List<TourLogDto>> getTourLogsByTourId(@Path("id") Long tourId);
}
