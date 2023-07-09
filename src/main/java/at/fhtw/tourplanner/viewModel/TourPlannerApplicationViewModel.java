package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.model.TourItem;

public class TourPlannerApplicationViewModel {


    private final DetailsViewModel detailsViewModel;

    private final OverviewViewModel overviewViewModel;

    private final TourLogsTabViewModel tourLogsTabViewModel;


    public TourPlannerApplicationViewModel(DetailsViewModel detailsViewModel,
                                           OverviewViewModel overviewViewModel, TourLogsTabViewModel tourLogsTabViewModel) {
        this.detailsViewModel = detailsViewModel;
        this.overviewViewModel = overviewViewModel;
        this.tourLogsTabViewModel = tourLogsTabViewModel;
        this.overviewViewModel.addSelectionChangedListener(this::selectTour);
    }

    private void selectTour(TourItem selectedTourItem) {
        this.detailsViewModel.updateOverviewTab(selectedTourItem);
        this.tourLogsTabViewModel.setTourModel(selectedTourItem);
    }

}
