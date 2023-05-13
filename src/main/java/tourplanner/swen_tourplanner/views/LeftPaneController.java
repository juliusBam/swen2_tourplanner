package tourplanner.swen_tourplanner.views;

import tourplanner.swen_tourplanner.viewModels.LeftPaneViewModel;

public class LeftPaneController implements ITourPlannerController {

    private final LeftPaneViewModel leftPaneViewModel;
    public LeftPaneController(LeftPaneViewModel leftPaneViewModel) {
        this.leftPaneViewModel = leftPaneViewModel;
    }

    @Override
    public void initialize() {

    }
}
