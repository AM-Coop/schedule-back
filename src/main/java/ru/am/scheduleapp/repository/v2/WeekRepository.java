package ru.am.scheduleapp.repository.v2;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.am.scheduleapp.model.document.v2.Week;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface WeekRepository extends CrudRepository<Week, String> {

    public Optional<Week> findWeekByDateFromAndDateTo(LocalDate dateFrom, LocalDate dateTo);


    @Query(value = "select * from week w where w.date_from <= ?1 and w.date_to >= ?1", nativeQuery = true)
    Optional<Week> findByEventDate(LocalDate eventStartDate);

}
