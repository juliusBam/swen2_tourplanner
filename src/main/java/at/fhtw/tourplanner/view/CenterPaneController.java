package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.viewModel.CenterPaneViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

    public CenterPaneController(CenterPaneViewModel centerPaneViewModel) {
        this.centerPaneViewModel = centerPaneViewModel;
    }

    @Override
    @FXML
    public void initialize() {
        nameLabel.textProperty().bind(centerPaneViewModel.getNameProperty());
        tourDescriptionLabel.textProperty().bind(centerPaneViewModel.getTourDescriptionProperty());
        fromLabel.textProperty().bind(centerPaneViewModel.getFromProperty());
        toLabel.textProperty().bind(centerPaneViewModel.getToProperty());
        transportTypeLabel.textProperty().bind(centerPaneViewModel.getTransportTypeProperty());
        distanceLabel.textProperty().bind(centerPaneViewModel.getTourDistanceProperty());
        estimatedTimeLabel.textProperty().bind(centerPaneViewModel.getEstimatedTimeProperty());
        routeImageView.imageProperty().bind(centerPaneViewModel.getImageProperty());
    }



}
