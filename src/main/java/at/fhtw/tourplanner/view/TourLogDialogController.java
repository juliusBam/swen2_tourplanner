package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.viewModel.TourLogDialogViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Window;
import lombok.Getter;

import java.io.IOException;

public class TourLogDialogController extends Dialog<TourLog> {

    private final TourLogDialogViewModel tourLogDialogViewModel;

    @FXML
    private ButtonType submitBtn;

    @FXML
    private Slider difficultyInputSlider = new Slider();

    @FXML
    private Slider ratingInputSlider = new Slider();

    @FXML
    private TextField timeInput = new TextField();

    @FXML
    private TextArea commentInput = new TextArea();

    @FXML
    @Getter
    private TextField timePickerHoursTextField;
    @FXML
    @Getter
    private TextField timePickerMinutesTextField;

    @FXML
    private Label ratingLabel;
    @FXML
    private DatePicker tourLogDateInput = new DatePicker();

    @FXML
    private Label difficultyLabel;

    @FXML
    private DatePicker datePicker;


    //region error labels
    @FXML
    private Label errorDateLabel = new Label();

    @FXML
    private Label errorRatingLabel = new Label();

    @FXML
    private Label errorDifficultyLabel = new Label();

    @FXML
    private Label errorTimeLabel = new Label();

    @FXML
    private Label errorCommentLabel = new Label();
    //endregion

    public TourLogDialogController(TourLog tourLog, Window owner, TourLogDialogViewModel tourLogDialogViewModel, String title) {
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
            if (buttonType.getButtonData().equals(ButtonType.OK.getButtonData())) {
                return tourLogDialogViewModel.getTourLog();
            }
            return null;
        });

        this.tourLogDialogViewModel = tourLogDialogViewModel;
        ratingInputSlider.setMajorTickUnit(1);
        difficultyInputSlider.setMajorTickUnit(1);
        ratingInputSlider.setMinorTickCount(0);
        difficultyInputSlider.setMinorTickCount(0);
        ratingInputSlider.snapToTicksProperty().set(true);
        difficultyInputSlider.snapToTicksProperty().set(true);

        ratingInputSlider.valueProperty().bindBidirectional(this.tourLogDialogViewModel.getRatingProperty());
        ratingLabel.textProperty().bind(ratingInputSlider.valueProperty().asString("%.0f"));
        difficultyInputSlider.valueProperty().bindBidirectional(this.tourLogDialogViewModel.getDifficultyProperty());
        timeInput.textProperty().bindBidirectional(this.tourLogDialogViewModel.getTimeProperty());
        difficultyLabel.textProperty().bind(difficultyInputSlider.valueProperty().asString("%.0f"));
        commentInput.textProperty().bindBidirectional(this.tourLogDialogViewModel.getCommentProperty());
        datePicker.valueProperty().bindBidirectional(this.tourLogDialogViewModel.getLocalDateProperty());
        timePickerMinutesTextField.textProperty().bindBidirectional(this.tourLogDialogViewModel.getMinutesProperty());
        timePickerHoursTextField.textProperty().bindBidirectional(this.tourLogDialogViewModel.getHoursProperty());

        this.errorDateLabel.visibleProperty().bind(this.tourLogDialogViewModel.getDateValidity().not());
        this.errorRatingLabel.visibleProperty().bind(this.tourLogDialogViewModel.getRatingValidity().not());
        this.errorDifficultyLabel.visibleProperty().bind(this.tourLogDialogViewModel.getDifficultyValidity().not());
        this.errorTimeLabel.visibleProperty().bind(this.tourLogDialogViewModel.getTimeValidity().not());
        this.errorCommentLabel.visibleProperty().bind(this.tourLogDialogViewModel.getCommentValidity().not());
    }

    public void initialize() {
        timePickerHoursTextField.setPromptText("HH");
        timePickerMinutesTextField.setPromptText("MM");
        TextFormatter<String> hoursTextFormatter = getNumericFormatter();
        TextFormatter<String> minutesTextFormatter = getNumericFormatter();
        timePickerHoursTextField.setTextFormatter(hoursTextFormatter);
        timePickerMinutesTextField.setTextFormatter(minutesTextFormatter);
    }

    private void onSubmit(ActionEvent actionEvent) {
        if (!this.tourLogDialogViewModel.validate()) {
            actionEvent.consume();
        }
    }

    private boolean validateInput() {
        return true;
    }

    private TextFormatter<String> getNumericFormatter() {
        return new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }

            String text = change.getControlNewText();

            if (text.isEmpty()) {
                return change;
            }
            if (text.length() > 2 || !isNumeric(text)) {
                return null;
            }
            return change;
        });
    }

    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
        //todo add validation
        //if (invalid) {
        //    actionEvent.consume();
        //}
    }
}
