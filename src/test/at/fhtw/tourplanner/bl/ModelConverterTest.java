package at.fhtw.tourplanner.bl;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.bl.model.TourStats;
import at.fhtw.tourplanner.dal.dto.TourItemDto;
import at.fhtw.tourplanner.dal.dto.TourLogDto;
import at.fhtw.tourplanner.dal.dto.TourStatsDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelConverterTest {

    private final ModelConverter modelConverter = new ModelConverter();

    @Test
    void testTourItemDtoToModel() {
        TourLogDto tourLogDto1 = new TourLogDto(1L, 1688968156, "comment of log entry 1",
                5, 70L, 7, 1L);

        TourLogDto tourLogDto2 = new TourLogDto(2L, 1688966598, "comment of log entry 2",
                2, 50L, 8, 1L);


        List<TourLogDto> tourLogDtoList = List.of(tourLogDto1, tourLogDto2);

        TourItemDto tourItemDto = new TourItemDto(
                1L, "test tour", "this is a test", "Wien", "Graz", "CAR",
                100., 3600L, "box", tourLogDtoList, null);

        TourStatsDto tourStatsDto = new TourStatsDto(1, 0.5, 60., 5.5, 4.5);

        tourItemDto.setTourStats(tourStatsDto);

        TourItem conversionResult = modelConverter.tourItemDtoToModel(tourItemDto);

        TourStats convertedStats = conversionResult.getTourStats();

        assertEquals(1L, conversionResult.getId());
        assertEquals("test tour", conversionResult.getName());
        assertEquals("this is a test", conversionResult.getDescription());
        assertEquals("Wien", conversionResult.getFrom());
        assertEquals("Graz", conversionResult.getTo());
        assertEquals("car", conversionResult.getTransportType());
        assertEquals(100., conversionResult.getTourDistanceKilometers());
        assertEquals(3600L, conversionResult.getEstimatedTimeSeconds());
        assertEquals("box", conversionResult.getBoundingBoxString());

        assertEquals(1, convertedStats.getPopularity());
        assertEquals(0.5, convertedStats.getChildFriendliness());
        assertEquals(60., convertedStats.getAverageTime());
        assertEquals(5.5, convertedStats.getAverageRating());
        assertEquals(4.5, convertedStats.getAverageDifficulty());

        assertEquals(2, conversionResult.getTourLogs().size());
    }

    @Test
    void testTourItemModelToDto() {
        TourLog tourLog1 = new TourLog(1L, 1688968156, "comment of log entry 1",
                5, 70L, 7);

        TourLog tourLog2 = new TourLog(2L, 1688966598, "comment of log entry 2",
                2, 50L, 8);


        List<TourLog> tourLogList = List.of(tourLog1, tourLog2);

        TourItem tourItem = new TourItem(
                1L, "test tour", "this is a test", "Wien", "Graz", "fastest",
                100., 3600L, "box", tourLogList, null);

        TourStats tourStats = new TourStats(1, 0.5, 60., 5.5, 4.5);

        tourItem.setTourStats(tourStats);

        TourItemDto conversionResult = modelConverter.tourItemModelToDto(tourItem);

        TourStatsDto convertedStats = conversionResult.getTourStats();

        assertEquals(1L, conversionResult.getId());
        assertEquals("test tour", conversionResult.getName());
        assertEquals("this is a test", conversionResult.getDescription());
        assertEquals("Wien", conversionResult.getFrom());
        assertEquals("Graz", conversionResult.getTo());
        assertEquals("CAR", conversionResult.getTransportType());
        assertEquals(100., conversionResult.getTourDistanceKilometers());
        assertEquals(3600L, conversionResult.getEstimatedTimeSeconds());
        assertEquals("box", conversionResult.getRouteInformation());

        assertEquals(1, convertedStats.getPopularity());
        assertEquals(0.5, convertedStats.getChildFriendliness());
        assertEquals(60., convertedStats.getAverageTime());
        assertEquals(5.5, convertedStats.getAverageRating());
        assertEquals(4.5, convertedStats.getAverageDifficulty());

        assertEquals(2, conversionResult.getTourLogs().size());
    }
}