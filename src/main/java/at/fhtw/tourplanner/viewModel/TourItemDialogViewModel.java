package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.service.MapQuestService;
import at.fhtw.tourplanner.dal.api.response.RouteResponse;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;


public class TourItemDialogViewModel {
    @Getter
    private final TourItem tourItem;
    @Getter
    private final StringProperty nameProperty = new SimpleStringProperty();
    @Getter
    private final StringProperty descriptionProperty = new SimpleStringProperty();
    @Getter
    private final StringProperty fromProperty = new SimpleStringProperty();
    @Getter
    private final StringProperty toProperty = new SimpleStringProperty();
    @Getter
    private final StringProperty transportTypeProperty = new SimpleStringProperty();

    private final MapQuestService mapQuestService;

    public TourItemDialogViewModel(TourItem tourItem, MapQuestService mapQuestService) {
        super();
        this.tourItem = tourItem;
        this.mapQuestService = mapQuestService;
        nameProperty.addListener((arg, oldVal, newVal) -> {
            updateTourModel();
        });
        fromProperty.addListener((arg, oldVal, newVal) -> {
            updateTourModel();
        });
        toProperty.addListener((arg, oldVal, newVal) -> {
            updateTourModel();
        });
        transportTypeProperty.addListener((arg, oldVal, newVal) -> {
            updateTourModel();
        });
        descriptionProperty.addListener((arg, oldVal, newVal) -> {
            updateTourModel();
        });
    }

    private void updateTourModel() {
        tourItem.updateFields(nameProperty.getValue(), fromProperty.getValue(), toProperty.getValue(), descriptionProperty.getValue(), transportTypeProperty.getValue());
    }

    public RouteResponse searchRoute() {
        if (fromProperty.getValue() == null || toProperty.getValue() == null) {
            System.out.println("At least one input is missing");
            return null;
        }
        System.out.println("Route search from/to: " + fromProperty.getValue() + " " + toProperty.getValue());
        return mapQuestService.searchRoute(fromProperty.getValue(), toProperty.getValue());
    }
}
