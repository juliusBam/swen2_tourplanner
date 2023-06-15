package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.viewModel.CenterPaneViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public final class CenterPaneController implements TourPlannerController {

    private final CenterPaneViewModel centerPaneViewModel;
    @FXML
    public TextField nameTextField;
    @FXML
    public TextField descriptionTextField;
    @FXML
    public TextField fromTextField;
    @FXML
    public TextField toTextField;
    @FXML
    public TextField transportTypeTextField;
    @FXML
    public TextField distanceTextField;
    @FXML
    public TextField estimatedTimeTextField;

    public CenterPaneController(CenterPaneViewModel centerPaneViewModel) {
        this.centerPaneViewModel = centerPaneViewModel;
    }

    public CenterPaneViewModel getCenterPaneViewModel() {
        return centerPaneViewModel;
    }

    @Override
    @FXML
    public void initialize() {
        distanceTextField.textProperty().bindBidirectional(centerPaneViewModel.tourDistanceProperty());
        estimatedTimeTextField.textProperty().bindBidirectional(centerPaneViewModel.estimatedTimeProperty());
    }


}
