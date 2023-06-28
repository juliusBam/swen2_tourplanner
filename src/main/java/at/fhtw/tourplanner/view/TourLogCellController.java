package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.viewModel.TourLogCellViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TourLogCellController implements TourPlannerController {

    private final TourLogCellViewModel tourLogCellViewModel;

    @FXML
    private Label nameLabel;

    @FXML
    private Button editBtn;

    public TourLogCellController(TourLogCellViewModel tourLogCellViewModel) {
        this.tourLogCellViewModel = tourLogCellViewModel;
    }

    @Override
    public void initialize() {

    }

    public void onButtonEdit(ActionEvent actionEvent) {
        tourLogCellViewModel.editTourLog();
    }

    public void setNameLabel(String newLabel) {
        this.nameLabel.setText(newLabel);
    }
}
