package at.fhtw.tourplanner.dal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourLogDto {
    private Long id;
    private Integer timeStamp;
    private String comment;

    @JsonProperty("difficulty")
    private String difficulty;
    private Long totalTimeMinutes;
    @JsonProperty("rating")
    private String rating;
    private Long tourId;

}