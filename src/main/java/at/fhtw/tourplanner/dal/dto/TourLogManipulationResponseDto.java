package at.fhtw.tourplanner.dal.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourLogManipulationResponseDto {

    @JsonUnwrapped
    private TourLogDto tourLog;

    @JsonSerialize(as = TourStatsDto.class)
    private TourStatsDto tourStats;

}
