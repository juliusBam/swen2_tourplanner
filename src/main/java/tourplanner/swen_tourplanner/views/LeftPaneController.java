package tourplanner.swen_tourplanner.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import tourplanner.swen_tourplanner.viewModels.LeftPaneViewModel;

public final class LeftPaneController implements ITourPlannerController {

    //region Fxml Elements
    @FXML
    public Button createNewTourBtn;

    //region ContextMenuItems
    @FXML
    public MenuItem cMenuNew;

    @FXML
    public MenuItem cMenuDelete;

    @FXML
    public MenuItem cMenuOpt;
    //endregion

    @FXML
    public ListView toursListView;

    @FXML
    public TextField toursSearchTextInput;
    //endregion

    private final LeftPaneViewModel leftPaneViewModel;
    public LeftPaneController(LeftPaneViewModel leftPaneViewModel) {
        this.leftPaneViewModel = leftPaneViewModel;
    }

    @Override
    @FXML
    public void initialize() {

    }
}
