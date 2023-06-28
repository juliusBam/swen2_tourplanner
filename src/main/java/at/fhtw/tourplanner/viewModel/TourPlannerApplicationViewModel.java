package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.model.TourItem;

public class TourPlannerApplicationViewModel {


    private final BottomPaneViewModel bottomPaneViewModel;

    private final CenterPaneViewModel centerPaneViewModel;

    private final LeftPaneViewModel leftPaneViewModel;

    private final TopMenuViewModel topMenuViewModel;

    private final TourLogsTabViewModel tourLogsTabViewModel;


    public TourPlannerApplicationViewModel(BottomPaneViewModel bottomPaneViewModel, CenterPaneViewModel centerPaneViewModel,
                                           LeftPaneViewModel leftPaneViewModel, TopMenuViewModel topMenuViewModel, TourLogsTabViewModel tourLogsTabViewModel) {
        this.bottomPaneViewModel = bottomPaneViewModel;
        this.centerPaneViewModel = centerPaneViewModel;
        this.leftPaneViewModel = leftPaneViewModel;
        this.topMenuViewModel = topMenuViewModel;
        this.tourLogsTabViewModel = tourLogsTabViewModel;
        this.leftPaneViewModel.addSelectionChangedListener(this::selectTour);
    }

    private void selectTour(TourItem selectedTourItem) {
        centerPaneViewModel.setTourModel(selectedTourItem);
        this.tourLogsTabViewModel.setObservableTourLogs(selectedTourItem.getTourLogs());
    }

}
