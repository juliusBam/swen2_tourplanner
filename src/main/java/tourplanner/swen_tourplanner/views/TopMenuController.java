package tourplanner.swen_tourplanner.views;

import tourplanner.swen_tourplanner.viewModels.TopMenuViewModel;

public final class TopMenuController implements ITourPlannerController {
    private final TopMenuViewModel topMenuViewModel;

    public TopMenuController(TopMenuViewModel topMenuViewModel) {
        this.topMenuViewModel = topMenuViewModel;
    }

    @Override
    public void initialize() {

    }
}
