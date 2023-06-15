package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.service.MapQuestService;
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
    int currentId;
    private List<SelectionChangedListener> listeners = new ArrayList<>();
    private ObservableList<TourItem> observableTourItems = FXCollections.observableArrayList();

    public LeftPaneViewModel(TourItemService tourItemService, MapQuestService mapQuestService) {
        this.tourItemService = tourItemService;
        this.mapQuestService = mapQuestService;
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

    public void addNewTour() {
        TourItem tour = tourItemService.create();
        observableTourItems.add(tour);
    }

    public void deleteTour(TourItem tourItem) {
        tourItemService.delete(tourItem);
        observableTourItems.remove(tourItem);
    }

    public interface SelectionChangedListener {
        void changeSelection(TourItem tourItem);
    }
}
