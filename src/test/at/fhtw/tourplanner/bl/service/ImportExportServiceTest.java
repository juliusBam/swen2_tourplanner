package at.fhtw.tourplanner.bl.service;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.bl.model.TourStats;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class ImportExportServiceTest {

    ImportExportService importExportService = new ImportExportService(new ObjectMapper());

    static final String json = "{\"name\":\"Roadtrip\",\"description\":\"At vero eos et accusam et justo duo dolores et ea rebum. \",\"from\":\"Wien\",\"to\":\"Dornbirn\",\"transportType\":\"car\",\"tourDistanceKilometers\":607.7,\"estimatedTimeSeconds\":21219,\"boundingBoxString\":\"48.33066000000023,9.721210000000037,47.41323999999978,16.36842\",\"tourLogs\":[{\"timeStamp\":1688162400,\"comment\":\"Duis autem vel eum iriure dolor.\",\"difficulty\":5,\"totalTimeMinutes\":360,\"rating\":3},{\"timeStamp\":1688248800,\"comment\":\"Ut wisi enim ad minim veniam.\",\"difficulty\":1,\"totalTimeMinutes\":315,\"rating\":8},{\"timeStamp\":1688853600,\"comment\":\"Das ist ein Test.\",\"difficulty\":4,\"totalTimeMinutes\":480,\"rating\":6}],\"tourStats\":{\"popularity\":1,\"childFriendliness\":0.06666666666666668,\"averageTime\":385.0,\"averageRating\":5.666666666666667,\"averageDifficulty\":3.3333333333333335}}\n";

    @BeforeAll
    static void init() {
        try {
            FileWriter myWriter = new FileWriter("testExport.json");
            myWriter.write(json);
            myWriter.close();
        } catch (IOException e) {
            System.err.println("Could not create temporary test file! Test: " + ImportExportServiceTest.class.getSimpleName());
            System.err.println(e.getMessage());
        }
    }

    @AfterAll
    static void clean() {
        try {
            Files.deleteIfExists(Path.of("testExport.json"));
            Files.deleteIfExists(Path.of("temp.json"));
        } catch (IOException e) {
            System.err.println("Could not delete temporary test file! Test: " + ImportExportServiceTest.class.getSimpleName());
            System.err.println(e.getMessage());
        }
    }

    @Test
    void testImportSimpleFields() {
        TourItem importedTourItem = importExportService.importTour("testExport.json");

        assertNotNull(importedTourItem);
        assertEquals("Roadtrip", importedTourItem.getName());
        assertEquals("At vero eos et accusam et justo duo dolores et ea rebum. ", importedTourItem.getDescription());
        assertEquals("Wien", importedTourItem.getFrom());
        assertEquals("Dornbirn", importedTourItem.getTo());
        assertEquals("car", importedTourItem.getTransportType());
        assertEquals(607.7, importedTourItem.getTourDistanceKilometers());
        assertEquals(21219, importedTourItem.getEstimatedTimeSeconds());
    }

    @Test
    void testImportTourLogs() {
        TourItem importedTourItem = importExportService.importTour("testExport.json");
        List<TourLog> importedTourLogs = importedTourItem.getTourLogs();

        // there should be three log entries
        assertEquals(3, importedTourLogs.size());

        // check first log
        assertEquals(1688162400, importedTourLogs.get(0).getTimeStamp());
        assertEquals("Duis autem vel eum iriure dolor.", importedTourLogs.get(0).getComment());
        assertEquals(5, importedTourLogs.get(0).getDifficulty());
        assertEquals(360, importedTourLogs.get(0).getTotalTimeMinutes());
        assertEquals(3, importedTourLogs.get(0).getRating());

        // check second log
        assertEquals(1688248800, importedTourLogs.get(1).getTimeStamp());
        assertEquals("Ut wisi enim ad minim veniam.", importedTourLogs.get(1).getComment());
        assertEquals(1, importedTourLogs.get(1).getDifficulty());
        assertEquals(315, importedTourLogs.get(1).getTotalTimeMinutes());
        assertEquals(8, importedTourLogs.get(1).getRating());

        // check third log
        assertEquals(1688853600, importedTourLogs.get(2).getTimeStamp());
        assertEquals("Das ist ein Test.", importedTourLogs.get(2).getComment());
        assertEquals(4, importedTourLogs.get(2).getDifficulty());
        assertEquals(480, importedTourLogs.get(2).getTotalTimeMinutes());
        assertEquals(6, importedTourLogs.get(2).getRating());
    }

    @Test
    void testImportTourStats() {
        TourItem importedTourItem = importExportService.importTour("testExport.json");
        TourStats importedTourStats = importedTourItem.getTourStats();

        assertEquals(1, importedTourStats.getPopularity());
        assertEquals(0.06666666666666668, importedTourStats.getChildFriendliness());
        assertEquals(385.0, importedTourStats.getAverageTime());
        assertEquals(5.666666666666667, importedTourStats.getAverageRating());
        assertEquals(3.3333333333333335, importedTourStats.getAverageDifficulty());
    }

    @Test
    void testExport() {
        List<TourLog> tourLogs = List.of(
                new TourLog(1L, 1688162400, "Duis autem vel eum iriure dolor.", 5, 360L, 3),
                new TourLog(2L, 1688248800, "Ut wisi enim ad minim veniam.", 1, 315L, 8),
                new TourLog(3L, 1688853600, "Das ist ein Test.", 4, 480L, 6));
        TourItem tourItem = new TourItem(1L, "Roadtrip", "At vero eos et accusam et justo duo dolores et ea rebum. ", "Wien", "Dornbirn", "car", 607.7, 21219L, "48.33066000000023,9.721210000000037,47.41323999999978,16.36842", tourLogs);
        tourItem.setTourStats(new TourStats(1, 0.06666666666666668, 385.0, 5.666666666666667, 3.3333333333333335));

        importExportService.exportTour(tourItem, "temp.json", false);

        Path filePath = Path.of("temp.json");
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePath.toUri()), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            System.err.println("Could not read temporary test file! Test: " + ImportExportServiceTest.class.getSimpleName());
            System.err.println(e.getMessage());
        }

        String fileContent = contentBuilder.toString();
        assertEquals(json, fileContent);
    }

}