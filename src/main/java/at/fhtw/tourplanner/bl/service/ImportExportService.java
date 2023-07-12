package at.fhtw.tourplanner.bl.service;

import at.fhtw.tourplanner.bl.model.TourItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;

public class ImportExportService {

    private final ObjectMapper objectMapper;

    public ImportExportService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        final SimpleFilterProvider filter = new SimpleFilterProvider();
        filter.addFilter("tourItem_skip_id", SimpleBeanPropertyFilter.serializeAllExcept("id"));
        filter.addFilter("tourLog_skip_id", SimpleBeanPropertyFilter.serializeAllExcept("id", "tourId"));
        objectMapper.setFilterProvider(filter);
    }

    public void exportTour(TourItem tourItem, String path, boolean showAlert) {
        try {
            objectMapper.writeValue(new File(path), tourItem);
            if (showAlert) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "The selected tour has been exported.");
                alert.setHeaderText("Export complete");
                alert.showAndWait();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Message: " + e.getMessage());
            alert.setHeaderText("Export failed");
            alert.showAndWait();
        }
    }

    public TourItem importTour(String path) {
        try {
            return objectMapper.readValue(new File(path), TourItem.class);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Message: " + e.getMessage());
            alert.setHeaderText("Export failed");
            alert.showAndWait();
            return null;
        }
    }
}
