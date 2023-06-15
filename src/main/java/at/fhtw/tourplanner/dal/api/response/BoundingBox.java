package at.fhtw.tourplanner.dal.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BoundingBox {
    private Lr ul;
    private Lr lr;
}
