package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.service.TourItemService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CenterPaneViewModel {

    private final TourItemService tourItemService;
    private final StringProperty tourDistance = new SimpleStringProperty();
    private final StringProperty estimatedTime = new SimpleStringProperty();
    private TourItem tourItem;
    private volatile boolean isInitValue = true;

    public CenterPaneViewModel(TourItemService tourItemService) {
        this.tourItemService = tourItemService;


    }

    public StringProperty tourDistanceProperty() {
        return tourDistance;
    }

    public StringProperty estimatedTimeProperty() {
        return estimatedTime;
    }

    public void setTourModel(TourItem tourItem) {
        isInitValue = true;
        if (tourItem == null) {
            // select the first in the list
            tourDistance.set("");
            estimatedTime.set("");
            return;
        }
        this.tourItem = tourItem;
        tourDistance.setValue(tourItem.getTourDistanceKilometers());
        estimatedTime.setValue(tourItem.getEstimatedTimeSeconds());
        isInitValue = false;
    }

}
