package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.service.ImportExportService;
import at.fhtw.tourplanner.bl.service.MapQuestService;
import at.fhtw.tourplanner.bl.service.ReportService;
import at.fhtw.tourplanner.bl.service.TourItemService;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class LeftPaneViewModel {
    private final TourItemService tourItemService;
    @Getter
    private final MapQuestService mapQuestService;
    @Getter
    private final ReportService reportService;
    @Getter
    private final ImportExportService importExportService;

    private List<SelectionChangedListener> listeners = new ArrayList<>();
    private ObservableList<TourItem> observableTourItems = FXCollections.observableArrayList();

    public LeftPaneViewModel(TourItemService tourItemService, MapQuestService mapQuestService, ReportService reportService, ImportExportService importExportService) {
        this.tourItemService = tourItemService;
        this.mapQuestService = mapQuestService;
        this.reportService = reportService;
        this.importExportService = importExportService;
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
        // TODO: send imported tour logs to BE and persist them!
        observableTourItems.add(savedItem);
    }

    public interface SelectionChangedListener {
        void changeSelection(TourItem tourItem);
    }
}
