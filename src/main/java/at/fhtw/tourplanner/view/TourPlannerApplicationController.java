package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.viewModel.TourPlannerApplicationViewModel;

public final class TourPlannerApplicationController implements TourPlannerController {

    private final TourPlannerApplicationViewModel tourPlannerApplicationViewModel;

    public TourPlannerApplicationController(TourPlannerApplicationViewModel tourPlannerApplicationViewModel) {
        this.tourPlannerApplicationViewModel = tourPlannerApplicationViewModel;
    }

    @Override
    public void initialize() {

    }
}
