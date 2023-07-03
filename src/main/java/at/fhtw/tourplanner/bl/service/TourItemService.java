package at.fhtw.tourplanner.bl.service;


import at.fhtw.tourplanner.bl.ModelConverter;
import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.bl.model.TourStats;
import at.fhtw.tourplanner.dal.dto.TourItemDto;
import at.fhtw.tourplanner.dal.repository.TourItemRepository;
import javafx.application.Platform;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class TourItemService implements Service<TourItem> {

    private final TourItemRepository tourItemRepository;

    private final ModelConverter modelConverter;

    public interface TourStatsListener {
        void updateStats(TourStats tourStats);
    }

    public interface ErrorListener {
        void onError(String errorMsg);
    }

    public TourItemService(TourItemRepository tourItemRepository) {
        this.tourItemRepository = tourItemRepository;
        this.modelConverter = new ModelConverter();
    }


    @Override
    public List<TourItem> getAll() {
        List<TourItemDto> tourItemDtos = tourItemRepository.findAll();
        return tourItemDtos.stream().map(this.modelConverter::tourItemDtoToModel).toList();
    }

    @Override
    public TourItem create(TourItem tourItem) {
        TourItemDto tourItemDto = tourItemRepository.save(this.modelConverter.tourItemModelToDto(tourItem));
        return this.modelConverter.tourItemDtoToModel(tourItemDto);
    }

    @Override
    public void delete(TourItem tourItem) {
        tourItemRepository.delete(this.modelConverter.tourItemModelToDto(tourItem));
    }

    @Override
    public void update(TourItem tourItem) {
        tourItemRepository.save(this.modelConverter.tourItemModelToDto(tourItem));
    }

    @Override
    public TourItem findOne(Long tourId) {
        TourItemDto tourItemDto = this.tourItemRepository.findOne(tourId);
        return this.modelConverter.tourItemDtoToModel(tourItemDto);
    }

    @Override
    public Call<TourItemDto> findOneAsync(Long tourId) {
        return this.tourItemRepository.findOneAsync(tourId);
    }

    public void setTourStats(Long id, TourStatsListener tourStatsListener, ErrorListener errorListener) {

        Call<TourItemDto> apiCall = findOneAsync(id);
        apiCall.enqueue(new Callback<TourItemDto>() {
            @Override
            public void onResponse(Call<TourItemDto> call, Response<TourItemDto> response) {
                if (response.body() != null) {
                    Platform.runLater(() -> {
                        //update application thread
                        TourStats fetchedTourStats = new ModelConverter().tourItemDtoToModel(response.body()).getTourStats();

                        tourStatsListener.updateStats(fetchedTourStats);

                    });
                } else {

                    Platform.runLater(() -> {
                        //update application thread
                        try {
                            errorListener.onError(response.errorBody().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    });

                }
            }

            @Override
            public void onFailure(Call<TourItemDto> call, Throwable throwable) {
                //todo print error
                Platform.runLater(() -> {
                    //update application thread
                    errorListener.onError(throwable.getMessage());

                });

            }
        });

    }
}
