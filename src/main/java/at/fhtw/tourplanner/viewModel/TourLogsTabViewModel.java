package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.ModelConverter;
import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.bl.service.TourLogService;
import at.fhtw.tourplanner.dal.dto.TourLogDto;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class TourLogsTabViewModel {

    private final TourLogService tourLogService;

    private ObservableList<TourLog> observableTourLogs = FXCollections.observableArrayList();

    private TourItem selectedTourItem;
    private TourLog selectedTourLog;

    //region properties
    @Getter
    private final StringProperty dateProperty = new SimpleStringProperty();

    @Getter
    private final StringProperty commentProperty = new SimpleStringProperty();

    @Getter
    private final StringProperty ratingProperty = new SimpleStringProperty();

    @Getter
    private final StringProperty difficultyProperty = new SimpleStringProperty();

    @Getter
    private final StringProperty timeProperty = new SimpleStringProperty();

    @Getter
    private final BooleanProperty loadingTourLogs = new SimpleBooleanProperty();
    //endregion
    public TourLogsTabViewModel(TourLogService tourLogService) {
        this.tourLogService = tourLogService;
    }

    public ObservableList<TourLog> getObservableTourLogs() {
        return observableTourLogs;
    }

    public void setTourModel(TourItem selectedTour) {
        this.loadingTourLogs.set(true);
        this.selectedTourItem = selectedTour;
        this.observableTourLogs.clear();
        if (selectedTour != null) {
            Call<List<TourLogDto>> apiCall = this.tourLogService.getByTourIdAsync(selectedTour.getId());
            apiCall.enqueue(new Callback<List<TourLogDto>>() {
                @Override
                public void onResponse(Call<List<TourLogDto>> call, Response<List<TourLogDto>> response) {
                    ModelConverter modelConverter = new ModelConverter();

                    if (response.body() != null) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                //update application thread
                                System.out.println("Fetched with data");
                                loadingTourLogs.set(false);
                                observableTourLogs.addAll(response.body().stream().map(modelConverter::tourLogDtoToModel).toList());
                            }
                        });
                    }
                    System.out.println("Fetched");
                }

                @Override
                public void onFailure(Call<List<TourLogDto>> call, Throwable throwable) {
                    System.out.println("Error");
                }
            });
            //this.observableTourLogs.addAll(this.tourLogService.getByTourId(selectedTour.getId()));
        }

    }

    public void addNewTourLog(TourLog tourLog) {
        TourItem savedItem = tourLogService.create(tourLog, this.selectedTourItem.getId());
        this.setTourModel(savedItem);
    }

    public void updateTourLog(TourLog updatedTourLog) {
        TourItem savedItem = tourLogService.update(updatedTourLog, this.selectedTourItem.getId());
        this.setTourModel(savedItem);
    }


    public void deleteTourLog() {
        TourItem savedItem = tourLogService.delete(this.selectedTourLog.getId());
        this.setTourModel(savedItem);
    }

    public ChangeListener<TourLog> getChangeListener() {
        return (observableValue, oldValue, newValue) -> setSelectedTourLog(newValue);
    }


    private void setSelectedTourLog(TourLog tourLog) {
        this.selectedTourLog = tourLog;
        if (tourLog == null) {
            this.dateProperty.set("");
            this.commentProperty.set("");
            this.difficultyProperty.set("");
            this.ratingProperty.set("");
            this.timeProperty.set("");
        } else {
            this.dateProperty.set(tourLog.getTimeStamp().toString());
            this.commentProperty.set(tourLog.getComment());
            this.difficultyProperty.set(tourLog.getDifficulty());
            this.ratingProperty.set(tourLog.getRating());
            this.timeProperty.set(tourLog.getTotalTimeMinutes().toString());
            System.out.println("Tour log changed, clicked");
            System.out.println("New tour log is: " + tourLog.getComment());
        }
    }

}
