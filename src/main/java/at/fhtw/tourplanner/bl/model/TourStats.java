package at.fhtw.tourplanner.bl.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TourStats {

    private Integer popularity;

    private Double childFriendliness;

    private Double averageTime;

    private Double averageRating;

    private Double averageDifficulty;
}
