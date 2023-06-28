package at.fhtw.tourplanner.bl.service;


import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.model.TourLog;
import at.fhtw.tourplanner.dal.dto.TourItemDto;
import at.fhtw.tourplanner.dal.dto.TourLogDto;
import at.fhtw.tourplanner.dal.repository.TourItemRepository;

import java.util.List;

public class TourItemService implements Service<TourItem> {

    private final TourItemRepository tourItemRepository;

    public TourItemService(TourItemRepository tourItemRepository) {
        this.tourItemRepository = tourItemRepository;
    }


    @Override
    public List<TourItem> getAll() {
        List<TourItemDto> tourItemDtos = tourItemRepository.findAll();
        return tourItemDtos.stream().map(this::tourItemDtoToModel).toList();
    }

    @Override
    public TourItem create(TourItem tourItem) {
        TourItemDto tourItemDto = tourItemRepository.save(tourItemModelToDto(tourItem));
        return tourItemDtoToModel(tourItemDto);
    }

    @Override
    public void delete(TourItem tourItem) {
        tourItemRepository.delete(tourItemModelToDto(tourItem));
    }

    @Override
    public void update(TourItem tourItem) {
        tourItemRepository.save(tourItemModelToDto(tourItem));
    }

    private TourItem tourItemDtoToModel(TourItemDto tourItemDto) {
        return new TourItem(tourItemDto.getId(), tourItemDto.getName(), tourItemDto.getDescription(),
                tourItemDto.getFrom(), tourItemDto.getTo(), deserializeTourType(tourItemDto.getTransportType()),
                tourItemDto.getTourDistanceKilometers(), tourItemDto.getEstimatedTimeSeconds(),
                tourItemDto.getRouteInformation(), tourItemDto.getTourLogs().stream().map((TourLog::new)).toList(), tourItemDto.getPopularity(),
                tourItemDto.getChildFriendliness(), tourItemDto.getAverageTime(), tourItemDto.getAverageRating(),
                tourItemDto.getAverageDifficulty());
    }

    private TourItemDto tourItemModelToDto(TourItem tourItem) {
        return new TourItemDto(tourItem.getId(), tourItem.getName(), tourItem.getDescription(), tourItem.getFrom(),
                tourItem.getTo(), serializeTourType(tourItem.getTransportType()), tourItem.getTourDistanceKilometers(),
                tourItem.getEstimatedTimeSeconds(), tourItem.getBoundingBoxString(), tourItem.getTourLogs().stream().map((tourLog) -> new TourLogDto(tourLog, tourItem.getId())).toList(),
                tourItem.getPopularity(), tourItem.getChildFriendliness(), tourItem.getAverageTime(),
                tourItem.getAverageRating(), tourItem.getAverageDifficulty());
    }

    private String serializeTourType(String type) {
        if (type == null) {
            return "";
        }
        if (type.equals("fastest")) {
            return "CAR";
        }
        if (type.equals("bicycle")) {
            return "BIKE";
        }
        return "WALK";
    }

    private String deserializeTourType(String type) {
        if (type == null) {
            return "";
        }
        if (type.equals("CAR")) {
            return "car";
        }
        if (type.equals("BIKE")) {
            return "bicycle";
        }
        return "pedestrian";
    }

}
