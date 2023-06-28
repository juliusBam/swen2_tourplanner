package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.viewModel.CenterPaneViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;

public final class CenterPaneController implements TourPlannerController {

    private final CenterPaneViewModel centerPaneViewModel;
    @FXML
    public Label nameLabel;
    @FXML
    public Label tourDescriptionLabel;
    @FXML
    public Label fromLabel;
    @FXML
    public Label toLabel;
    @FXML
    public Label transportTypeLabel;
    @FXML
    public Label distanceLabel;
    @FXML
    public Label estimatedTimeLabel;

    @FXML
    public ImageView routeImageView;

    @FXML
    public TabPane centerPaneTabs;

    @FXML
    public Label loadingLabel;

    public CenterPaneController(CenterPaneViewModel centerPaneViewModel) {
        this.centerPaneViewModel = centerPaneViewModel;
    }

    @Override
    @FXML
    public void initialize() {

        centerPaneTabs.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> {
                    if (this.centerPaneViewModel.isInitValue()) {
                        return;
                    }
                    //ov.getValue()
                    if (ov.getValue().getId().equals("routeTab")) {
                        //todo load the img
                        System.out.println("Opening routeTab, you have to load the img");
                    }
                    if (ov.getValue().getId().equals("statisticsTab")) {
                        this.centerPaneViewModel.setTourStatistics();
                    }

                });

        nameLabel.textProperty().bind(centerPaneViewModel.getNameProperty());
        tourDescriptionLabel.textProperty().bind(centerPaneViewModel.getTourDescriptionProperty());
        fromLabel.textProperty().bind(centerPaneViewModel.getFromProperty());
        toLabel.textProperty().bind(centerPaneViewModel.getToProperty());
        transportTypeLabel.textProperty().bind(centerPaneViewModel.getTransportTypeProperty());
        distanceLabel.textProperty().bind(centerPaneViewModel.getTourDistanceProperty());
        estimatedTimeLabel.textProperty().bind(centerPaneViewModel.getEstimatedTimeProperty());

        //binds the content of the image to the image "value"
        routeImageView.imageProperty().bind(centerPaneViewModel.getImageProperty());
        //binds the visibility of the image to the showImageProp
        routeImageView.visibleProperty().bind(this.centerPaneViewModel.getShowImage());

        //binds the visibility of the loading label to the negation of the showImageProp
        loadingLabel.visibleProperty().bind(this.centerPaneViewModel.getShowImage().not());

    }



}
