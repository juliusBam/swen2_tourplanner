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
import javafx.scene.control.Alert;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TourLogsTabViewModel {

    private final TourLogService tourLogService;

    private final ObservableList<TourLog> observableTourLogs = FXCollections.observableArrayList();

    @Getter
    private TourItem selectedTourItem;

    private TourLog selectedTourLog;

    private final List<UpdateSelectedTourLogListener> listeners = new ArrayList<>();
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

    @Getter
    private final ObjectProperty<TourLog> toggleTourLogSelection = new SimpleObjectProperty<>();
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

            this.tourLogService.updateTourLogsAsync(selectedTour.getId(), this::setTourLogs, this::handleRequestError);

        }

    }

    public void setTourLogs(List<TourLog> tourLogs) {
        loadingTourLogs.set(false);
        observableTourLogs.addAll(tourLogs);
    }

    public void addNewTourLog(TourLog tourLog) {

        this.tourLogService.saveAsync(tourLog, this.selectedTourItem.getId(), "Error creating the new tour log",  this::handleTourLogCreation, this::handleRequestError);

    }

    private void handleTourLogCreation(TourLogManipulationOutput manipulationResponse) {
        this.updateTourStatistics(manipulationResponse.tourStats());
        this.observableTourLogs.add(manipulationResponse.tourLog());
        this.selectedTourItem.addNewTourLog(manipulationResponse.tourLog());
        this.notifyUpdateSelectionListeners(manipulationResponse.tourLog());
    }

    public void updateTourLog(TourLog updatedTourLog) {

        this.tourLogService.saveAsync(updatedTourLog, this.selectedTourItem.getId(), "Error updating the new tour log", this::handleTourLogEdit, this::handleRequestError);

    }

    public void handleTourLogEdit(TourLogManipulationOutput manipulationResponse) {

        this.updateTourStatistics(manipulationResponse.tourStats());
        this.selectedTourLog = manipulationResponse.tourLog();
        this.setSelectedTourLog(selectedTourLog);

        Optional<TourLog> tourLogToSelect = this.getObservableTourLogs().stream().filter((tourLog -> tourLog.getId().equals(selectedTourLog.getId()))).findFirst();
        if (tourLogToSelect.isPresent()) {
            int indexOfItem = this.getObservableTourLogs().indexOf(tourLogToSelect.get());

            this.observableTourLogs.remove(indexOfItem);
            this.observableTourLogs.add(manipulationResponse.tourLog());

        }
        this.selectedTourItem.updateTourLog(manipulationResponse.tourLog());

        this.notifyUpdateSelectionListeners(manipulationResponse.tourLog());
    }


    public void deleteTourLog() {

        tourLogService.deleteAsync(this.selectedTourLog.getId(), this::handleTourLogDelete, this::handleRequestError);

    }

    public void handleTourLogDelete(TourLogManipulationOutput manipulationResponse) {
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
                    TimeConverter.convertTimeStampToString("dd/MM/yyyy", tourLog.getTimeStamp())
            );
            this.tourLogSelected.set(true);
            this.commentProperty.set(tourLog.getComment());
            this.difficultyProperty.set(tourLog.getDifficulty());
            this.ratingProperty.set(tourLog.getRating());
            this.timeProperty.set(String.format("%d:%02d (H:MM)",tourLog.getTotalTimeMinutes() / 60, tourLog.getTotalTimeMinutes() % 60));
        }
    }

    private void updateTourStatistics(TourStats tourStats) {
        this.selectedTourItem.setTourStats(tourStats);
    }

    private void handleRequestError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.setHeaderText(title);
        alert.showAndWait();
    }

    public void addSelectionChangedListener(UpdateSelectedTourLogListener listener) {
        listeners.add(listener);
    }

    private void notifyUpdateSelectionListeners(TourLog newVal) {
        for (var listener : listeners) {
            listener.changeSelection(newVal);
        }
    }

    public interface UpdateSelectedTourLogListener {
        void changeSelection(TourLog tourLog);
    }

}
