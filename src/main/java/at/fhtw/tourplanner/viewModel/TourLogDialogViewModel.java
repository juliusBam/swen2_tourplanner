package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.view.TourLogDialogController;
import javafx.beans.property.*;
import javafx.beans.property.*;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDate;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;

public class TourLogDialogViewModel {

    @Getter
    private final TourLog tourLog;

    @Getter
    private final IntegerProperty ratingProperty = new SimpleIntegerProperty();

    @Getter
    private final IntegerProperty difficultyProperty = new SimpleIntegerProperty();

    @Getter
    private final StringProperty minutesProperty = new SimpleStringProperty();
    @Getter
    private final StringProperty hoursProperty = new SimpleStringProperty();

    @Getter
    private final StringProperty timeProperty = new SimpleStringProperty();

    @Getter
    private final StringProperty commentProperty = new SimpleStringProperty();

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

    @Getter
    private final SimpleObjectProperty<LocalDate> localDateProperty = new SimpleObjectProperty<>();

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
        localDateProperty.setValue(tourLog.getTimeStamp() != null ? new Timestamp(tourLog.getTimeStamp().longValue() * 1000).toLocalDateTime().toLocalDate() : null);
        setInitialTimeValue(tourLog.getTotalTimeMinutes());

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
        localDateProperty.addListener((arg, oldVal, newVal) -> {
            this.updateTourLogModel();
        });
        minutesProperty.addListener((arg, oldVal, newVal) -> {
            this.updateTourLogModel();
        });
        hoursProperty.addListener((arg, oldVal, newVal) -> {
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
        this.tourLog.updateFields(
                ratingProperty.getValue(),
                difficultyProperty.getValue(),
                getTime(),
                commentProperty.getValue(),
                getDateValue());
    }

    private Long getDateValue() {
        if (localDateProperty.getValue() != null)
        {
            return Timestamp.valueOf(localDateProperty.getValue().atStartOfDay()).getTime() / 1000;
        }
        return null;
    }

    public Long getTime() {
        int hours;
        int minutes;
        try {
            hours = Integer.parseInt(hoursProperty.getValue());
            minutes = Integer.parseInt(minutesProperty.getValue());
        } catch (NumberFormatException e) {
            return null;
        }
        return Duration.ofMinutes(hours * 60L + minutes).toMinutes();
    }

    public void setInitialTimeValue(Long totalTimeMinutes) {
        if (totalTimeMinutes == null) {
            return;
        }
        long minutes = totalTimeMinutes % 60;
        String minutesString;
        if (minutes < 10) {
            minutesString = "0" + minutes;
        }
        else {
            minutesString = String.valueOf(minutes);
        }
        minutesProperty.set(minutesString);
        hoursProperty.set(String.valueOf((totalTimeMinutes - totalTimeMinutes % 60) / 60));
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
        if (this.tourLog.getComment() == null || this.tourLog.getComment().length() > 500 || this.tourLog.getComment().length() < 5 || this.tourLog.getComment().contains("@")) {
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
