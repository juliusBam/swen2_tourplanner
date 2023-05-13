package tourplanner.swen_tourplanner.views;

import tourplanner.swen_tourplanner.viewModels.TourPlannerApplicationViewModel;

public class TourPlannerApplicationController implements ITourPlannerController {

    private final TourPlannerApplicationViewModel tourPlannerApplicationViewModel;

    public TourPlannerApplicationController(TourPlannerApplicationViewModel tourPlannerApplicationViewModel) {
        this.tourPlannerApplicationViewModel = tourPlannerApplicationViewModel;
    }

    @Override
    public void initialize() {

    }
}
