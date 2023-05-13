package tourplanner.swen_tourplanner.views;

import tourplanner.swen_tourplanner.viewModels.CenterPaneViewModel;

public final class CenterPaneController implements ITourPlannerController {

    private final CenterPaneViewModel centerPaneViewModel;
    public CenterPaneController(CenterPaneViewModel centerPaneViewModel) {
        this.centerPaneViewModel = centerPaneViewModel;
    }

    @Override
    public void initialize() {

    }
}
