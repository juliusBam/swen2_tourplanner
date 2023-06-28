package at.fhtw.tourplanner.bl.service;

import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.dal.dto.TourLogDto;

import java.util.List;

public class TourLogService {

    public List<TourLog> getAllByTourId(Long id) {
        return null;
    }

    public static TourLog tourLogDtoToModel(TourLogDto tourLogDto) {
        return new TourLog(
            tourLogDto.getTourId(),
                tourLogDto.getTimeStamp(),
                tourLogDto.getComment(),
                TourLogService.deserializeDifficulty(tourLogDto.getDifficulty()),
                tourLogDto.getTotalTimeMinutes(),
                TourLogService.deserializeRating(tourLogDto.getRating())
        );
    }

    public static TourLogDto tourLogModelToDto(Long tourId, TourLog tourLog) {
        return new TourLogDto(
               tourLog.getId(),
               tourLog.getTimeStamp(),
               tourLog.getComment(),
               TourLogService.serializeDifficulty(tourLog.getDifficulty()),
               tourLog.getTotalTimeMinutes(),
                TourLogService.serializeRating(tourLog.getRating()),
                tourId
        );
    }

    private static String deserializeDifficulty(String type) {
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

    private static String serializeDifficulty(String type) {
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

    private static String deserializeRating(String type) {
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

    private static String serializeRating(String type) {
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

}
