package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.viewModel.TourLogsTabViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class TourLogsTabController implements TourPlannerController {

    @FXML
    public final TourLogsTabViewModel tourLogsTabViewModel;

    @FXML
    public ListView<TourLog> tourLogsListView;

    @FXML
    public Label dateLabel;

    @FXML
    public Label commentLabel;

    @FXML
    public Label ratingLabel;

    @FXML
    public Label difficultyLabel;

    @FXML
    public Label timeLabel;

    @FXML
    public Button editBtn;

    @FXML
    public Button deleteBtn;

    @FXML
    public Button newBtn;


    @Override
    public void initialize() {
        //binding of list and change listener
        this.tourLogsListView.setItems(this.tourLogsTabViewModel.getObservableTourLogs());
        this.tourLogsListView.setCellFactory(tourLog -> new TourLogCell(this));
        this.tourLogsListView.getSelectionModel().selectedItemProperty().addListener(
                this.tourLogsTabViewModel.getChangeListener()
        );

        //props binding
        dateLabel.textProperty().bind(this.tourLogsTabViewModel.getDateProperty());
        commentLabel.textProperty().bind(this.tourLogsTabViewModel.getCommentProperty());
        ratingLabel.textProperty().bind(this.tourLogsTabViewModel.getRatingProperty());
        difficultyLabel.textProperty().bind(this.tourLogsTabViewModel.getDifficultyProperty());
        timeLabel.textProperty().bind(this.tourLogsTabViewModel.getTimeProperty());

    }

    public TourLogsTabController(TourLogsTabViewModel tourLogsTabViewModel) {
        this.tourLogsTabViewModel = tourLogsTabViewModel;
    }

    public void onEditBtnClick(ActionEvent actionEvent) {
        this.tourLogsTabViewModel.editTour();
    }

}
