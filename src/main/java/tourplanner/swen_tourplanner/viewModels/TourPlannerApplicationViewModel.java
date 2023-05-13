package tourplanner.swen_tourplanner.viewModels;

import tourplanner.swen_tourplanner.viewModels.BottomPaneViewModel;
import tourplanner.swen_tourplanner.viewModels.CenterPaneViewModel;
import tourplanner.swen_tourplanner.viewModels.LeftPaneViewModel;
import tourplanner.swen_tourplanner.viewModels.TopMenuViewModel;

public class TourPlannerApplicationViewModel {


    private final BottomPaneViewModel bottomPaneViewModel;

    private final CenterPaneViewModel centerPaneViewModel;

    private final LeftPaneViewModel leftPaneViewModel;

    private final TopMenuViewModel topMenuViewModel;

    public TourPlannerApplicationViewModel(BottomPaneViewModel bottomPaneViewModel, CenterPaneViewModel centerPaneViewModel,
                                            LeftPaneViewModel leftPaneViewModel, TopMenuViewModel topMenuViewModel)
    {
        this.bottomPaneViewModel = bottomPaneViewModel;
        this.centerPaneViewModel = centerPaneViewModel;
        this.leftPaneViewModel = leftPaneViewModel;
        this.topMenuViewModel = topMenuViewModel;
    }

}
