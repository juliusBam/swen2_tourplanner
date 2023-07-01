package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.service.MapQuestService;
import at.fhtw.tourplanner.bl.service.TourItemService;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import lombok.Getter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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

    private TourItem tourItem;

    @Getter
    private volatile boolean isInitValue = true;

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
        this.loadingLabelProperty.set("Loading....");
        this.setImage();
        //imageProperty.setValue(mapQuestService.fetchRouteImage(tourItem.getFrom(), tourItem.getTo(), tourItem.getBoundingBoxString()));
        isInitValue = false;
    }

    public void setTourStatistics() {
        if (tourItem != null) {
            //todo update the statistics of the tourItem
            nameProperty.set(this.tourItem.getName());

        }
    }

    public void setImage() {

        //imageProperty.setValue(mapQuestService.fetchRouteImage(tourItem.getFrom(), tourItem.getTo(), tourItem.getBoundingBoxString()));
        Call<ResponseBody> apiReq = this.mapQuestService.fetchRouteImageAsync(tourItem.getFrom(), tourItem.getTo(), tourItem.getBoundingBoxString());

        apiReq.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();

                byte[] bytes = new byte[0];
                try {
                    bytes = responseBody.bytes();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                InputStream inputStream = new ByteArrayInputStream(bytes);

                //needed when we want to interact with the UI from a different thread
                Platform.runLater(() -> {
                    //update application thread
                    imageProperty.setValue(new Image(inputStream));
                    showImage.set(true);
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                loadingLabelProperty.set("Error loading image");
            }
        });

    }

}
