package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.view.TourLogDialogController;
import javafx.beans.property.*;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDate;

public class TourLogDialogViewModel {

    @Getter
    private final TourLog tourLog;

    @Getter
    private final IntegerProperty ratingProperty = new SimpleIntegerProperty();

    @Getter
    private final IntegerProperty difficultyProperty = new SimpleIntegerProperty();

    @Getter
    private final StringProperty timeProperty = new SimpleStringProperty();

    @Getter
    private final StringProperty commentProperty = new SimpleStringProperty();

    @Getter
    private final SimpleObjectProperty<LocalDate> datePickerProperty = new SimpleObjectProperty<LocalDate>();

    //region validity props
    @Getter
    private final BooleanProperty ratingValidity = new SimpleBooleanProperty();

    @Getter
    private final BooleanProperty difficultyValidity = new SimpleBooleanProperty();

    @Getter
    private final BooleanProperty timeValidity = new SimpleBooleanProperty();

    @Getter
    private final BooleanProperty commentValidity = new SimpleBooleanProperty();

    @Getter
    private final BooleanProperty dateValidity = new SimpleBooleanProperty();
    //endregion

    public TourLogDialogViewModel(TourLog tourLog) {
        super();
        this.tourLog = tourLog;
        this.resetErrorLabels();
        //this.ratingProperty.setValue(tourLog.getRating() == null ? "" : tourLog.getRating());
        this.ratingProperty.setValue(tourLog.getRating());
        //this.difficultyProperty.setValue(tourLog.getDifficulty() == null ? "" : tourLog.getDifficulty());
        this.difficultyProperty.setValue(tourLog.getDifficulty());
        this.timeProperty.setValue(tourLog.getTotalTimeMinutes() == null ? "" : tourLog.getTotalTimeMinutes().toString());
        this.commentProperty.setValue(tourLog.getComment() == null ? "" : tourLog.getComment());

        ratingProperty.addListener((arg, oldVal, newVal) -> {
            this.updateTourLogModel();
        });
        difficultyProperty.addListener((arg, oldVal, newVal) -> {
            this.updateTourLogModel();
        });
        timeProperty.addListener((arg, oldVal, newVal) -> {
            this.updateTourLogModel();
        });
        commentProperty.addListener((arg, oldVal, newVal) -> {
            this.updateTourLogModel();
        });
        datePickerProperty.addListener((arg, oldVal, newVal) -> {
            this.updateTourLogModel();
        });

    }

    private void resetErrorLabels() {
        this.ratingValidity.set(true);
        this.difficultyValidity.set(true);
        this.timeValidity.set(true);
        this.commentValidity.set(true);
        this.dateValidity.set(true);
    }

    private void updateTourLogModel() {

        Long timeValue = 0L;
        if (this.datePickerProperty.getValue() != null) {
            timeValue = Timestamp.valueOf(this.datePickerProperty.getValue().atStartOfDay()).getTime();
            timeValue = timeValue/1000;
            System.out.println(timeValue);
        }


        this.tourLog.updateFields(
               ratingProperty.getValue(),
                difficultyProperty.getValue(),
                timeProperty.getValue(),
                commentProperty.getValue(),
                timeValue.intValue()
                //todo convert date
        );
    }

    public boolean validate() {

        boolean valid = true;
        //todo validate if date is in the past
        if (this.tourLog.getTimeStamp() == null) {
            valid = false;
            this.dateValidity.set(false);
        } else {
            this.dateValidity.set(true);
        }

        //validate if comment length is between 5 and 500
        if (this.tourLog.getComment() == null || this.tourLog.getComment().length() > 500 || this.tourLog.getComment().length() < 5) {
            valid = false;
            this.commentValidity.set(false);
        } else {
            this.commentValidity.set(true);
        }

        //validate if difficulty is between 0 and 10
        if (this.tourLog.getDifficulty() == null || this.tourLog.getDifficulty() > 10 || this.tourLog.getDifficulty() < 0) {
            valid = false;
            this.difficultyValidity.set(false);
        } else {
            this.difficultyValidity.set(true);
        }

        if (this.tourLog.getRating() == null || this.tourLog.getRating() > 10 || this.tourLog.getRating() < 0) {
            valid = false;
            this.ratingValidity.set(false);
        } else {
            this.ratingValidity.set(true);
        }

        if (this.tourLog.getTotalTimeMinutes() == null) {
            valid = false;
            this.timeValidity.set(false);
        } else {
            this.timeValidity.set(true);
        }
        return valid;
    }
}
