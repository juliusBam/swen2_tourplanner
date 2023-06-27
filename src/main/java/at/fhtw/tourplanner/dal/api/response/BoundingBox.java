package at.fhtw.tourplanner.dal.api.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BoundingBox {
    @Getter
    @JsonAlias({ "ul" })
    private CoordinatePair upperLeft;
    @Getter
    @JsonAlias({ "lr" })
    private CoordinatePair lowerRight;

    public String toSearchString() {
        return upperLeft.getLatitude() + "," + upperLeft.getLongitude() + "," + lowerRight.getLatitude() + "," + lowerRight.getLongitude();
    }
}
