package at.fhtw.tourplanner.dal.repository;

import at.fhtw.tourplanner.dal.api.TourPlannerAPI;
import at.fhtw.tourplanner.dal.dto.TourItemDto;
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
        if (entity.getId() == null) {
            try {
                Response<TourItemDto> response = api.postTour().execute();
                return response.body();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                Byte[] image = new Byte[100];
                Response<TourItemDto> response = api.putTour(entity.getId(), entity, image).execute();
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
}
