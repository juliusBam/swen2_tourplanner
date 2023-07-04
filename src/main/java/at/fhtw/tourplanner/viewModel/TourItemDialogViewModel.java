package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.service.MapQuestService;
import at.fhtw.tourplanner.dal.api.response.RouteResponse;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


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

    private final List<UpdateRouteListener> updateRouteListeners = new ArrayList<>();

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

    public void searchRoute() {

        this.mapQuestService.searchRoute(tourItem, this::handleRouteResult, this::handleReqError);
        //return routeResponse;
    }

    public void handleRouteResult(RouteResponse routeResponse) {
        this.notifyListeners(routeResponse);
        this.validateSearch();
    }

    public void handleReqError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.setHeaderText(title);
        alert.showAndWait();
    }

    public void setRouteData(double distance, long time, String boundingBoxString) {
        tourItem.setRouteData(distance, time, boundingBoxString);
    }

    public void addListenerToUpdateRoute(UpdateRouteListener updateRouteListener) {
        this.updateRouteListeners.add(updateRouteListener);
    }

    private void notifyListeners(RouteResponse routeResponse) {
        for (var listener : updateRouteListeners) {
            listener.updateRoute(routeResponse);
        }
    }

    public boolean validateSearch() {

         boolean valid = true;

        if (this.tourItem.getTo() == null || this.tourItem.getTo().isEmpty() || this.tourItem.getTo().length() > 50) {
            valid = false;
            this.toValidity.set(false);
        } else {
            this.toValidity.set(true);
        }

        if (this.tourItem.getFrom() == null || this.tourItem.getFrom().isEmpty() || this.tourItem.getFrom().length() > 50) {
            valid = false;
            this.fromValidity.set(false);
        } else {
            this.fromValidity.set(true);
        }

        if (this.tourItem.getTransportType() == null || this.tourItem.getTransportType().isEmpty()) {
            valid = false;
            this.transportTypeValidity.set(false);
        } else {
            this.transportTypeValidity.set(true);
        }

        return valid;
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
        } else {
            this.descriptionValidity.set(true);
        }

        if (this.tourItem.getTourDistanceKilometers() == null || this.tourItem.getTourDistanceKilometers() < 0) {
            valid = false;
            this.distanceValidity.set(false);
        } else {
            this.distanceValidity.set(true);
        }

        if (this.tourItem.getEstimatedTimeSeconds() == null || this.tourItem.getEstimatedTimeSeconds() < 0) {
            valid = false;
            this.timeValidity.set(false);
        } else {
            this.timeValidity.set(true);
        }

        if (this.tourItem.getBoundingBoxString() == null || this.tourItem.getBoundingBoxString().isEmpty() || this.tourItem.getBoundingBoxString().length() > 1000) {
            valid = false;
            this.routeInfoValidity.set(false);
        } else {
            this.routeInfoValidity.set(true);
        }

        if (!this.validateSearch()) {
            valid = false;
        }

        return valid;

    }

    public interface UpdateRouteListener {
        void updateRoute(RouteResponse routeResponse);
    }
}
