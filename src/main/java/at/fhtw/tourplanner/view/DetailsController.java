package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.viewModel.DetailsViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public final class DetailsController implements TourPlannerController {

    private final DetailsViewModel detailsViewModel;
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
    public AnchorPane routeImagePane;
    @FXML
    public ImageView routeImageView;

    @FXML
    public TabPane centerPaneTabs;

    @FXML
    public Label popularityLabel;
    @FXML
    public Label avgTimeLabel;
    @FXML
    public Label avgRatingLabel;
    @FXML
    public Label avgDifficultyLabel;
    @FXML
    public Label childFriendlinessLabel;

    @FXML
    public AnchorPane tourLogsTabPane;
    @FXML
    public SplitPane tourLogsTabContent;

    public DetailsController(DetailsViewModel detailsViewModel) {
        this.detailsViewModel = detailsViewModel;
    }

    @Override
    @FXML
    public void initialize() {

        centerPaneTabs.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> {
                    if (this.detailsViewModel.isInitValue()) {
                        return;
                    }
                    if (ov.getValue().getId().equals("routeTab")) {
                        this.detailsViewModel.setImage();
                    }
                    if (ov.getValue().getId().equals("statisticsTab")) {
                        this.detailsViewModel.updateTourStatsProps();
                    }
                });

        nameLabel.textProperty().bind(detailsViewModel.getNameProperty());
        tourDescriptionLabel.textProperty().bind(detailsViewModel.getTourDescriptionProperty());
        fromLabel.textProperty().bind(detailsViewModel.getFromProperty());
        toLabel.textProperty().bind(detailsViewModel.getToProperty());
        transportTypeLabel.textProperty().bind(detailsViewModel.getTransportTypeProperty());
        distanceLabel.textProperty().bind(detailsViewModel.getTourDistanceProperty());
        estimatedTimeLabel.textProperty().bind(detailsViewModel.getEstimatedTimeProperty());

        //binds the content of the image to the image "value"
        routeImageView.imageProperty().bind(detailsViewModel.getImageProperty());
        //binds the visibility of the image to the showImageProp
        routeImageView.visibleProperty().bind(this.detailsViewModel.getShowImage());

        routeImageView.imageProperty().addListener((arg, oldValue, newValue) -> {
            centerImageView();
        });

        routeImagePane.widthProperty().addListener((arg, oldVal, newVal) -> {
            routeImageView.fitWidthProperty().set(routeImagePane.widthProperty().doubleValue());
            centerImageView();
        });

        routeImagePane.heightProperty().addListener((arg, oldVal, newVal) -> {
            routeImageView.fitHeightProperty().set(newVal.doubleValue());
            centerImageView();
        });

        tourLogsTabContent.prefWidthProperty().bind(tourLogsTabPane.widthProperty());
        tourLogsTabContent.prefHeightProperty().bind(tourLogsTabPane.heightProperty());

        this.popularityLabel.textProperty().bind(this.detailsViewModel.getPopularityProperty());
        this.avgTimeLabel.textProperty().bind(this.detailsViewModel.getAvgTimeProperty());
        this.avgRatingLabel.textProperty().bind(this.detailsViewModel.getAvgRatingProperty());
        this.avgDifficultyLabel.textProperty().bind(this.detailsViewModel.getAvgDifficultyProperty());
        this.childFriendlinessLabel.textProperty().bind(this.detailsViewModel.getChildFriendlinessProperty());
    }

    private void centerImageView() {
        if (routeImageView.getImage() == null) {
            return;
        }
        double aspectRatio = routeImageView.getImage().getWidth() / routeImageView.getImage().getHeight();
        routeImageView.setX(Math.max((routeImagePane.widthProperty().doubleValue() - routeImagePane.heightProperty().doubleValue() * aspectRatio) / 2, 0));
        routeImageView.setY(Math.max((routeImagePane.heightProperty().doubleValue() - routeImagePane.widthProperty().doubleValue() / aspectRatio) / 2, 0));
    }


}
