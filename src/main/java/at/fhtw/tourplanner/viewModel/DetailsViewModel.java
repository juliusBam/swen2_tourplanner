package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.model.TourStats;
import at.fhtw.tourplanner.bl.service.MapQuestService;
import at.fhtw.tourplanner.bl.service.TourItemService;
import javafx.beans.property.*;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import lombok.Getter;

public class DetailsViewModel {

    private final TourItemService tourItemService;

    private final MapQuestService mapQuestService;
    @Getter
    private final StringProperty nameProperty = new SimpleStringProperty();
    @Getter
    private final StringProperty tourDescriptionProperty = new SimpleStringProperty();
    @Getter
    private final StringProperty fromProperty = new SimpleStringProperty();
    @Getter
    private final StringProperty toProperty = new SimpleStringProperty();
    @Getter
    private final StringProperty transportTypeProperty = new SimpleStringProperty();
    @Getter
    private final StringProperty tourDistanceProperty = new SimpleStringProperty();
    @Getter
    private final StringProperty estimatedTimeProperty = new SimpleStringProperty();
    @Getter
    private final ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();

    @Getter
    private final BooleanProperty showImage = new SimpleBooleanProperty();

    private final BooleanProperty requestingImage = new SimpleBooleanProperty(false);

    private TourItem tourItem;

    @Getter
    private volatile boolean isInitValue = true;

    @Getter
    private final StringProperty popularityProperty = new SimpleStringProperty();

    @Getter
    private final StringProperty avgTimeProperty = new SimpleStringProperty();

    @Getter
    private final StringProperty avgRatingProperty = new SimpleStringProperty();

    @Getter
    private final StringProperty avgDifficultyProperty = new SimpleStringProperty();

    @Getter
    private final StringProperty childFriendlinessProperty = new SimpleStringProperty();

    public DetailsViewModel(TourItemService tourItemService, MapQuestService mapQuestService) {
        this.tourItemService = tourItemService;
        this.mapQuestService = mapQuestService;
        showImage.set(false);
    }

    public void updateOverviewTab(TourItem tourItem) {
        isInitValue = true;
        this.showImage.set(false);
        if (tourItem == null) {
            // select the first in the list
            nameProperty.set("");
            tourDescriptionProperty.set("");
            fromProperty.set("");
            toProperty.set("");
            transportTypeProperty.set("");
            tourDistanceProperty.set("");
            estimatedTimeProperty.set("");
            return;
        }
        this.tourItem = tourItem;
        nameProperty.setValue(tourItem.getName());
        tourDescriptionProperty.setValue(tourItem.getDescription());
        fromProperty.setValue(tourItem.getFrom());
        toProperty.setValue(tourItem.getTo());
        transportTypeProperty.setValue(formatTransportType(tourItem.getTransportType()));
        tourDistanceProperty.setValue(String.format("%.2f km", tourItem.getTourDistanceKilometers()));
        estimatedTimeProperty.setValue(String.format("%d:%02d:%02d (H:MM:SS)", tourItem.getEstimatedTimeSeconds() / 3600, (tourItem.getEstimatedTimeSeconds() % 3600) / 60, (tourItem.getEstimatedTimeSeconds() % 60)));
        this.updateTourStatsProps();
        if (requestingImage.get()) {
            mapQuestService.cancelSetRouteImage();
            requestingImage.set(false);
        }
        setImage();
        //imageProperty.setValue(mapQuestService.fetchRouteImage(tourItem.getFrom(), tourItem.getTo(), tourItem.getBoundingBoxString()));
        isInitValue = false;
    }

    private String formatTransportType(String transportType) {
        switch (transportType) {
            case "car" -> {
                return "Car tour";
            }
            case "pedestrian" -> {
                return "Hiking tour";
            }
            case "bicycle" -> {
                return "Bicycle tour";
            }
            default -> {
                return "Unknown tour type";
            }
        }
    }

    public void setImage() {
        if (requestingImage.get()) {
            return;
        }
        requestingImage.set(true);
        mapQuestService.setRouteImage(tourItem, this::updateImage, this::onErrorFetchImage);
    }

    public void onErrorFetchImage(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.setHeaderText(title + tourItem.getName());
        alert.showAndWait();
    }

    private void updateImage(Image routeImage) {
        requestingImage.set(false);
        if (routeImage == null) {
            showImage.set(false);
        } else {
            imageProperty.setValue(routeImage);
            showImage.set(true);
            requestingImage.set(false);
        }
    }

    public void updateTourStatsProps() {

        if (this.tourItem == null || this.tourItem.getTourStats() == null) {

            this.popularityProperty.set("");
            this.avgTimeProperty.set("");
            this.avgRatingProperty.set("");
            this.avgDifficultyProperty.set("");
            this.childFriendlinessProperty.set("");

        } else {

            this.tourItemService.setTourStats(this.tourItem.getId(), this::setTourStats, this::handleReqError);

        }
    }

    public void setTourStats(TourStats tourStats) {

        popularityProperty.set(String.format("#%d", tourStats.getPopularity()));
        long avgTimeSeconds = Math.round(tourStats.getAverageTime() * 60);
        avgTimeProperty.set(String.format("%d:%02d:%02d (H:MM:SS)", avgTimeSeconds / 3600, avgTimeSeconds % 3600 / 60, avgTimeSeconds % 60));
        avgRatingProperty.set(String.format("%,.2f", tourStats.getAverageRating()));
        avgDifficultyProperty.set(String.format("%,.2f", tourStats.getAverageDifficulty()));
        childFriendlinessProperty.set(String.format("%,.2f", tourStats.getChildFriendliness()));

    }

    public void handleReqError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.setHeaderText(title);
        alert.showAndWait();
    }

}
