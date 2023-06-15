package at.fhtw.tourplanner.dal.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;


@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {
    private double distance;
    private long time;
    private BoundingBox boundingBox;
}
