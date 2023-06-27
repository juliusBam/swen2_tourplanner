package at.fhtw.tourplanner.dal.dto;

import at.fhtw.tourplanner.bl.model.TourLog;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourItemDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private String name;
    private String description;
    @JsonAlias({"start", "from"})
    @JsonProperty("start")
    private String from;
    @JsonAlias({"end", "to"})
    @JsonProperty("end")
    private String to;
    @JsonAlias({"vehicle"})
    @JsonProperty("vehicle")
    private String transportType;
    private Double tourDistanceKilometers;
    private Long estimatedTimeSeconds;
    private String routeInformation;
    @JsonAlias("logs")
    private List<TourLog> tourLogs;
    private Integer popularity;
    private Double childFriendliness;
    @JsonAlias("avgTime")
    private Double averageTime;
    @JsonAlias("avgRating")
    private Double averageRating;
    @JsonAlias("avgDifficulty")
    private Double averageDifficulty;
}
