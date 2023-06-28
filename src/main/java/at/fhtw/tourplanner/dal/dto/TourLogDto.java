package at.fhtw.tourplanner.dal.dto;

import at.fhtw.tourplanner.bl.model.TourLog;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourLogDto {
        private Long id;
        private Integer timeStamp;
        private String comment;

        @JsonProperty("difficulty")
        private String difficulty;
        private Long totalTimeMinutes;
        @JsonProperty("rating")
        private String rating;
        private Long tourId;

        public TourLogDto(TourLog tourLog, Long tourId) {
            this.id = tourLog.getId();
            this.timeStamp = tourLog.getTimeStamp();
            this.comment = tourLog.getComment();
            this.difficulty = tourLog.getDifficulty();
            this.totalTimeMinutes = tourLog.getTotalTimeMinutes();
            this.rating = tourLog.getRating();
            this.tourId = tourId;
        }
}
