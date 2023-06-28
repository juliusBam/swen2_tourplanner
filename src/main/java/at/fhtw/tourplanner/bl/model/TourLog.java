package at.fhtw.tourplanner.bl.model;

import lombok.Getter;
import lombok.Setter;

import static java.lang.Long.parseLong;

@Getter
@Setter
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

    public TourLog() {

    }

    public void updateFields(String rating, String difficulty, String totalTimeMinutes, String comment, long timeStamp) {
        this.rating = rating;
        this.difficulty = difficulty;
        this.totalTimeMinutes = parseLong(totalTimeMinutes);
        this.comment = comment;
        this.timeStamp = (int) timeStamp;
    }
}
