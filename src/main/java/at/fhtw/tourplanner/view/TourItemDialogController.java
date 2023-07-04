package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.dal.api.response.RouteResponse;
import at.fhtw.tourplanner.viewModel.TourItemDialogViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.io.IOException;

public class TourItemDialogController extends Dialog<TourItem> {
    private final TourItemDialogViewModel tourItemDialogViewModel;
    @FXML
    Label distanceLabel = new Label();
    @FXML
    private TextField nameTextField = new TextField();
    @FXML
    private TextArea descriptionTextArea = new TextArea();
    @FXML
    private TextField fromTextField = new TextField();
    @FXML
    private TextField toTextField = new TextField();
    @FXML
    private ComboBox<String> transportTypeComboBox = new ComboBox<>();
    @FXML
    private Label timeLabel = new Label();
    @FXML
    private ButtonType searchButton;
    @FXML
    private ButtonType submitButton;
    private Boolean locationFound = false;

    public TourItemDialogController(Window owner, TourItemDialogViewModel tourItemDialogViewModel, String title) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/at/fhtw/tourplanner/view/TourItemDialog.fxml"));
        loader.setController(this);

        DialogPane dialogPane;
        try {
            dialogPane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dialogPane.lookupButton(submitButton).addEventFilter(ActionEvent.ANY, this::onSubmit);
        dialogPane.lookupButton(searchButton).addEventFilter(ActionEvent.ANY, this::onSearch);

        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);

        setTitle(title);
        setDialogPane(dialogPane);

        setResultConverter(buttonType -> {
            if (buttonType.getButtonData().equals(ButtonType.OK.getButtonData())) {
                return tourItemDialogViewModel.getTourItem();
            }
            return null;
        });

        descriptionTextArea.setWrapText(true);

        this.tourItemDialogViewModel = tourItemDialogViewModel;
        nameTextField.textProperty().bindBidirectional(tourItemDialogViewModel.getNameProperty());
        descriptionTextArea.textProperty().bindBidirectional(tourItemDialogViewModel.getDescriptionProperty());
        fromTextField.textProperty().bindBidirectional(tourItemDialogViewModel.getFromProperty());
        toTextField.textProperty().bindBidirectional(tourItemDialogViewModel.getToProperty());
        transportTypeComboBox.valueProperty().bindBidirectional(tourItemDialogViewModel.getTransportTypeProperty());

    }

    private void onSearch(ActionEvent actionEvent) {
        RouteResponse routeResponse = tourItemDialogViewModel.searchRoute();
        if (routeResponse == null || routeResponse.getInfo().getStatusCode() != 0) {
            locationFound = false;
            System.out.println("No route found");
        } else {
            locationFound = true;
            System.out.println("Route found");
            String boundingBoxString = routeResponse.getRoute().getBoundingBox().toSearchString();
            double distance = routeResponse.getRoute().getDistance();
            long time = routeResponse.getRoute().getTime();
            tourItemDialogViewModel.setRouteData(distance, time, boundingBoxString);
            distanceLabel.setText(String.format("%.2f km", distance));
            timeLabel.setText(String.format("%d:%02d:%02d", time / 3600, (time % 3600) / 60, (time % 60)));
        }
        actionEvent.consume();
    }

    private void onSubmit(ActionEvent actionEvent) {
        if (!locationFound || !this.tourItemDialogViewModel.validate()) {
            actionEvent.consume();
        }
    }


}
