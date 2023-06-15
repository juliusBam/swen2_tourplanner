package at.fhtw.tourplanner.bl.model;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class TourItem implements Serializable {

    private Long id;
    private String name;
    private String description;
    private String from;
    private String to;
    private String transportType;
    private String tourDistanceKilometers;
    private String estimatedTimeSeconds;

    private List<TourLog> tourLogs;

    public TourItem(Long id, String name, String description, String from, String to, String transportType, String tourDistanceKilometers, String estimatedTimeSeconds, List<TourLog> tourLogs) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.from = from;
        this.to = to;
        this.transportType = transportType;
        this.tourDistanceKilometers = tourDistanceKilometers;
        this.estimatedTimeSeconds = estimatedTimeSeconds;
        this.tourLogs = tourLogs;
    }

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
}
