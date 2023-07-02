package at.fhtw.tourplanner.bl.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

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

    private TourStats tourStats;
    //todo convert field into tour stats
    /*
    private Integer popularity;
    private Double childFriendliness;
    private Double averageTime;
    private Double averageRating;
    private Double averageDifficulty;
    */


    public TourItem() {
    }

    public TourItem(Long id, String name, String description, String from, String to, String transportType, Double tourDistanceKilometers, Long estimatedTimeSeconds, String boundingBoxString, List<TourLog> tourLogs) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.from = from;
        this.to = to;
        this.transportType = transportType;
        this.tourDistanceKilometers = tourDistanceKilometers;
        this.estimatedTimeSeconds = estimatedTimeSeconds;
        this.boundingBoxString = boundingBoxString;
        this.tourLogs = tourLogs;
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

    public void setTourStats(TourStats tourStats) {
        this.tourStats = tourStats;
        /*this.popularity = tourStats.getPopularity();
        this.childFriendliness = tourStats.getChildFriendliness();
        this.averageDifficulty = tourStats.getAverageDifficulty();
        this.averageRating = tourStats.getAverageRating();
        this.averageTime = tourStats.getAverageTime();*/
    }

    public void addNewTourLog(TourLog tourLog) {
        this.tourLogs.add(tourLog);
    }

    public void removeTourLog(TourLog tourLog) {
        Optional<TourLog> tourLogToDelete = this.tourLogs.stream().filter((tourLogInList -> tourLogInList.getId().equals(tourLog.getId()))).findFirst();
        if (tourLogToDelete.isPresent()) {
            this.tourLogs.remove(tourLogToDelete.get());
        }
    }

    public void updateTourLog(TourLog tourLog) {
        Optional<TourLog> tourLogToUpdate = this.tourLogs.stream().filter((tourLogInList -> tourLogInList.getId().equals(tourLog.getId()))).findFirst();
        if (tourLogToUpdate.isPresent()) {
            int indexOfItem = this.tourLogs.indexOf(tourLogToUpdate.get());

            this.tourLogs.set(indexOfItem, tourLog);
        }
    }
}
