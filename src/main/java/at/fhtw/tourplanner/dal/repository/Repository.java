package at.fhtw.tourplanner.dal.repository;

import java.util.List;

public interface Repository<T, ID> {
    T save(T entity);

    T findOne(ID primaryKey);

    List<T> findAll();

    Long count();

    void delete(T entity);

    boolean exists(ID primaryKey);
}
