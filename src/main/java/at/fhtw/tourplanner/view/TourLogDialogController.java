package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.viewModel.TourLogDialogViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.io.IOException;

public class TourLogDialogController extends Dialog<TourLog> {

    private final TourLogDialogViewModel tourLogDialogViewModel;

    @FXML
    private ButtonType submitBtn;

    @FXML
    private ComboBox<String> ratingComboBox = new ComboBox<>();

    @FXML
    private ComboBox<String> difficultyComboBox = new ComboBox<>();

    @FXML
    private TextField timeInput = new TextField();

    @FXML
    private TextArea commentInput = new TextArea();



    public TourLogDialogController(Window owner, TourLogDialogViewModel tourLogDialogViewModel, String title) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/at/fhtw/tourplanner/view/TourLogDialog.fxml"));
        loader.setController(this);

        DialogPane dialogPane;
        try {
            dialogPane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        dialogPane.lookupButton(submitBtn).addEventFilter(ActionEvent.ANY, this::onSubmit);

        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);

        setTitle(title);
        setDialogPane(dialogPane);

        this.setResultConverter(buttonType -> {
            System.out.println("here");
            if (buttonType.getButtonData().equals(ButtonType.OK.getButtonData())) {
                return tourLogDialogViewModel.getTourLog();
            }
            return null;
        });

        this.tourLogDialogViewModel = tourLogDialogViewModel;
        this.ratingComboBox.valueProperty().bindBidirectional(this.tourLogDialogViewModel.getRatingProperty());
        this.difficultyComboBox.valueProperty().bindBidirectional(this.tourLogDialogViewModel.getDifficultyProperty());
        this.timeInput.textProperty().bindBidirectional(this.tourLogDialogViewModel.getTimeProperty());
        this.commentInput.textProperty().bindBidirectional(this.tourLogDialogViewModel.getCommentProperty());
    }

    public void initialize() {

    }

    private void onSubmit(ActionEvent actionEvent) {
        //todo add validation
        //if (invalid) {
        //    actionEvent.consume();
        //}
    }
}
