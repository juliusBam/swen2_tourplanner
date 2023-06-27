package at.fhtw.tourplanner.bl.service;

import java.util.List;

public interface Service<T> {

    List<T> getAll();

    T create(T item);

    void delete(T item);

    void update(T item);

}
