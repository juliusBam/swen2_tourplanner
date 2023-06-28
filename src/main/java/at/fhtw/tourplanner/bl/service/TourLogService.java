package at.fhtw.tourplanner.bl.service;

import at.fhtw.tourplanner.bl.ModelConverter;
import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.dal.dto.TourItemDto;
import at.fhtw.tourplanner.dal.dto.TourLogDto;
import at.fhtw.tourplanner.dal.repository.TourLogRepository;

import java.util.List;

public class TourLogService {

    private final ModelConverter modelConverter;

    private final TourLogRepository tourLogRepository;

    public TourLogService(TourLogRepository tourLogRepository) {
        this.tourLogRepository = tourLogRepository;
        this.modelConverter = new ModelConverter();
    }

    public List<TourLog> getByTourId(Long tourId) {
        List<TourLogDto> tourLogs = this.tourLogRepository.getAllByTour(tourId);
        return tourLogs.stream().map(this.modelConverter::tourLogDtoToModel).toList();
    }

    public TourItem create(TourLog newTourLog, Long tourItemId) {
        TourItemDto tourItemDto = this.tourLogRepository.save(this.modelConverter.tourLogModelToDto(tourItemId, newTourLog));
        return this.modelConverter.tourItemDtoToModel(tourItemDto);
    }

    public TourItem update(TourLog newTourLog, Long tourItemId) {
        TourItemDto tourItemDto = this.tourLogRepository.save(this.modelConverter.tourLogModelToDto(tourItemId, newTourLog));
        return this.modelConverter.tourItemDtoToModel(tourItemDto);
    }

    public TourItem delete(Long tourItemId) {
        TourItemDto tourItemDto = this.tourLogRepository.delete(tourItemId);
        return this.modelConverter.tourItemDtoToModel(tourItemDto);
    }

}
