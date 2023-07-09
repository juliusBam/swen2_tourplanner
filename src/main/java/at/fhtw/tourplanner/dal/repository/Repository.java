package at.fhtw.tourplanner.dal.repository;

import at.fhtw.tourplanner.dal.dto.TourItemDto;
import retrofit2.Call;

import java.util.List;

public interface Repository<T, ID> {
    T save(T entity);

    T findOne(ID primaryKey);

    List<T> findAll();

    Long count();

    void delete(T entity);

    boolean exists(ID primaryKey);

    Call<TourItemDto> findOneAsync(ID itemId);
}
