package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.service.MapQuestService;
import at.fhtw.tourplanner.dal.api.response.RouteResponse;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

    @Getter
    private final BooleanProperty nameValidity = new SimpleBooleanProperty(true);

    @Getter
    private final BooleanProperty descriptionValidity = new SimpleBooleanProperty(true);

    @Getter
    private final BooleanProperty fromValidity = new SimpleBooleanProperty(true);

    @Getter
    private final BooleanProperty toValidity = new SimpleBooleanProperty(true);

    @Getter
    private final BooleanProperty transportTypeValidity = new SimpleBooleanProperty(true);

    @Getter
    private final BooleanProperty routeInfoValidity = new SimpleBooleanProperty(true);

    @Getter
    private final BooleanProperty distanceValidity = new SimpleBooleanProperty(true);

    @Getter
    private final BooleanProperty timeValidity = new SimpleBooleanProperty(true);

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

    public boolean validate() {

        boolean valid = true;

        if (this.tourItem.getName() == null || this.tourItem.getName().isEmpty() || this.tourItem.getName().length() > 50) {
            valid = false;
            this.nameValidity.set(false);
        } else {
            this.nameValidity.set(true);
        }

        if (this.tourItem.getDescription() == null || this.tourItem.getDescription().isEmpty() || this.tourItem.getDescription().length() > 500) {
            valid = false;
            this.descriptionValidity.set(false);
        } {
            this.descriptionValidity.set(true);
        }

        if (this.tourItem.getTo() == null || this.tourItem.getTo().isEmpty() || this.tourItem.getTo().length() > 50) {
            valid = false;
            this.toValidity.set(false);
        } {
            this.toValidity.set(false);
        }

        if (this.tourItem.getFrom() == null || this.tourItem.getFrom().isEmpty() || this.tourItem.getFrom().length() > 50) {
            valid = false;
            this.fromValidity.set(false);
        } {
            this.fromValidity.set(false);
        }

        if (this.tourItem.getTransportType() == null || this.tourItem.getTransportType().isEmpty()) {
            valid = false;
            this.transportTypeValidity.set(false);
        } {
            this.transportTypeValidity.set(false);
        }

        if (this.tourItem.getBoundingBoxString() == null || this.tourItem.getBoundingBoxString().isEmpty() || this.tourItem.getBoundingBoxString().length() > 1000) {
            valid = false;
            this.routeInfoValidity.set(false);
        } {
            this.routeInfoValidity.set(false);
        }

        if (this.tourItem.getTourDistanceKilometers() == null || this.tourItem.getTourDistanceKilometers() < 0) {
            valid = false;
            this.distanceValidity.set(false);
        } {
            this.distanceValidity.set(false);
        }

        if (this.tourItem.getEstimatedTimeSeconds() == null || this.tourItem.getEstimatedTimeSeconds() < 0) {
            valid = false;
            this.timeValidity.set(false);
        } {
            this.timeValidity.set(false);
        }

        return valid;

    }
}
