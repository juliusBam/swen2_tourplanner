package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.ModelConverter;
import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.model.TourStats;
import at.fhtw.tourplanner.bl.service.MapQuestService;
import at.fhtw.tourplanner.bl.service.TourItemService;
import at.fhtw.tourplanner.dal.dto.TourItemDto;
import at.fhtw.tourplanner.dal.dto.TourLogDto;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import lombok.Getter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class CenterPaneViewModel {

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
    private final StringProperty loadingLabelProperty = new SimpleStringProperty();

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

    public CenterPaneViewModel(TourItemService tourItemService, MapQuestService mapQuestService) {
        this.tourItemService = tourItemService;
        this.mapQuestService = mapQuestService;
        loadingLabelProperty.set("No record available");
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
            loadingLabelProperty.set("No record available");
            return;
        }
        this.tourItem = tourItem;
        nameProperty.setValue(tourItem.getName());
        tourDescriptionProperty.setValue(tourItem.getDescription());
        fromProperty.setValue(tourItem.getFrom());
        toProperty.setValue(tourItem.getTo());
        transportTypeProperty.setValue(tourItem.getTransportType());
        tourDistanceProperty.setValue(String.format("%.2f km", tourItem.getTourDistanceKilometers()));
        estimatedTimeProperty.setValue(String.format("%d:%02d:%02d", tourItem.getEstimatedTimeSeconds() / 3600, (tourItem.getEstimatedTimeSeconds() % 3600) / 60, (tourItem.getEstimatedTimeSeconds() % 60)));
//      this.loadingLabelProperty.set("Loading....");
        this.setTourStatsProperties();
//        this.loadingLabelProperty.set("Loading....");
        if (requestingImage.get()) {
            System.out.println("Cancelling current call");
            mapQuestService.cancelSetRouteImage();
            requestingImage.set(false);
        }
        setImage();
        //imageProperty.setValue(mapQuestService.fetchRouteImage(tourItem.getFrom(), tourItem.getTo(), tourItem.getBoundingBoxString()));
        isInitValue = false;
    }

    public void setImage() {
        if (requestingImage.get()) {
            System.out.println("Image request cancelled; request already in progress");
            return;
        }
        requestingImage.set(true);
        mapQuestService.setRouteImage(tourItem, this::updateImage);
    }

    private void updateImage(Image routeImage) {
        requestingImage.set(false);
        if (routeImage == null) {
            showImage.set(false);
        }
        else {
            imageProperty.setValue(routeImage);
            showImage.set(true);
            requestingImage.set(false);
        }
    }

    public void setTourStatsProperties() {
        if (this.tourItem == null || this.tourItem.getTourStats() == null) {

            this.popularityProperty.set("");
            this.avgTimeProperty.set("");
            this.avgRatingProperty.set("");
            this.avgDifficultyProperty.set("");
            this.childFriendlinessProperty.set("");

        } else {

            Call<TourItemDto> apiCall = this.tourItemService.findOneAsync(this.tourItem.getId());

            apiCall.enqueue(new Callback<TourItemDto>() {
                @Override
                public void onResponse(Call<TourItemDto> call, Response<TourItemDto> response) {
                    if (response.body() != null) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                //update application thread
                                TourStats fetchedTourStats = new ModelConverter().tourItemDtoToModel(response.body()).getTourStats();

                                //todo fetch props from d

                                popularityProperty.set(fetchedTourStats.getPopularity().toString());
                                avgTimeProperty.set(fetchedTourStats.getAverageTime().toString());
                                avgRatingProperty.set(fetchedTourStats.getAverageRating().toString());
                                avgDifficultyProperty.set(fetchedTourStats.getAverageDifficulty().toString());
                                childFriendlinessProperty.set(fetchedTourStats.getChildFriendliness().toString());


                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<TourItemDto> call, Throwable throwable) {
                    //todo print error
                    System.out.println("Error");
                }
            });

        }
    }

}
