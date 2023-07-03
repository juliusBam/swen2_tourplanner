package at.fhtw.tourplanner.bl.service;

import at.fhtw.tourplanner.bl.ModelConverter;
import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.bl.model.TourLogManipulationOutput;
import at.fhtw.tourplanner.dal.dto.TourLogDto;
import at.fhtw.tourplanner.dal.dto.TourLogManipulationResponseDto;
import at.fhtw.tourplanner.dal.repository.TourLogRepository;
import javafx.application.Platform;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class TourLogService {

    private final ModelConverter modelConverter;

    private final TourLogRepository tourLogRepository;

    public interface TourLogsListener {
        public void setTourLogs(List<TourLog> tourLogs);
    }

    public TourLogService(TourLogRepository tourLogRepository) {
        this.tourLogRepository = tourLogRepository;
        this.modelConverter = new ModelConverter();
    }

    public List<TourLog> getByTourId(Long tourId) {
        List<TourLogDto> tourLogs = this.tourLogRepository.getAllByTour(tourId);
        return tourLogs.stream().map(this.modelConverter::tourLogDtoToModel).toList();
    }

    public TourLogManipulationOutput create(TourLog newTourLog, Long tourItemId) {
        TourLogManipulationResponseDto tourLogManipulationResult = this.tourLogRepository.save(this.modelConverter.tourLogModelToDto(tourItemId, newTourLog));

        return new TourLogManipulationOutput(
                this.modelConverter.tourLogDtoToModel(tourLogManipulationResult.getTourLog()),
                this.modelConverter.tourStatsDtoToModel(tourLogManipulationResult.getTourStats())
        );
        //return this.modelConverter.tourItemDtoToModel(tourItemDto);
    }

    public TourLogManipulationOutput update(TourLog newTourLog, Long tourItemId) {
        TourLogManipulationResponseDto tourLogManipulationResult = this.tourLogRepository.save(this.modelConverter.tourLogModelToDto(tourItemId, newTourLog));
        return new TourLogManipulationOutput(
                this.modelConverter.tourLogDtoToModel(tourLogManipulationResult.getTourLog()),
                        this.modelConverter.tourStatsDtoToModel(tourLogManipulationResult.getTourStats())
        );
        //return this.modelConverter.tourItemDtoToModel(tourItemDto);
    }

    public TourLogManipulationOutput delete(Long tourItemId) {
        TourLogManipulationResponseDto tourLogManipulationResult = this.tourLogRepository.delete(tourItemId);

        return new TourLogManipulationOutput(
                this.modelConverter.tourLogDtoToModel(tourLogManipulationResult.getTourLog()),
                this.modelConverter.tourStatsDtoToModel(tourLogManipulationResult.getTourStats())
        );
        //return this.modelConverter.tourItemDtoToModel(tourItemDto);
    }

    public void updateTourLogsAsync(Long tourId, TourLogsListener tourLogsListener) {

        Call<List<TourLogDto>> apiCall = this.tourLogRepository.getAllByTourAsync(tourId);
        apiCall.enqueue(new Callback<List<TourLogDto>>() {
            @Override
            public void onResponse(Call<List<TourLogDto>> call, Response<List<TourLogDto>> response) {
                ModelConverter modelConverter = new ModelConverter();

                if (response.body() != null) {
                    Platform.runLater(() -> {
                        //update application thread
                        tourLogsListener.setTourLogs(response.body().stream().map(modelConverter::tourLogDtoToModel).toList());
                    });
                }
            }

            @Override
            public void onFailure(Call<List<TourLogDto>> call, Throwable throwable) {
                System.out.println("Error");
            }
        });
    }

}
