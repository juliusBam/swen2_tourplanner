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
        Optional<TourLog> dialogResult = editTourLogDialog(oldItem, "Edit Tour Log");
        if (dialogResult.isPresent()) {
            TourLog newTourLog = dialogResult.get();
            this.tourLogsTabViewModel.updateTourLog(newTourLog);
            this.updateTourLogSelection(newTourLog);
        }
    }

    private void updateTourLogSelection(TourLog newTourLog) {
        Optional<TourLog> tourLogToSelect = this.tourLogsTabViewModel.getObservableTourLogs().stream().filter((tourLog -> tourLog.getId().equals(newTourLog.getId()))).findFirst();
        if (tourLogToSelect.isPresent()) {
            //select it
            int indexOfItem = this.tourLogsTabViewModel.getObservableTourLogs().indexOf(tourLogToSelect.get());

            this.tourLogsListView.getSelectionModel().select(indexOfItem);
            this.tourLogsListView.scrollTo(indexOfItem);
            this.tourLogsListView.getFocusModel().focus(indexOfItem);

        }

    }

    public void onButtonDelete() {
        this.tourLogsTabViewModel.deleteTourLog();
    }

    public void onButtonAdd() {
        TourLog newTourLog = new TourLog();
        Optional<TourLog> dialogResult = this.editTourLogDialog(newTourLog, "Create new Tour Log");
        System.out.println(dialogResult);
        if (dialogResult.isPresent()) {
            newTourLog = dialogResult.get();
            this.tourLogsTabViewModel.addNewTourLog(newTourLog);
        }
    }

    public Optional<TourLog> editTourLogDialog(TourLog tourLog, String dialogTitle) {
        Window window = tourLogsListView.getScene().getWindow();
        Stage stage = (Stage) window;
        TourLogDialogViewModel tourLogDialogViewModel = new TourLogDialogViewModel(tourLog);
        TourLogDialogController dialog = new TourLogDialogController(stage, tourLogDialogViewModel, dialogTitle);
        return dialog.showAndWait();
    }



}
