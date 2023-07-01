package at.fhtw.tourplanner.bl;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.bl.model.TourStats;
import at.fhtw.tourplanner.bl.service.TourLogService;
import at.fhtw.tourplanner.dal.dto.TourItemDto;
import at.fhtw.tourplanner.dal.dto.TourLogDto;
import at.fhtw.tourplanner.dal.dto.TourStatsDto;

public class ModelConverter {

    //region tourLog Converting
    public TourLog tourLogDtoToModel(TourLogDto tourLogDto) {
        return new TourLog(
                tourLogDto.getId(),
                tourLogDto.getTimeStamp(),
                tourLogDto.getComment(),
                this.deserializeDifficulty(tourLogDto.getDifficulty()),
                tourLogDto.getTotalTimeMinutes(),
                this.deserializeRating(tourLogDto.getRating())
        );
    }

    public TourLogDto tourLogModelToDto(Long tourId, TourLog tourLog) {
        return new TourLogDto(
                tourLog.getId(),
                tourLog.getTimeStamp(),
                tourLog.getComment(),
                this.serializeDifficulty(tourLog.getDifficulty()),
                tourLog.getTotalTimeMinutes(),
                this.serializeRating(tourLog.getRating()),
                tourId
        );
    }

    private String deserializeDifficulty(String type) {
        if (type == null) {
            return "";
        }
        if (type.equals("EASY")) {
            return "easy";
        }
        if (type.equals("HARD")) {
            return "hard";
        }
        return "medium";
    }

    private String serializeDifficulty(String type) {
        if (type == null) {
            return "";
        }
        if (type.equals("easy")) {
            return "EASY";
        }
        if (type.equals("hard")) {
            return "HARD";
        }
        return "MEDIUM";
    }

    private String deserializeRating(String type) {
        if (type == null) {
            return "";
        }
        if (type.equals("BAD")) {
            return "bad";
        }
        if (type.equals("GOOD")) {
            return "good";
        }
        return "mediocre";
    }

    private String serializeRating(String type) {
        if (type == null) {
            return "";
        }
        if (type.equals("bad")) {
            return "BAD";
        }
        if (type.equals("good")) {
            return "GOOD";
        }
        return "DECENT";
    }
    //endregion

    //region tourItem converting
    public TourItem tourItemDtoToModel(TourItemDto tourItemDto) {
        return new TourItem(tourItemDto.getId(), tourItemDto.getName(), tourItemDto.getDescription(),
                tourItemDto.getFrom(), tourItemDto.getTo(), deserializeTourType(tourItemDto.getTransportType()),
                tourItemDto.getTourDistanceKilometers(), tourItemDto.getEstimatedTimeSeconds(),
                tourItemDto.getRouteInformation(),
                tourItemDto.getTourLogs() == null ? null : tourItemDto.getTourLogs().stream().map(this::tourLogDtoToModel).toList(),
                tourItemDto.getTourStats().getPopularity(),
                tourItemDto.getTourStats().getChildFriendliness(), tourItemDto.getTourStats().getAverageTime(), tourItemDto.getTourStats().getAverageRating(),
                tourItemDto.getTourStats().getAverageDifficulty());
    }

    public TourItemDto tourItemModelToDto(TourItem tourItem) {
        return new TourItemDto(tourItem.getId(), tourItem.getName(), tourItem.getDescription(), tourItem.getFrom(),
                tourItem.getTo(), serializeTourType(tourItem.getTransportType()), tourItem.getTourDistanceKilometers(),
                tourItem.getEstimatedTimeSeconds(), tourItem.getBoundingBoxString(),
                tourItem.getTourLogs() == null ? null : tourItem.getTourLogs().stream().map((tourLog) -> this.tourLogModelToDto(tourItem.getId(), tourLog)).toList(),
                new TourStatsDto(tourItem.getPopularity(), tourItem.getChildFriendliness(), tourItem.getAverageTime(),
                        tourItem.getAverageRating(), tourItem.getAverageDifficulty())
                );
    }

    private String serializeTourType(String type) {
        if (type == null) {
            return "";
        }
        if (type.equals("fastest")) {
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
