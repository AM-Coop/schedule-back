package ru.am.scheduleapp.repository.v2;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.am.scheduleapp.model.document.v2.Location;

import java.util.Optional;

@Repository
public interface LocationRepository extends CrudRepository<Location, String> {

    Optional<Location> findByNum(int num);

    Optional<Location> findByNameAndRegion(String name, String region);

    Optional<Location> findByName(String name);


}
