package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.bl.service.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class LeftPaneViewModel {
    private final TourItemService tourItemService;
    private final TourLogService tourLogService;
    @Getter
    private final MapQuestService mapQuestService;
    @Getter
    private final ReportService reportService;
    @Getter
    private final ImportExportService importExportService;

    private final List<SelectionChangedListener> listeners = new ArrayList<>();
    private final ObservableList<TourItem> observableTourItems = FXCollections.observableArrayList();

    public LeftPaneViewModel(TourItemService tourItemService, MapQuestService mapQuestService, ReportService reportService, ImportExportService importExportService, TourLogService tourLogService) {
        this.tourItemService = tourItemService;
        this.mapQuestService = mapQuestService;
        this.reportService = reportService;
        this.importExportService = importExportService;
        this.tourLogService = tourLogService;
        setTours(this.tourItemService.getAll());
    }

    public ObservableList<TourItem> getObservableTours() {
        return observableTourItems;
    }

    public ChangeListener<TourItem> getChangeListener() {
        return (observableValue, oldValue, newValue) -> notifyListeners(newValue);
    }

    public void addSelectionChangedListener(SelectionChangedListener listener) {
        listeners.add(listener);
    }

    public void removeSelectionChangedListener(SelectionChangedListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(TourItem newValue) {
        for (var listener : listeners) {
            listener.changeSelection(newValue);
        }
    }

    public void setTours(List<TourItem> tourItems) {
        observableTourItems.clear();
        observableTourItems.addAll(tourItems);
    }

    public void addNewTour(TourItem tourItem) {
        TourItem savedItem = tourItemService.create(tourItem);
        observableTourItems.add(savedItem);
    }

    public void editTour(TourItem newItem, TourItem oldItem) {
        // TODO use returned saved item and store it in memory (as done in addNewTour)
        tourItemService.update(newItem);
        observableTourItems.remove(oldItem);
        observableTourItems.add(newItem);
    }

    public void deleteTour(TourItem tourItem) {
        tourItemService.delete(tourItem);
        observableTourItems.remove(tourItem);
    }

    public void importTour(TourItem tourItem) {
        TourItem savedItem = tourItemService.create(tourItem);
        for (TourLog log : tourItem.getTourLogs()) {
            tourLogService.create(log, savedItem.getId());
        }
        observableTourItems.add(savedItem);
    }

    public ObservableList<TourItem> handleSearch(String searchString) {
        return FXCollections.observableList(observableTourItems.stream().filter(tourItem -> searchInTour(tourItem, searchString)).toList());
    }


    private boolean searchInTour(TourItem tourItem, String searchText) {
        boolean foundInTour = tourItem.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                              tourItem.getFrom().toLowerCase().contains(searchText.toLowerCase()) ||
                              tourItem.getTo().toLowerCase().contains(searchText.toLowerCase()) ||
                              tourItem.getTransportType().toLowerCase().contains(searchText.toLowerCase()) ||
                              tourItem.getDescription().toLowerCase().contains(searchText.toLowerCase()) ||
                              String.valueOf(tourItem.getEstimatedTimeSeconds()).contains(searchText.toLowerCase()) ||
                              String.valueOf(tourItem.getTourDistanceKilometers()).contains(searchText.toLowerCase());
        if (foundInTour) {
            return true;
        }
        boolean foundInLogs = tourItem.getTourLogs().stream().anyMatch(tourLog -> searchInTourLog(tourLog, searchText));
        // TODO: also search computed attributes
        return foundInLogs;
    }

    private boolean searchInTourLog(TourLog tourLog, String searchText) {
        return tourLog.getComment().toLowerCase().contains(searchText.toLowerCase()) ||
               tourLog.getRating().toLowerCase().contains(searchText.toLowerCase()) ||
               tourLog.getDifficulty().toLowerCase().contains(searchText.toLowerCase()) ||
               String.valueOf(tourLog.getTotalTimeMinutes()).toLowerCase().contains(searchText.toLowerCase()) ||
               String.valueOf(tourLog.getTimeStamp()).toLowerCase().contains(searchText.toLowerCase());
    }

    public interface SelectionChangedListener {
        void changeSelection(TourItem tourItem);
    }
}
