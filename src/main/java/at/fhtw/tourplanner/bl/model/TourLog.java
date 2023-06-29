package at.fhtw.tourplanner.bl.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Setter;
import lombok.Getter;

import static java.lang.Long.parseLong;


@Getter
@Setter
@JsonFilter("tourLog_skip_id")
public class TourLog {

    private Long id;
    private Integer timeStamp;
    private String comment;

    private String difficulty;
    private Long totalTimeMinutes;

    private String rating;

    // private Long tourId;

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
        if (!totalTimeMinutes.isEmpty())
        {
            this.totalTimeMinutes = parseLong(totalTimeMinutes);
        }
        this.comment = comment;
        this.timeStamp = (int) timeStamp;
    }
}