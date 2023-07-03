package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.TimeConverter;
import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.bl.model.TourLogManipulationOutput;
import at.fhtw.tourplanner.bl.model.TourStats;
import at.fhtw.tourplanner.bl.service.TourLogService;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

public class TourLogsTabViewModel {

    private final TourLogService tourLogService;

    private final ObservableList<TourLog> observableTourLogs = FXCollections.observableArrayList();

    @Getter
    private TourItem selectedTourItem;

    private TourLog selectedTourLog;

    //region properties
    @Getter
    private final StringProperty dateProperty = new SimpleStringProperty();

    @Getter
    private final StringProperty commentProperty = new SimpleStringProperty();

    @Getter
    private final IntegerProperty ratingProperty = new SimpleIntegerProperty();

    @Getter
    private final IntegerProperty difficultyProperty = new SimpleIntegerProperty();

    @Getter
    private final StringProperty timeProperty = new SimpleStringProperty();

    @Getter
    private final BooleanProperty loadingTourLogs = new SimpleBooleanProperty();

    @Getter
    private final BooleanProperty tourLogSelected = new SimpleBooleanProperty(false);
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

            this.tourLogService.updateTourLogsAsync(selectedTour.getId(), this::setTourLogs);

        }

    }

    public void setTourLogs(List<TourLog> tourLogs) {
        loadingTourLogs.set(false);
        observableTourLogs.addAll(tourLogs);
    }

    public void addNewTourLog(TourLog tourLog) {
        TourLogManipulationOutput manipulationResponse = tourLogService.create(tourLog, this.selectedTourItem.getId());

        this.updateTourStatistics(manipulationResponse.tourStats());
        this.observableTourLogs.add(manipulationResponse.tourLog());
        this.selectedTourItem.addNewTourLog(manipulationResponse.tourLog());

    }

    public void updateTourLog(TourLog updatedTourLog) {
        TourLogManipulationOutput manipulationResponse = tourLogService.update(updatedTourLog, this.selectedTourItem.getId());

        this.updateTourStatistics(manipulationResponse.tourStats());
        this.selectedTourLog = manipulationResponse.tourLog();
        this.setSelectedTourLog(selectedTourLog);

        Optional<TourLog> tourLogToSelect = this.getObservableTourLogs().stream().filter((tourLog -> tourLog.getId().equals(updatedTourLog.getId()))).findFirst();
        if (tourLogToSelect.isPresent()) {
            int indexOfItem = this.getObservableTourLogs().indexOf(tourLogToSelect.get());

            this.observableTourLogs.remove(indexOfItem);
            this.observableTourLogs.add(manipulationResponse.tourLog());

        }
        this.selectedTourItem.updateTourLog(manipulationResponse.tourLog());
    }


    public void deleteTourLog() {
        TourLogManipulationOutput manipulationResponse = tourLogService.delete(this.selectedTourLog.getId());

        this.selectedTourItem.removeTourLog(this.selectedTourLog);
        this.updateTourStatistics(manipulationResponse.tourStats());
        this.observableTourLogs.remove(this.selectedTourLog);

    }

    public ChangeListener<TourLog> getChangeListener() {
        return (observableValue, oldValue, newValue) -> setSelectedTourLog(newValue);
    }

    private void setSelectedTourLog(TourLog tourLog) {
        this.selectedTourLog = tourLog;
        if (tourLog == null) {
            this.tourLogSelected.set(false);
            this.dateProperty.set("");
            this.commentProperty.set("");
            this.difficultyProperty.set(0);
            this.ratingProperty.set(0);
            this.timeProperty.set("");
        } else {
            this.dateProperty.set(
                    TimeConverter.convertTimeStampToString("dd-MM-yyyy HH:mm", tourLog.getTimeStamp())
            );
            this.tourLogSelected.set(true);
            this.commentProperty.set(tourLog.getComment());
            this.difficultyProperty.set(tourLog.getDifficulty());
            this.ratingProperty.set(tourLog.getRating());
            this.timeProperty.set(tourLog.getTotalTimeMinutes().toString());
            System.out.println("Tour log changed, clicked");
            System.out.println("New tour log is: " + tourLog.getComment());
        }
    }

    private void updateTourStatistics(TourStats tourStats) {
        this.selectedTourItem.setTourStats(tourStats);
    }

}
