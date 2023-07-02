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
        if (tourItem.getId() != null) {
            nameProperty.setValue(tourItem.getName());
            descriptionProperty.setValue(tourItem.getDescription());
            fromProperty.setValue(tourItem.getFrom());
            toProperty.setValue(tourItem.getTo());
            transportTypeProperty.setValue(tourItem.getTransportType());
        }
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
        tourItem.updateFields(nameProperty.getValue(), fromProperty.getValue(), toProperty.getValue(), descriptionProperty.getValue(), processChoice(transportTypeProperty.getValue()));
    }

    private String processChoice(String value) {
        if (value == null) {
            return "";
        }
        if (value.equals("pedestrian")) {
            return "pedestrian";
        }
        if (value.equals("bicycle")) {
            return "bicycle";
        }
        return "fastest";
    }

    public RouteResponse searchRoute() {
        return mapQuestService.searchRoute(tourItem);
    }

    public void setRouteData(double distance, long time, String boundingBoxString) {
        tourItem.setRouteData(distance, time, boundingBoxString);
    }
}
