package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.viewModel.LeftPaneViewModel;
import at.fhtw.tourplanner.viewModel.TourItemDialogViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Optional;

public final class LeftPaneController implements TourPlannerController {

    private final LeftPaneViewModel leftPaneViewModel;
    @FXML
    public ListView<TourItem> toursListView;
    @FXML
    public Button addTourBtn;
    @FXML
    public Button editTourBtn;
    @FXML
    public Button deleteTourBtn;
    @FXML
    public MenuItem cMenuNew;
    @FXML
    public MenuItem cMenuDelete;
    @FXML
    public MenuItem cMenuEdit;
    @FXML
    public TextField toursSearchTextInput;

    public LeftPaneController(LeftPaneViewModel leftPaneViewModel) {
        this.leftPaneViewModel = leftPaneViewModel;
    }

    @Override
    @FXML
    public void initialize() {
        toursListView.setItems(leftPaneViewModel.getObservableTours());
        toursListView.getSelectionModel().selectedItemProperty().addListener(leftPaneViewModel.getChangeListener());
    }

    public void onButtonAdd(ActionEvent actionEvent) {
        TourItem tourItem = new TourItem();
        Optional<TourItem> tourItemOptional = editTourDialog(tourItem, "Add Tour");
        if (tourItemOptional.isPresent()) {
            tourItem = tourItemOptional.get();
            leftPaneViewModel.addNewTour(tourItem);
        }
    }

    public void onButtonEdit(ActionEvent actionEvent) {
        if (toursListView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        TourItem oldItem = toursListView.getSelectionModel().getSelectedItem();
        Optional<TourItem> tourItemOptional = editTourDialog(oldItem, "Edit Tour");
        if (tourItemOptional.isPresent()) {
            TourItem newItem = tourItemOptional.get();
            leftPaneViewModel.editTour(newItem, oldItem);
        }
    }

    public void onButtonDelete(ActionEvent actionEvent) {
        if (toursListView.getSelectionModel().getSelectedItem() != null) {
            leftPaneViewModel.deleteTour(toursListView.getSelectionModel().getSelectedItem());
        }
    }

    private Optional<TourItem> editTourDialog(TourItem tourItem, String title) {
        Window window = toursListView.getScene().getWindow();
        Stage stage = (Stage) window;
        TourItemDialogViewModel tourItemDialogViewModel = new TourItemDialogViewModel(tourItem, leftPaneViewModel.getMapQuestService());
        TourItemDialogController dialog = new TourItemDialogController(stage, tourItemDialogViewModel, title);
        return dialog.showAndWait();
    }

}
