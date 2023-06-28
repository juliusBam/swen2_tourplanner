package at.fhtw.tourplanner.dal.repository;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.dal.api.TourPlannerAPI;
import at.fhtw.tourplanner.dal.dto.TourItemDto;
import at.fhtw.tourplanner.dal.dto.TourLogDto;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class TourLogRepository {
    private final TourPlannerAPI api;

    public TourLogRepository(TourPlannerAPI api) {
        this.api = api;
    }

    public TourItemDto save(TourLogDto tourLogDto) throws RuntimeException {
        //here we distinguish between put and post
        if (tourLogDto.getId() == null) {
            try {
                Response<TourItemDto> response = api.createTourLog(tourLogDto).execute();
                return response.body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                Response<TourItemDto> response = api.updateTourLog(tourLogDto).execute();
                return response.body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public TourItemDto delete(Long tourLogId) {
        try {
            Response<TourItemDto> response = api.deleteTourLog(tourLogId).execute();
            return response.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TourLogDto> getAllByTour(Long tourId) {
        try {
            Response<List<TourLogDto>> response = api.getTourLogsByTourId(tourId).execute();
            return response.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
