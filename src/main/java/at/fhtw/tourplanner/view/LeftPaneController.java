package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.viewModel.LeftPaneViewModel;
import at.fhtw.tourplanner.viewModel.TourItemDialogViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.util.Optional;

public final class LeftPaneController implements TourPlannerController {

    private final LeftPaneViewModel leftPaneViewModel;
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
    public MenuItem cMenuNew;
    @FXML
    public MenuItem cMenuDelete;
    @FXML
    public MenuItem cMenuEdit;
    @FXML
    public TextField toursSearchTextInput;
    private FileChooser pdfFileChooser;
    private FileChooser jsonFileChooser;

    public LeftPaneController(LeftPaneViewModel leftPaneViewModel) {
        this.leftPaneViewModel = leftPaneViewModel;
    }

    @Override
    @FXML
    public void initialize() {
        toursListView.setItems(leftPaneViewModel.getObservableTours());
        toursListView.getSelectionModel().selectedItemProperty().addListener(leftPaneViewModel.getChangeListener());
        pdfFileChooser = new FileChooser();
        pdfFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        pdfFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF documents (*.pdf)", "*.pdf"));
        jsonFileChooser = new FileChooser();
        jsonFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        jsonFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JavaScript Object Notation files (*.json)", "*.json"));
        // use button action instead of listener
        // fetch all tours from BE before search
        // performSearch()
        toursSearchTextInput.textProperty().addListener((observable, oldValue, newValue) -> {
            performSearch(newValue);
        });

        this.leftPaneViewModel.addApplyFilterChangeListener(this::reapplySearchIfNecessary);
    }

    private void performSearch(String searchString) {
        try {
            toursListView.setItems(leftPaneViewModel.handleSearch(searchString));
        } catch (IllegalArgumentException e) {
            //todo open alert
            System.out.println(e);
        } catch (Exception e) {
            //todo open alert
            System.out.println("unknwon error");
        }

    }

    //todo check with peter how we can execute the reapply search if necessary
    public void onButtonAdd(ActionEvent actionEvent) {
        TourItem tourItem = new TourItem();
        Optional<TourItem> tourItemOptional = editTourDialog(tourItem, "Add Tour");
        if (tourItemOptional.isPresent()) {
            tourItem = tourItemOptional.get();
            leftPaneViewModel.addNewTour(tourItem);
        }
    }

    private void reapplySearchIfNecessary() {
        if (!toursSearchTextInput.getText().isEmpty()) {
            performSearch(toursSearchTextInput.getText());
        }
        this.toursListView.refresh();
    }

    public void onButtonEdit(ActionEvent actionEvent) {
        if (toursListView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        TourItem oldItem = toursListView.getSelectionModel().getSelectedItem();
        Optional<TourItem> tourItemOptional = editTourDialog(oldItem, "Edit Tour");
        if (tourItemOptional.isPresent()) {
            TourItem newItem = tourItemOptional.get();
            leftPaneViewModel.editTour(newItem, oldItem);
        }
    }

    public void onButtonDelete(ActionEvent actionEvent) {
        if (toursListView.getSelectionModel().getSelectedItem() != null) {
            leftPaneViewModel.deleteTour(toursListView.getSelectionModel().getSelectedItem());
        }
    }

    private Optional<TourItem> editTourDialog(TourItem tourItem, String title) {
        Window window = toursListView.getScene().getWindow();
        Stage stage = (Stage) window;
        TourItemDialogViewModel tourItemDialogViewModel = new TourItemDialogViewModel(tourItem, leftPaneViewModel.getMapQuestService());
        TourItemDialogController dialog = new TourItemDialogController(stage, tourItemDialogViewModel, title);
        return dialog.showAndWait();
    }

    public void onButtonSummaryReport(ActionEvent actionEvent) {
        Window window = toursListView.getScene().getWindow();
        Stage stage = (Stage) window;
        pdfFileChooser.setInitialFileName("summary");
        File selectedFile = pdfFileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            leftPaneViewModel.getReportService().downloadSummaryReport(selectedFile.getAbsolutePath());
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
            String sessionId = leftPaneViewModel.getMapQuestService().getSessionId(tourItem);
            leftPaneViewModel.getReportService().downloadDetailReport(tourItem, sessionId, selectedFile.getAbsolutePath());
        }
    }

    public void onButtonImportTour(ActionEvent actionEvent) {
        Window window = toursListView.getScene().getWindow();
        Stage stage = (Stage) window;

        jsonFileChooser.setInitialFileName("");
        File selectedFile = jsonFileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            TourItem tourItem = leftPaneViewModel.getImportExportService().importTour(selectedFile.getAbsolutePath());
            leftPaneViewModel.importTour(tourItem);
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
            leftPaneViewModel.getImportExportService().exportTour(tourItem, selectedFile.getAbsolutePath());
        }
    }
}
