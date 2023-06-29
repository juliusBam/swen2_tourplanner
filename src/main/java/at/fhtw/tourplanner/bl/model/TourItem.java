package at.fhtw.tourplanner.bl.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
@JsonFilter("tourItem_skip_id")
public class TourItem implements Serializable {

    private Long id;
    private String name;
    private String description;
    private String from;
    private String to;
    private String transportType;
    private Double tourDistanceKilometers;
    private Long estimatedTimeSeconds;
    private String boundingBoxString;
    private List<TourLog> tourLogs;

    private Integer popularity;
    private Double childFriendliness;
    private Double averageTime;
    private Double averageRating;
    private Double averageDifficulty;


    public TourItem() {
    }

    @Override
    public String toString() {
        return name;
    }

    public void updateFields(String name, String from, String to, String description, String transportType) {
        this.name = name;
        this.from = from;
        this.to = to;
        this.description = description;
        this.transportType = transportType;
    }

    public void setRouteData(double distance, long time, String boundingBoxString) {
        this.tourDistanceKilometers = distance;
        this.estimatedTimeSeconds = time;
        this.boundingBoxString = boundingBoxString;
    }
}
