package at.fhtw.tourplanner.viewModel;

import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.view.TourLogDialogController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

public class TourLogDialogViewModel {

    @Getter
    private final TourLog tourLog;

    @Getter
    private final StringProperty ratingProperty = new SimpleStringProperty();

    @Getter
    private final StringProperty difficultyProperty = new SimpleStringProperty();

    @Getter
    private final StringProperty timeProperty = new SimpleStringProperty();

    @Getter
    private final StringProperty commentProperty = new SimpleStringProperty();

    public TourLogDialogViewModel(TourLog tourLog) {

        this.tourLog = tourLog;

        if (tourLog != null) {

            this.ratingProperty.setValue(tourLog.getRating());
            this.difficultyProperty.setValue(tourLog.getDifficulty());
            this.timeProperty.setValue(tourLog.getTotalTimeMinutes().toString());
            this.commentProperty.setValue(tourLog.getComment());

        }

    }

}
