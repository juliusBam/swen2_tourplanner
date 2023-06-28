package at.fhtw.tourplanner.bl.model;

import at.fhtw.tourplanner.dal.dto.TourLogDto;
import lombok.Getter;

@Getter
public class TourLog {

    private Long id;
    private Integer timeStamp;
    private String comment;

    private String difficulty;
    private Long totalTimeMinutes;

    private String rating;

    public TourLog(Long id, Integer timeStamp, String comment, String difficulty, Long totalTimeMinutes, String rating) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.comment = comment;
        this.difficulty = difficulty;
        this.totalTimeMinutes = totalTimeMinutes;
        this.rating = rating;
    }
}
