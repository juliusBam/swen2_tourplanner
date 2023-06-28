package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.viewModel.TourItemDialogViewModel;
import at.fhtw.tourplanner.viewModel.TourLogDialogViewModel;
import at.fhtw.tourplanner.viewModel.TourLogsTabViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Optional;

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

    public void onButtonEdit() {
        if (tourLogsListView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        TourLog oldItem = tourLogsListView.getSelectionModel().getSelectedItem();
        Optional<TourItem> dialogResult = editTourLogDialog(oldItem, "Edit Tour Log");
        if (dialogResult.isPresent()) {
            //todo check with Peter how
            //TourItem newItem = tourItemOptional.get();
            //leftPaneViewModel.editTour(newItem, oldItem);
        }
    }

    public void onButtonDelete() {
        //todo send the request
        //todo update tour
        //todo check with Peter how
    }

    public void onButtonAdd() {
        Optional<TourItem> dialogResult = this.editTourLogDialog(null, "Create new Tour Log");
        if (dialogResult.isPresent()) {
            //todo check with Peter how
            //TourItem newItem = tourItemOptional.get();
            //leftPaneViewModel.editTour(newItem, oldItem);
        }
        //todo send the request
        //todo update tour
        //todo check with Peter how
    }

    public Optional<TourItem> editTourLogDialog(TourLog tourLog, String dialogTitle) {
        Window window = tourLogsListView.getScene().getWindow();
        Stage stage = (Stage) window;
        TourLogDialogViewModel tourLogDialogViewModel = new TourLogDialogViewModel(tourLog);
        TourLogDialogController dialog = new TourLogDialogController(stage, tourLogDialogViewModel, dialogTitle);
        //todo open edit tour log dialog
        //TourItemDialogController dialog = new TourItemDialogController(stage, tourItemDialogViewModel, title);
        //return dialog.showAndWait();
        return dialog.showAndWait();
    }



}
