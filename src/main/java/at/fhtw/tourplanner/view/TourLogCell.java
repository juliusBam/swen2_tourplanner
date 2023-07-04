package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.bl.TimeConverter;
import at.fhtw.tourplanner.bl.model.TourLog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

//https://stackoverflow.com/questions/36657299/how-can-i-populate-a-listview-in-javafx-using-custom-objects
public class TourLogCell extends ListCell<TourLog> {

    private final Label title = new Label();
    private final Label detail = new Label();
    private final VBox layout = new VBox(title, detail);

    public TourLogCell(TourLogsTabController tourLogsTabController) {
        super();
        title.setStyle("-fx-font-size: 20px;");
    }

    @Override
    protected void updateItem(TourLog tourLog, boolean empty) {
            super.updateItem(tourLog, empty);

            setText(null);

            if (empty || tourLog == null || tourLog.getTimeStamp() == null || tourLog.getRating() == null || tourLog.getDifficulty() == null) {
                title.setText(null);
                detail.setText(null);
                setGraphic(null);
            } else {
                title.setText(TimeConverter.convertTimeStampToString("dd/MM/yyyy", tourLog.getTimeStamp()));

                detail.setText(String.format("Rating: %d | Difficulty: %d", tourLog.getRating(), tourLog.getDifficulty()));
                setGraphic(layout);
            }
    }

}
