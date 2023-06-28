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

    public TourLog(TourLogDto tourLogDto) {
        this.id = tourLogDto.getId();
        this.timeStamp = tourLogDto.getTimeStamp();
        this.comment = tourLogDto.getComment();
        this.difficulty = tourLogDto.getDifficulty();
        this.totalTimeMinutes = tourLogDto.getTotalTimeMinutes();
        this.rating = tourLogDto.getRating();
    }

}
