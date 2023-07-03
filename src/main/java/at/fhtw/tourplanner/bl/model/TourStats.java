package at.fhtw.tourplanner.bl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TourStats {

    private Integer popularity;

    private Double childFriendliness;

    private Double averageTime;

    private Double averageRating;

    private Double averageDifficulty;
}
