package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.model.TourItem;

public class TourPlannerApplicationViewModel {


    private final BottomPaneViewModel bottomPaneViewModel;

    private final CenterPaneViewModel centerPaneViewModel;

    private final LeftPaneViewModel leftPaneViewModel;

    private final TopMenuViewModel topMenuViewModel;


    public TourPlannerApplicationViewModel(BottomPaneViewModel bottomPaneViewModel, CenterPaneViewModel centerPaneViewModel,
                                           LeftPaneViewModel leftPaneViewModel, TopMenuViewModel topMenuViewModel) {
        this.bottomPaneViewModel = bottomPaneViewModel;
        this.centerPaneViewModel = centerPaneViewModel;
        this.leftPaneViewModel = leftPaneViewModel;
        this.topMenuViewModel = topMenuViewModel;
        this.leftPaneViewModel.addSelectionChangedListener(this::selectTour);
    }

    private void selectTour(TourItem selectedTourItem) {
        centerPaneViewModel.setTourModel(selectedTourItem);
    }

}
