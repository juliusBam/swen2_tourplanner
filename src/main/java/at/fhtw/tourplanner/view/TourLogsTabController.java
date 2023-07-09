package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.viewModel.TourLogDialogViewModel;
import at.fhtw.tourplanner.viewModel.TourLogsTabViewModel;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
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

    @FXML
    public Label loadingLabel;

    @FXML
    public SplitPane tourLogsSplitPane;

    @FXML
    public VBox tourLogDetailsContainer = new VBox();

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
        ratingLabel.textProperty().bind(this.tourLogsTabViewModel.getRatingProperty().asString());
        difficultyLabel.textProperty().bind(this.tourLogsTabViewModel.getDifficultyProperty().asString());
        timeLabel.textProperty().bind(this.tourLogsTabViewModel.getTimeProperty());


        //the visibility of the split pane containing the stuff 4 the tour logs is bound to the loading
        tourLogsSplitPane.visibleProperty().bind(this.tourLogsTabViewModel.getLoadingTourLogs().not());

        loadingLabel.visibleProperty().bind(this.tourLogsTabViewModel.getLoadingTourLogs());

        this.tourLogDetailsContainer.visibleProperty().bind(this.tourLogsTabViewModel.getTourLogSelected());

        this.tourLogsTabViewModel.addSelectionChangedListener(this::updateTourLogSelection);

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
        if (tourLogsListView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        this.tourLogsTabViewModel.deleteTourLog();
    }

    public void onButtonAdd() {
        if (this.tourLogsTabViewModel.getSelectedTourItem() == null) {
            return;
        }
        TourLog newTourLog = new TourLog();
        Optional<TourLog> dialogResult = this.editTourLogDialog(newTourLog, "Create new Tour Log");
        if (dialogResult.isPresent()) {
            newTourLog = dialogResult.get();
            this.tourLogsTabViewModel.addNewTourLog(newTourLog);
        }
    }

    public Optional<TourLog> editTourLogDialog(TourLog tourLog, String dialogTitle) {
        Window window = tourLogsListView.getScene().getWindow();
        Stage stage = (Stage) window;
        TourLogDialogViewModel tourLogDialogViewModel = new TourLogDialogViewModel(tourLog);
        TourLogDialogController dialog = new TourLogDialogController(tourLog, stage, tourLogDialogViewModel, dialogTitle);
        return dialog.showAndWait();
    }

}
