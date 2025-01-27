package at.backend.tourist.places.core.Service;

import java.util.List;

public interface CommonService<T, TI> {
    T create(TI entity);
    T getById(Long id);
    List<T> getAll();
    void delete(Long id);
}
