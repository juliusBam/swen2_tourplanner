package at.fhtw.tourplanner.dal.repository;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.dal.api.TourPlannerAPI;
import at.fhtw.tourplanner.dal.dto.TourItemDto;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class TourItemRepository implements Repository<TourItemDto, Long> {

    TourPlannerAPI api;

    public TourItemRepository(TourPlannerAPI api) {
        this.api = api;
    }

    @Override
    public TourItemDto save(TourItemDto entity) {
        System.out.println("help");
        if (entity.getId() == null) {
            try {
                Response<TourItemDto> response = api.postTour(entity).execute();
                return response.body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                Response<TourItemDto> response = api.putTour(entity).execute();
                return response.body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public TourItemDto findOne(Long primaryKey) {
        try {
            Response<TourItemDto> response = api.getTour(primaryKey).execute();
            return response.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TourItemDto> findAll() {
        try {
            Response<List<TourItemDto>> response = api.getTours().execute();
            return response.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long count() {
        try {
            Response<Long> response = api.getTourCount().execute();
            return response.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(TourItemDto entity) {
        try {
            api.deleteTour(entity.getId()).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exists(Long primaryKey) {
        TourItemDto tourItemDto = findOne(primaryKey);
        return tourItemDto != null;
    }

    @Override
    public Call<TourItemDto> findOneAsync(Long tourId) {
        return api.getTour(tourId);
    }
}
