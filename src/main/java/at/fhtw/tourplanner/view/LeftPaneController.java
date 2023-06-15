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
    //region Fxml Elements
    @FXML
    public Button addTourBtn;
    @FXML
    public Button editTourBtn;
    @FXML
    public Button deleteTourBtn;
    //region ContextMenuItems
    @FXML
    public MenuItem cMenuNew;
    @FXML
    public MenuItem cMenuDelete;
    //endregion
    @FXML
    public MenuItem cMenuEdit;
    //endregion
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
        Window window = toursListView.getScene().getWindow();
        Stage stage = (Stage) window;
        TourItemDialogViewModel tourItemDialogViewModel = new TourItemDialogViewModel(new TourItem(), leftPaneViewModel.getMapQuestService());
        TourItemDialogController dialog = new TourItemDialogController(stage, tourItemDialogViewModel);
        Optional<TourItem> tourItemOptional = dialog.showAndWait();
        if (tourItemOptional.isPresent()) {
            TourItem tourItem = tourItemOptional.get();
        }
    }

    public void onButtonDelete(ActionEvent actionEvent) {
        if (toursListView.getSelectionModel().getSelectedItem() != null) {
            leftPaneViewModel.deleteTour(toursListView.getSelectionModel().getSelectedItem());
        }
    }

    public void onButtonEdit(ActionEvent actionEvent) {
    }

}
