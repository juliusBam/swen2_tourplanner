package at.fhtw.tourplanner.bl.service;

import at.fhtw.tourplanner.bl.MessageExtractor;
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

import java.io.IOException;
import java.util.List;

public class TourLogService {

    private final ModelConverter modelConverter;

    private final TourLogRepository tourLogRepository;

    private final MessageExtractor messageExtractor = new MessageExtractor();

    public interface TourLogsListener {
        void setTourLogs(List<TourLog> tourLogs);
    }

    public interface ReqErrorListener {
        void reactToError(String title, String msg);
    }

    public interface TourLogManipulationListener {
        void handleSuccess(TourLogManipulationOutput manipulationResponse);
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

    public void saveAsync(TourLog updatedTourLog, Long tourItemId, String errorBoxTitle, TourLogManipulationListener tourLogManipulationListener, ReqErrorListener reqErrorListener) {

        Call<TourLogManipulationResponseDto> apiCall = this.tourLogRepository.saveAsync(this.modelConverter.tourLogModelToDto(tourItemId, updatedTourLog));

        apiCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<TourLogManipulationResponseDto> call, Response<TourLogManipulationResponseDto> response) {

                if (response.body() != null) {

                    Platform.runLater(() -> {

                        TourLogManipulationOutput tourLogManipulationOutput = new TourLogManipulationOutput(
                                modelConverter.tourLogDtoToModel(response.body().getTourLog()),
                                modelConverter.tourStatsDtoToModel(response.body().getTourStats())
                        );

                        tourLogManipulationListener.handleSuccess(tourLogManipulationOutput);

                    });

                } else {

                    Platform.runLater(() -> {
                        //update application thread
                        try {

                            //todo log
                            String msg = messageExtractor.extractMessageTemplate(response.errorBody().string());

                            reqErrorListener.reactToError(errorBoxTitle,
                                    msg.isBlank() ? "Unknown error" : msg
                            );
                        } catch (IOException | NullPointerException e) {
                            reqErrorListener.reactToError(errorBoxTitle, "Unknown error");
                        }
                    });

                }

            }

            @Override
            public void onFailure(Call<TourLogManipulationResponseDto> call, Throwable throwable) {
                Platform.runLater(() -> {
                    //update application thread
                    reqErrorListener.reactToError(errorBoxTitle, throwable.getMessage());
                });
            }
        });
    }

    public TourLogManipulationOutput delete(Long tourItemId) {
        TourLogManipulationResponseDto tourLogManipulationResult = this.tourLogRepository.delete(tourItemId);

        return new TourLogManipulationOutput(
                this.modelConverter.tourLogDtoToModel(tourLogManipulationResult.getTourLog()),
                this.modelConverter.tourStatsDtoToModel(tourLogManipulationResult.getTourStats())
        );
    }

    public void deleteAsync(Long tourLogId, TourLogManipulationListener tourLogManipulationListener, ReqErrorListener reqErrorListener) {

        Call<TourLogManipulationResponseDto> apiCall = this.tourLogRepository.deleteAsync(tourLogId);

        apiCall.enqueue(new Callback<>() {

            private final String errorBoxTitle = "Error deleting the tour log";
            @Override
            public void onResponse(Call<TourLogManipulationResponseDto> call, Response<TourLogManipulationResponseDto> response) {

                if (response.body() != null) {

                    Platform.runLater(() -> {

                        TourLogManipulationOutput tourLogManipulationOutput = new TourLogManipulationOutput(
                                modelConverter.tourLogDtoToModel(response.body().getTourLog()),
                                modelConverter.tourStatsDtoToModel(response.body().getTourStats())
                        );

                        tourLogManipulationListener.handleSuccess(tourLogManipulationOutput);

                    });

                } else {

                    Platform.runLater(() -> {
                        //update application thread
                        try {

                            //todo log
                            String msg = messageExtractor.extractMessageTemplate(response.errorBody().string());

                            reqErrorListener.reactToError(this.errorBoxTitle,
                                    msg.isBlank() ? "Unknown error" : msg
                            );
                        } catch (IOException | NullPointerException e) {
                            reqErrorListener.reactToError(this.errorBoxTitle, "Unknown error");
                        }
                    });

                }

            }

            @Override
            public void onFailure(Call<TourLogManipulationResponseDto> call, Throwable throwable) {
                Platform.runLater(() -> {
                    //update application thread
                    reqErrorListener.reactToError(this.errorBoxTitle, throwable.getMessage());
                });
            }
        });

    }

    public void updateTourLogsAsync(Long tourId, TourLogsListener tourLogsListener, ReqErrorListener reqErrorListener) {

        Call<List<TourLogDto>> apiCall = this.tourLogRepository.getAllByTourAsync(tourId);
        apiCall.enqueue(new Callback<>() {
            private final String errorBoxTitle = "Error fetching the tour logs";

            @Override
            public void onResponse(Call<List<TourLogDto>> call, Response<List<TourLogDto>> response) {

                if (response.body() != null) {
                    Platform.runLater(() -> {
                        //update application thread
                        tourLogsListener.setTourLogs(response.body().stream().map(modelConverter::tourLogDtoToModel).toList());
                    });
                } else {

                    Platform.runLater(() -> {
                        //update application thread
                        try {

                            //todo log
                            String msg = messageExtractor.extractMessageTemplate(response.errorBody().string());

                            reqErrorListener.reactToError(this.errorBoxTitle,
                                    msg.isBlank() ? "Unknown error" : msg
                            );
                        } catch (IOException | NullPointerException e) {
                            reqErrorListener.reactToError(this.errorBoxTitle, "Unknown error");
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<TourLogDto>> call, Throwable throwable) {
                Platform.runLater(() -> {
                    //update application thread
                    reqErrorListener.reactToError(this.errorBoxTitle, throwable.getMessage());
                });
            }
        });
    }

}
