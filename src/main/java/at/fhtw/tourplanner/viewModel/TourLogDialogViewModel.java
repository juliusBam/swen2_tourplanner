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

    public TourLogDialogViewModel(TourLog tourLog) {
        super();
        this.tourLog = tourLog;

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

}
