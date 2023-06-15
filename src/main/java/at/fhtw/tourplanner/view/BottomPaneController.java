package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.viewModel.BottomPaneViewModel;

public final class BottomPaneController implements TourPlannerController {

    private final BottomPaneViewModel bottomPaneViewModel;

    public BottomPaneController(BottomPaneViewModel bottomPaneViewModel) {
        this.bottomPaneViewModel = bottomPaneViewModel;
    }

    @Override
    public void initialize() {

    }
}
