package at.fhtw.tourplanner.bl.service;


import at.fhtw.tourplanner.bl.ModelConverter;
import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.dal.dto.TourItemDto;
import at.fhtw.tourplanner.dal.repository.TourItemRepository;

import java.util.List;

public class TourItemService implements Service<TourItem> {

    private final TourItemRepository tourItemRepository;

    private final ModelConverter modelConverter;

    public TourItemService(TourItemRepository tourItemRepository) {
        this.tourItemRepository = tourItemRepository;
        this.modelConverter = new ModelConverter();
    }


    @Override
    public List<TourItem> getAll() {
        List<TourItemDto> tourItemDtos = tourItemRepository.findAll();
        return tourItemDtos.stream().map(this.modelConverter::tourItemDtoToModel).toList();
    }

    @Override
    public TourItem create(TourItem tourItem) {
        TourItemDto tourItemDto = tourItemRepository.save(this.modelConverter.tourItemModelToDto(tourItem));
        return this.modelConverter.tourItemDtoToModel(tourItemDto);
    }

    @Override
    public void delete(TourItem tourItem) {
        tourItemRepository.delete(this.modelConverter.tourItemModelToDto(tourItem));
    }

    @Override
    public void update(TourItem tourItem) {
        tourItemRepository.save(this.modelConverter.tourItemModelToDto(tourItem));
    }

}
