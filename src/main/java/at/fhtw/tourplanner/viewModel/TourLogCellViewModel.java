package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.model.TourItem;

public class TourLogCellViewModel {

    private TourItem tour;
    public void editTourLog() {
        this.tour.updateFields("New name test", tour.getFrom(), tour.getTo(), tour.getDescription(), tour.getTransportType());
    }

    public void setTourItem(TourItem tourItem) {
        this.tour = tourItem;
    }
}
