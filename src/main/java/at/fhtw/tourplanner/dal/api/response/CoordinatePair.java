package at.fhtw.tourplanner.dal.api.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoordinatePair {
    @Getter
    @JsonAlias({ "lat" })
    private String latitude;
    @Getter
    @JsonAlias({ "lng" })
    private String longitude;
}
