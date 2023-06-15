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
    private TextField descriptionTextField = new TextField();
    @FXML
    private TextField fromTextField = new TextField();
    @FXML
    private TextField toTextField = new TextField();
    @FXML
    private TextField transportTypeTextField = new TextField();
    @FXML
    private Label timeLabel = new Label();
    @FXML
    private ButtonType searchButton;
    @FXML
    private ButtonType submitButton;
    private Boolean locationFound = false;

    public TourItemDialogController(Window owner, TourItemDialogViewModel tourItemDialogViewModel) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/at/fhtw/tourplanner/view/TourItemDialog.fxml"));
        loader.setController(this);

        DialogPane dialogPane = null;
        try {
            dialogPane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dialogPane.lookupButton(submitButton).addEventFilter(ActionEvent.ANY, this::onSubmit);
        dialogPane.lookupButton(searchButton).addEventFilter(ActionEvent.ANY, this::onSearch);

        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);

        setTitle("Add Tour");
        setDialogPane(dialogPane);

        setResultConverter(buttonType -> {
            if (buttonType.equals(ButtonType.OK)) {
                return tourItemDialogViewModel.getTourItem();
            }
            return null;
        });


        this.tourItemDialogViewModel = tourItemDialogViewModel;
        nameTextField.textProperty().bindBidirectional(tourItemDialogViewModel.getNameProperty());
        descriptionTextField.textProperty().bindBidirectional(tourItemDialogViewModel.getDescriptionProperty());
        fromTextField.textProperty().bindBidirectional(tourItemDialogViewModel.getFromProperty());
        toTextField.textProperty().bindBidirectional(tourItemDialogViewModel.getToProperty());
        transportTypeTextField.textProperty().bindBidirectional(tourItemDialogViewModel.getTransportTypeProperty());
    }

    private void onSearch(ActionEvent actionEvent) {
        RouteResponse routeResponse = tourItemDialogViewModel.searchRoute();
        if (routeResponse == null || routeResponse.getInfo().getStatuscode() != 0) {
            locationFound = false;
            System.out.println("No route found");
        } else {
            locationFound = true;
            System.out.println("Route found");
            double distance = routeResponse.getRoute().getDistance();
            long time = routeResponse.getRoute().getTime();
            distanceLabel.setText(Double.toString(distance));
            timeLabel.setText(Long.toString(time));
        }

        actionEvent.consume();
    }

    private void onSubmit(ActionEvent actionEvent) {
        if (!locationFound) {
            actionEvent.consume();
        }
    }


}
