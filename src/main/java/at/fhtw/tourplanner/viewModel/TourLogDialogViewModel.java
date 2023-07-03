package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.view.TourLogDialogController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

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

    }

    private void updateTourLogModel() {
        this.tourLog.updateFields(
               ratingProperty.getValue(),
                difficultyProperty.getValue(),
                timeProperty.getValue(),
                commentProperty.getValue(),
                System.currentTimeMillis() / 1000L
        );
    }

}
