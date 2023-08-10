package ru.am.scheduleapp.repository.v2;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.am.scheduleapp.model.entity.v2.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends CrudRepository<Event, String> {

    Optional<Event> findByNum(int num);

    List<Event> findAllByDateBetween(LocalDate from, LocalDate to);

}
