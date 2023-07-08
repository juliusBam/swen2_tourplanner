package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.SearchInputParser;
import at.fhtw.tourplanner.bl.model.SearchInputParserOutput;
import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.bl.service.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import lombok.Getter;

import java.security.InvalidParameterException;
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

    private final List<ApplyFilterListener> filterListeners = new ArrayList<>();
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

    public void addApplyFilterChangeListener(ApplyFilterListener listener) {
        this.filterListeners.add(listener);
    }

    public void removeFilterChangeListener(ApplyFilterListener listener) {
        this.filterListeners.remove(listener);
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

    private void notifyFilterChangeListeners() {
        for (var listener : this.filterListeners) {
            listener.filterTours();
        }
    }

    public void setTours(List<TourItem> tourItems) {
        observableTourItems.clear();
        observableTourItems.addAll(tourItems);
    }

    public void addNewTour(TourItem tourItem) {
        this.tourItemService.createTourAsync(tourItem, this::handleTourAdded, this::handleTourReqError);
    }

    public void handleTourAdded(TourItem tourItem) {
        observableTourItems.add(tourItem);
        //manipulate property
        this.notifyFilterChangeListeners();
    }

    public void editTour(TourItem newItem, TourItem oldItem) {
        //tourItemService.update(newItem);

        tourItemService.updateTourAsync(newItem, oldItem, this::handleEditTour, this::handleTourReqError);

    }

    public void handleEditTour(TourItem newItem, TourItem oldItem) {
        observableTourItems.remove(oldItem);
        observableTourItems.add(newItem);

        this.notifyFilterChangeListeners();
    }

    public void handleTourReqError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.setHeaderText(title);
        alert.showAndWait();
    }

    public void deleteTour(TourItem tourItem) {

        this.tourItemService.deleteAsync(tourItem, this::handleDeleteTour, this::handleTourReqError);

    }

    public void handleDeleteTour(TourItem tourItem) {
        observableTourItems.remove(tourItem);
        this.notifyFilterChangeListeners();
    }

    public void importTour(TourItem tourItem) {
        TourItem savedItem = tourItemService.create(tourItem);
        for (TourLog log : tourItem.getTourLogs()) {
            tourLogService.create(log, savedItem.getId());
        }
        observableTourItems.add(savedItem);
    }

    public ObservableList<TourItem> handleSearch(String searchString) throws IllegalArgumentException {

        SearchInputParser searchInputParser = new SearchInputParser();

        SearchInputParserOutput parserOutput = searchInputParser.parseSearchInput(searchString);

        String searchText = parserOutput.searchInput();

        // TODO: also search computed attributes

        ObservableList<TourItem> fullTextResult = FXCollections.observableList(observableTourItems.stream().filter(tourItem -> searchInTour(tourItem, searchText)).toList());

        if (fullTextResult.isEmpty()) {
            return fullTextResult;
        }

        if (parserOutput.params() == null || parserOutput.params().isEmpty()) {
            return fullTextResult;
        }

        return searchInputParser.applyFilterParams(parserOutput.params(), fullTextResult);

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

        return foundInLogs;
    }

    private boolean searchInTourLog(TourLog tourLog, String searchText) {
        return tourLog.getComment().toLowerCase().contains(searchText.toLowerCase()) ||
               String.valueOf(tourLog.getRating()).contains(searchText.toLowerCase()) ||
               String.valueOf(tourLog.getDifficulty()).contains(searchText.toLowerCase()) ||
               String.valueOf(tourLog.getTotalTimeMinutes()).contains(searchText.toLowerCase()) ||
               String.valueOf(tourLog.getTimeStamp()).contains(searchText.toLowerCase());
    }

    public interface SelectionChangedListener {
        void changeSelection(TourItem tourItem);
    }

    public interface ApplyFilterListener {
        void filterTours();
    }
}
