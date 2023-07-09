package at.fhtw.tourplanner.bl.service;

import at.fhtw.tourplanner.dal.dto.TourItemDto;
import retrofit2.Call;

import java.util.List;

public interface Service<T> {

    List<T> getAll();

    T create(T item);

    void delete(T item);

    void update(T item);

    T findOne(Long itemId);

    Call<TourItemDto> findOneAsync(Long itemId);
}
