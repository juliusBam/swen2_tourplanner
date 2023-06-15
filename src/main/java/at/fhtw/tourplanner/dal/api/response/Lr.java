package at.fhtw.tourplanner.dal.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Lr {
    private double lat;
    private double lng;
}
