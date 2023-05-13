package tourplanner.swen_tourplanner.views;

import tourplanner.swen_tourplanner.viewModels.BottomPaneViewModel;

public class BottomPaneController implements ITourPlannerController {

    private final BottomPaneViewModel bottomPaneViewModel;

    public BottomPaneController(BottomPaneViewModel bottomPaneViewModel) {
        this.bottomPaneViewModel = bottomPaneViewModel;
    }

    @Override
    public void initialize() {

    }
}
