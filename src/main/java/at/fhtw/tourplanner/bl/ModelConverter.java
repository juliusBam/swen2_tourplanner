package at.fhtw.tourplanner.bl;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.bl.model.TourStats;
import at.fhtw.tourplanner.dal.dto.TourItemDto;
import at.fhtw.tourplanner.dal.dto.TourLogDto;
import at.fhtw.tourplanner.dal.dto.TourStatsDto;

import java.util.ArrayList;

public class ModelConverter {

    //region tourLog Converting
    public TourLog tourLogDtoToModel(TourLogDto tourLogDto) {
        return new TourLog(
                tourLogDto.getId(),
                tourLogDto.getTimeStamp(),
                tourLogDto.getComment(),
                tourLogDto.getDifficulty(),
                tourLogDto.getTotalTimeMinutes(),
                tourLogDto.getRating()
        );
    }

    public TourLogDto tourLogModelToDto(Long tourId, TourLog tourLog) {
        return new TourLogDto(
                tourLog.getId(),
                tourLog.getTimeStamp(),
                tourLog.getComment(),
                tourLog.getDifficulty(),
                tourLog.getTotalTimeMinutes(),
                tourLog.getRating(),
                tourId
        );
    }
    //endregion

    //region tourItem converting
    public TourItem tourItemDtoToModel(TourItemDto tourItemDto) {
        return new TourItem(tourItemDto.getId(), tourItemDto.getName(), tourItemDto.getDescription(),
                tourItemDto.getFrom(), tourItemDto.getTo(), deserializeTourType(tourItemDto.getTransportType()),
                tourItemDto.getTourDistanceKilometers(), tourItemDto.getEstimatedTimeSeconds(),
                tourItemDto.getRouteInformation(),
                //tourItemDto.getTourLogs() == null ? null : tourItemDto.getTourLogs().stream().map(this::tourLogDtoToModel).toList(),
                tourItemDto.getTourLogs() == null ? null : new ArrayList<>(
                                tourItemDto.getTourLogs().stream().map(this::tourLogDtoToModel).toList()
                ),

                        //tourItemDto.getTourLogs().stream().map(this::tourLogDtoToModel).toList(),
                this.tourStatsDtoToModel(tourItemDto.getTourStats()));
    }

    public TourItemDto tourItemModelToDto(TourItem tourItem) {
        return new TourItemDto(tourItem.getId(), tourItem.getName(), tourItem.getDescription(), tourItem.getFrom(),
                tourItem.getTo(), serializeTourType(tourItem.getTransportType()), tourItem.getTourDistanceKilometers(),
                tourItem.getEstimatedTimeSeconds(), tourItem.getBoundingBoxString(),
                tourItem.getTourLogs() == null ? null : tourItem.getTourLogs().stream().map((tourLog) -> this.tourLogModelToDto(tourItem.getId(), tourLog)).toList(),
                this.tourStatsModelToDto(tourItem.getTourStats())
                );
    }

    private String serializeTourType(String type) {
        if (type == null) {
            return "";
        }
        if (type.equals("fastest") || type.equals("car")) {
            return "CAR";
        }
        if (type.equals("bicycle")) {
            return "BIKE";
        }
        return "WALK";
    }

    private String deserializeTourType(String type) {
        if (type == null) {
            return "";
        }
        if (type.equals("CAR")) {
            return "car";
        }
        if (type.equals("BIKE")) {
            return "bicycle";
        }
        return "pedestrian";
    }

    //endregion

    //region tourStats
    public TourStatsDto tourStatsModelToDto(TourStats tourStats) {

        if (tourStats == null) {
            return null;
        }

        return new TourStatsDto(tourStats.getPopularity(), tourStats.getChildFriendliness(),
                                tourStats.getAverageTime(), tourStats.getAverageRating(), tourStats.getAverageDifficulty());

    }

    public TourStats tourStatsDtoToModel(TourStatsDto tourStatsDto) {

        return new TourStats(
                tourStatsDto.getPopularity(),
                tourStatsDto.getChildFriendliness(),
                tourStatsDto.getAverageTime(),
                tourStatsDto.getAverageRating(),
                tourStatsDto.getAverageDifficulty()
        );

    }
    //endregion

}
