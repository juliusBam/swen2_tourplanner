package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.bl.service.TourLogService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

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
    //endregion
    public TourLogsTabViewModel(TourLogService tourLogService) {
        this.tourLogService = tourLogService;
    }

    public ObservableList<TourLog> getObservableTourLogs() {
        return observableTourLogs;
    }

    public void setTourModel(TourItem selectedTour) {
        this.selectedTourItem = selectedTour;
        this.observableTourLogs.clear();
        if (selectedTour != null) {
            this.observableTourLogs.addAll(selectedTour.getTourLogs());
        }

    }

    //todo implement via tourLogService
    public void addNewTour(TourItem tourItem) {
        //TourItem savedItem = tourItemService.create(tourItem);
        //observableTourItems.add(savedItem);
    }

    public void editTour() {
        System.out.println("Editing tour");
        this.selectedTourItem.updateFields(
                "New cool name",
                selectedTourItem.getFrom(),
                selectedTourItem.getTo(),
                selectedTourItem.getDescription(),
                selectedTourItem.getTransportType()
        );
    }

    public void deleteTour(TourItem tourItem) {
        //tourItemService.delete(tourItem);
        //observableTourItems.remove(tourItem);
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
