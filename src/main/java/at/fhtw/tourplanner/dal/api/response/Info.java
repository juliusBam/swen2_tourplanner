package at.fhtw.tourplanner.dal.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Info {
    @Getter
    private long statuscode;
}
