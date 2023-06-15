package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.viewModel.TopMenuViewModel;

public final class TopMenuController implements TourPlannerController {
    private final TopMenuViewModel topMenuViewModel;

    public TopMenuController(TopMenuViewModel topMenuViewModel) {
        this.topMenuViewModel = topMenuViewModel;
    }

    @Override
    public void initialize() {

    }
}
