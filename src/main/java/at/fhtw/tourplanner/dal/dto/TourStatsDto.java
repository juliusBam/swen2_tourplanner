package at.fhtw.tourplanner.dal.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourStatsDto {

    private Integer popularity;

    private Double childFriendliness;

    @JsonAlias("avgTime")
    private Double averageTime;
    @JsonAlias("avgRating")
    private Double averageRating;
    @JsonAlias("avgDifficulty")
    private Double averageDifficulty;

}
