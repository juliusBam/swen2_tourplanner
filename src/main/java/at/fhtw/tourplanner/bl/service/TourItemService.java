package at.fhtw.tourplanner.bl.service;


import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.dal.dto.TourItemDto;
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
    public TourItem create() {
        TourItem newTourItem = new TourItem();
        newTourItem.updateFields("New Tour", "", "", "0", "");
        TourItemDto tourItemDto = tourItemRepository.save(tourItemModelToDto(newTourItem));
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
                tourItemDto.getFrom(), tourItemDto.getTo(), tourItemDto.getTransportType(),
                tourItemDto.getTourDistanceKilometers().toString(), tourItemDto.getEstimatedTimeSeconds().toString(),
                tourItemDto.getTourLogs());
    }

    private TourItemDto tourItemModelToDto(TourItem tourItem) {
        return new TourItemDto(tourItem.getId(), tourItem.getName(), tourItem.getDescription(), tourItem.getFrom(),
                tourItem.getTo(), tourItem.getTransportType(), Double.parseDouble(tourItem.getTourDistanceKilometers()),
                Long.parseLong(tourItem.getEstimatedTimeSeconds()), tourItem.getTourLogs());
    }


}
