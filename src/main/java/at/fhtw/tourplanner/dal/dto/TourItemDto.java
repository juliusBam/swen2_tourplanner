package at.fhtw.tourplanner.dal.dto;

import at.fhtw.tourplanner.bl.model.TourLog;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourItemDto {
    private Long id;
    private String name;
    private String description;
    private String from;
    private String to;
    private String transportType;
    private Double tourDistanceKilometers;
    private Long estimatedTimeSeconds;
    private List<TourLog> tourLogs;
}
