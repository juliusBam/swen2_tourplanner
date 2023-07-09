package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.viewModel.OverviewViewModel;
import at.fhtw.tourplanner.viewModel.TourItemDialogViewModel;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.util.Optional;

public final class OverviewController implements TourPlannerController {

    private final OverviewViewModel overviewViewModel;
    @FXML
    public ListView<TourItem> toursListView;
    @FXML
    public Button addTourBtn;
    @FXML
    public Button editTourBtn;
    @FXML
    public Button deleteTourBtn;
    @FXML
    public Button summaryReportBtn;
    @FXML
    public Button detailReportBtn;
    @FXML
    public Button importTourBtn;
    @FXML
    public Button exportTourBtn;
    @FXML
    public Button searchBtn;
    @FXML
    public Button refreshBtn;
    @FXML
    public MenuItem cMenuNew;
    @FXML
    public MenuItem cMenuDelete;
    @FXML
    public MenuItem cMenuEdit;
    @FXML
    public TextField toursSearchTextInput;

    private FileChooser pdfFileChooser;

    private FileChooser jsonFileChooser;

    public OverviewController(OverviewViewModel overviewViewModel) {
        this.overviewViewModel = overviewViewModel;
    }

    @Override
    @FXML
    public void initialize() {
        toursListView.setItems(overviewViewModel.getObservableTours());
        toursListView.getSelectionModel().selectedItemProperty().addListener(overviewViewModel.getChangeListener());
        pdfFileChooser = new FileChooser();
        pdfFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        pdfFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF documents (*.pdf)", "*.pdf"));
        jsonFileChooser = new FileChooser();
        jsonFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        jsonFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JavaScript Object Notation files (*.json)", "*.json"));
        overviewViewModel.addApplyFilterChangeListener(this::reapplySearchIfNecessary);
        overviewViewModel.refreshRequested.addListener((arg, oldVal, newVal) -> {
            if (newVal) {
                refreshTourList();
            }
            overviewViewModel.resetRefreshRequest();
        });
    }

    private void performSearch(String searchString) {
        Task<ObservableList<TourItem>> task = new Task<>() {
            @Override
            protected ObservableList<TourItem> call() {
                return overviewViewModel.handleSearch(searchString);
            }
        };
        task.setOnSucceeded(t -> {
            toursListView.setItems(task.getValue());
        });
        task.setOnFailed(e -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getSource().getException().getMessage());
            alert.setHeaderText("Error performing the search");
            alert.showAndWait();
        });
        new Thread(task).start();
    }

    public void onButtonAdd(ActionEvent actionEvent) {
        TourItem tourItem = new TourItem();
        Optional<TourItem> tourItemOptional = editTourDialog(tourItem, "Add Tour");
        if (tourItemOptional.isPresent()) {
            tourItem = tourItemOptional.get();
            overviewViewModel.addNewTour(tourItem);
        }
    }

    private void reapplySearchIfNecessary() {
        if (!toursSearchTextInput.getText().isEmpty()) {
            performSearch(toursSearchTextInput.getText());
        }
        refreshTourList();
    }

    public void onButtonEdit(ActionEvent actionEvent) {
        if (toursListView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        TourItem oldItem = toursListView.getSelectionModel().getSelectedItem();
        Optional<TourItem> tourItemOptional = editTourDialog(oldItem, "Edit Tour");
        if (tourItemOptional.isPresent()) {
            TourItem newItem = tourItemOptional.get();
            overviewViewModel.editTour(newItem, oldItem);
        }
    }

    public void onButtonDelete(ActionEvent actionEvent) {
        if (toursListView.getSelectionModel().getSelectedItem() != null) {
            overviewViewModel.deleteTour(toursListView.getSelectionModel().getSelectedItem());
        }
    }

    private Optional<TourItem> editTourDialog(TourItem tourItem, String title) {
        Window window = toursListView.getScene().getWindow();
        Stage stage = (Stage) window;
        TourItemDialogViewModel tourItemDialogViewModel = new TourItemDialogViewModel(tourItem, overviewViewModel.getMapQuestService());
        TourItemDialogController dialog = new TourItemDialogController(stage, tourItemDialogViewModel, title);
        return dialog.showAndWait();
    }

    public void onButtonSummaryReport(ActionEvent actionEvent) {
        if (overviewViewModel.getObservableTours().isEmpty()) {
            return;
        }
        Window window = toursListView.getScene().getWindow();
        Stage stage = (Stage) window;
        pdfFileChooser.setInitialFileName("summary");
        File selectedFile = pdfFileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            overviewViewModel.getReportService().downloadSummaryReport(selectedFile.getAbsolutePath());
        }
    }

    public void onButtonDetailReport(ActionEvent actionEvent) {
        TourItem tourItem = toursListView.getSelectionModel().getSelectedItem();
        if (tourItem == null) {
            return;
        }
        Window window = toursListView.getScene().getWindow();
        Stage stage = (Stage) window;
        pdfFileChooser.setInitialFileName(tourItem.getName().replace(" ", "-") + "_report");
        File selectedFile = pdfFileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            String sessionId = overviewViewModel.getMapQuestService().getSessionId(tourItem);
            overviewViewModel.getReportService().downloadDetailReport(tourItem, sessionId, selectedFile.getAbsolutePath());
        }
    }

    public void onButtonImportTour(ActionEvent actionEvent) {
        Window window = toursListView.getScene().getWindow();
        Stage stage = (Stage) window;

        jsonFileChooser.setInitialFileName("");
        File selectedFile = jsonFileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            TourItem tourItem = overviewViewModel.getImportExportService().importTour(selectedFile.getAbsolutePath());
            if (tourItem != null) {
                overviewViewModel.importTour(tourItem);
            }
        }
    }

    public void onButtonExportTour(ActionEvent actionEvent) {
        TourItem tourItem = toursListView.getSelectionModel().getSelectedItem();
        if (tourItem == null) {
            return;
        }
        Window window = toursListView.getScene().getWindow();
        Stage stage = (Stage) window;

        jsonFileChooser.setInitialFileName(tourItem.getName().replace(" ", "-") + "_export");
        File selectedFile = jsonFileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            overviewViewModel.getImportExportService().exportTour(tourItem, selectedFile.getAbsolutePath());
        }
    }

    public void onButtonSearch(ActionEvent actionEvent) {
        performSearch(toursSearchTextInput.getText());
    }

    private void refreshTourList() {
        Task<ObservableList<TourItem>> task = new Task<>() {
            @Override
            protected ObservableList<TourItem> call() {
                return overviewViewModel.refreshTours();
            }
        };
        task.setOnSucceeded(t -> {
            toursListView.setItems(task.getValue());
        });
        new Thread(task).start();
    }

    public void onButtonRefresh(ActionEvent actionEvent) {
        refreshTourList();
    }
}
