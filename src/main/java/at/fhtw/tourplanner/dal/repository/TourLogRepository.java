package at.fhtw.tourplanner.dal.repository;

import at.fhtw.tourplanner.dal.api.TourPlannerAPI;
import at.fhtw.tourplanner.dal.dto.TourLogDto;
import at.fhtw.tourplanner.dal.dto.TourLogManipulationResponseDto;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class TourLogRepository {
    private final TourPlannerAPI api;

    public TourLogRepository(TourPlannerAPI api) {
        this.api = api;
    }

    public TourLogManipulationResponseDto save(TourLogDto tourLogDto) throws RuntimeException {
        //here we distinguish between put and post
        if (tourLogDto.getId() == null) {
            try {
                Response<TourLogManipulationResponseDto> response = api.createTourLog(tourLogDto).execute();
                return response.body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                Response<TourLogManipulationResponseDto> response = api.updateTourLog(tourLogDto).execute();
                return response.body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Call<TourLogManipulationResponseDto> saveAsync(TourLogDto tourLogDto) {

        if (tourLogDto.getId() == null) {
            return this.api.createTourLog(tourLogDto);
        }

        return this.api.updateTourLog(tourLogDto);

    }

    public TourLogManipulationResponseDto delete(Long tourLogId) {
        try {
            Response<TourLogManipulationResponseDto> response = api.deleteTourLog(tourLogId).execute();
            return response.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Call<TourLogManipulationResponseDto> deleteAsync(Long tourLogId) {
        return this.api.deleteTourLog(tourLogId);
    }

    public List<TourLogDto> getAllByTour(Long tourId) {
        try {
            Response<List<TourLogDto>> response = api.getTourLogsByTourId(tourId).execute();
            return response.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Call<List<TourLogDto>> getAllByTourAsync(Long tourId) {
        return api.getTourLogsByTourId(tourId);
    }

}
