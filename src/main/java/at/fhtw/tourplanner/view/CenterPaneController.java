package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.viewModel.CenterPaneViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
    public AnchorPane routeImagePane;
    @FXML
    public ImageView routeImageView;

    @FXML
    public TabPane centerPaneTabs;

    @FXML
    public Label loadingLabel;
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
                    if (ov.getValue().getId().equals("routeTab")) {
                        this.centerPaneViewModel.setImage();
                    }
                    if (ov.getValue().getId().equals("statisticsTab")) {
                        this.centerPaneViewModel.updateTourStatsProps();
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

        loadingLabel.textProperty().bind(this.centerPaneViewModel.getLoadingLabelProperty());
        //binds the visibility of the loading label to the negation of the showImageProp
        loadingLabel.visibleProperty().bind(this.centerPaneViewModel.getShowImage().not());

        this.popularityLabel.textProperty().bind(this.centerPaneViewModel.getPopularityProperty());
        this.avgTimeLabel.textProperty().bind(this.centerPaneViewModel.getAvgTimeProperty());
        this.avgRatingLabel.textProperty().bind(this.centerPaneViewModel.getAvgRatingProperty());
        this.avgDifficultyLabel.textProperty().bind(this.centerPaneViewModel.getAvgDifficultyProperty());
        this.childFriendlinessLabel.textProperty().bind(this.centerPaneViewModel.getChildFriendlinessProperty());
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
