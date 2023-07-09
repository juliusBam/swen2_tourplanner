package at.fhtw.tourplanner.dal.api;

import at.fhtw.tourplanner.dal.dto.TourItemDto;
import at.fhtw.tourplanner.dal.dto.TourLogManipulationResponseDto;
import okhttp3.ResponseBody;
import at.fhtw.tourplanner.dal.dto.TourLogDto;
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

    @GET("report/{id}")
    Call<ResponseBody> getReport(@Path("id") Long id, @Query("sessionId") String session);

    @GET("report/summarize")
    Call<ResponseBody> getSummaryReport();

    @POST("tourlog")
    Call<TourLogManipulationResponseDto> createTourLog(@Body TourLogDto tourLogDto);

    @PUT("tourlog")
    Call<TourLogManipulationResponseDto> updateTourLog(@Body TourLogDto tourLogDto);

    @DELETE("tourlog/{id}")
    Call<TourLogManipulationResponseDto> deleteTourLog(@Path("id") Long tourLogId);

    @GET("tourlog/{id}")
    Call<List<TourLogDto>> getTourLogsByTourId(@Path("id") Long tourId);
}
